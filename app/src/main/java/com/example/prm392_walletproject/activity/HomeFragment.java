package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.AccountAdapter;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    TextView add_account_button, totalbalance;
    ImageView find, set;
    AppDatabase db;
    Long total;
    Locale loc = Locale.getDefault();
    NumberFormat nf = NumberFormat.
            getCurrencyInstance(loc);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
        List<Account> list = db.accountDao().getAll();
        RecyclerView recyclerView = view.findViewById(R.id.accountRecycleView);
        totalbalance = view.findViewById(R.id.totalbalance);
        total = db.accountDao().getTotalAmount();
        totalbalance.setText(nf.format(total)+"");


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new AccountAdapter(getActivity().getApplicationContext(), list, this));
        //add Account
        add_account_button = view.findViewById(R.id.add_account_button);
        add_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new AddAcountFragment()).addToBackStack("HomeFragment").commit();
            }
        });

        //find
        find = view.findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SearchFragment()).addToBackStack("HomeFragment").commit();
            }
        });

        //setting
        set = view.findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SettingFragment()).addToBackStack("HomeFragment").commit();
            }
        });

        Toolbar toolbar = view.findViewById(R.id.top_nav);

        // settings
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.topnav_setting:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SettingFragment()).commit();
                        return true;
                }
                return false;
            }
        });

        return view;
    }

}