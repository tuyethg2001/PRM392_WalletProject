package com.example.prm392_walletproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_walletproject.entity.Template;
import com.example.prm392_walletproject.entity.Template_Account;

import java.util.List;

@Dao
public interface TemplateDao {
    @Query("SELECT * FROM template")
    List<Template> getAll();

    @Query("SELECT name FROM template")
    String[] getAllName();

    @Query("SELECT * FROM template WHERE LOWER(name) LIKE :name AND account_id == :accountId")
    List<Template> checkDuplicate(String name, int accountId);

    @Query("SELECT * FROM template WHERE tid == :id")
    Template getTemplateById(int id);

    @Query("SELECT * FROM template WHERE account_id == :id")
    List<Template> getTemplateByAccountId(int id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void createTemplate(Template template);

    @Insert
    void insertTempAccount(Template_Account template_account);

    @Update
    void updateTemplate(Template template);

    @Update
    void updateTempAccount(Template_Account template_account);

    @Delete
    void deleteTemplate(Template template);

    @Delete
    void deleteTempAccount(Template_Account template_account);
}