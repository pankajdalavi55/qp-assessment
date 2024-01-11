package com.store.grocery.controller;

import com.store.grocery.entity.CreateOrderRequest;
import com.store.grocery.entity.GroceryItem;
import com.store.grocery.exception.ApiException;
import com.store.grocery.response.ErrorResponse;
import com.store.grocery.response.SuccessResponse;
import com.store.grocery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/view-available-grocery-items")
    public ResponseEntity<Object> viewAvailableGroceryItems() {
        try {
            List<GroceryItem> availableItems = userService.viewAvailableGroceryItems();
            SuccessResponse<List<GroceryItem>> successResponse = new SuccessResponse<>(true, "Successfully fetched available grocery items", availableItems);
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }

    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        System.out.println("Input createOrderRequest "+ createOrderRequest);
        try {
            userService.createOrder(createOrderRequest);
            SuccessResponse<String> successResponse = new SuccessResponse<>(true, "Order created successfully", "Order created");
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }
}
