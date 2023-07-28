package com.example.prm392_walletproject.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Budget;
import com.example.prm392_walletproject.entity.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BudgetViewEdit extends Fragment {

    private Budget budget;

    public BudgetViewEdit(Budget budget) {
        this.budget = budget;
    }

    View view;
    EditText edt_name;
    EditText edt_amount;
    AutoCompleteTextView actv_account, actv_category, actv_from, actv_to;
//    CheckBox cb_noti;

    String name;
    long amount;
    int accountId, categoryId;
    String start, from, to;
//    Boolean noti;

    ArrayAdapter<String> arrayAdapter;
    ListBudgetFragment lbf = new ListBudgetFragment();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.budget_view_edit, container, false);
        edt_name = view.findViewById(R.id.edt_budgetviewedit_name);
        edt_amount = view.findViewById(R.id.edt_budgetviewedit_amount);
        actv_account = view.findViewById(R.id.actv_budgetviewedit_account);
        actv_category = view.findViewById(R.id.actv_budgetviewedit_category);
        actv_from = view.findViewById(R.id.actv_budgetviewedit_from);
        actv_to = view.findViewById(R.id.actv_budgetviewedit_to);
//        cb_noti = view.findViewById(R.id.cb_budgetviewedit_noti);

        //name
        edt_name.setText(budget.getName());
        //amount
        edt_amount.setText(budget.getAmount().toString());
        //account
        Account account = AppDatabase.getInstance(getContext()).accountDao().getAccountById(budget.getAccountId());
        if (account != null) {
            actv_account.setText(account.getName());
            accountId = account.getAid();
        }
        else {
            //get the default account: All - initiate when the app is downloaded
            try {
                actv_account.setText(AppDatabase.getInstance(getContext()).accountDao().getAll().get(0).getName());
                accountId = AppDatabase.getInstance(getContext()).accountDao().getAll().get(0).getAid();
            } catch (Exception ex) {
                showAlertDialog("No account available", "You must have at least 01 account to use this function");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, lbf).commit();
            }
        }


        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.budget_dropdown_items, R.id.tv_budget_dropdown_item, AppDatabase.getInstance(getContext()).accountDao().getAllName());
        actv_account.setAdapter(arrayAdapter);
        actv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyboard(getActivity());
//                String item = parent.getItemAtPosition(position).toString();
//                accountId = Integer.parseInt(String.valueOf(id));
                accountId = AppDatabase.getInstance(getContext()).accountDao().getAll().get(position).getAid();
//                Toast.makeText(getContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
            }
        });
        //category
        Category category = AppDatabase.getInstance(getContext()).cateDao().getCateById(budget.getCategoryId());
        if (category != null) {
            actv_category.setText(category.getName());
            categoryId = category.getCid();
        }
        else {
            //get the default category: All - initiate when the app is downloaded
            try {
                actv_category.setText(AppDatabase.getInstance(getContext()).cateDao().getAll().get(0).getName());
                categoryId = AppDatabase.getInstance(getContext()).cateDao().getAll().get(0).getCid();
            } catch (Exception ex) {
                showAlertDialog("No category available", "You must have at least 01 category to use this function");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, lbf).commit();
            }
        }


        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.budget_dropdown_items, R.id.tv_budget_dropdown_item, AppDatabase.getInstance(getContext()).cateDao().getAllName());
        actv_category.setAdapter(arrayAdapter);
        actv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyboard(getActivity());
//                String item = parent.getItemAtPosition(position).toString();
//                categoryId = Integer.parseInt(String.valueOf(id));
                categoryId = AppDatabase.getInstance(getContext()).cateDao().getAll().get(position).getCid();
//                Toast.makeText(getContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
            }
        });
        //from
        actv_from.setText(budget.getStartDate());

        actv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                showDatePickerDialog(actv_from);
            }
        });
        //to
        actv_to.setText(budget.getEndDate());

        actv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                showDatePickerDialog(actv_to);
            }
        });
        //noti
//        cb_noti.setChecked(budget.isNotified());
//        noti = cb_noti.isChecked();
//        cb_noti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideSoftKeyboard(getActivity());
//                noti = cb_noti.isChecked();
//            }
//        });
        //create date
        start = budget.getCreatedDate();

        ListBudgetFragment listBudgetFragment = new ListBudgetFragment();
        ((FrameLayout) view.findViewById(R.id.fl_budgetviewedit_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                quitEdit(listBudgetFragment);
            }
        });

        ((FrameLayout) view.findViewById(R.id.fl_budgetviewedit_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure want to modify this budget?");
                builder.setMessage("Data will be changed");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (validateEdit()) {
                            Budget b = AppDatabase.getInstance(getContext()).budgetDao().checkDuplicate(budget.getBid());
                            if (b != null) {
                                b.setAccountId(accountId);
                                b.setName(name);
                                b.setAmount(amount);
                                b.setCategoryId(categoryId);
                                b.setCreatedDate(start);
                                b.setStartDate(from);
                                b.setEndDate(to);
//                                b.setNotified(noti);
                                AppDatabase.getInstance(getContext()).budgetDao().updateBudget(b);
                                getParentFragmentManager().beginTransaction().replace(R.id.fl_container, listBudgetFragment).commit();
                                Toast.makeText(getContext(), "Modified successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "No budget found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

        ((FrameLayout) view.findViewById(R.id.fl_budgetviewedit_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to delete this budget?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase.getInstance(getContext()).budgetDao().deleteBudget(budget);
                        getParentFragmentManager().beginTransaction().replace(R.id.fl_container, listBudgetFragment).commit();
                        Toast.makeText(getContext(), "Delete successful", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });


//        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                if (isEnabled()) {
//                    quitEdit(listBudgetFragment);
//                }
//            }
//        });

        // back press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager()
                            .popBackStack();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    public void quitEdit(Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure want to quit?");
        builder.setMessage("Your data will not be saved");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getParentFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public Boolean validateEdit() {
        if (edt_name.getText().toString().trim().isEmpty()) {
            edt_name.requestFocus();
            edt_name.setError("Please fill out this field");
            return false;
        } else {
            String[] str = edt_name.getText().toString().split("\\s+");
            name = "";
            for (String s: str) {
                name += s + " ";
            }
            name = name.trim();
            if (edt_amount.getText().toString().isEmpty()) {
                edt_amount.requestFocus();
                edt_amount.setError("Please fill out this field");
                return false;
            } else {
                try {
                    amount = Long.parseLong(edt_amount.getText().toString());
                } catch (Exception ex) {
                    showAlertDialog("Amount error", "Amount must be decimal");
                }

                if (actv_from.getText().toString().isEmpty()) {
                    actv_from.requestFocus();
                    actv_from.setError("Please fill out this field");
                    return false;
                } else {
                    from = actv_from.getText().toString();
                    if (actv_to.getText().toString().isEmpty()) {
                        actv_to.requestFocus();
                        actv_to.setError("Please fill out this field");
                        return false;
                    } else {
                        to = actv_to.getText().toString();
                    }
                }
            }
        }
        try {
            Date _from = new SimpleDateFormat("dd / MM / yyyy").parse(from);
            Date _to = new SimpleDateFormat("dd / MM / yyyy").parse(to);
            if (_from.compareTo(_to) > 0) {
                showAlertDialog("Date error", "The From date cannot be latter than To date");
                return false;
            }
        } catch (ParseException e) {
            showAlertDialog("Date error", e.getMessage());
            return false;
        }
        return true;
    }

    public void showAlertDialog(String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showDatePickerDialog(AutoCompleteTextView actv) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                actv.setText((i2 < 10 ? "0" + i2 : i2) + " / " + (i1 + 1 < 10 ? "0" + (i1 + 1) : i1 + 1) + " / " + i);
            }
        }, year, month, day);
        dialog.show();
    }
}