package com.example.restaurantmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RelativeLayout mainDrawer;

    FragmentTransaction transaction;
    Fragment fooListFragment = new FoodListFragment();
    Fragment foodMapFragment = new FoodMapFragment();
    Fragment foodKeepFragment = new FoodKeepFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("맛집리스트");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        mainDrawer = findViewById(R.id.main_drawer);

        transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fooListFragment).commit();

        NavigationView navigation=findViewById(R.id.drawer_menu);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(mainDrawer);
                transaction= getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.nav_list) {
                    transaction.replace(R.id.main_content, fooListFragment).commit();
                    getSupportActionBar().setTitle("맛집리스트");
                }else if(item.getItemId() == R.id.nav_map) {
                    transaction.replace(R.id.main_content, foodMapFragment).commit();
                    getSupportActionBar().setTitle("지도리스트");
                }else if(item.getItemId() == R.id.nav_keep) {
                    transaction.replace(R.id.main_content, foodKeepFragment).commit();
                    getSupportActionBar().setTitle("즐겨찾기");
                }else if(item.getItemId() == R.id.nav_profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }else if(item.getItemId() == R.id.nav_register) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    } //onCreate

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences preferences=getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        name.setText(preferences.getString("name", "홍길동"));
        String strFile=preferences.getString("image", "");
        if(!strFile.equals("")) image.setImageBitmap(BitmapFactory.decodeFile(strFile));
    } //onRestart

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(mainDrawer)) {
                drawerLayout.close();
            }else {
                drawerLayout.openDrawer(mainDrawer);
            }
        }
        return super.onOptionsItemSelected(item);
    } //onOptionsItemSelected
} //MainActivity
