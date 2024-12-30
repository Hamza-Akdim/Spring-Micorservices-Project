package com.arguig.microservices.order;

import com.arguig.microservices.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0) // start a wiremock server for test in a random port
@EmbeddedKafka(partitions = 1, topics = {"order-placed"})
@EnableKafka
class OrderServiceApplicationTests {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port= port;
    }

    static {
        mySQLContainer.start();
    }


    @Test
    void shouldSubmitOrder() {
        String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1,
                     "userDetails": {
                        "email": "test@example.com",
                        "name": "Test"
                     }
                }
                """;

        InventoryClientStub.stubInventoryCall("iphone_15", 1);

        RestAssured.given()
                .contentType("application/json")
                .body(submitOrderJson)
                .when()
                .post("/api/order")
                .then()
                .statusCode(201)
                .contentType("text/plain") // Expect the response content-type as text/plain
                .body(Matchers.equalTo("Order Created Successful"));
    }
}