package com.store.grocery.repository;

import com.store.grocery.entity.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {

    List<GroceryItem> findByQuantityGreaterThan(double quantity);
}
