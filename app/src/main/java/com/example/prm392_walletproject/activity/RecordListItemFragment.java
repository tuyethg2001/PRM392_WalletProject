package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392_walletproject.R;

public class RecordListItemFragment extends Fragment {
    private String title;

    public RecordListItemFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_record_tab_item, container, false);
        return root;
    }
}