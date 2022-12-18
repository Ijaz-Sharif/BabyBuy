package com.example.babybuy.Model;

public class User {
    String name,userId,email,password;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }
    public User(String name, String email,String password) {
        this.name = name;
        this.email = email;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }
}
