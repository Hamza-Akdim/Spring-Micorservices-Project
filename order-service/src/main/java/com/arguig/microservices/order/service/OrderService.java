package com.arguig.microservices.order.service;

import com.arguig.microservices.order.client.InventoryClient;
import com.arguig.microservices.order.dto.OrderRequest;
import com.arguig.microservices.order.model.Order;
import com.arguig.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isProductInStock){
            // map OrderRequest to Order Object
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            // save Order to DB
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product with skuCode :"+ orderRequest.skuCode() + " is not in stock");
        }
    }
}
