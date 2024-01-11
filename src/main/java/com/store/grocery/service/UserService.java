package com.store.grocery.service;

import com.store.grocery.entity.CreateOrderRequest;
import com.store.grocery.entity.GroceryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<GroceryItem> viewAvailableGroceryItems();

    void createOrder(CreateOrderRequest createOrderRequest);
}
