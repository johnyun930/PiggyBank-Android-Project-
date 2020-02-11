package com.example.piggybank;

public class Categoryinfo {

   private String userid;
   private int categoryid;
   private  int categorytype;
   private  String category;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(int categorytype) {
        this.categorytype = categorytype;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
