package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    private int rid;

    @ColumnInfo(name = "account_id")
    private int accountId;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "amount")
    private Long amount;

    @ColumnInfo(name = "record_type_id")
    private int recordTypeId;

    @ColumnInfo(name = "created_time")
    private String createdTime;

    @ColumnInfo(name = "account_transfer_id")
    private int accountTransferId;

    @ColumnInfo(name = "is_from_acc")
    private boolean isFromAcc;

    public Record() {
    }

    public Record(int accountId, int categoryId, Long amount, int recordTypeId, String createdTime, int accountTransferId, boolean isFromAcc) {
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.recordTypeId = recordTypeId;
        this.createdTime = createdTime;
        this.accountTransferId = accountTransferId;
        this.isFromAcc = isFromAcc;
    }

    public Record(int accountId, int categoryId, String description, Long amount, int recordTypeId, String createdTime) {
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.recordTypeId = recordTypeId;
        this.createdTime = createdTime;
    }

    public Record(int rid, int accountId, int categoryId, String description, Long amount, int recordTypeId, String createdTime) {
        this.rid = rid;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.recordTypeId = recordTypeId;
        this.createdTime = createdTime;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getAccountTransferId() {
        return accountTransferId;
    }

    public void setAccountTransferId(int accountTransferId) {
        this.accountTransferId = accountTransferId;
    }

    public boolean isFromAcc() {
        return isFromAcc;
    }

    public void setFromAcc(boolean fromAcc) {
        isFromAcc = fromAcc;
    }
}
