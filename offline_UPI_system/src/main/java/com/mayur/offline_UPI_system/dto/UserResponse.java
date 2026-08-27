package com.mayur.offline_UPI_system.dto;

public class UserResponse {

    private int id;
    private String name;
    private String upiId;
    private String phoneNumber;

    public UserResponse() {
    }

    public UserResponse(int id, String name, String upiId, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.upiId = upiId;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}