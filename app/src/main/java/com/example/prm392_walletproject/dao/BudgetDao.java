package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_walletproject.entity.Budget;

import java.util.List;

@Dao
public interface BudgetDao {
    @Query("SELECT * FROM Budget")
    List<Budget> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Budget... budgets);

    @Query("SELECT * FROM budget WHERE bid = :id")
    Budget checkDuplicate(int id);

    @Query("SELECT * FROM budget WHERE account_id = :id")
    List<Budget> getByAccountId(int id);

    @Update
    void updateBudget(Budget budget);

    @Delete
    void deleteBudget(Budget budget);

    @Query("SELECT * FROM Budget WHERE account_id = :id")
    List<Budget> getAllByAccount(String id);

    @Delete
    void delete(Budget budget);
}
