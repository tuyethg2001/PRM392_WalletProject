package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"budget_id, account_id"})
public class Budget_Account {
    @ColumnInfo(name = "budget_id")
    private int budgetId;

    @ColumnInfo(name = "account_id")
    private int accountId;

    public Budget_Account(int bid, int accountId) {
        this.budgetId = bid;
        this.accountId = accountId;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
