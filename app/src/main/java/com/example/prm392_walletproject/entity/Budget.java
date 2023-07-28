package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int bid;

    @ColumnInfo(name = "account_id")
    private int accountId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "amount")
    private Long amount;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "created_date")
    private String createdDate;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    @ColumnInfo(name = "is_notified")
    private boolean isNotified;

    public Budget() {

    }

    public Budget(int accountId, String name, Long amount, int categoryId, String createdDate, String startDate, String endDate, boolean isNotified) {
        this.accountId = accountId;
        this.name = name;
        this.amount = amount;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isNotified = isNotified;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }
}
