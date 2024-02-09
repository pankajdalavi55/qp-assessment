package com.store.grocery.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuccessResponse<T> implements Serializable, APIResponse {

    private boolean success;
    private String message;
    private T data;

}
