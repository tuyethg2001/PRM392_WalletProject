package com.example.prm392_walletproject.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.TypeAdapter;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Type;

import java.util.ArrayList;
import java.util.List;

public class AddAcountFragment extends Fragment {

    private Spinner spinner;
    private TypeAdapter typeAdapter;
    ImageView addAccount, cancelAddAccount;
    EditText input_accname, input_accnumber, input_value, current_value;
    Spinner spinner_category;
    Type cate;
    AccountDao adb;
    String accName, accNumber, accValue;
//    AppDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_account, container, false);
//        db = Room.databaseBuilder(getActivity().getApplicationContext(),
//                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();


        spinner = view.findViewById(R.id.spinner_category);
        typeAdapter = new TypeAdapter(getContext(), R.layout.type_list_select, getListCategory());
        spinner.setAdapter(typeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        input_accname = view.findViewById(R.id.input_accname);
        input_accnumber = view.findViewById(R.id.input_accnumber);
        input_value = view.findViewById(R.id.input_value);


        //Thêm Account

        addAccount = view.findViewById(R.id.updateAccount);
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accName = input_accname.getText().toString().trim().replaceAll("\\s+", " ");;
                accNumber = input_accnumber.getText().toString().trim();
                accValue = input_value.getText().toString().trim();
                cate = (Type) spinner.getSelectedItem();
                if (check()){
                    Account a = new Account(accName, accNumber,
                            cate.getName(), Long.parseLong(accValue), String.valueOf(getActivity().getColor(R.color.teal_700))); // chua chon color, de tam
                    AppDatabase.getInstance(getContext()).accountDao().insertAccount(a);
                    Log.d("Add account", "Success");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                }
            }
        });

        //Bỏ thêm account, quay lại Home

        cancelAddAccount = view.findViewById(R.id.cancelUpdateAccount);
        cancelAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
            }
        });


        return view;
    }

    private List<Type> getListCategory(){
        List<Type> list = new ArrayList<>();
        list.add(new Type("General"));
        list.add(new Type("Cash"));
        list.add(new Type("Current account"));
        list.add(new Type("Credit Card"));
        list.add(new Type("Saving account"));
        list.add(new Type("Bonus"));
        list.add(new Type("Insurance"));
        list.add(new Type("Investment"));
        list.add(new Type("Loan"));
        list.add(new Type("Mortgage"));
        list.add(new Type("Account with overdraft"));
        return list;
    }

    private boolean check(){
        if (accName == null || accName.length() == 0){
            input_accname.setError("Account name is required!");
            return false;
        }
        if (accNumber == null || accNumber.length() == 0){
            input_accnumber.setError("Account number is required!");
            return false;
        }
        if (accValue == null || accValue.length() == 0){
            input_value.setError("Please input value!");
            return false;
        }

        return true;
    }

}
