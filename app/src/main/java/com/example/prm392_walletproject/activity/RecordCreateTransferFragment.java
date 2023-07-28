package com.example.prm392_walletproject.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.prm392_walletproject.entity.Record;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RecordCreateTransferFragment extends Fragment {
    BottomNavigationView bottomNav;

    Spinner spinner_from;
    Spinner spinner_to;
    ImageView iv_close;
    ImageView iv_done;
    EditText et_created_date;
    EditText et_created_time;
    EditText et_amount;

    AppDatabase db;
    AccountDao accountDao;
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
        recordDao = db.recordDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_create_transfer, container, false);

        Calendar calendar = Calendar.getInstance();

        List<Account> accounts = accountDao.getAll();
        if (accounts.isEmpty()) {
            Account none = new Account();
            none.setName("None");
            accounts.add(none);
        }

        spinner_from = view.findViewById(R.id.spinner_from);
        spinner_to = view.findViewById(R.id.spinner_to);
        iv_close = view.findViewById(R.id.iv_close);
        iv_done = view.findViewById(R.id.iv_done);
        et_created_date = view.findViewById(R.id.et_created_date);
        et_created_time = view.findViewById(R.id.et_created_time);
        et_amount = view.findViewById(R.id.et_amount);

        // set select from + to account
        List<Account> fromAccounts = new ArrayList<>(accounts);
        if (fromAccounts.size() != 1) {
            RecordCreateSpinnerAdapter fromAccSpinnerAdapter = new RecordCreateSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, fromAccounts);
            spinner_from.setAdapter(fromAccSpinnerAdapter);
            spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Account fromAccount = fromAccounts.get(position);

                    // set spinner to
                    int fromAccId = fromAccount.getAid();
                    List<Account> toAccounts = new ArrayList<>(accounts);
                    for (Account account : toAccounts) {
                        if (account.getAid() == fromAccId) {
                            toAccounts.remove(account);
                            break;
                        }
                    }

                    RecordCreateSpinnerAdapter toAccSpinnerAdapter = new RecordCreateSpinnerAdapter(view.getContext(), R.layout.record_spinner_item, toAccounts);
                    spinner_to.setAdapter(toAccSpinnerAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });
        }

        // set date
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

        // set time
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

        // close
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNav.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // add
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields()) {
                    Long amount = Long.valueOf(String.valueOf(et_amount.getText()));

                    Account fromAcc = (Account) spinner_from.getSelectedItem();
                    int fromAccId = fromAcc.getAid();

                    Account toAcc = (Account) spinner_to.getSelectedItem();
                    int toAccId = toAcc.getAid();

                    String createdTime = et_created_date.getText() + " " + et_created_time.getText();

                    // update from acc
                    // update from acc value
                    fromAcc.setValue(fromAcc.getValue() - amount);
                    accountDao.insertAccount(fromAcc);
                    // insert rec for from acc
                    recordDao.insert(new Record(fromAccId, 1, amount, 2, createdTime, toAccId, true));

                    // update to acc
                    // update to acc value
                    toAcc.setValue(toAcc.getValue() + amount);
                    accountDao.insertAccount(toAcc);
                    // insert rec for to acc
                    recordDao.insert(new Record(toAccId, 1, amount, 2, createdTime, fromAccId, false));

                    bottomNav.setVisibility(View.VISIBLE);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, new ListRecordFragment())
                            .commit();
                }
            }
        });

        return view;
    }

    private boolean checkAllFields() {
        if (spinner_from.getCount() < 2) {
            ((TextView) spinner_from.getSelectedView().findViewById(R.id.tv_record_spiner)).setError("You need to have at least 2 accounts to create a transfer.");
            Toast toast = Toast.makeText(getContext(), "You need to have at least 2 accounts to create a transfer.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        if (et_amount.length() == 0 || Long.valueOf(String.valueOf(et_amount.getText())) == 0) {
            et_amount.setError("This field is required");
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

        // check from account number
        Account fromAcc = (Account) spinner_from.getSelectedItem();
        if (fromAcc.getValue() < Long.valueOf(String.valueOf(et_amount.getText()))) {
            et_amount.setError("Exceeded the amount in the account.");
            Toast toast = Toast.makeText(getContext(), "The amount in the transfer account has been exceeded.", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        return true;
    }

}