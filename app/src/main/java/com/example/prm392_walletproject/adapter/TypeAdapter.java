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
import com.example.prm392_walletproject.entity.Type;

import java.util.List;

public class TypeAdapter extends ArrayAdapter<Type> {
    public TypeAdapter(@NonNull Context context, int resource, @NonNull List<Type> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_list_select, parent, false);
        TextView tvSelected = convertView.findViewById(R.id.type_selected);
        Type type = this.getItem(position);
        if(type != null){
            tvSelected.setText(type.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_list, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.type_list);
        Type type = this.getItem(position);
        if(type != null){
            tvCategory.setText(type.getName());
        }
        return convertView;
    }
}

