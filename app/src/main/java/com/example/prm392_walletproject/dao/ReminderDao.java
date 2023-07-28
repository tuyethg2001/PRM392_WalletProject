package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_walletproject.entity.Reminder;

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reminder reminder);

    @Query("SELECT * FROM Reminder WHERE reminder_id = 1")
    Reminder getReminder();
}
