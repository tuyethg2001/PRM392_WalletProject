package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_walletproject.entity.RecordType;

import java.util.List;

@Dao
public interface RecordTypeDao {
    @Query("SELECT * FROM recordtype ORDER BY rtid ASC")
    List<RecordType> getAll();

    @Query("SELECT * FROM recordtype WHERE rtid == :id")
    RecordType getTRecTypeById(int id);

    @Query("SELECT name FROM recordtype ORDER BY rtid ASC LIMIT 2")
    String[] getAllNameToAddTemplate();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertRecType(RecordType recordType);
}
