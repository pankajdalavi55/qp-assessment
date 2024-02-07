package com.store.grocery.service;

import com.store.grocery.entity.*;
import com.store.grocery.exception.ApiException;
import com.store.grocery.repository.GroceryItemRepository;
import com.store.grocery.repository.OrderItemRepository;
import com.store.grocery.repository.OrderRepository;
import com.store.grocery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void testViewAvailableGroceryItems_Success() {
        // Mock data
        // Mock Data for Grocery items
        List<GroceryItem> availableItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200)
        );
        when(groceryItemRepository.findByQuantityGreaterThan(0)).thenReturn(availableItems);

        // Call service method
        List<GroceryItem> result = userServiceImpl.viewAvailableGroceryItems();

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(availableItems.size(), result.size(), "Result list size should match input list size");
        for (int i = 0; i < availableItems.size(); i++) {
            assertEquals(availableItems.get(i), result.get(i), "Grocery item at index " + i + " should match");
        }
        verify(groceryItemRepository, times(1)).findByQuantityGreaterThan(0);
    }

    @Test
    void testViewAvailableGroceryItems_Exception() {
        // Mock repository method to throw an exception
        when(groceryItemRepository.findByQuantityGreaterThan(0)).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> userServiceImpl.viewAvailableGroceryItems());

        // Verify
        assertEquals("Failed to retrieve grocery items", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(groceryItemRepository, times(1)).findByQuantityGreaterThan(0);
    }

    //order testing
    @Test
    void testCreateOrder_Success() {
        // Mock data
        long userId = 1L;
        long itemId1 = 1L;
        long itemId2 = 2L;
        double qty1 = 2.0;
        double qty2 = 3.0;
        double price1 = 1.99;
        double price2 = 0.99;

        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(userId);
        Map<Long, Double> itemWithQuantity = new HashMap<>();
        itemWithQuantity.put(itemId1, qty1);
        itemWithQuantity.put(itemId2, qty2);
        orderRequest.setItemWithQuantity(itemWithQuantity);

        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        GroceryItem groceryItem1 = new GroceryItem(1L, "Sugar", price1, UnitOfMeasurement.KILOGRAM, 150.00);
        groceryItem1.setId(itemId1);
        when(groceryItemRepository.findById(itemId1)).thenReturn(Optional.of(groceryItem1));

        GroceryItem groceryItem2 = new GroceryItem(2L, "Rin Soap", price2, UnitOfMeasurement.PIECE, 200);
        groceryItem2.setId(itemId2);
        when(groceryItemRepository.findById(itemId2)).thenReturn(Optional.of(groceryItem2));

        // Call service method
        assertDoesNotThrow(() -> userServiceImpl.createOrder(orderRequest));

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(groceryItemRepository, times(1)).findById(itemId1);
        verify(groceryItemRepository, times(1)).findById(itemId2);
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(any());
    }

    @Test
    void testCreateOrder_UserNotFound() {
        // Mock data
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> userServiceImpl.createOrder(orderRequest));

        // Verify
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(groceryItemRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any());
        verify(orderItemRepository, never()).saveAll(any());
    }

    @Test
    void testCreateOrder_ItemNotFound() {
        // Mock data
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(1L);
        Map<Long, Double> itemWithQuantity = new HashMap<>();
        itemWithQuantity.put(1L, 2.0);
        orderRequest.setItemWithQuantity(itemWithQuantity);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(groceryItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that the service method throws ApiException
        var exception = assertThrows(ApiException.class, () ->
                userServiceImpl.createOrder(orderRequest)
        );

        // Verify
        assertEquals("Grocery item not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(groceryItemRepository, times(1)).findById(1L);
//        verify(orderRepository, never()).save(any());
//        verify(orderItemRepository, never()).saveAll(any());
    }

}
