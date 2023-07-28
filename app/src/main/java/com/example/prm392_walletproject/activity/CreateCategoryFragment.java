package com.example.prm392_walletproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class    CreateCategoryFragment extends Fragment implements View.OnClickListener{
    View view;
    Uri imageUri;
    StorageReference storageReference;
    Toolbar toolbar;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.topnav_setting:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new SettingFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            case android.R.id.home:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, new SearchFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_category, container, false);

        view = inflater.inflate(R.layout.fragment_create_category, container, false);
        Button btn_select = (Button) view.findViewById(R.id.btn_select_createCategory);
        Button btn_save = (Button) view.findViewById(R.id.btn_save_createCategory);
        btn_select.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        // set up toolbar
        toolbar = view.findViewById(R.id.top_nav);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        setHasOptionsMenu(true);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_search);
            activity.getSupportActionBar().setTitle("");
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager()
                            .popBackStack();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            ((ImageView)view.findViewById(R.id.img_firebase)).setImageURI(imageUri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_createCategory:
                selectImage();
                break;
            case R.id.btn_save_createCategory:
                if (!validateName()) {
                    return;
                }
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String filename = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
        if(imageUri == null || imageUri.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Image not found!", Toast.LENGTH_SHORT).show();
            return;
        }
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String name = ((TextView)view.findViewById(R.id.tv_name_createCategory)).getText().toString();
                        Category temp = new Category(name, uri.toString(), true);
                        db.cateDao().insertCategory(temp);
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, listCategoryFragment).commit();
            }
        });
    }

    private boolean validateName() {
        TextView tv_name = (TextView)view.findViewById(R.id.tv_name_createCategory);
        String val = tv_name.getText().toString().replaceAll("\\s+"," ").trim();
        if (val.isEmpty()) {
            tv_name.setError("Field can not be empty");
            return false;
        } else {
            String capitalize = "";
            String[] splited = val.split(" ");
            for (int i = 0; i < splited.length; i++) {
                splited[i] = splited[i].substring(0, 1).toUpperCase() + splited[i].substring(1);
                capitalize += splited[i] + " ";
            }
            capitalize = capitalize.trim();
            tv_name.setText(capitalize);
            tv_name.setError(null);
            return true;
        }

    }
}