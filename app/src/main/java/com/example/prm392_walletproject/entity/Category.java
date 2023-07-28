package com.example.prm392_walletproject.entity;

import android.graphics.drawable.Icon;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int cid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "status")
    private boolean status;

    @ColumnInfo(name = "default")
    private boolean defaultCategory = false;

    public Category() {
    }

    public Category(String name, String icon, boolean status) {
        this.name = name;
        this.icon = icon;
        this.status = status;
    }

    public Category(int cid, String name, String icon, boolean status) {
        this.cid = cid;
        this.name = name;
        this.icon = icon;
        this.status = status;
    }

    public Category(int cid, String name, String icon, boolean status, boolean defaultCategory) {
        this.cid = cid;
        this.name = name;
        this.icon = icon;
        this.status = status;
        this.defaultCategory = defaultCategory;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {return icon;}

    public void setIcon(String icon) {this.icon = icon;}

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
    }
}
