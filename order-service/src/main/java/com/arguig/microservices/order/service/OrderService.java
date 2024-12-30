package com.arguig.microservices.order.service;

import com.arguig.microservices.order.client.InventoryClient;
import com.arguig.microservices.order.dto.OrderRequest;
import com.arguig.microservices.order.event.OrderPlacedEvent;
import com.arguig.microservices.order.model.Order;
import com.arguig.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;  // <key:name of topic, value sent to topic>

    public void placeOrder(OrderRequest orderRequest){
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        System.out.println("skucode"+orderRequest.skuCode());
        if (isProductInStock){
            // map OrderRequest to Order Object
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            // save Order to DB
            orderRepository.save(order);

            // Send the message to kafka Topic
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
            orderPlacedEvent.setOrderNumber((order.getOrderNumber()));
            orderPlacedEvent.setName(orderRequest.userDetails().name());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());


            log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-laced", orderPlacedEvent);
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
        } else {
            throw new RuntimeException("Product with skuCode :"+ orderRequest.skuCode() + " is not in stock");
        }
    }
}
