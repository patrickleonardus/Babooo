package com.bantoo.babooo.Model;

public class User {
    public String role;
    public String name;
    public String email;
    public String phoneNumber;
    public String password;
    public String address;
//    public String token;

    public User(String role,String name, String email, String phoneNumber, String password, String address) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
//        this.token = token;
    }
}
