package com.microservices.orderservice.service;

import com.microservices.orderservice.dto.InventoryResponse;
import com.microservices.orderservice.dto.OrderLineItemsDto;
import com.microservices.orderservice.dto.OrderRequest;
import com.microservices.orderservice.model.Order;
import com.microservices.orderservice.model.OrderLineItems;
import com.microservices.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final WebClient webClient;
    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().
                    stream().map(this::mapToDTO).toList();

            // set orderLineItems
            order.setOrderLineItemsList(orderLineItems);

            List<String> skuCodes = order.getOrderLineItemsList().stream()
                            .map(OrderLineItems::getSkuCode).toList();
                                                       // getter method -> getSkuCode
            // Call INVENTORY service
            // http://localhost:8083/api/inventory?skuCode=iphone-13&skuCode=iphone-13-red
           InventoryResponse[] resArray = webClient.get().
                   uri("http://localhost:8083/api/inventory",
                           uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve().bodyToMono(InventoryResponse[].class).block();      // to sync operation -> block

        Boolean allProductsInStock = Arrays.stream(resArray).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
               orderRepository.save(order);
           }else{
               throw new IllegalAccessException("product is not on the stock, please try again later");
           }

    }

    private OrderLineItems mapToDTO(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(orderLineItemsDto.getId());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
