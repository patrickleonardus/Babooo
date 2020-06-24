package com.bantoo.babooo.Model;

import java.util.Comparator;

public class Maid extends User {

    public int cost;
    public int rating;
    public String dateOfBirth;
    private String maidUniqueKey;
    private int salary;
    private String photoUrl;
    private int experience;
    private int popularity;

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getMaidUniqueKey() {
        return maidUniqueKey;
    }

    public void setMaidUniqueKey(String maidUniqueKey) {
        this.maidUniqueKey = maidUniqueKey;
    }

    public Maid(String role, String name, String email, String phoneNumber, String password, String address, String token, String dateOfBirth, int cost, int rating) {
        super(role, name, email, phoneNumber, password, address, token);
        this.dateOfBirth = dateOfBirth;
        this.cost = cost;
        this.rating = rating;
    }

    public static Comparator<Maid> salaryDescending = new Comparator<Maid>() {
        @Override
        public int compare(Maid o1, Maid o2) {
            int salary1 = o1.getSalary();
            int salary2 = o2.getSalary();
            //descending
            return salary2 - salary1;
        }
    };

    public static Comparator<Maid> salaryAscending = new Comparator<Maid>() {
        @Override
        public int compare(Maid o1, Maid o2) {
            int salary1 = o1.getSalary();
            int salary2 = o2.getSalary();
            //ascending
            return salary1 - salary2;
        }
    };
}