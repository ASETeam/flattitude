package com.aseupc.flattitude.Models;

import java.util.Date;

/**
 * Created by Vavou on 10/01/2016.
 */
public class BudgetOperation {

    private int id;

    /**
     * The user who have done the budget operation
     * If null, it means that it comes from the common budget of the flat
     */
    private User user;

    /**
     * The user who have done the transaction
     */
    private User userSource;

    /**
     * The flat concerned by the budget operation
     */
    private Flat flat;

    /**
     * The amount of the budget operation
     */
    private double amount;

    /**
     * The description of the budget operation
     */
    private String description;

    /**
     * The date of the budget operation
     */
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserSource() {
        return userSource;
    }

    public void setUserSource(User userSource) {
        this.userSource = userSource;
    }

    public BudgetOperation(User user, User userSource, Flat flat, Date date, double amount, String description) {
        this.user = user;
        this.userSource = userSource;
        this.flat = flat;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public BudgetOperation(int id, User user, User userSource, Flat flat, Date date, double amount, String description) {
        this.id = id;
        this.user = user;
        this.userSource = userSource;
        this.flat = flat;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}
