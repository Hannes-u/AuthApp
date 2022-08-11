package com.example.authapp.models.helper;

public class ChangePasswordSuccessfulResponse {
    private String message;

    public ChangePasswordSuccessfulResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
