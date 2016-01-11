package com.aseupc.flattitude.Models;

import java.util.Date;

/**
 * Created by Vavou on 10/01/2016.
 */
public class BudgetOperation {

    /**
     * The user who have done the budget operation
     * If null, it means that it comes from the common budget of the flat
     */
    private User user;

    /**
     * The flat concerned by the budget operation
     */
    private Flat flat;

    /**
     * The amount of the budget operation
     */
    private float amount;

    /**
     * The description of the budget operation
     */
    private String description;

    /**
     * The date of the budget operation
     */
    private Date date;

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

    public float getAmount() {
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

    public BudgetOperation(User user, Flat flat, Date date, float amount, String description) {
        this.user = user;
        this.flat = flat;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}
