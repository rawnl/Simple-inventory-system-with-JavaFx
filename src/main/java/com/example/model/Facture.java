package com.example.model;

import java.sql.Date;

public class Facture {
    private int number;
    private Client client;
    private User user;
    private Date date ;
    private double total;
    
    public Facture() {
        super();
    }

    public Facture(int number, Client client, User user, Date date, double total) {
        this.number = number;
        this.client = client;
        this.user = user;
        this.date = date;
        this.total = total;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
