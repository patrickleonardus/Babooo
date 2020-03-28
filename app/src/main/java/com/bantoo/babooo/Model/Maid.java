package com.bantoo.babooo.Model;

public class Maid extends User{

    public int cost;
    public int rating;
    public long dateOfBirth;

    public Maid(String role, String name, String email, String phoneNumber, String password, String address, String token, long dateOfBirth, int cost, int rating) {
        super(role, name, email, phoneNumber, password, address, token);
        this.dateOfBirth = dateOfBirth;
        this.cost = cost;
        this.rating = rating;
    }
}
