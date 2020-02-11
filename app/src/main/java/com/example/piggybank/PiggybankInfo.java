package com.example.piggybank;

import java.io.Serializable;

public class PiggybankInfo implements Serializable {

    private String title;
    private double amout;
    private String date;
    private  int bankid ;


    public int getBankid() {
        return bankid;
    }

    public void setBankid(int bankid) {
        this.bankid = bankid;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public double getAmout() {
        return amout;
    }

    public void setAmout(double amout) {
        this.amout = amout;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
