package com.store.grocery.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable {
    private boolean success;
    private String errorMessage;

    public ErrorResponse(boolean isSucess, String message) {
        this.success = isSucess;
        this.errorMessage = message;
    }
}
