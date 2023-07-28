package com.example.prm392_walletproject.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.activity.EditCategoryFragment;
import com.example.prm392_walletproject.entity.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{
    List<Category> categoryList;
    CategoryHolder holder;
    FragmentManager fragmentManager;
    int tempId = -1;
    int SelectedItem = -1;
    boolean needValue = false;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if(needValue == true && categoryList.get(position).getCid()==1) {
            //Not appear Transfer when select
            ((ConstraintLayout) holder.tv_title_category.getParent()).setVisibility(View.GONE);
        }
        if(categoryList.get(position).isDefaultCategory()) {
            holder.btn_edit_category.setVisibility(View.GONE);
        } else {
            holder.btn_edit_category.setVisibility(View.VISIBLE);
        }
        this.holder = holder;
        if(position == SelectedItem && needValue == true) {
            ((ConstraintLayout) holder.tv_title_category.getParent()).setBackgroundColor(Color.parseColor("#42FFC0"));
        } else {
            ((ConstraintLayout) holder.tv_title_category.getParent()).setBackgroundColor(Color.TRANSPARENT);
        }
        holder.tv_title_category.setText(categoryList.get(position).getName());
        Picasso.get().load(categoryList.get(position).getIcon()).into(holder.img_icon_category);
        holder.tv_id_category.setText(String.valueOf(categoryList.get(position).getCid()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView tv_title_category;
        TextView tv_id_category;
        Button btn_edit_category;
        ImageView img_icon_category;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tv_id_category = itemView.findViewById(R.id.tv_id_category);
            tv_title_category = itemView.findViewById(R.id.tv_title_category);
            btn_edit_category = itemView.findViewById(R.id.btn_edit_category);
            img_icon_category = itemView.findViewById(R.id.img_icon_category);
            btn_edit_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", tv_id_category.getText().toString());
                    EditCategoryFragment editCategoryFragment = new EditCategoryFragment();
                    editCategoryFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.fl_container, editCategoryFragment).addToBackStack(null).commit();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tempId = Integer.parseInt(tv_id_category.getText().toString());
                    notifyItemChanged(SelectedItem);
                    SelectedItem = getAdapterPosition();
                    notifyItemChanged(SelectedItem);
                }
            });
        }
    }

    public int getIdValue(boolean needValue) {
        return needValue?tempId:-1;
    }

    public void setNeedValue(boolean needValue) {
        this.needValue = needValue;
    }
}
