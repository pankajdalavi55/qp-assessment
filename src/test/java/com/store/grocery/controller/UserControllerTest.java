package com.store.grocery.controller;

import com.store.grocery.entity.CreateOrderRequest;
import com.store.grocery.entity.GroceryItem;
import com.store.grocery.entity.UnitOfMeasurement;
import com.store.grocery.exception.ApiException;
import com.store.grocery.response.APIResponse;
import com.store.grocery.response.ErrorResponse;
import com.store.grocery.response.SuccessResponse;
import com.store.grocery.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testViewAvailableGroceryItems() throws ApiException {
        // Mock data
        List<GroceryItem> mockGroceryAvailableItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200)
        );


        // Mock service method
        Mockito.when(userService.viewAvailableGroceryItems()).thenReturn(mockGroceryAvailableItems);

        // Call controller method
        ResponseEntity<APIResponse> response = userController.viewAvailableGroceryItems();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SuccessResponse<List<GroceryItem>> successResponse = (SuccessResponse<List<GroceryItem>>) response.getBody();
        assert successResponse != null;
        assertTrue(successResponse.isSuccess());
        assertEquals("Successfully fetched available grocery items", successResponse.getMessage());
        assertEquals(mockGroceryAvailableItems, successResponse.getData());
    }

    @Test
    void testCreateOrder() throws ApiException {
        // Mock request
        CreateOrderRequest request = new CreateOrderRequest();

        // Call controller method
        ResponseEntity<APIResponse> response = userController.createOrder(request);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SuccessResponse<String> successResponse = (SuccessResponse<String>) response.getBody();
        assert successResponse != null;
        assertTrue(successResponse.isSuccess());
        assertEquals("Order created successfully", successResponse.getMessage());
        assertEquals("Order created", successResponse.getData());
    }

    @Test
    void testViewAvailableGroceryItemsException() throws ApiException {
        // Mock service method to throw an exception
        when(userService.viewAvailableGroceryItems()).thenThrow(new ApiException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR));

        // Call controller method
        ResponseEntity<APIResponse> response = userController.viewAvailableGroceryItems();

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assert errorResponse != null;
        assertFalse(errorResponse.isSuccess());
        assertEquals("Internal Server Error", errorResponse.getErrorMessage());
    }

    @Test
    void testCreateOrderException() throws ApiException {
        // Mock request
        CreateOrderRequest request = new CreateOrderRequest();

        // Mock service method to throw an exception
        doThrow(new ApiException("Bad Request", HttpStatus.BAD_REQUEST)).when(userService).createOrder(request);

        // Call controller method
        ResponseEntity<APIResponse> response = userController.createOrder(request);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assert errorResponse != null;
        assertFalse(errorResponse.isSuccess());
        assertEquals("Bad Request", errorResponse.getErrorMessage());
    }
}
