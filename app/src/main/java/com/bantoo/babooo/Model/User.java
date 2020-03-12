package com.bantoo.babooo.Model;

public class User {
    public String name;
    public String email;
    public String phoneNumber;
    public String password;
    public String token;

    public User(String name, String email, String phoneNumber, String password, String token) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.token = token;
    }
}
