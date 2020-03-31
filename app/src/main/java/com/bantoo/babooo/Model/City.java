package com.bantoo.babooo.Model;

import java.io.Serializable;

public class City implements Serializable {
    private int cityID;
    private String cityName;
    private boolean isChecked;

    public City() {

    }

    public City(int cityID, String cityName, boolean isChecked) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.isChecked = isChecked;
    }

    public int getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
