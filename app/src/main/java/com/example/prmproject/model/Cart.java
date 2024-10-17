package com.example.prmproject.model;

public class Cart {
    private int id;
    private int quantity;
    private String username;
    private int toyId;

    public Cart(int id, String username, int toyId, int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.username = username;
        this.toyId = toyId;
    }

    public Cart(String username, int toyId, int quantity) {
        this.quantity = quantity;
        this.username = username;
        this.toyId = toyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getToyId() {
        return toyId;
    }

    public void setToyId(int toyId) {
        this.toyId = toyId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", username='" + username + '\'' +
                ", toyId=" + toyId +
                '}';
    }
}
