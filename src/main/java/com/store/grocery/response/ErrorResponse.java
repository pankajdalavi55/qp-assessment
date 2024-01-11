package com.store.grocery.response;

public class ErrorResponse {
    private boolean success;
    private String errorMessage;

    public ErrorResponse(boolean isSucess, String message) {
        this.success = isSucess;
        this.errorMessage = message;
    }
}
