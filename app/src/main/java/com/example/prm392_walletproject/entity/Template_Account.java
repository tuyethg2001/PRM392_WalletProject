package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys={"template_id", "account_id"})
public class Template_Account {
    @ColumnInfo(name = "template_id")
    private int templateId;

    @ColumnInfo(name = "account_id")
    private int accountId;

    public Template_Account(int templateId, int accountId) {
        this.templateId = templateId;
        this.accountId = accountId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
