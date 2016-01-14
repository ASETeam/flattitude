package com.aseupc.flattitude.Models;

import java.util.Date;

/**
 * Created by Vavou on 14/01/2016.
 */
public class BudgetOperationDBAdapter {
    private int id;

    private String userName;

    private int flatId;

    private double amount;

    private String description;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFlatId() {
        return flatId;
    }

    public void setFlatId(int flatId) {
        this.flatId = flatId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BudgetOperationDBAdapter(int id, String userName, int flatId, double amount, String description, Date date) {
        this.id = id;
        this.userName = userName;
        this.flatId = flatId;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public BudgetOperationDBAdapter(BudgetOperation bo) {
        this.id = bo.getId();
        this.userName = bo.getUser() != null ? bo.getUser().getFirstname() : null;
        this.flatId = Integer.parseInt(bo.getFlat().getServerid());
        this.amount = bo.getAmount();
        this.description = bo.getDescription();
        this.date = bo.getDate();
    }
}
