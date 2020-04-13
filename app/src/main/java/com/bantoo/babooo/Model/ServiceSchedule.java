package com.bantoo.babooo.Model;

import java.util.Date;

public class ServiceSchedule {
    private String orderDate;
    private String serviceType;
    private String maid;
    private String orderMonth;
    private String status;
    private String orderTime;
    private String address;
    private String maidPhoneNumber;
    private String phoneNumber;
    private String orderYear;

    public String getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(String orderYear) {
        this.orderYear = orderYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ServiceSchedule(String orderDate, String serviceType, String maid, String orderMonth, String status, String orderTime, String address,
                           String maidPhoneNumber) {
        this.orderDate = orderDate;
        this.serviceType = serviceType;
        this.maid = maid;
        this.orderMonth = orderMonth;
        this.status = status;
        this.orderTime = orderTime;
        this.address = address;
        this.maidPhoneNumber = maidPhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMaid() {
        return maid;
    }

    public void setMaid(String maid) {
        this.maid = maid;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaidPhoneNumber() {
        return maidPhoneNumber;
    }

    public void setMaidPhoneNumber(String maidPhoneNumber) {
        this.maidPhoneNumber = maidPhoneNumber;
    }

}
