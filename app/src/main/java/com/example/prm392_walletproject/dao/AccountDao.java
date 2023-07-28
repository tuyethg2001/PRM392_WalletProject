package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Template_Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account ORDER BY aid ASC")
    List<Account> getAll();

    @Query("SELECT * FROM account WHERE aid == :id")
    Account getAccountById(int id);

    @Query("SELECT SUM(VALUE) FROM account")
    Long getTotalAmount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Account> accounts);

    @Query("SELECT name FROM account ORDER BY aid ASC")
    String[] getAllName();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(Account account);

    @Update
    void updateAccount(Account account);

    @Delete
    void deleteAccount(Account account);

    @Query("DELETE FROM account WHERE aid = :id")
    void deleteById(int id);
}
