package com.store.grocery.entity;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateOrderRequest {

    private Long orderId;
    private Long userId;
    private Map<Long, Double>  itemWithQuantity;

}
