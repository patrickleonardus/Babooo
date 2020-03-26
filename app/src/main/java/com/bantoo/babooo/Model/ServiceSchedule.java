package com.bantoo.babooo.Model;

public class ServiceSchedule {
    private String orderDate;
    private String serviceType;
    private String maid;
    private String orderMonth;
    private String status;
    private String orderTime;
    private String address;

    public ServiceSchedule(String orderDate, String serviceType, String maid, String orderMonth, String status, String orderTime, String address) {
        this.orderDate = orderDate;
        this.serviceType = serviceType;
        this.maid = maid;
        this.orderMonth = orderMonth;
        this.status = status;
        this.orderTime = orderTime;
        this.address = address;
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

}
