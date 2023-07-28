package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY cid ASC")
    List<Category> getAll();

    @Query("SELECT * FROM category WHERE cid == :id")
    Category getCateById(int id);

    @Query("SELECT icon FROM category WHERE cid == :id")
    int getCategoryById(int id);

    @Query("SELECT COUNT(cid) FROM category")
    int getCategorySize();

    @Query("SELECT name FROM category ORDER BY cid ASC")
    String[] getAllName();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... categories);

    @Query("SELECT * FROM Category WHERE status = 1")
    List<Category> getAllByStatus();
}
