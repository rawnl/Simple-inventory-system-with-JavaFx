package com.example.model;

public class Article {
    private int id;
    private String barcode;
    private String articleName;
    private Category category;
    private String brand;
    private String model;
    private double price;
    private int quantity;
    
    public Article() {
        super();
    }
    
    public Article(int id, String barcode, String articleName,Category category, String brand,String model, double price, int quantity) {
        this.id = id;
        this.barcode = barcode;
        this.articleName = articleName;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getArticleName() {
        return articleName;
    }
    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
