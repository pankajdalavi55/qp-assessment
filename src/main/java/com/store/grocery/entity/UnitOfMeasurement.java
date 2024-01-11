package com.store.grocery.entity;

import lombok.Getter;


@Getter
public enum UnitOfMeasurement {
    PIECE("Piece"),
    KILOGRAM("Kilogram"),
    LITER("Liter"),
    MILLILITER("Milliliter");

    private final String displayName;

    UnitOfMeasurement(String displayName) {
        this.displayName = displayName;
    }

}
