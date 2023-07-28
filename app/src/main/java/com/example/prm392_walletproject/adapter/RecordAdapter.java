package com.example.prm392_walletproject.adapter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.activity.RecordDetailFragment;
import com.example.prm392_walletproject.activity.RecordTransferDetailFragment;
import com.example.prm392_walletproject.entity.Account;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Record_Category;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Record_Category> records;

    public RecordAdapter(List<Record_Category> records) {
        this.records = records;
    }

    private String priceWithDecimal (Long price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_transfer_item, parent, false);
            return new RecordTransferHolder(view);
        }

        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Record_Category record_category = records.get(position);
        Record record = record_category.getRecord();
        Category category = record_category.getCategory();

        if (getItemViewType(position) == 0) {
            RecordHolder recordHolder = (RecordHolder) holder;
            if (category != null) {
                Picasso.get().load(category.getIcon()).into(recordHolder.ic_category);
                recordHolder.tv_rc_name.setText(category.getName());
            }
            recordHolder.tv_desc.setText(record.getDescription());
            recordHolder.tv_amount.setText(priceWithDecimal(record.getAmount()));
            if (record.getRecordTypeId() == 1) {
                recordHolder.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.income));
            } else {
                recordHolder.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.expense));
            }

            recordHolder.layout_record_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("recId", record.getRid());

                    RecordDetailFragment fragment = new RecordDetailFragment();
                    fragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            RecordTransferHolder recordTransferHolder = (RecordTransferHolder) holder;
            Account outside = new Account();
            outside.setName("...outside");
            Account fromAcc = record_category.getFromAccount();
            if (fromAcc == null) {
                fromAcc = outside;
            }
            Account toAcc = record_category.getToAccount();
            if (toAcc == null) {
                toAcc = outside;
            }

            if (category != null) {
                Picasso.get().load(category.getIcon()).into(recordTransferHolder.ic_category);
                recordTransferHolder.tv_rc_name.setText(category.getName());
            }

            recordTransferHolder.tv_from_acc.setText(fromAcc.getName());
            recordTransferHolder.tv_to_acc.setText(toAcc.getName());
            recordTransferHolder.tv_amount.setText(priceWithDecimal(record.getAmount()));
            if (record.isFromAcc()) {
                recordTransferHolder.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.expense));
                recordTransferHolder.tv_from_acc.setTypeface(recordTransferHolder.tv_from_acc.getTypeface(), Typeface.BOLD);
            } else {
                recordTransferHolder.tv_amount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.income));
                recordTransferHolder.tv_to_acc.setTypeface(recordTransferHolder.tv_to_acc.getTypeface(), Typeface.BOLD);
            }

            recordTransferHolder.layout_record_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("recId", record.getRid());

                    RecordTransferDetailFragment fragment = new RecordTransferDetailFragment();
                    fragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Record_Category record_category = records.get(position);
        Record record = record_category.getRecord();
        if (record.getRecordTypeId() == 2) {
             return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_record_item;
        ImageView ic_category;
        TextView tv_rc_name;
        TextView tv_desc;
        TextView tv_amount;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            layout_record_item = itemView.findViewById(R.id.layout_record_item);
            ic_category = itemView.findViewById(R.id.ic_category);
            tv_rc_name = itemView.findViewById(R.id.tv_rc_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_amount = itemView.findViewById(R.id.tv_amount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }

    public class RecordTransferHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_record_item;
        ImageView ic_category;
        TextView tv_rc_name;
        TextView tv_amount;
        TextView tv_from_acc;
        TextView tv_to_acc;

        public RecordTransferHolder(@NonNull View itemView) {
            super(itemView);
            layout_record_item = itemView.findViewById(R.id.layout_record_item);
            ic_category = itemView.findViewById(R.id.ic_category);
            tv_rc_name = itemView.findViewById(R.id.tv_rc_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_from_acc = itemView.findViewById(R.id.tv_from_acc);
            tv_to_acc = itemView.findViewById(R.id.tv_to_acc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }
}
