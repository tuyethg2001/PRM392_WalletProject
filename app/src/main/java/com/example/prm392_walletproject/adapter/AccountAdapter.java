package com.example.prm392_walletproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.activity.AddAcountFragment;
import com.example.prm392_walletproject.activity.EditAccountFragment;
import com.example.prm392_walletproject.activity.HomeFragment;
import com.example.prm392_walletproject.entity.Account;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Account> list;

    Locale loc = Locale.getDefault();
    NumberFormat nf = NumberFormat.
            getCurrencyInstance(loc);

    Fragment listAccFragment;

    public AccountAdapter(Context context, List<Account> list, Fragment listAccFragment) {
        this.context = context;
        this.list = list;
        this.listAccFragment = listAccFragment;
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountHolder(LayoutInflater.from(context).inflate(R.layout.account_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AccountHolder holderA = (AccountHolder) holder;
        holderA.acc_type.setText(list.get(position).getName());
        holderA.acc_amount.setText(nf.format(list.get(position).getValue())+"");
        holderA.editAccount.setBackgroundColor(R.color.teal_700);
        holderA.editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAccFragment.getFragmentManager().beginTransaction().replace(R.id.fl_container, new EditAccountFragment(list.get(position))).addToBackStack("HomeFragment").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class AccountHolder extends RecyclerView.ViewHolder{

        TextView acc_type, acc_amount;
        ConstraintLayout editAccount;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            editAccount = itemView.findViewById(R.id.editAccount);
            acc_type = itemView.findViewById(R.id.acc_type);
            acc_amount = itemView.findViewById(R.id.acc_amount);
        }
    }
}