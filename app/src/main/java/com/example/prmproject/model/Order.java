package com.example.prmproject.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int totalPrice;
    private String username;
    private String createdAt;

    public Order(int id, String username, int totalPrice, String createdAt) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Order(String username, int totalPrice, String createdAt) {
        this.totalPrice = totalPrice;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Order(String username, int totalPrice) {
        this.totalPrice = totalPrice;
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
