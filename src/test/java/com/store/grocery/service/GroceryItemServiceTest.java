package com.store.grocery.service;

import com.store.grocery.entity.GroceryItem;
import com.store.grocery.entity.UnitOfMeasurement;
import com.store.grocery.exception.ApiException;
import com.store.grocery.repository.GroceryItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroceryItemServiceTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks
    private GroceryItemService groceryItemService;

    @Test
    public void testAddGroceryItem_Success(){

        // Mock Data for Grocery items
        List<GroceryItem> mockGroceryItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200)
        );

        when(groceryItemRepository.saveAll(mockGroceryItems)).thenReturn(mockGroceryItems);

        List<GroceryItem> response = groceryItemService.addGroceryItem(mockGroceryItems);

        // assert methods
        assertEquals(mockGroceryItems.size(), response.size(), "Result list size should match input list size");
        assertEquals(response, mockGroceryItems);

        // Verify that the repository method was called once with the correct list of grocery items
        verify(groceryItemRepository, times(1)).saveAll(mockGroceryItems);

    }

    @Test
    void testAddGroceryItem_Exception() {
        // Mock Data for Grocery items
        List<GroceryItem> mockGroceryItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200)
        );

        // Mock repository method to throw an exception
        when(groceryItemRepository.saveAll(mockGroceryItems)).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.addGroceryItem(mockGroceryItems));

        // Verify
        assertEquals("Failed to add grocery item", exception.getMessage());
    }

    // Fetch Grocery Item testing
    @Test
    void testFetchAllGroceryItems_Success() {
        // Mock data
        List<GroceryItem> mockGroceryItems = List.of(
                new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00),
                new GroceryItem(2L, "Rin Soap", 12, UnitOfMeasurement.PIECE, 200)
        );

        // Mock repository method
        when(groceryItemRepository.findAll()).thenReturn(mockGroceryItems);

        // Call service method
        List<GroceryItem> result = groceryItemService.fetchAllGroceryItems();

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(mockGroceryItems.size(), result.size(), "Result list size should match input list size");
        for (int i = 0; i < mockGroceryItems.size(); i++) {
            assertEquals(mockGroceryItems.get(i), result.get(i), "Grocery item at index " + i + " should match");
        }
        verify(groceryItemRepository, times(1)).findAll();
    }

    @Test
    void testFetchAllGroceryItems_Exception() {
        // Mock repository method to throw an exception
        when(groceryItemRepository.findAll()).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.fetchAllGroceryItems());

        // Verify
        assertEquals("Failed to retrieve grocery items", exception.getMessage());
    }

    // Remove grocery item testing
    @Test
    void testRemoveGroceryItem_Success() {
        // Mock data
        Long itemId = 1L;
        GroceryItem groceryItem = new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00);
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.of(groceryItem));

        // Call service method
        assertDoesNotThrow(() -> groceryItemService.removeGroceryItem(itemId));

        // Verify
        verify(groceryItemRepository, times(1)).findById(itemId);
        //verify(groceryItemRepository, times(1)).deleteById(List.of(itemId));
    }

    @Test
    void testRemoveGroceryItem_NotFound() {
        // Mock data
        Long itemId = 1L;
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.removeGroceryItem(itemId));

        // Verify
        assertEquals("Grocery item not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).deleteAllById(any());
    }

    @Test
    void testRemoveGroceryItem_InternalServerError() {
        // Mock data
        Long itemId = 1L;
        when(groceryItemRepository.findById(itemId)).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.removeGroceryItem(itemId));

        // Verify
        assertEquals("Failed to remove grocery item", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).deleteAllById(any());
    }

    // update grocery item testing
    @Test
    void testUpdateGroceryItem_Success() {
        // Mock data
        Long itemId = 1L;
        GroceryItem updatedItem = new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00);
        GroceryItem existingItem = new GroceryItem(1L, "Sugar", 38.50, UnitOfMeasurement.KILOGRAM, 150.00);
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(groceryItemRepository.save(existingItem)).thenReturn(existingItem);

        // Call service method
        GroceryItem result = groceryItemService.updateGroceryItem(itemId, updatedItem);

        // Verify
        assertNotNull(result, "Result should not be null");
        assertEquals(updatedItem.getName(), result.getName(), "Updated item name should match");
        assertEquals(updatedItem.getPrice(), result.getPrice(), "Updated item price should match");
        assertEquals(updatedItem.getUnitOfMeasure(), result.getUnitOfMeasure(), "Updated item unit of measure should match");
        assertEquals(updatedItem.getQuantity(), result.getQuantity(), "Updated item quantity should match");
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, times(1)).save(existingItem);
    }

    @Test
    void testUpdateGroceryItem_NotFound() {
        // Mock data
        Long itemId = 1L;
        GroceryItem updatedItem = new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00);
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.updateGroceryItem(itemId, updatedItem));

        // Verify
        assertEquals("Grocery item not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).save(any());
    }

    @Test
    void testUpdateGroceryItem_InternalServerError() {
        // Mock data
        Long itemId = 1L;
        GroceryItem updatedItem = new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00);
        when(groceryItemRepository.findById(itemId)).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.updateGroceryItem(itemId, updatedItem));

        // Verify
        assertEquals("Failed to update grocery item", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).save(any());
    }

    //manage inventory testing
    @Test
    void testManageInventory_Success() {
        // Mock data
        Long itemId = 1L;
        double quantity = 10.0;
        GroceryItem groceryItem = new GroceryItem(1L, "Sugar", 42.50, UnitOfMeasurement.KILOGRAM, 150.00);
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.of(groceryItem));
        when(groceryItemRepository.save(groceryItem)).thenReturn(groceryItem);

        // Call service method
        assertDoesNotThrow(() -> groceryItemService.manageInventory(itemId, quantity));

        // Verify
        assertEquals(quantity, groceryItem.getQuantity(), "Inventory quantity should be updated");
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, times(1)).save(groceryItem);
    }

    @Test
    void testManageInventory_NotFound() {
        // Mock data
        Long itemId = 1L;
        double quantity = 10.0;
        when(groceryItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.manageInventory(itemId, quantity));

        // Verify
        assertEquals("Grocery item not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).save(any());
    }

    @Test
    void testManageInventory_InternalServerError() {
        // Mock data
        Long itemId = 1L;
        double quantity = 10.0;
        when(groceryItemRepository.findById(itemId)).thenThrow(new RuntimeException());

        // Verify that the service method throws ApiException
        ApiException exception = assertThrows(ApiException.class, () -> groceryItemService.manageInventory(itemId, quantity));

        // Verify
        assertEquals("Failed to manage inventory", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        verify(groceryItemRepository, times(1)).findById(itemId);
        verify(groceryItemRepository, never()).save(any());
    }
}
