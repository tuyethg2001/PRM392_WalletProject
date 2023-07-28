package com.example.prm392_walletproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.RecordSpinnerAdapter;
import com.example.prm392_walletproject.adapter.RecordTabAdapter;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.dao.CategoryDao;
import com.example.prm392_walletproject.dao.RecordDao;
import com.example.prm392_walletproject.dao.RecordTypeDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.RecordType;
import com.example.prm392_walletproject.entity.Record_Category;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListRecordFragment extends Fragment {
    LinkedHashMap<String, List<Record_Category>> data;
    ViewPager2 recordListViewPager;
    TextView tv_balance;
    TextView tv_empty_1;
    TextView tv_empty_2;
    Spinner spinner;
    RecordTabAdapter recordListItemAdapter;
    FloatingActionButton btn_add_record;
    FloatingActionButton btn_transfer;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    AppDatabase db;
    RecordDao recordDao;
    AccountDao accountDao;
    CategoryDao categoryDao;

    private String priceWithDecimal (Long price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price) + " VND";
    }

    private List<Record_Category> getRecordCategoryList(int month, int year, int accountId) {
        List<Record> records = recordDao.getByMonth(month, year, accountId);
        List<Record_Category> recordCategoryList = new ArrayList<>();
        for (Record record: records) {
            if (record.getRecordTypeId() == 2) {
                int fromAccId = record.getAccountTransferId();
                if (record.isFromAcc()) {
                    fromAccId = record.getAccountId();
                }

                int toAccId = record.getAccountId();
                if (record.isFromAcc()) {
                    toAccId = record.getAccountTransferId();
                }
                Record_Category record_category = new Record_Category(record,
                                                                    categoryDao.getCateById(record.getCategoryId()),
                                                                    accountDao.getAccountById(fromAccId),
                                                                    accountDao.getAccountById(toAccId));
                recordCategoryList.add(record_category);
            } else {
                Record_Category record_category = new Record_Category(record, categoryDao.getCateById(record.getCategoryId()));
                recordCategoryList.add(record_category);
            }
        }

        return recordCategoryList;
    }

    private void getData(int accountId) {
        data.clear();
        Calendar cal = Calendar.getInstance();

        String time = recordDao.getMinCreatedDate(accountId);
        if (time == null) {
            return;
        }

        Timestamp minDate = Timestamp.valueOf(time);
        cal.setTime(minDate);
        int minMonth = cal.get(Calendar.MONTH) + 1;
        int minYear = cal.get(Calendar.YEAR);

        cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);

        cal.add(Calendar.MONTH, -1);
        int boundMonth = cal.get(Calendar.MONTH) + 1;
        int boundYear = cal.get(Calendar.YEAR);

        cal.setTime(minDate);
        if (!(minMonth == currentMonth && minYear == currentYear)) {
            SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
            for (int year = minYear; year <= boundYear; year++) {
                for (int month = 1; month <= 12; month++) {
                    if (year == minYear && month < minMonth) {
                        continue;
                    }

                    data.put(format.format(cal.getTime()), getRecordCategoryList(month, year, accountId));

                    if (year == boundYear && month == boundMonth) {
                        break;
                    }
                    cal.add(Calendar.MONTH, 1);
                }
            }
        }
        data.put("Current month", getRecordCategoryList(currentMonth, currentYear, accountId));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new LinkedHashMap<>();
        db = AppDatabase.getInstance(requireContext().getApplicationContext());
        recordDao = db.recordDao();
        accountDao = db.accountDao();
        categoryDao = db.cateDao();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.topnav_setting:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new SettingFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            case android.R.id.home:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new SearchFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_list_record, container, false);

        spinner = view.findViewById(R.id.spinner_account);
        tv_balance = view.findViewById(R.id.tv_balance);
        tv_empty_1 = view.findViewById(R.id.tv_empty_1);
        tv_empty_2 = view.findViewById(R.id.tv_empty_2);
        toolbar = view.findViewById(R.id.top_nav);

        // set up toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        setHasOptionsMenu(true);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_search);
            activity.getSupportActionBar().setTitle("");
        }

        List<Account> accounts = new ArrayList<>();
        Account all = new Account();
        all.setName("All");
        all.setAid(0);
        accounts.add(all);

        List<Account> accountList = accountDao.getAll();
        if (!accountList.isEmpty()) {
            accounts.addAll(accountDao.getAll());
        }

        RecordSpinnerAdapter recordSpinnerAdapter = new RecordSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, accounts);
        spinner.setAdapter(recordSpinnerAdapter);

        recordListViewPager = view.findViewById(R.id.recordListViewPager);
        TabLayout recordListTabLayout = view.findViewById(R.id.recordListTabLayout);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Account account = (Account) spinner.getSelectedItem();
                getData(account.getAid());

                long allBalance = 0;
                if (!accountList.isEmpty()) {
                    for (int index = 1; index < accounts.size(); index++) {
                        allBalance += accounts.get(index).getValue();
                    }
                }
                all.setValue(allBalance);

                tv_balance.setText(priceWithDecimal(account.getValue()));
                if (data.isEmpty()) {
                    recordListTabLayout.setVisibility(View.GONE);
                    tv_empty_1.setVisibility(View.VISIBLE);
                    tv_empty_2.setVisibility(View.VISIBLE);
                } else {
                    recordListTabLayout.setVisibility(View.VISIBLE);
                    tv_empty_1.setVisibility(View.GONE);
                    tv_empty_2.setVisibility(View.GONE);

                    recordListItemAdapter = new RecordTabAdapter( data, recordListViewPager);
                    recordListViewPager.setAdapter(recordListItemAdapter);
                    recordListViewPager.setCurrentItem(recordListItemAdapter.getItemCount() - 1);
                    new TabLayoutMediator(
                            recordListTabLayout,
                            recordListViewPager,
                            new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                    int index = 0;
                                    for (Map.Entry<String,List<Record_Category>> e : data.entrySet() ) {
                                        String key = e.getKey();
                                        if (index == position) {
                                            tab.setText(key);
                                            break;
                                        }
                                        index++;
                                    }
                                }
                            }
                    ).attach();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // new record button
        btn_add_record = view.findViewById(R.id.btn_add_record);
        btn_add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new RecordCreateFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // transfer
        btn_transfer = view.findViewById(R.id.btn_transfer);
        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new RecordCreateTransferFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}