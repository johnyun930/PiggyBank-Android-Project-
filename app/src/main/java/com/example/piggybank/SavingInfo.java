package com.example.piggybank;

import java.io.Serializable;

public class SavingInfo implements Serializable {

    private int savingid;
    private double moneyamout;
    private int category;
    private String categorydescription;
    private String date;
    private String description;


    public int getSavingid() {
        return savingid;
    }

    public void setSavingid(int savingid) {
        this.savingid = savingid;
    }

    public double getMoneyamout() {
        return moneyamout;
    }

    public void setMoneyamout(double moneyamout) {
        this.moneyamout = moneyamout;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCategorydescription() {
        return categorydescription;
    }

    public void setCategorydescription(String categorydescription) {
        this.categorydescription = categorydescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
