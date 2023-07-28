package com.example.prm392_walletproject.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.activity.BudgetViewEdit;
import com.example.prm392_walletproject.entity.Budget;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetHolder> {

    List<Budget> budgetList;
    BudgetHolder holder;
    Fragment listBudgetFragment;

    public BudgetAdapter(List<Budget> budgetList, Fragment listBudgetFragment) {
        this.budgetList = budgetList;
        this.listBudgetFragment = listBudgetFragment;
    }

    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetHolder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;

        holder.tv_createdDate.setText("Created date: " + budgetList.get(position).getCreatedDate());
        holder.tv_name.setText(budgetList.get(position).getName());
        holder.tv_price.setText(String.valueOf(budgetList.get(position).getAmount()));
        holder.tv_start.setText(budgetList.get(position).getStartDate());
        holder.tv_end.setText(budgetList.get(position).getEndDate());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                holder.cv.setBackgroundColor(listBudgetFragment.getContext().getColor(R.color.teal_700));
                BudgetViewEdit budgetViewEdit = new BudgetViewEdit(budgetList.get(position));
                listBudgetFragment.getParentFragmentManager().beginTransaction().replace(R.id.fl_container, budgetViewEdit).addToBackStack("ListBudgetFragment").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public class BudgetHolder extends RecyclerView.ViewHolder {
        TextView tv_createdDate, tv_name, tv_start, tv_end, tv_price;
        CardView cv;

        public BudgetHolder(@NonNull View itemView) {
            super(itemView);
            tv_createdDate = itemView.findViewById(R.id.tv_createdDate_budget);
            tv_name = itemView.findViewById(R.id.tv_name_budget);
            tv_price = itemView.findViewById(R.id.tv_price_budget);
            tv_start = itemView.findViewById(R.id.tv_start_budget);
            tv_end = itemView.findViewById(R.id.tv_end_budget);
            cv = itemView.findViewById(R.id.cv_budget);
        }
    }
}
