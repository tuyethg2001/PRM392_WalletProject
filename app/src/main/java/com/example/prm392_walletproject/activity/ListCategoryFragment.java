package com.example.prm392_walletproject.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.adapter.CategoryAdapter;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Category;

import java.util.List;

public class ListCategoryFragment extends Fragment {
    static int idValue = -1;
    boolean needValue = false;

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

        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
//        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
//                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();

        // Default data
//        if(db.cateDao().getAll().size()==0) {
//            Category a = new Category(1, "Transfer, withdraw", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FTransfer_default.png?alt=media&token=01d956d6-f6b0-4105-9401-6620a2f1bebb", true, true);
//            Category b = new Category(2, "Food", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FFood_default.png?alt=media&token=6753cfd4-9ec7-419b-9524-95521c5268d6", true, true);
//            Category c = new Category(3, "House", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FHouse_default.png?alt=media&token=61af5f3c-7e6a-431c-87ac-111666494626", true, true);
//            Category d = new Category(4, "Vehicle", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FVehicle_default.png?alt=media&token=07143aff-b164-4390-aeb8-8d51c391f1f7", true, true);
//            Category e = new Category(5, "Financial", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FFinancial_default.png?alt=media&token=17ee90f0-6369-49bb-b8e4-7a916218b072", true, true);
//            Category f = new Category(6, "Life", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FLife_default.png?alt=media&token=628d56ca-af2f-4bcb-a64c-9c7428355903", true, true);
//            Category g = new Category(7, "Device", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FDevices_default.png?alt=media&token=e713ffdc-4264-4e5b-b68a-450dd909ba87", true, true);
//            Category h = new Category(8, "Clothe", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FClothe_default.png?alt=media&token=88b28047-ef60-4e24-bd0e-5304b33433db", true, true);
//            Category i = new Category(9, "Equipment", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FEquipment_default.png?alt=media&token=bd8720d3-10c2-4c28-8adf-f201278752b3", true, true);
//            Category j = new Category(10, "Education", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FEducation_default.png?alt=media&token=46fe6d45-6ad1-4aed-a586-3eb358bf410b", true, true);
//            Category k = new Category(11, "Book", "https://firebasestorage.googleapis.com/v0/b/walletproject-240c8.appspot.com/o/images%2FBook_default.png?alt=media&token=dd238a66-ea17-4ab3-b58e-c004aebdc401", true, true);
//            db.cateDao().insertAll(a, b, c, d, e, f, g, h, i, j, k);
//        }
        // End of set data

//        List<Category> categoryList = db.cateDao().getAllByStatus();
        List<Category> categoryList = AppDatabase.getInstance(getContext()).cateDao().getAllByStatus();
        RecyclerView recyclerView = view.findViewById(R.id.rec_categoryList);
        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        adapter.setNeedValue(needValue);
        adapter.setFragmentManager(getActivity().getSupportFragmentManager());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        if(needValue==true) {
            ((ImageButton) view.findViewById(R.id.btn_add_categoryList)).setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            ((ImageButton) view.findViewById(R.id.btn_add_categoryList)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle result = new Bundle();
                    idValue = adapter.getIdValue(needValue);
                    result.putInt("categoryId", idValue);
                    getParentFragmentManager().setFragmentResult("requestCategoryId", result);

                    getParentFragmentManager().popBackStack();
                }
            });
        } else {
            ((ImageButton) view.findViewById(R.id.btn_add_categoryList)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateCategoryFragment createCategoryFragment = new CreateCategoryFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, createCategoryFragment).addToBackStack("demo").commit();
                }
            });
        }
        
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

        return view;
    }

    public int getIdValue() {
        return idValue;
    }

    public void setNeedValue(boolean needValue) {
        this.needValue = needValue;
    }
}