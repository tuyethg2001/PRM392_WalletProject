package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_walletproject.entity.Record;

import java.sql.Date;
import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT MIN(created_time) FROM record " +
            "WHERE CASE WHEN :accountId = 0 THEN 1 = 1 ELSE account_id = :accountId END")
    String getMinCreatedDate(int accountId);

    @Query("SELECT * FROM record " +
            "WHERE CAST((strftime('%m', created_time)) AS INTEGER) = :month AND CAST((strftime('%Y', created_time)) AS INTEGER)= :year " +
            "AND CASE WHEN :accountId = 0 THEN 1 = 1 ELSE account_id = :accountId END")
    List<Record> getByMonth(int month, int year, int accountId);

    @Query("SELECT * FROM record WHERE rid = :id")
    Record getById(int id);

    @Query("SELECT * FROM record " +
            "WHERE account_id = :accTransferId " +
                "AND created_time = :createdTime " +
                "AND account_transfer_id = :accId")
    Record getTransferRecord(int accId, int accTransferId, String createdTime);

    @Query("SELECT * FROM record")
    List<Record> getAll();

    @Query("SELECT * FROM record WHERE account_id = :id")
    List<Record> getByAccountId(int id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Record> records);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(Record record);

    @Delete
    void delete(Record record);

}
