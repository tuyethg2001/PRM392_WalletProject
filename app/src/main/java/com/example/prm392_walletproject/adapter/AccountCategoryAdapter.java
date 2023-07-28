package com.example.prm392_walletproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.entity.Category;

import java.util.List;

public class AccountCategoryAdapter extends ArrayAdapter<Category> {


    public AccountCategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_type_list_select, parent, false);
        TextView tvSelected = convertView.findViewById(R.id.type_selected);
        Category category = this.getItem(position);
        if(category != null){
            tvSelected.setText(category.getName());
        }
        return convertView;    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_type_list, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.type_list);
        Category category = this.getItem(position);
        if(category != null){
            tvCategory.setText(category.getName());
        }
        return convertView;
    }
}
