package com.example.prm392_walletproject.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.RecordCreateSpinnerAdapter;
import com.example.prm392_walletproject.dao.AccountDao;
import com.example.prm392_walletproject.dao.CategoryDao;
import com.example.prm392_walletproject.dao.RecordDao;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Template;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordCreateFragment extends Fragment {
    private Template template;

    public RecordCreateFragment() {
    }

    public RecordCreateFragment(Template template) {
        this.template = template;
    }

    BottomNavigationView botomNav;
    Calendar calendar = Calendar.getInstance();

    Spinner spinner_account;
    Spinner spinner_type;
    ImageView ic_category;
    EditText et_category;
    EditText et_description;
    EditText et_created_date;
    EditText et_created_time;
    EditText et_amount;
    ImageView iv_done;
    ImageView iv_close;

    AppDatabase db;
    AccountDao accountDao;
    CategoryDao categoryDao;
    RecordDao recordDao;

    @Override
    public void onDestroy() {
        super.onDestroy();
        botomNav.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        botomNav = getActivity().findViewById(R.id.bottom_nav);
        botomNav.setVisibility(View.GONE);

        db = AppDatabase.getInstance(requireContext().getApplicationContext());
        accountDao = db.accountDao();
        categoryDao = db.cateDao();
        recordDao = db.recordDao();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_create, container, false);

        ic_category = view.findViewById(R.id.ic_category);
        et_category = view.findViewById(R.id.et_category);
        et_description = view.findViewById(R.id.et_description);
        et_amount = view.findViewById(R.id.et_amount);
        et_created_date = view.findViewById(R.id.et_created_date);
        et_created_time = view.findViewById(R.id.et_created_time);
        iv_done = view.findViewById(R.id.iv_done);
        iv_close = view.findViewById(R.id.iv_close);

        // close
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botomNav.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // type
        spinner_type = view.findViewById(R.id.spinner_type);
        String[] types = {"Income", "Expense"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, types);
        spinner_type.setAdapter(typeAdapter);

        // account
        spinner_account = view.findViewById(R.id.spinner_account);
        List<Account> accounts = accountDao.getAll();
        if (accounts.isEmpty()) {
            Account none = new Account();
            none.setName("None");
            accounts.add(none);
        }
        RecordCreateSpinnerAdapter recordSpinnerAdapter = new RecordCreateSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, accounts);
        spinner_account.setAdapter(recordSpinnerAdapter);

        // category
        final Category[] category = new Category[1];
        getParentFragmentManager().setFragmentResultListener("requestCategoryId", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                int categoryId = bundle.getInt("categoryId");
                category[0] = categoryDao.getCateById(categoryId);
                if(category[0]!=null) {
                    Picasso.get().load(category[0].getIcon()).into(ic_category);
                    et_category.setText(category[0].getName());
                }
            }
        });
        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        listCategoryFragment.setNeedValue(true);
        (view.findViewById(R.id.et_category)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fl_container, listCategoryFragment).addToBackStack(null).commit();
            }
        });

        //date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        et_created_date.setText(dateFormat.format(calendar.getTime()));
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                et_created_date.setText(dateFormat.format(calendar.getTime()));
            }
        };
        et_created_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        et_created_time.setText(timeFormat.format(calendar.getTime()));
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                et_created_time.setText(timeFormat.format(calendar.getTime()));
            }
        };
        et_created_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        // add
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    Account account = (Account) spinner_account.getSelectedItem();
                    int accountId = account.getAid();

                    int categoryId = category[0].getCid();
                    String description = removeSpace(String.valueOf(et_description.getText()));

                    Long amount = Long.valueOf(String.valueOf(et_amount.getText()));

                    int type = 0;
                    if (((String) spinner_type.getSelectedItem()).equals("Income")) {
                        type = 1;
                        account.setValue(account.getValue() + amount);
                    } else {
                        account.setValue(account.getValue() - amount);
                    }
                    accountDao.insertAccount(account);

                    String createdTime = et_created_date.getText() + " " + et_created_time.getText();

                    recordDao.insert(new Record(accountId, categoryId, description, amount, type, createdTime));
                    botomNav.setVisibility(View.VISIBLE);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, new ListRecordFragment())
                            .commit();
                }
            }
        });

        // get from template
        if (template != null) {
            et_amount.setText(String.valueOf(template.getAmount()));

            if (template.getRecordTypeId() == 1) {
                spinner_type.setSelection(0);
            } else {
                spinner_type.setSelection(1);
            }

            for (int i = 0; i < spinner_account.getCount(); i++) {
                if (((Account) spinner_account.getItemAtPosition(i)).getAid() == template.getAccountId()) {
                    spinner_account.setSelection(i);
                }
            }

            Category categoryTemplate = categoryDao.getCateById(template.getCategoryId());
            category[0] = categoryTemplate;
            Picasso.get().load(categoryTemplate.getIcon()).into(ic_category);
            et_category.setText(categoryTemplate.getName());

            et_description.setText(template.getNote());
        }

        return view;
    }

    private boolean checkAllFields() {
        if (et_amount.length() == 0 || Long.valueOf(String.valueOf(et_amount.getText())) == 0) {
            et_amount.setError("This field is required");
            return false;
        }

        Account account = (Account) spinner_account.getSelectedItem();
        if (account.getName().equals("None")) {
            ((TextView) spinner_account.getSelectedView().findViewById(R.id.tv_record_spiner)).setError("This field is empty");
            Toast toast = Toast.makeText(getContext(), "Do not have an account.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        if (et_category.length() == 0) {
            et_category.setError("This field is required");
            return false;
        }

        if (et_created_date.length() == 0) {
            et_created_date.setError("This field is required");
            return false;
        }

        if (et_created_time.length() == 0) {
            et_created_time.setError("\"This field is required");
            return false;
        }

        // check max date
        String createdTime = et_created_date.getText() + " " + et_created_time.getText();
        Calendar createdCal = Calendar.getInstance();
        createdCal.setTime(Timestamp.valueOf(createdTime));
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 1);
        if (createdCal.after(now)) {
            et_created_date.setError("The datetime cannot be in the future.");
            et_created_time.setError("The datetime cannot be in the future.");
            Toast toast = Toast.makeText(getContext(), "The datetime cannot be in the future.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        // check account number
        if (account.getValue() < Long.valueOf(String.valueOf(et_amount.getText()))) {
            et_amount.setError("Exceeded the amount in the account.");
            Toast toast = Toast.makeText(getContext(), "Exceeded the amount in the account.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        return true;
    }

    private String removeSpace(String string) {
        return string.replaceAll("\\s+"," ").trim();
    }
}