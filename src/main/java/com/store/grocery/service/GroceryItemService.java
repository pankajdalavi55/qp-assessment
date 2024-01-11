package com.store.grocery.service;

import com.store.grocery.entity.GroceryItem;
import com.store.grocery.exception.ApiException;
import com.store.grocery.repository.GroceryItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class GroceryItemService {

    private final GroceryItemRepository groceryItemRepository;

    public GroceryItemService(GroceryItemRepository groceryItemRepository) {
        this.groceryItemRepository = groceryItemRepository;
    }

    public List<GroceryItem> addGroceryItem(List<GroceryItem> groceryItem) {
        try {
            log.info("Adding new new grocery item..!");
            return groceryItemRepository.saveAll(groceryItem);

        } catch (Exception e) {
            throw new ApiException("Failed to add grocery item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<GroceryItem> fetchAllGroceryItems() {
        try {
            // Implement logic to view all grocery items
            return groceryItemRepository.findAll();
        } catch (Exception e) {
            throw new ApiException("Failed to retrieve grocery items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void removeGroceryItem(Long itemId) {
        try {
//            Optional<GroceryItem> existingItem = groceryItemRepository.findById(itemId);
//            existingItem.ifPresent(groceryItem ->groceryItemRepository.deleteById(groceryItem.getId()));
            GroceryItem existingItem = groceryItemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Grocery item not found"));
            groceryItemRepository.deleteAllById(Collections.singleton(existingItem.getId()));
        } catch (EntityNotFoundException e) {
            throw new ApiException("Grocery item not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ApiException("Failed to remove grocery item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public GroceryItem updateGroceryItem(Long itemId, GroceryItem updatedItem) {
        try {
            // Implement logic to update a grocery item
            GroceryItem existingItem = groceryItemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Grocery item not found"));

            existingItem.setName(updatedItem.getName());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setUnitOfMeasure(updatedItem.getUnitOfMeasure());
            existingItem.setQuantity(updatedItem.getQuantity());

            return groceryItemRepository.save(existingItem);
        } catch (EntityNotFoundException e) {
            throw new ApiException("Grocery item not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ApiException("Failed to update grocery item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void manageInventory(Long itemId, double quantity) {
        try {
            // Implement logic to manage inventory
            GroceryItem groceryItem = groceryItemRepository.findById(itemId)
                    .orElseThrow(() -> new EntityNotFoundException("Grocery item not found"));

            groceryItem.setQuantity(quantity);

            groceryItemRepository.save(groceryItem);
        } catch (EntityNotFoundException e) {
            throw new ApiException("Grocery item not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ApiException("Failed to manage inventory", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
