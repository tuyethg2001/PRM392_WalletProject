package com.example.prm392_walletproject.entity;

public class Record_Category {

    private Record record;
    private Category category;
    private Account fromAccount;
    private Account toAccount;

    public Record_Category(Record record, Category category) {
        this.record = record;
        this.category = category;
    }

    public Record_Category(Record record, Category category, Account fromAccount, Account toAccount) {
        this.record = record;
        this.category = category;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }
}
