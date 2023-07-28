package com.example.prm392_walletproject.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.User;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener{
    AppDatabase db;
    View tempView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragement = inflater.inflate(R.layout.fragment_change_password, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
        ((Button)viewFragement.findViewById(R.id.btn_change_authenticator)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        tempView = viewFragement;
        return viewFragement;

    }

    public void changePassword() {
        if(validate()==true) {
            User user = new User(1, ((TextView)tempView.findViewById(R.id.tv_change_password_input1)).getText().toString(),
                    db.userDao().getUser().getEmail());
            db.userDao().insert(user);
//            Intent it = new Intent(getActivity(), MainActivity.class);
//            startActivity(it);
            Toast.makeText(getActivity(), "Password has been changed!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SettingFragment()).commit();
        }
    }

    private boolean validate() {
        TextView tv_oldPass = (TextView)tempView.findViewById(R.id.tv_change_password_input);
        TextView tv_pass = (TextView)tempView.findViewById(R.id.tv_change_password_input1);
        TextView tv_repass = (TextView)tempView.findViewById(R.id.tv_change_password_input2);
        String oldPass = tv_oldPass.getText().toString().trim();
        String pass = tv_pass.getText().toString().trim();
        String repass = tv_repass.getText().toString().trim();
        if (oldPass == null || oldPass.equals("")) {
            tv_oldPass.setError("Field can not be empty");
            return false;
        } else if (!db.userDao().getUser().getPin().equals(oldPass)) {
//            Log.d("MyCheck", "validate: " + db.userDao().getUser() + "||" + oldPass);
            tv_oldPass.setError("Old password is not correct");
            return false;
        } else if (repass == null || pass.equals("")) {
            tv_pass.setError("Field can not be empty");
            return false;
        } else if (repass == null || repass.equals("")) {
            tv_repass.setError("Field can not be empty");
            return false;
        } else if (!pass.equals(repass)) {
            tv_repass.setError("Password and confirm must be the same");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {

    }
}