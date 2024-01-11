package com.store.grocery.service;

import com.store.grocery.entity.GroceryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final GroceryItemService groceryItemService;

    public AdminServiceImpl(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    @Override
    public List<GroceryItem> addGroceryItem(List<GroceryItem> groceryItems) {
        return groceryItemService.addGroceryItem(groceryItems);
    }

    @Override
    public List<GroceryItem> fetchAllGroceryItems() {
        return groceryItemService.fetchAllGroceryItems();
    }

    @Override
    public void removeGroceryItem(Long itemId) {
        groceryItemService.removeGroceryItem(itemId);
    }

    @Override
    public GroceryItem updateGroceryItem(Long itemId, GroceryItem updatedItem) {
        return groceryItemService.updateGroceryItem(itemId, updatedItem);
    }

    @Override
    public void manageInventory(Long itemId, double quantity) {
        groceryItemService.manageInventory(itemId, quantity);

    }
}
