package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.RecordListAdapter;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.dao.CategoryDao;
import com.example.prm392_walletproject.dao.RecordDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Record_Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    BottomNavigationView botomNav;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView tv_income_total_num;
    TextView tv_expense_total_num;
    TextView tv_total_sum;
    TextView tv_results_num;
    TextView tv_empty;
    ImageView iv_back;
    CardView cardView;

    LinkedHashMap<String, List<Record_Category>> data;
    RecordListAdapter adapter;

    AppDatabase db;
    RecordDao recordDao;
    AccountDao accountDao;
    CategoryDao categoryDao;

    private String priceWithDecimal (Integer price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    private void filter(String search) {
        data.clear();
        search = search.toLowerCase(Locale.getDefault());

        List<Record> records = recordDao.getAll();
        List<Record_Category> recordCategoryList = new ArrayList<>();
        for (Record record: records) {
            Record_Category record_category;
            if (record.getRecordTypeId() == 2) {
                int fromAccId = record.getAccountTransferId();
                if (record.isFromAcc()) {
                    fromAccId = record.getAccountId();
                }

                int toAccId = record.getAccountId();
                if (record.isFromAcc()) {
                    toAccId = record.getAccountTransferId();
                }
                record_category = new Record_Category(record,
                        categoryDao.getCateById(record.getCategoryId()),
                        accountDao.getAccountById(fromAccId),
                        accountDao.getAccountById(toAccId));
            } else {
                record_category = new Record_Category(record, categoryDao.getCateById(record.getCategoryId()));
            }

            if (search.length() == 0) {
                recordCategoryList.add(record_category);
            } else {
                Category category = record_category.getCategory();
                if (category.getName().toLowerCase(Locale.getDefault()).contains(search)
                        || (record.getDescription() != null && record.getDescription().toLowerCase(Locale.getDefault()).contains(search))
                        || record.getAmount().toString().toLowerCase(Locale.getDefault()).contains(search)) {
                    recordCategoryList.add(record_category);
                }
            }
        }

        if (recordCategoryList.isEmpty()) {
            tv_empty.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        } else {
            tv_empty.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            Collections.sort(recordCategoryList, new Comparator<Record_Category>() {
                @Override
                public int compare(Record_Category record_category, Record_Category record_category1) {
                    Timestamp time = Timestamp.valueOf(record_category.getRecord().getCreatedTime());
                    Timestamp time1 = Timestamp.valueOf(record_category1.getRecord().getCreatedTime());
                    return time1.compareTo(time);
                }
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < recordCategoryList.size(); i++) {
                Record_Category record_category = recordCategoryList.get(i);
                Record record = record_category.getRecord();
                Timestamp time = Timestamp.valueOf(record.getCreatedTime());
                String date = dateFormat.format(time);
                if (data.containsKey(date)) {
                    data.get(date).add(record_category);
                } else {
                    List<Record_Category> newList = new ArrayList<>();
                    newList.add(record_category);
                    data.put(date, newList);
                }
            }
        }
    }

    private void setRecyclerView(String search) {
        filter(search);
        adapter = new RecordListAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        int incomeNum = 0;
        int expenseNum = 0;
        int resultNum = 0;
        for (Map.Entry<String, List<Record_Category>> e : data.entrySet()) {
            List<Record_Category> value = e.getValue();
            for (Record_Category record_category: value) {
                Record record = record_category.getRecord();
                resultNum++;
                switch (record.getRecordTypeId()) {
                    case 0:
                        expenseNum += record.getAmount();
                        break;
                    case 1:
                        incomeNum += record.getAmount();
                        break;
                    case 2:
                        if (record.isFromAcc()) {
                            expenseNum += record.getAmount();
                        } else {
                            incomeNum += record.getAmount();
                        }
                }
            }
        }
        int sum = incomeNum - expenseNum;
        tv_income_total_num.setText(priceWithDecimal(incomeNum));
        tv_expense_total_num.setText(priceWithDecimal(expenseNum));
        tv_total_sum.setText(priceWithDecimal(sum));
        tv_results_num.setText(resultNum + " results");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getInstance(requireContext().getApplicationContext());
        recordDao = db.recordDao();
        accountDao = db.accountDao();
        categoryDao = db.cateDao();

        botomNav = getActivity().findViewById(R.id.bottom_nav);
        botomNav.setVisibility(View.GONE);

        data = new LinkedHashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.rv_search);
        searchView = view.findViewById(R.id.searchview);
        tv_income_total_num = view.findViewById(R.id.tv_income_total_num);
        tv_expense_total_num = view.findViewById(R.id.tv_expense_total_num);
        tv_total_sum = view.findViewById(R.id.tv_total_sum);
        tv_results_num = view.findViewById(R.id.tv_results_num);
        tv_empty = view.findViewById(R.id.tv_empty);
        iv_back = view.findViewById(R.id.iv_back);
        cardView = view.findViewById(R.id.cardView);

        searchView.setOnQueryTextListener(this);
        setRecyclerView("");

        // close
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botomNav.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    botomNav.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText.trim();
        setRecyclerView(text);

        return false;
    }
}