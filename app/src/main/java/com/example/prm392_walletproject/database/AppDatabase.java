package com.example.prm392_walletproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//import com.example.prm392_walletproject.converter.Converters;
import com.example.prm392_walletproject.dao.BudgetDao;
import com.example.prm392_walletproject.dao.CategoryDao;
import com.example.prm392_walletproject.dao.UserDao;
import com.example.prm392_walletproject.dao.ReminderDao;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.dao.RecordDao;
import com.example.prm392_walletproject.dao.RecordTypeDao;
import com.example.prm392_walletproject.dao.TemplateDao;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.User;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.RecordType;
import com.example.prm392_walletproject.entity.Reminder;
import com.example.prm392_walletproject.entity.Template;
import com.example.prm392_walletproject.entity.Template_Account;

@Database(entities = {Account.class, Budget.class, Category.class, Record.class, RecordType.class,
        Template.class, Template_Account.class, User.class, Reminder.class}, version = 6)
//@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "wallet.db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract AccountDao accountDao();
    public abstract TemplateDao tempDao();
    public abstract CategoryDao cateDao();
    public abstract BudgetDao budgetDao();
    public abstract UserDao userDao();
    public abstract ReminderDao reminderDao();
    public abstract RecordDao recordDao();
    public abstract RecordTypeDao recTypeDao();
}
