package com.store.grocery.service;

import com.store.grocery.entity.*;
import com.store.grocery.exception.ApiException;
import com.store.grocery.repository.GroceryItemRepository;
import com.store.grocery.repository.OrderItemRepository;
import com.store.grocery.repository.OrderRepository;
import com.store.grocery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final GroceryItemRepository groceryItemRepository;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public UserServiceImpl(GroceryItemRepository groceryItemRepository, UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.groceryItemRepository = groceryItemRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<GroceryItem> viewAvailableGroceryItems() {
        try {

            return groceryItemRepository.findByQuantityGreaterThan(0);
        } catch (Exception e) {
            throw new ApiException("Failed to retrieve grocery items", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void createOrder(CreateOrderRequest OrderRequest) {
        try {

            User user = userRepository.findById(OrderRequest.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            Order order = new Order();
            order.setUser(user);

            double totalPrice = 0;

            Map<Long, Double> itemWithQty = (OrderRequest.getItemWithQuantity() != null)
                    ? OrderRequest.getItemWithQuantity()
                    : new HashMap<>();
            orderRepository.save(order);

            for (Map.Entry<Long, Double> map : itemWithQty.entrySet()) {

                Long itemId = map.getKey();
                double qty = map.getValue();

                GroceryItem groceryItem = groceryItemRepository.findById(itemId)
                        .orElseThrow(() -> new EntityNotFoundException("Grocery item not found"));

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setItem(groceryItem);
                orderItem.setQuantity(qty);
                orderItem.setTotalPrice(groceryItem.getPrice() * qty);

                totalPrice += orderItem.getTotalPrice();

                order.getOrderItems().add(orderItem);
            }

            order.setTotalPrice(totalPrice);
            orderItemRepository.saveAll(order.getOrderItems());
            orderRepository.save(order);

        } catch (EntityNotFoundException e) {
            throw new ApiException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ApiException("Failed to create order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
