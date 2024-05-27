package com.skku.BitCO2e.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String username;
    private String email;
    private String password;

    // Default constructor (required by Firebase)
    public User() {
    }

    // Constructor with parameters
    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
