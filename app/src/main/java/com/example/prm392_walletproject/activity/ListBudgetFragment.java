package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.BudgetAdapter;
import com.example.prm392_walletproject.adapter.RecordSpinnerAdapter;
import com.example.prm392_walletproject.adapter.TemplateAdapter;
import com.example.prm392_walletproject.dao.BudgetDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ListBudgetFragment extends Fragment implements AdapterView.OnItemClickListener {
    Toolbar toolbar;

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
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_list_budget, container, false);
        BudgetCreate budgetCreate = new BudgetCreate();
        ((FloatingActionButton) view.findViewById(R.id.fab_budgetlist_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, budgetCreate).addToBackStack("ListBudgetFragment").commit();
            }
        });
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
//         Add to demo
//        if(db.budgetDao().getAll().size()==0) {
//            Budget b = new Budget(2, "New Car", Long.valueOf(1000000000), 2, "22 / 10 / 2022", "23 / 10 / 2022", "24 / 11 / 2022", false);
//            Budget c = new Budget(2, "New House", new Long(2000000000), 2, "22 / 10 / 2022", "24 / 10 / 2022", "24 / 11 / 2022", false);
//            Budget d = new Budget(2, "New PC", Long.valueOf(100000000), 2, "22 / 10 / 2022", "23 / 10 / 2022", "24 / 10 / 2023", false);
//            db.budgetDao().insertAll(b, c, d);
//        }
//
//        List<Account> listTemp = new ArrayList<>();
//        Account account = new Account(1, "Long", 1, "type", Long.parseLong("1000000"), "color", "icon");
//        Account account1 = new Account(2, "Tuyet", 1, "type", Long.parseLong("1000000"), "color", "icon");
//        listTemp.add(account);
//        listTemp.add(account1);
//        AppDatabase.getInstance(getContext()).accountDao().insertAll(listTemp);

        List<Account> listAccount = new ArrayList<>();
        Account all = new Account();
        all.setName("All");
        all.setAid(0);
        listAccount.add(all);
        listAccount.addAll(db.accountDao().getAll());
        Spinner dropdown = view.findViewById(R.id.sp_budgetList);

        RecordSpinnerAdapter recordSpinnerAdapter = new RecordSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, listAccount);
        dropdown.setAdapter(recordSpinnerAdapter);


//        Category category=new Category();
//        category.setName("name");
//        category.setIcon(String.valueOf(R.drawable.ic_category));
//        AppDatabase.getInstance(getContext()).cateDao().insertCategory(category);



        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v,
                                       int position, long id) {
                Account item = (Account) dropdown.getSelectedItem();
                if (item != null) {
                    List<Budget> budgetList;
                    if (item.getAid() == 0) {
                        budgetList = db.budgetDao().getAll();
                    } else {
                        budgetList = db.budgetDao().getAllByAccount(String.valueOf(item.getAid()));
                    }
                    RecyclerView recyclerView = view.findViewById(R.id.rec_budgetList);
                    BudgetAdapter adapter = new BudgetAdapter(budgetList, ListBudgetFragment.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        // set up toolbar
        toolbar = view.findViewById(R.id.top_nav);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        setHasOptionsMenu(true);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_search);
            activity.getSupportActionBar().setTitle("");
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}