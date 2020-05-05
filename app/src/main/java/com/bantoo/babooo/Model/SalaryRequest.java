package com.bantoo.babooo.Model;

public class SalaryRequest {
    private String maidName;
    private String phoneNumber;
    private String coinsRequest;

    public SalaryRequest(String maidName, String phoneNumber, String coinsRequest) {
        this.maidName = maidName;
        this.phoneNumber = phoneNumber;
        this.coinsRequest = coinsRequest;
    }

    public String getMaidName() {
        return maidName;
    }

    public void setMaidName(String maidName) {
        this.maidName = maidName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCoinsRequest() {
        return coinsRequest;
    }

    public void setCoinsRequest(String coinsRequest) {
        this.coinsRequest = coinsRequest;
    }
}
