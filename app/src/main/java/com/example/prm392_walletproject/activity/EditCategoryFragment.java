package com.example.prm392_walletproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditCategoryFragment extends Fragment implements View.OnClickListener{
    View view;
    Uri imageUri;
    StorageReference storageReference;
    AppDatabase db;
    int id;
    Toolbar toolbar;

    String oldUri;
    String oldName;

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
        Bundle bundle = this.getArguments();
        String id_info = null;
        if(bundle != null){
            id_info = bundle.getString("id");
        }

        if (id_info == null || id_info.equals("")) {
            id = -1;
        } else {
            id = Integer.parseInt(id_info);
        }

        view = inflater.inflate(R.layout.fragment_create_category, container, false);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();
        Category temp = db.cateDao().getCateById(id);
        ((TextView)view.findViewById(R.id.tv_title_create_category)).setText("Edit Category");
        ((TextView)view.findViewById(R.id.tv_name_createCategory)).setText(temp.getName());
        Picasso.get().load(temp.getIcon()).into((ImageView)view.findViewById(R.id.img_firebase));
        view.findViewById(R.id.btn_delete_createCategory).setVisibility(View.VISIBLE);
        oldUri = temp.getIcon();
        oldName = temp.getName();

        Button btn_select = (Button) view.findViewById(R.id.btn_select_createCategory);
        Button btn_save = (Button) view.findViewById(R.id.btn_save_createCategory);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete_createCategory);
        btn_select.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

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
                moveToNext();
                break;
            case R.id.btn_delete_createCategory:
                deleteByAlert();
                break;
        }
    }

    private void deleteByAlert() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_baseline_delete)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                        moveToNext();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();
    }

    private void delete() {
        db.cateDao().insertCategory(new Category(id, oldName, oldUri, false));
    }

    private void uploadImage() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String filename = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);
        if(imageUri == null || imageUri.equals("")) {
            String name = ((TextView)view.findViewById(R.id.tv_name_createCategory)).getText().toString();
            Category temp = new Category(id, name, oldUri, true);
            db.cateDao().insertCategory(temp);
        } else {
            FirebaseStorage.getInstance().getReference("images/" +
                    oldUri.substring(oldUri.indexOf("images%2F") + 9, oldUri.indexOf("?"))).delete();
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String name = ((TextView)view.findViewById(R.id.tv_name_createCategory)).getText().toString();
                            Category temp = new Category(id, name, uri.toString(), true);
                            db.cateDao().insertCategory(temp);
                        }
                    });
                }
            });
        }
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

    private void moveToNext() {
        ListCategoryFragment listCategoryFragment = new ListCategoryFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, listCategoryFragment).commit();
    }
}