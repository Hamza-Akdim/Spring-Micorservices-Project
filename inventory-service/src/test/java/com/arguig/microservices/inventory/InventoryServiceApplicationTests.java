package com.arguig.microservices.inventory;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port=port;
	}

	static {
		mySQLContainer.start();
	}


	@Test
	void shouldReadInventory() {
		String reqBody = """
				{
					"skuCode" : "PC",
					"quantity" : "10"
				}
				""";
		RestAssured.given()
				.when()
				.get("/api/inventory?skuCode=PC&quantity=10")
				.then()
				.statusCode(200)
				.contentType("application/json")  // Expect application/json as the content type
				.body(Matchers.equalTo("true"));
	}

}
