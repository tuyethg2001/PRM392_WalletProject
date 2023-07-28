package com.example.prm392_walletproject.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordDetailFragment extends Fragment {

    BottomNavigationView bottomNav;
    Calendar calendar = Calendar.getInstance();

    Spinner spinner_account;
    Spinner spinner_type;
    ImageView ic_category;
    EditText et_category;
    EditText et_description;
    EditText et_created_date;
    EditText et_created_time;
    EditText et_amount;
    ImageView iv_delete;
    ImageView iv_done;

    AppDatabase db;
    AccountDao accountDao;
    CategoryDao categoryDao;
    RecordDao recordDao;

    @Override
    public void onDestroy() {
        super.onDestroy();
        bottomNav.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNav = getActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.GONE);

        db = AppDatabase.getInstance(requireContext().getApplicationContext());
        accountDao = db.accountDao();
        categoryDao = db.cateDao();
        recordDao = db.recordDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_detail, container, false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

        ic_category = view.findViewById(R.id.ic_category);
        et_category = view.findViewById(R.id.et_category);
        et_description = view.findViewById(R.id.et_description);
        et_amount = view.findViewById(R.id.et_amount);
        et_created_date = view.findViewById(R.id.et_created_date);
        et_created_time = view.findViewById(R.id.et_created_time);
        iv_done = view.findViewById(R.id.iv_done);
        iv_delete = view.findViewById(R.id.iv_delete);
        spinner_account = view.findViewById(R.id.spinner_account);
        spinner_type = view.findViewById(R.id.spinner_type);

        // type
        String[] types = {"Income", "Expense"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, types);
        spinner_type.setAdapter(typeAdapter);

        // account
        List<Account> accounts = accountDao.getAll();
        if (accounts.isEmpty()) {
            Account none = new Account();
            none.setName("None");
            accounts.add(none);
        }
        RecordCreateSpinnerAdapter recordSpinnerAdapter = new RecordCreateSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, accounts);
        spinner_account.setAdapter(recordSpinnerAdapter);

        // category
        final int[] categoryId = {-1};
        getParentFragmentManager().setFragmentResultListener("requestCategoryId", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                categoryId[0] = bundle.getInt("categoryId");
                Category category = categoryDao.getCateById(categoryId[0]);
                Picasso.get().load(category.getIcon()).into(ic_category);
                et_category.setText(category.getName());
            }
        });

        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        listCategoryFragment.setNeedValue(true);
        (view.findViewById(R.id.et_category)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.wrapper, listCategoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //date
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
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.d("checkkkk", "" + hour);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                Log.d("checkkkk", "" + calendar.get(Calendar.HOUR_OF_DAY));
                et_created_time.setText(timeFormat.format(calendar.getTime()));
            }
        };
        et_created_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getActivity(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        // set value
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int recId = bundle.getInt("recId");
            Record record = recordDao.getById(recId);
            calendar.setTime(Timestamp.valueOf(record.getCreatedTime()));

            et_amount.setText(String.valueOf(record.getAmount()));

            if (record.getRecordTypeId() == 1) {
                spinner_type.setSelection(0);
            } else {
                spinner_type.setSelection(1);
            }

            for (int i = 0; i < spinner_account.getCount(); i++) {
                if (((Account) spinner_account.getItemAtPosition(i)).getAid() == record.getAccountId()) {
                    spinner_account.setSelection(i);
                    break;
                }
            }

            Category categoryTemplate = categoryDao.getCateById(record.getCategoryId());
            Picasso.get().load(categoryTemplate.getIcon()).into(ic_category);
            et_category.setText(categoryTemplate.getName());
            et_description.setText(record.getDescription());
            et_created_date.setText(dateFormat.format(calendar.getTime()));
            et_created_time.setText(timeFormat.format(calendar.getTime()));
        }

        // close
        ImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNav.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // update
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    int recId = bundle.getInt("recId");
                    Record record = recordDao.getById(recId);

                    Account account = (Account) spinner_account.getSelectedItem();
                    int accountId = account.getAid();

                    int cateId = record.getCategoryId();
                    if (categoryId[0] != -1) {
                        cateId = categoryId[0];
                    }

                    String description = removeSpace(String.valueOf(et_description.getText()));

                    Long amount = Long.valueOf(String.valueOf(et_amount.getText()));

                    int type = 0;
                    if (((String) spinner_type.getSelectedItem()).equals("Income")) {
                        type = 1;
                        account.setValue(account.getValue() + amount);
                    } else {
                        account.setValue(account.getValue() - amount);
                    }
                    if (record.getRecordTypeId() == 0) {
                        account.setValue(account.getValue() + record.getAmount());
                    } else {
                        account.setValue(account.getValue() - record.getAmount());
                    }
                    accountDao.insertAccount(account);

                    String createdTime = et_created_date.getText() + " " + et_created_time.getText();

                    recordDao.insert(new Record(recId, accountId, cateId, description, amount, type, createdTime));
                    bottomNav.setVisibility(View.VISIBLE);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, new ListRecordFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // delete
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Do you really want to delete this item?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Record record = recordDao.getById(bundle.getInt("recId"));
                                recordDao.delete(record);

                                Account account = accountDao.getAccountById(record.getAccountId());
                                if (record.getRecordTypeId() == 0) {
                                    account.setValue(account.getValue() + record.getAmount());
                                } else if (record.getRecordTypeId() == 1) {
                                    account.setValue(account.getValue() - record.getAmount());
                                }
                                accountDao.insertAccount(account);

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fl_container, new ListRecordFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

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