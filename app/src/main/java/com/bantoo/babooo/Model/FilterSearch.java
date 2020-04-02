package com.bantoo.babooo.Model;

import java.io.Serializable;
import java.util.List;

public class FilterSearch implements Serializable {

    private String maxCost, minCost, maxYears, minYears, minAge, maxAge;
    private int popularity;
    private List<City> cityFilter;

    public FilterSearch() {
    }

    public FilterSearch(String maxCost, String minCost, String maxYears, String minYears, String minAge, String maxAge, int popularity, List<City> cityFilter) {
        this.maxCost = maxCost;
        this.minCost = minCost;
        this.maxYears = maxYears;
        this.minYears = minYears;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.popularity = popularity;
        this.cityFilter = cityFilter;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxYears() {
        return maxYears;
    }

    public void setMaxYears(String maxYears) {
        this.maxYears = maxYears;
    }

    public String getMinYears() {
        return minYears;
    }

    public void setMinYears(String minYears) {
        this.minYears = minYears;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public List<City> getCityFilter() {
        return cityFilter;
    }

    public void setCityFilter(List<City> cityFilter) {
        this.cityFilter = cityFilter;
    }
}
