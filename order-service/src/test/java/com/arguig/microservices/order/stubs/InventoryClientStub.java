package com.arguig.microservices.order.stubs;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

// in this stub we define how wiremock responds to the api call
public class InventoryClientStub {

    public static void stubInventoryCall(String skuCode, Integer quantity) {
        stubFor(get(urlEqualTo("/api/inventory?skuCode=" + skuCode + "&quantity=" + quantity))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
    }
}