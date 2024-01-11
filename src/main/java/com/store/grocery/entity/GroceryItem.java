package com.store.grocery.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "qp_grocery_item")
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unitOfMeasure;

    private double quantity;

}
