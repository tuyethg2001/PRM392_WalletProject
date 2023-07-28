package com.example.prm392_walletproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.RecordType;
import com.example.prm392_walletproject.entity.Template;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static boolean open = false;
    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ListRecordFragment listRecordFragment = new ListRecordFragment();
    ListBudgetFragment listBudgetFragment = new ListBudgetFragment();
    ListTemplateFragment listTemplateFragment = new ListTemplateFragment(this);
    SettingFragment settingFragment = new SettingFragment();
    CreateCategoryFragment createCategoryFragment = new CreateCategoryFragment();
    ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
    ReminderFragment reminderFragment = new ReminderFragment();
    GetDataFromCategoryFragment getDataFromCategoryFragment = new GetDataFromCategoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, homeFragment).commit();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnav_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl_container, homeFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.btnav_category:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl_container, listCategoryFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.btnav_budget:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl_container, listBudgetFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.btnav_record:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl_container, listRecordFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.btnav_template:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl_container, listTemplateFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }

    public void setData() {
        // Account
        if (AppDatabase.getInstance(getApplicationContext()).accountDao().getAll().size() == 0) {
            Account account = new Account();
            account.setName("Tien mat");
            account.setValue(100000L);
            account.setColor(String.valueOf(getApplicationContext().getColor(R.color.teal_700)));
            account.setIcon("ic_wallet");
            AppDatabase.getInstance(getApplicationContext()).accountDao().insertAccount(account);

            account = new Account();
            account.setName("TPBank");
            account.setValue(3000000L);
            account.setColor(String.valueOf(getApplicationContext().getColor(R.color.teal_700)));
            account.setIcon("ic_clock");
            AppDatabase.getInstance(getApplicationContext()).accountDao().insertAccount(account);

            account = new Account();
            account.setName("MB Bank");
            account.setValue(5000000L);
            account.setColor(String.valueOf(getApplicationContext().getColor(R.color.teal_700)));
            account.setIcon("ic_clock");
            AppDatabase.getInstance(getApplicationContext()).accountDao().insertAccount(account);
        }

        // Cate
        if (AppDatabase.getInstance(getApplicationContext()).cateDao().getAll().size() == 0) {
            Category a = new Category(1, "Transfer, withdraw", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FTransfer_default.png?alt=media&token=01d956d6-f6b0-4105-9401-6620a2f1bebb", true, true);
            Category b = new Category(2, "Food", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FFood_default.png?alt=media&token=6753cfd4-9ec7-419b-9524-95521c5268d6", true, true);
            Category c = new Category(3, "House", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FHouse_default.png?alt=media&token=61af5f3c-7e6a-431c-87ac-111666494626", true, true);
            Category d = new Category(4, "Vehicle", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FVehicle_default.png?alt=media&token=07143aff-b164-4390-aeb8-8d51c391f1f7", true, true);
            Category e = new Category(5, "Financial", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FFinancial_default.png?alt=media&token=17ee90f0-6369-49bb-b8e4-7a916218b072", true, true);
            Category f = new Category(6, "Life", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FLife_default.png?alt=media&token=628d56ca-af2f-4bcb-a64c-9c7428355903", true, true);
            Category g = new Category(7, "Device", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FDevices_default.png?alt=media&token=e713ffdc-4264-4e5b-b68a-450dd909ba87", true, true);
            Category h = new Category(8, "Clothe", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FClothe_default.png?alt=media&token=88b28047-ef60-4e24-bd0e-5304b33433db", true, true);
            Category i = new Category(9, "Equipment", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FEquipment_default.png?alt=media&token=bd8720d3-10c2-4c28-8adf-f201278752b3", true, true);
            Category j = new Category(10, "Education", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FEducation_default.png?alt=media&token=46fe6d45-6ad1-4aed-a586-3eb358bf410b", true, true);
            Category k = new Category(11, "Book", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FBook_default.png?alt=media&token=dd238a66-ea17-4ab3-b58e-c004aebdc401", true, true);
            AppDatabase.getInstance(getApplicationContext()).cateDao().insertAll(a, b, c, d, e, f, g, h, i, j, k);
        }

        //Record type
        if (AppDatabase.getInstance(getApplicationContext()).recTypeDao().getAll().size() == 0) {
            RecordType rct = new RecordType();
            rct.setRtid(0);
            rct.setName("Expense");
            AppDatabase.getInstance(getApplicationContext()).recTypeDao().insertRecType(rct);

            rct.setRtid(1);
            rct.setName("Income");
            AppDatabase.getInstance(getApplicationContext()).recTypeDao().insertRecType(rct);

            rct.setRtid(2);
            rct.setName("Transfer");
            AppDatabase.getInstance(getApplicationContext()).recTypeDao().insertRecType(rct);
        }

        // Template
        if (AppDatabase.getInstance(getApplicationContext()).tempDao().getAll().size() == 0) {
            Template template = new Template();
            template.setName("Ăn sáng");
            template.setAmount(30000L);
            template.setAccountId(1);
            template.setCategoryId(1);
            template.setRecordTypeId(0);
            template.setNote("Tiền ăn sáng t2-6");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Lương");
            template.setAmount(5000000L);
            template.setAccountId(2);
            template.setCategoryId(5);
            template.setRecordTypeId(1);
            template.setNote("Lương cứng hàng tháng");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Mua quần áo");
            template.setAmount(1000000L);
            template.setAccountId(2);
            template.setCategoryId(8);
            template.setRecordTypeId(0);
            template.setNote("");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Tiền đóng học");
            template.setAmount(27300000L);
            template.setAccountId(2);
            template.setCategoryId(10);
            template.setRecordTypeId(0);
            template.setNote("Tiền học 1 kỳ");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Thuê trọ");
            template.setAmount(1500000L);
            template.setAccountId(3);
            template.setCategoryId(3);
            template.setRecordTypeId(0);
            template.setNote("1 tháng");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Tiền mạng");
            template.setAmount(90000L);
            template.setAccountId(3);
            template.setCategoryId(9);
            template.setRecordTypeId(0);
            template.setNote("1 tháng");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Ăn trưa");
            template.setAmount(35000L);
            template.setAccountId(1);
            template.setCategoryId(2);
            template.setRecordTypeId(0);
            template.setNote("mỗi ngày");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);

            template = new Template();
            template.setName("Ăn tối");
            template.setAmount(35000L);
            template.setAccountId(1);
            template.setCategoryId(2);
            template.setRecordTypeId(0);
            template.setNote("mỗi ngày");
            AppDatabase.getInstance(getApplicationContext()).tempDao().createTemplate(template);
        }


        // Record
        if (AppDatabase.getInstance(getApplicationContext()).recordDao().getAll().size() == 0) {
            List<Record> records = new ArrayList<>();
            Record record = new Record();
            record.setAccountId(1);
            record.setCategoryId(2);
            record.setAmount(Long.valueOf(1000000));
            record.setRecordTypeId(1);
            record.setCreatedTime("2022-10-23 06:30:00");
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(1);
            record.setCategoryId(3);
            record.setAmount(Long.valueOf(1000000));
            record.setRecordTypeId(0);
            record.setCreatedTime("2022-10-23 12:12:00");
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(2);
            record.setCategoryId(5);
            record.setAmount(Long.valueOf(200000));
            record.setRecordTypeId(0);
            record.setCreatedTime("2022-08-25 18:25:30");
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(2);
            record.setCategoryId(5);
            record.setAmount(Long.valueOf(50000));
            record.setRecordTypeId(1);
            record.setCreatedTime("2022-08-25 06:25:30");
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(1);
            record.setCategoryId(1);
            record.setAmount(Long.valueOf(10000));
            record.setRecordTypeId(2);
            record.setCreatedTime("2022-08-25 23:00:30");
            record.setFromAcc(true);
            record.setAccountTransferId(2);
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(2);
            record.setCategoryId(1);
            record.setAmount(Long.valueOf(10000));
            record.setRecordTypeId(2);
            record.setCreatedTime("2022-08-25 23:00:30");
            record.setFromAcc(false);
            record.setAccountTransferId(1);
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(1);
            record.setCategoryId(1);
            record.setAmount(Long.valueOf(200000));
            record.setRecordTypeId(2);
            record.setCreatedTime("2022-08-20 23:00:30");
            record.setFromAcc(true);
            record.setAccountTransferId(2);
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);

            record.setAccountId(2);
            record.setCategoryId(1);
            record.setAmount(Long.valueOf(200000));
            record.setRecordTypeId(2);
            record.setCreatedTime("2022-08-20 23:00:30");
            record.setFromAcc(false);
            record.setAccountTransferId(1);
            AppDatabase.getInstance(getApplicationContext()).recordDao().insert(record);
        }

        //Budget
        if (AppDatabase.getInstance(getApplicationContext()).budgetDao().getAll().size() == 0) {
            Budget b = new Budget(2, "New Car", Long.valueOf(1000000000), 4, "22 / 10 / 2022", "23 / 10 / 2022", "24 / 11 / 2022", false);
            Budget c = new Budget(1, "New House", new Long(2000000000), 3, "23 / 10 / 2022", "24 / 10 / 2022", "24 / 11 / 2024", false);
            Budget d = new Budget(2, "New PC", Long.valueOf(100000000), 7, "21 / 10 / 2022", "23 / 10 / 2022", "24 / 10 / 2023", false);
            Budget e = new Budget(1, "Vacation", Long.valueOf(1000000000), 6, "20 / 10 / 2022", "23 / 11 / 2022", "24 / 11 / 2023", false);
            Budget f = new Budget(2, "Holiday", new Long(2000000000), 6, "22 / 9 / 2022", "24 / 10 / 2022", "24 / 11 / 2022", false);
            AppDatabase.getInstance(getApplicationContext()).budgetDao().insertAll(b, c, d, e, f);
        }
    }

}