package com.example.prmproject.model;

public class Toy {
    private int id;
    private String name;
    private int price;
    private int amount;
    private String thumbnail;
    private int categoryId;

    public Toy(int id, String name, int price, int amount, String thumbnail, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.thumbnail = thumbnail;
        this.categoryId = categoryId;
    }

    public Toy( String name, int price, int amount, String thumbnail, int categoryId) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.thumbnail = thumbnail;
        this.categoryId = categoryId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", thumbnail='" + thumbnail + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
