package com.example.prm392_walletproject.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.prm392_walletproject.R;

public class GetDataFromCategoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_data_from_category, container, false);

        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        listCategoryFragment.setNeedValue(true);
        listCategoryFragment.getIdValue();
        ((Button)view.findViewById(R.id.btn_test_data)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "result");
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });

        return view;
    }



}