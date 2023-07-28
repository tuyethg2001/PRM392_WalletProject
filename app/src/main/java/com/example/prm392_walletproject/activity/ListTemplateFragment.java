package com.example.prm392_walletproject.activity;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.TemplateAdapter;
import com.example.prm392_walletproject.database.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListTemplateFragment extends Fragment {
    RecyclerView rv;
    View view;
    Toolbar toolbar;

    private MainActivity activity;

    public ListTemplateFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
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
        view = inflater.inflate(R.layout.fragment_list_template, container, false);
        activity.findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE);

        rv = view.findViewById(R.id.rec_templates);

        TemplateAdapter tempAdapter = new TemplateAdapter(AppDatabase.getInstance(getContext()).tempDao().getAll(), this, activity);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(tempAdapter);

        TemplateCreate templateCreate = new TemplateCreate(activity);
        ((FloatingActionButton) view.findViewById(R.id.fab_templatelist_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, templateCreate)
                        .addToBackStack(null)
                        .commit();
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

}