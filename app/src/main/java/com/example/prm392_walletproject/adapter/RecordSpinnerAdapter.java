package com.example.prm392_walletproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.entity.Account;

import java.util.List;

public class RecordSpinnerAdapter extends ArrayAdapter<Account> {
    LayoutInflater inflater;

    public RecordSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Account> accounts) {
        super(context, resource, accounts);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.record_spinner_item, null, true);
        Account account = getItem(position);
        TextView tv_record_spiner = rowView.findViewById(R.id.tv_record_spiner);

        tv_record_spiner.setText(account.getName());

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.record_spinner_item, parent, false);
        }

        Account account = getItem(position);
        TextView tv_record_spiner = convertView.findViewById(R.id.tv_record_spiner);

        tv_record_spiner.setText(account.getName());

        return convertView;
    }
}
