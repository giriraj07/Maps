package com.example.maps;

public class User {
    private String name;
    private Float walletAmount;
    private int age;
    private String phoneNo;

    public User(String name, Float walletAmount, int age, String phoneNo) {
        this.name = name;
        this.walletAmount = walletAmount;
        this.age = age;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public Float getWalletAmount() {
        return walletAmount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWalletAmount(Float walletAmount) {
        walletAmount = walletAmount;
    }

    public void setAge(int age) {
        age = age;
    }

    public void setPhoneNo(String phoneNo) {
        phoneNo = phoneNo;
    }

    public int getAge() {
        return age;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
}
