package com.example.restaurantmanagement;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RegisterActivity extends AppCompatActivity {
    FragmentTransaction transaction;
    Fragment fragmentLocation = new RegisterLocationFragment();
    Fragment fragmentInput = new RegisterInputFragment();
    Fragment fragmentImage = new RegisterImageFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("맛집등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        transaction= getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment, fragmentLocation).commit();
    } //onCreate
    public void replaceFragment(String type, Bundle bundle) {
        transaction=getSupportFragmentManager().beginTransaction();
        if(type == "location") {
            transaction.replace(R.id.fragment, fragmentLocation).commit();
        }else if(type == "input") {
            fragmentInput.setArguments(bundle);
            transaction.replace(R.id.fragment, fragmentInput).commit();
        }else if(type == "image") {
            fragmentImage.setArguments(bundle);
            transaction.replace(R.id.fragment, fragmentImage).commit();
        }
    } //replaceFragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home || item.getItemId() == R.id.close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return super.onCreateOptionsMenu(menu);
    }
} //RegisterActivity