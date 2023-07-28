package com.example.prm392_walletproject.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.RecordType;
import com.example.prm392_walletproject.entity.Template;

import java.util.List;

public class TemplateViewEdit extends Fragment {

    private Template template;
    private MainActivity activity;
    View view;

    public TemplateViewEdit(Template template, MainActivity activity) {
        this.template = template;
        this.activity = activity;
    }

    EditText edt_name;
    EditText edt_amount;
    AutoCompleteTextView actv_account;
    AutoCompleteTextView actv_category;
    AutoCompleteTextView actv_type;
    EditText edt_note;
    String name;
    long amount;
    int accountId, categoryId, typeId;
    String note;

    ArrayAdapter<String> adapter;
    AwesomeValidation validation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.template_view_edit, container, false);
        activity.findViewById(R.id.bottom_nav).setVisibility(View.INVISIBLE);

        edt_name = view.findViewById(R.id.edt_templateedit_name);
        edt_amount = view.findViewById(R.id.edt_templateedit_amount);
        actv_account = view.findViewById(R.id.templateedit_actv_account);
        actv_category = view.findViewById(R.id.templateedit_actv_category);
        actv_type = view.findViewById(R.id.templateedit_actv_type);
        edt_note = view.findViewById(R.id.edt_templateedit_note);

        edt_name.setText(template.getName());
        edt_amount.setText(template.getAmount().toString());

        Account account = AppDatabase.getInstance(getContext()).accountDao().getAccountById(template.getAccountId());
        actv_account.setText(account.getName());

        Category category = AppDatabase.getInstance(getContext()).cateDao().getCateById(template.getCategoryId());
        actv_category.setText(category.getName());

        RecordType recordType = AppDatabase.getInstance(getContext()).recTypeDao().getTRecTypeById(template.getRecordTypeId());
        actv_type.setText(recordType.getName());

        edt_note.setText(template.getNote());

        accountId = template.getAccountId();
        categoryId = template.getCategoryId();
        typeId = template.getRecordTypeId();

        // SELECT ACCOUNT
        adapter = new ArrayAdapter<String>(getContext(), R.layout.template_dropdown_item,
                R.id.tv_templateadd_dropdown_item, AppDatabase.getInstance(getContext()).accountDao().getAllName());
        actv_account.setAdapter(adapter);

        actv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
                accountId = AppDatabase.getInstance(getContext()).accountDao().getAll().get(position).getAid();
            }
        });

        // SELECT CATEGORY
//        adapter = new ArrayAdapter<String>(getContext(), R.layout.template_dropdown_item,
//                R.id.tv_templateadd_dropdown_item, AppDatabase.getInstance(getContext()).cateDao().getAllName());
//        actv_category.setAdapter(adapter);

//        actv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                categoryId = AppDatabase.getInstance(getContext()).cateDao().getAll().get(position).getCid();
//            }
//        });

        final Category[] cate = new Category[1];
        activity.getSupportFragmentManager().setFragmentResultListener("requestCategoryId",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        int cateId = bundle.getInt("categoryId");
                        cate[0] = AppDatabase.getInstance(getContext()).cateDao().getCateById(cateId);
                        if(cate[0]!=null) {
                            categoryId = cate[0].getCid();
                            actv_category.setText(cate[0].getName());
                        }
                    }
                });
        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        listCategoryFragment.setNeedValue(true);
        actv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                        listCategoryFragment).addToBackStack(null).commit();
            }
        });
        // SELECT TYPE
        adapter = new ArrayAdapter<String>(getContext(), R.layout.template_dropdown_item,
                R.id.tv_templateadd_dropdown_item, AppDatabase.getInstance(getContext()).recTypeDao().getAllNameToAddTemplate());
        actv_type.setAdapter(adapter);

        actv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
                typeId = AppDatabase.getInstance(getContext()).recTypeDao().getAll().get(position).getRtid();
            }
        });

        // CANCEL
        ListTemplateFragment listTemplateFragment = new ListTemplateFragment(activity);
        ((FrameLayout) view.findViewById(R.id.fl_templateedit_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCreate(listTemplateFragment);
            }
        });

        // UPDATE
        ((FrameLayout) view.findViewById(R.id.fl_templateedit_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_name.getText().toString().trim().replaceAll("\\s+", " ");;

                if (name.equalsIgnoreCase(template.getName())) {
                    if (validationData()) {
                        doneUpdate(listTemplateFragment);
                    }
                } else {
                    List<Template> templates = AppDatabase.getInstance(getContext()).tempDao().checkDuplicate(name.toLowerCase(), accountId);
                    if (templates.size() != 0) {
                        hideKeyboard(view);
                        Toast.makeText(getContext(), "Template name already exist", Toast.LENGTH_SHORT).show();
                    } else if (validationData() && templates.size() == 0) {
                        doneUpdate(listTemplateFragment);
                    }
                }
            }
        });

        // DELETE
        ((FrameLayout) view.findViewById(R.id.fl_templateedit_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure to delete this template?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppDatabase.getInstance(getContext()).tempDao().deleteTemplate(template);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, listTemplateFragment).commit();
                        hideKeyboard(view);
                        Toast.makeText(getContext(), "delete success", Toast.LENGTH_SHORT).show();
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

        // SWITCH TO CREATE RECORD
        ((Button) view.findViewById(R.id.btn_templateedit_toRec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create record from this template?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecordCreateFragment recordCreateFragment = new RecordCreateFragment(template);
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_container, recordCreateFragment)
                                .commit();
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
//                    cancelCreate(listTemplateFragment);
//                }
//            }
//        });

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

    // FUNCTION
    public boolean validationData() {
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(getActivity(), R.id.edt_templateedit_name, RegexTemplate.NOT_EMPTY,
                R.string.name_field_empty);
        validation.addValidation(getActivity(), R.id.edt_templateedit_amount, RegexTemplate.NOT_EMPTY,
                R.string.amount_field_empty);

        if (validation.validate())
            return true;
        return false;
    }

    public void doneUpdate(Fragment fragment) {
        amount = Long.parseLong(String.valueOf(edt_amount.getText().toString()));
        name = edt_name.getText().toString().trim().replaceAll("\\s+", " ");;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Have you checked data yet?");
        builder.setMessage("You also can edit it after!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                amount = Long.parseLong(edt_amount.getText().toString());
                note = edt_note.getText().toString().trim().replaceAll("\\s+", " ");;

                template.setName(name);
                template.setAmount(amount);
                template.setAccountId(accountId);
                template.setCategoryId(categoryId);
                template.setRecordTypeId(typeId);
                template.setNote(note);

                AppDatabase.getInstance(getContext()).tempDao().updateTemplate(template);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                        fragment).commit();
                Toast.makeText(getContext(), "update success", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void cancelCreate(Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure to cancel?");
        builder.setMessage("Your input data will be deleted if you cancel!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}