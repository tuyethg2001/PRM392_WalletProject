package com.example.prm392_walletproject.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.converter.JavaMail;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.User;

import java.util.List;
import java.util.Random;

public class Authenticator extends AppCompatActivity {
    AppDatabase db;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();




        if(db.userDao().getUser()!=null) {
            setContentView(R.layout.activity_authenticator);
            // Forgot password mail link
            uri = getIntent().getData();
            if (uri != null) {
                String parameter = uri.getQueryParameter("password");
                ((TextView)findViewById(R.id.tv_password_input)).setText(parameter);
            }
        } else {
            setContentView(R.layout.activity_set_password);
        }
        //        Pin pin = new Pin(1, "123456");
        //        db.pinDao().insert(pin);


    }

    public void onClickEnter(View view) {
        String password = db.userDao().getUser().getPin();
        TextView tv_password = (TextView)findViewById(R.id.tv_password_input);
        String input = tv_password.getText().toString();
        if (password.equals(input)) {
            Intent it = new Intent(Authenticator.this, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        } else {
            tv_password.setError("Password is not correct");
        }
    }

    public void onClickSave(View view) {
        if(validate()==true) {
            User user = new User(1, ((TextView)findViewById(R.id.tv_set_password_input)).getText().toString(),
                    ((TextView)findViewById(R.id.tv_set_email)).getText().toString());
            db.userDao().insert(user);
            Intent it = new Intent(Authenticator.this, MainActivity.class);
            startActivity(it);
        }
    }

    private boolean validate() {
        String pattern = "\\w+@\\w+\\.\\w+";
        String pattern2 = "\\w+@\\w+\\.\\w+.\\w+";
        TextView tv_pass = (TextView)findViewById(R.id.tv_set_password_input);
        TextView tv_repass = (TextView)findViewById(R.id.tv_set_password_input2);
        TextView tv_email = (TextView)findViewById(R.id.tv_set_email);
        String pass = tv_pass.getText().toString().trim();
        String repass = tv_repass.getText().toString().trim();
        String email = tv_email.getText().toString().trim();
        if (pass == null || pass.equals("")) {
            tv_pass.setError("Password Field can not be empty");
            return false;
        } else if (repass == null || repass.equals("")) {
            tv_repass.setError("Password Field can not be empty");
            return false;
        } else if (!pass.equals(repass)) {
            tv_repass.setError("Password and confirm must be the same");
            return false;
        } else if (!email.matches(pattern) || !email.matches(pattern2)){
            tv_email.setError("Email not true");
            return false;
        } else {
            return true;
        }
    }

    public void onClickForgot(View view) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.labelicon_wallet)
                .setTitle("Password has been changed")
                .setMessage("Check your email for new password")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        dialog.show();
        String newPin = getRandomNumberString();
        User user = db.userDao().getUser();
        user.setPin(newPin);
        db.userDao().insert(user);
        String text = "Someone (hopefully you) has requested a password reset for your Wallet account\nYour new password is: " + newPin
                + "\nOr open this link in your phone: https://www.walletproject.infinityfreeapp.com/?password=" + newPin;
        new JavaMail(user.getEmail(), "Reset your Wallet password", text);
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}