package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Template {
    @PrimaryKey(autoGenerate = true)
    private int tid;

    @ColumnInfo(name = "account_id")
    private int accountId;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "amount")
    private Long amount;

    @ColumnInfo(name = "record_type_id")
    private int recordTypeId;

    @ColumnInfo(name = "note")
    private String note;

    public Template() {
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public int getRecordTypeId() {
        return recordTypeId;
    }

    public void setRecordTypeId(int recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
