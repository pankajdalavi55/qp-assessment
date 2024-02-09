package com.store.grocery.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable, APIResponse {
    private boolean success;
    private String errorMessage;

    public ErrorResponse(boolean isSucess, String message) {
        this.success = isSucess;
        this.errorMessage = message;
    }
}
