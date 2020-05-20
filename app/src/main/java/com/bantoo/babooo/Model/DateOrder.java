package com.bantoo.babooo.Model;

public class DateOrder {
    private String dateOrder;
    private String monthOrder;

    public DateOrder(String dateOrder, String monthOrder) {
        this.dateOrder = dateOrder;
        this.monthOrder = monthOrder;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getMonthOrder() {
        return monthOrder;
    }

    public void setMonthOrder(String monthOrder) {
        this.monthOrder = monthOrder;
    }
}
