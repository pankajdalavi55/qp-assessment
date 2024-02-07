package com.store.grocery.controller;

import com.store.grocery.entity.GroceryItem;
import com.store.grocery.exception.ApiException;
import com.store.grocery.response.ErrorResponse;
import com.store.grocery.response.SuccessResponse;
import com.store.grocery.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/add-grocery-item")
    public ResponseEntity<Object> addGroceryItem(@RequestBody List<GroceryItem> groceryItem) {
        try {
            List<GroceryItem> addedItems = adminService.addGroceryItem(groceryItem);
            SuccessResponse<List<GroceryItem>> successResponse = new SuccessResponse<>(true, "Grocery item added successfully", addedItems);
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }

    @GetMapping("/view-grocery-items")
    public ResponseEntity<Object> viewGroceryItems() {
        try {
            List<GroceryItem> groceryItems = adminService.fetchAllGroceryItems();
            SuccessResponse<List<GroceryItem>> successResponse = new SuccessResponse<>(true, "Successfully fetched grocery items", groceryItems);
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }

    @DeleteMapping("/remove-grocery-item/{itemId}")
    public ResponseEntity<Object> removeGroceryItem(@PathVariable Long itemId) {
        try {
            adminService.removeGroceryItem(itemId);
            SuccessResponse<String> successResponse = new SuccessResponse<>(true, "Grocery item removed successfully", "Item removed");
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }

    @PutMapping("/update-grocery-item/{itemId}")
    public ResponseEntity<Object> updateGroceryItem(@PathVariable Long itemId, @RequestBody GroceryItem updatedItem) {
        try {
            GroceryItem updatedGroceryItem = adminService.updateGroceryItem(itemId, updatedItem);
            SuccessResponse<GroceryItem> successResponse = new SuccessResponse<>(true, "Grocery item updated successfully", updatedGroceryItem);
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }

    @PatchMapping("/manage-inventory/{itemId}/{quantity}")
    public ResponseEntity<Object> manageInventory(@PathVariable Long itemId, @PathVariable double quantity) {
        try {
            adminService.manageInventory(itemId, quantity);
            SuccessResponse<String> successResponse = new SuccessResponse<>(true, "Inventory managed successfully", "Inventory managed");
            return ResponseEntity.ok(successResponse);
        } catch (ApiException e) {
            ErrorResponse errorResponse = new ErrorResponse(false, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }
    }
}
