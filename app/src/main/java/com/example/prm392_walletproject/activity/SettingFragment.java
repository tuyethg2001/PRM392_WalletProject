package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;

public class SettingFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
        if(db.reminderDao().getReminder()!=null) {
            ((TextView)view.findViewById(R.id.tv_des_setting1)).setText("Curent Time: "+ db.reminderDao().getReminder().getHour() + ":"
                    +  db.reminderDao().getReminder().getMinute() + "\nCurrent Status: " + (db.reminderDao().getReminder().getStatus()?"On":"Off"));
        }
        if(db.userDao().getUser()!=null) {
            ((TextView) view.findViewById(R.id.tv_des_setting3)).setText(db.userDao().getUser().getPin());
        }

        ((CardView)view.findViewById(R.id.cardview_setting2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReminderFragment reminderFragment = new ReminderFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, reminderFragment).addToBackStack("demo").commit();
            }
        });
        ((CardView)view.findViewById(R.id.cardview_setting3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, listCategoryFragment).addToBackStack("demo").commit();
            }
        });
        ((CardView)view.findViewById(R.id.cardview_setting4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, changePasswordFragment).addToBackStack("demo").commit();
            }
        });
        ImageButton img_btn = (ImageButton) view.findViewById(R.id.btn_img_setting);

        img_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (img_btn.getTag().equals("ic_show_password")) {
                    img_btn.setTag("ic_hide_password");
                    img_btn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_hide_password));
                    ((TextView) view.findViewById(R.id.tv_des_setting3)).setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    return;
                } else {
                    img_btn.setTag("ic_show_password");
                    img_btn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_show_password));
                    ((TextView) view.findViewById(R.id.tv_des_setting3)).setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
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
}