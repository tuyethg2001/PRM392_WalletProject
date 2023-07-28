package com.example.prm392_walletproject.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.activity.ListTemplateFragment;
import com.example.prm392_walletproject.activity.MainActivity;
import com.example.prm392_walletproject.activity.RecordCreateFragment;
import com.example.prm392_walletproject.activity.TemplateViewEdit;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Category;
import com.example.prm392_walletproject.entity.Template;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateHolder> {
    List<Template> templates;
    Fragment listTemplateFragment;
    MainActivity activity;

    public TemplateAdapter(List<Template> templates, Fragment fragment, MainActivity activity) {
        this.templates = templates;
        this.listTemplateFragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override

    public TemplateAdapter.TemplateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_recyclerview, parent, false);
        return new TemplateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateAdapter.TemplateHolder holder, @SuppressLint("RecyclerView") int position) {
        Category cate = AppDatabase.getInstance(listTemplateFragment.getContext()).cateDao().getCateById(templates.get(position).getCategoryId());
        Picasso.get().load(cate.getIcon()).into(holder.imv_icon);
        holder.tv_name.setText(templates.get(position).getName());
        holder.tv_note.setText(templates.get(position).getNote());

        holder.csl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                holder.csl.setBackgroundColor(listTemplateFragment.getContext().getColor(R.color.clicked));
                TemplateViewEdit templateViewEdit = new TemplateViewEdit(templates.get(position), activity);
                 activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, templateViewEdit)
                         .addToBackStack(null)
                        .commit();
            }
        });

        holder.imv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listTemplateFragment.getContext());
                builder.setTitle("Create record from this template?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecordCreateFragment recordCreateFragment = new RecordCreateFragment(templates.get(position));
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, recordCreateFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    class TemplateHolder extends RecyclerView.ViewHolder {
        ImageView imv_icon;
        TextView tv_name;
        TextView tv_note;
        ConstraintLayout csl;
        FrameLayout imv_more;

        public TemplateHolder(@NonNull View itemView) {
            super(itemView);
            imv_icon = itemView.findViewById(R.id.imv_templatelist_icon);
            tv_name = itemView.findViewById(R.id.tv_templatelist_templatename);
            tv_note = itemView.findViewById(R.id.tv_templatelist_templatenote);
            csl = itemView.findViewById(R.id.constraintlayout_template_rec);
            imv_more = itemView.findViewById(R.id.imv_templatelist_more);
        }
    }
}
