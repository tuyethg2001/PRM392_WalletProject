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
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.TypeAdapter;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Template;
import com.example.prm392_walletproject.entity.Type;

import java.util.ArrayList;
import java.util.List;

public class EditAccountFragment extends Fragment {

    private Account account;

    public EditAccountFragment(Account account) {
        this.account = account;
    }

    private Spinner spinner;
    private TypeAdapter typeAdapter;
    ImageView updateAccount, cancelUpdateAccount, deleteAccount;
    EditText input_accname, input_accnumber, input_value;
    Spinner spinner_category;
    Type cate;
    AccountDao adb;
    String accName, accNumber, accValue;
    //    AppDatabase db;
    int id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String id_info = null;
        if (bundle != null) {
            id_info = bundle.getString("id");
        }

        if (id_info == null || id_info.equals("")) {
            id = -1;
        } else {
            id = Integer.parseInt(id_info);
        }

        View view = inflater.inflate(R.layout.edit_account, container, false);
//        db = Room.databaseBuilder(getActivity().getApplicationContext(),
//                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();


        input_accname = view.findViewById(R.id.input_accname);
        input_accnumber = view.findViewById(R.id.input_accnumber);
        input_value = view.findViewById(R.id.input_value);

        input_accname.setText(account.getName());
        input_accnumber.setText(account.getBankAccount() + "");
        input_value.setText(account.getValue() + "");


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

        //Thêm Account

        updateAccount = view.findViewById(R.id.updateAccount);
        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accName = input_accname.getText().toString().trim().replaceAll("\\s+", " ");;
                accNumber = input_accnumber.getText().toString().trim();
                accValue = input_value.getText().toString().trim();
                cate = (Type) spinner.getSelectedItem();
                if (check()) {
                    Account a = new Account(); // chua chon color, de tam
                    a.setAid(account.getAid());
                    a.setName(accName);
                    a.setBankAccount(accNumber);
                    a.setValue(Long.parseLong(accValue));
                    a.setType(cate.getName());
                    AppDatabase.getInstance(getContext()).accountDao().updateAccount(a);
                    Log.d("Update account", "Success");
                    Toast.makeText(getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                }
            }
        });

        //Bỏ update account, quay lại Home

        cancelUpdateAccount = view.findViewById(R.id.cancelUpdateAccount);
        cancelUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
            }
        });

        //Delete account, quay lại Home
        deleteAccount = view.findViewById(R.id.btn_deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (account.getAid() == 1) {
                    Toast.makeText(getContext(), "Can not delete default account", Toast.LENGTH_SHORT).show();
                } else{
                    AppDatabase.getInstance(getContext()).accountDao().deleteById(account.getAid());

                    List<Record> r = AppDatabase.getInstance(getContext()).recordDao().getByAccountId(account.getAid());
                    for (Record re: r) {
                        AppDatabase.getInstance(getContext()).recordDao().delete(re);
                    }

                    List<Template> t = AppDatabase.getInstance(getContext()).tempDao().getTemplateByAccountId(account.getAid());
                    for(Template temp: t) {
                        AppDatabase.getInstance(getContext()).tempDao().deleteTemplate(temp);
                    }

                    List<Budget> b = AppDatabase.getInstance(getContext()).budgetDao().getByAccountId(account.getAid());
                    for(Budget bud: b) {
                        AppDatabase.getInstance(getContext()).budgetDao().delete(bud);
                    }

                    Toast.makeText(getContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment()).commit();
                }
            }
        });

        return view;
    }

    private List<Type> getListCategory() {
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

    private boolean check() {
        if (accName == null || accName.length() == 0) {
            input_accname.setError("Account name is required!");
            return false;
        }
        if (accNumber == null || accNumber.length() == 0) {
            input_accnumber.setError("Account number is required!");
            return false;
        }
        if (accValue == null || accValue.length() == 0) {
            input_value.setError("Please input value!");
            return false;
        }

        return true;
    }

}
