package com.example.model;

public class Commande {
    //private int idFacture;
    private int idArticle;
    private String articleName ;
    private double price ;
    private int quantity;
    private double total;
    public Commande() {
        super();
    }
/*     public Commande(int idFacture, int idArticle, int quantity) {
        //this.idFacture = idFacture;
        this.idArticle = idArticle;
        this.quantity = quantity;
    }*/
    /* 
    public int getIdFacture() {
        return idFacture;
    }
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }*/
    public int getIdArticle() {
        return idArticle;
    }
    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }
    
    public String getArticleName() {
        return articleName;
    }
    public void setArticleName(String articleName) {
        this.articleName = articleName;
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
    
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    
}
