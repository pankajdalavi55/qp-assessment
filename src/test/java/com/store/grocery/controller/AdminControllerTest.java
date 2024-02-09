package com.store.grocery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.grocery.entity.GroceryItem;
import com.store.grocery.entity.UnitOfMeasurement;
import com.store.grocery.exception.ApiException;
import com.store.grocery.response.APIResponse;
import com.store.grocery.response.ErrorResponse;
import com.store.grocery.response.SuccessResponse;
import com.store.grocery.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        adminController = new AdminController(adminService);
    }

    //To Test Controller method addGroceryItem
    @Test
    void testAddGroceryItem_Success() throws JsonProcessingException {
        List<GroceryItem> inputItems = Collections.singletonList(new GroceryItem());
        when(adminService.addGroceryItem(inputItems)).thenReturn(inputItems);

        ResponseEntity<APIResponse> response = adminController.addGroceryItem(inputItems);

        SuccessResponse<List<GroceryItem>> expectedResponse = new SuccessResponse<>(true, "Grocery item added successfully", inputItems);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), objectMapper.writeValueAsString(response.getBody()));
    }

    @Test
    void testAddGroceryItem_ApiException() throws JsonProcessingException {
        List<GroceryItem> inputItems = Collections.singletonList(new GroceryItem());
        ApiException apiException = new ApiException("Invalid input", HttpStatus.BAD_REQUEST);
        when(adminService.addGroceryItem(inputItems)).thenThrow(apiException);

        ResponseEntity<APIResponse> response = adminController.addGroceryItem(inputItems);

        ErrorResponse expectedResponse = new ErrorResponse(false, "Invalid input");
        assertEquals(apiException.getStatus(), response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), objectMapper.writeValueAsString(response.getBody()));
    }

    // View Grocery Items
    @Test
    public void testViewGroceryItems_Success() throws ApiException {
        // Mock data
        List<GroceryItem> mockGroceryItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200));

        // Mock adminService behavior
        when(adminService.fetchAllGroceryItems()).thenReturn(mockGroceryItems);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.viewGroceryItems();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(SuccessResponse.class, responseEntity.getBody());
        SuccessResponse<?> successResponse = (SuccessResponse<?>) responseEntity.getBody();
        assertTrue(successResponse.isSuccess());
        assertEquals("Successfully fetched grocery items", successResponse.getMessage());
        assertEquals(mockGroceryItems, successResponse.getData());

        // Verify that adminService method was called
        verify(adminService, times(1)).fetchAllGroceryItems();
    }

    @Test
    public void testViewGroceryItems_ApiException() throws ApiException {
        // Mock ApiException
        ApiException mockApiException = new ApiException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

        // Mock adminService behavior to throw ApiException
        when(adminService.fetchAllGroceryItems()).thenThrow(mockApiException);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.viewGroceryItems();

        // Verify the response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertInstanceOf(ErrorResponse.class, responseEntity.getBody());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertFalse(errorResponse.isSuccess());
        assertEquals("Internal Server Error", errorResponse.getErrorMessage());

        // Verify that adminService method was called
        verify(adminService, times(1)).fetchAllGroceryItems();
    }

    // To remove Grocery items
    @Test
    public void testRemoveGroceryItem_Success() throws ApiException {
        Long itemId = 123L;

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.removeGroceryItem(itemId);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(SuccessResponse.class, responseEntity.getBody());
        SuccessResponse<?> successResponse = (SuccessResponse<?>) responseEntity.getBody();
        assertTrue(successResponse.isSuccess());
        assertEquals("Grocery item removed successfully", successResponse.getMessage());
        assertEquals("Item removed", successResponse.getData());

        // Verify that adminService method was called
        verify(adminService, times(1)).removeGroceryItem(itemId);
    }

    @Test
    public void testRemoveGroceryItem_ApiException() throws ApiException {
        Long itemId = 123L;
        // Mock ApiException
        ApiException mockApiException = new ApiException("Item not found", HttpStatus.NOT_FOUND);

        // Mock adminService behavior to throw ApiException
        doThrow(mockApiException).when(adminService).removeGroceryItem(itemId);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.removeGroceryItem(itemId);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(ErrorResponse.class, responseEntity.getBody());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertFalse(errorResponse.isSuccess());
        assertEquals("Item not found", errorResponse.getErrorMessage());

        // Verify that adminService method was called
        verify(adminService, times(1)).removeGroceryItem(itemId);
    }

    //To test update method
    @Test
    public void testUpdateGroceryItem_Success() throws ApiException {
        Long itemId = 123L;
        GroceryItem updatedItem = new GroceryItem(123L, "Washing Powder pack of 1kg", 2.0, UnitOfMeasurement.PIECE, 100);

        // Mock adminService behavior
        when(adminService.updateGroceryItem(itemId, updatedItem)).thenReturn(updatedItem);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.updateGroceryItem(itemId, updatedItem);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(SuccessResponse.class, responseEntity.getBody());
        SuccessResponse<?> successResponse = (SuccessResponse<?>) responseEntity.getBody();
        assertTrue(successResponse.isSuccess());
        assertEquals("Grocery item updated successfully", successResponse.getMessage());
        assertEquals(updatedItem, successResponse.getData());

        // Verify that adminService method was called
        verify(adminService, times(1)).updateGroceryItem(itemId, updatedItem);
    }

    @Test
    public void testUpdateGroceryItem_ApiException() throws ApiException {
        Long itemId = 123L;
        GroceryItem updatedItem = new GroceryItem(123L, "Washing Powder pack of 1kg", 2.0, UnitOfMeasurement.PIECE, 100);
        // Mock ApiException
        ApiException mockApiException = new ApiException("Item not found", HttpStatus.NOT_FOUND);

        // Mock adminService behavior to throw ApiException
        doThrow(mockApiException).when(adminService).updateGroceryItem(itemId, updatedItem);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.updateGroceryItem(itemId, updatedItem);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertInstanceOf(ErrorResponse.class, responseEntity.getBody());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertFalse(errorResponse.isSuccess());
        assertEquals("Item not found", errorResponse.getErrorMessage());

        // Verify that adminService method was called
        verify(adminService, times(1)).updateGroceryItem(itemId, updatedItem);
    }

    // To test patch request to update the inventory
    @Test
    public void testManageInventory_Success() throws ApiException {
        Long itemId = 123L;
        double quantity = 50.0;

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.manageInventory(itemId, quantity);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(SuccessResponse.class, responseEntity.getBody());
        SuccessResponse<?> successResponse = (SuccessResponse<?>) responseEntity.getBody();
        assertTrue(successResponse.isSuccess());
        assertEquals("Inventory managed successfully", successResponse.getMessage());
        assertEquals("Inventory managed", successResponse.getData());

        // Verify that adminService method was called
        verify(adminService, times(1)).manageInventory(itemId, quantity);
    }

    @Test
    public void testManageInventory_ApiException() throws ApiException {
        Long itemId = 123L;
        double quantity = -10.0; // Negative quantity to simulate an invalid scenario
        // Mock ApiException
        ApiException mockApiException = new ApiException("Invalid quantity", HttpStatus.BAD_REQUEST);

        // Mock adminService behavior to throw ApiException
        doThrow(mockApiException).when(adminService).manageInventory(itemId, quantity);

        // Call the method under test
        ResponseEntity<APIResponse> responseEntity = adminController.manageInventory(itemId, quantity);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertInstanceOf(ErrorResponse.class, responseEntity.getBody());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertFalse(errorResponse.isSuccess());
        assertEquals("Invalid quantity", errorResponse.getErrorMessage());

        // Verify that adminService method was called
        verify(adminService, times(1)).manageInventory(itemId, quantity);
    }
}
