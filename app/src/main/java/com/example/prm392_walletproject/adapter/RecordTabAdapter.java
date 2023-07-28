package com.example.prm392_walletproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.entity.Record;
import com.example.prm392_walletproject.entity.Record_Category;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordTabAdapter extends RecyclerView.Adapter<RecordTabAdapter.RecordListItemHolder> {
    LinkedHashMap<String, List<Record_Category>> timeList;
    ViewPager2 viewPager2;

    public RecordTabAdapter(LinkedHashMap<String, List<Record_Category>> timeList, ViewPager2 viewPager2) {
        this.timeList = timeList;
        this.viewPager2 = viewPager2;
    }

    private String priceWithDecimal (Integer price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

    @NonNull
    @Override
    public RecordListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_record_tab_item, parent, false);
        return new RecordListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListItemHolder holder, int position) {
        int index = 0;
        for (Map.Entry<String, List<Record_Category>> e : timeList.entrySet() ) {
            List<Record_Category> values = e.getValue();

            if (index == position) {
                int incomeNum = 0;
                int expenseNum = 0;
                for (Record_Category record_category: values) {
                    Record record = record_category.getRecord();
                    switch (record.getRecordTypeId()) {
                        case 0:
                            expenseNum += record.getAmount();
                            break;
                        case 1:
                            incomeNum += record.getAmount();
                            break;
                        case 2:
                            if (record.isFromAcc()) {
                                expenseNum += record.getAmount();
                            } else {
                                incomeNum += record.getAmount();
                            }
                    }
                }
                int sum = incomeNum - expenseNum;
                holder.tv_income_num.setText(priceWithDecimal(incomeNum));
                holder.tv_expense_num.setText(priceWithDecimal(expenseNum));
                holder.tv_sum.setText(priceWithDecimal(sum));

                if (!values.isEmpty()) {
                    Collections.sort(values, new Comparator<Record_Category>() {
                        @Override
                        public int compare(Record_Category record_category, Record_Category record_category1) {
                            Timestamp time = Timestamp.valueOf(record_category.getRecord().getCreatedTime());
                            Timestamp time1 = Timestamp.valueOf(record_category1.getRecord().getCreatedTime());
                            return time1.compareTo(time);
                        }
                    });

                    LinkedHashMap<String, List<Record_Category>> data = new LinkedHashMap<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < values.size(); i++) {
                        Record_Category record_category = values.get(i);
                        Record record = record_category.getRecord();
                        Timestamp time = Timestamp.valueOf(record.getCreatedTime());
                        String date = dateFormat.format(time);
                       if (data.containsKey(date)) {
                           data.get(date).add(record_category);
                       } else {
                           List<Record_Category> newList = new ArrayList<>();
                           newList.add(record_category);
                           data.put(date, newList);
                       }
                    }

                    RecordListAdapter recordItemAdapter = new RecordListAdapter(data);
                    holder.rv_record_items.setLayoutManager(new LinearLayoutManager(holder.rv_record_items.getContext()));
                    holder.rv_record_items.setAdapter(recordItemAdapter);
                }
                break;
            }
            index++;
        }
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class RecordListItemHolder extends RecyclerView.ViewHolder {
        TextView tv_income_num;
        TextView tv_expense_num;
        TextView tv_sum;
        RecyclerView rv_record_items;

        public RecordListItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_income_num = itemView.findViewById(R.id.tv_income_total_num);
            tv_expense_num = itemView.findViewById(R.id.tv_expense_total_num);
            tv_sum = itemView.findViewById(R.id.tv_total_sum);
            rv_record_items = itemView.findViewById(R.id.rv_record_list_items);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });
        }
    }
}
