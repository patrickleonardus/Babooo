package com.bantoo.babooo.Model;

public class Maid extends User{

    public int cost;
    public int rating;

    public Maid(String role, String name, String email, String phoneNumber, String password, String address, String token, int cost, int rating) {
        super(role, name, email, phoneNumber, password, address, token);
        this.cost = cost;
        this.rating = rating;
    }
}
