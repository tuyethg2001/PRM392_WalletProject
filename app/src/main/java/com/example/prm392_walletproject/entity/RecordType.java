package com.example.prm392_walletproject.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordType {
    @PrimaryKey
    private int rtid;

    @ColumnInfo(name = "name")
    private String name;

    public RecordType() {

    }

    public RecordType(int rtid, String name) {
        this.rtid = rtid;
        this.name = name;
    }

    public int getRtid() {
        return rtid;
    }

    public void setRtid(int rtid) {
        this.rtid = rtid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
