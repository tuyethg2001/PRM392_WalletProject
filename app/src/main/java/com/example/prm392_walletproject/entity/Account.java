package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int aid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "bank_account")
    private String bankAccount;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "value")
    private Long value;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "icon")
    private String icon;

    public Account() {
    }

    public Account(int aid, String name, String bankAccount, String type, Long value, String color) {
        this.aid = aid;
        this.name = name;
        this.bankAccount = bankAccount;
        this.type = type;
        this.value = value;
        this.color = color;
        this.icon = icon;
    }

    public Account(String name, String bankAccount, String type, Long value, String color) {
        this.aid = aid;
        this.name = name;
        this.bankAccount = bankAccount;
        this.type = type;
        this.value = value;
        this.color = color;
    }

//    public Account(int aid,String name, int bankAccount, String type, Long value) {
//        this.name = name;
//        this.bankAccount = bankAccount;
//        this.type = type;
//        this.value = value;
//    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
