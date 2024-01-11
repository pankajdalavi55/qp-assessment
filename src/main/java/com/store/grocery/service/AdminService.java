package com.store.grocery.service;

import com.store.grocery.entity.GroceryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    List<GroceryItem> addGroceryItem(List<GroceryItem> groceryItems);

    List<GroceryItem> fetchAllGroceryItems();

    void removeGroceryItem(Long itemId);

    GroceryItem updateGroceryItem(Long itemId, GroceryItem updatedItem);

    void manageInventory(Long id, double quantity);

}
