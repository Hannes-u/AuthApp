package com.example.authapp.models.helper;

import java.util.List;

public class JwtResponse {
    private String access_token;
    private String username;

    public JwtResponse(String access_token, String username) {
        this.access_token = access_token;
        this.username = username;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
