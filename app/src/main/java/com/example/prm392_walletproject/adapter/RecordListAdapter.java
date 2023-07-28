package com.example.prm392_walletproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Record_Category;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordItemHolder> {
    LinkedHashMap<String, List<Record_Category>> records;

    public RecordListAdapter(LinkedHashMap<String, List<Record_Category>> records) {
        this.records = records;
    }

    private String priceWithDecimal (Integer price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    private String getDayStringOld(Date date) {
        DateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return formatter.format(date);
    }

    @NonNull
    @Override
    public RecordListAdapter.RecordItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item, parent, false);
        return new RecordItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.RecordItemHolder holder, int position) {
        int index = 0;
        for (Map.Entry<String, List<Record_Category>> e : records.entrySet() ) {
            Calendar cal = Calendar.getInstance();
            Timestamp time = Timestamp.valueOf(e.getValue().get(0).getRecord().getCreatedTime());
            cal.setTime(time);
            List<Record_Category> value = e.getValue();

            if (index == position) {
                holder.tv_day.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                holder.tv_week_day.setText(getDayStringOld(cal.getTime()));
                holder.tv_month_year.setText((cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

                int sum = 0;
                for (Record_Category r: value) {
                    Record record = r.getRecord();
                    switch (record.getRecordTypeId()) {
                        case 0:
                            sum -= r.getRecord().getAmount();
                            break;
                        case 1:
                            sum += r.getRecord().getAmount();
                            break;
                        case 2:
                            if (record.isFromAcc()) {
                                sum -= r.getRecord().getAmount();
                            } else {
                                sum += r.getRecord().getAmount();
                            }
                    }
                }
                holder.tv_sum.setText(priceWithDecimal(sum));

                Collections.sort(value, new Comparator<Record_Category>() {
                    @Override
                    public int compare(Record_Category record, Record_Category t1) {
                        Timestamp time = Timestamp.valueOf(record.getRecord().getCreatedTime());
                        Timestamp time1 = Timestamp.valueOf(t1.getRecord().getCreatedTime());
                        return time1.compareTo(time);
                    }
                });

                RecordAdapter recordAdapter = new RecordAdapter(value);
                holder.rv_record_items.setLayoutManager(new LinearLayoutManager(holder.rv_record_items.getContext()));
                holder.rv_record_items.setAdapter(recordAdapter);
                break;
            }
            index++;
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class RecordItemHolder extends RecyclerView.ViewHolder {
        TextView tv_day;
        TextView tv_week_day;
        TextView tv_month_year;
        TextView tv_sum;
        RecyclerView rv_record_items;

        public RecordItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_week_day = itemView.findViewById(R.id.tv_week_day);
            tv_month_year = itemView.findViewById(R.id.tv_month_year);
            tv_sum = itemView.findViewById(R.id.tv_sum);
            rv_record_items = itemView.findViewById(R.id.rv_record_items);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }
}
