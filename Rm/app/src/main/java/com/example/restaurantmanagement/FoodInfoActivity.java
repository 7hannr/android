package com.example.restaurantmanagement;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FoodInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    FoodDAO dao = new FoodDAO(); FoodVO vo=new FoodVO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_food_info);
        getSupportActionBar().setTitle("음식정보"); getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int id=intent.getIntExtra("id", 0);
        FoodHelper helper=new FoodHelper(this);
        vo=dao.foodRead(helper, id);
        ImageView image=findViewById(R.id.image);
        image.setImageBitmap(BitmapFactory.decodeFile(vo.getImage()));
        TextView name=findViewById(R.id.name); name.setText(vo.getName());
        TextView tel=findViewById(R.id.tel); tel.setText(vo.getTel());
        TextView address=findViewById(R.id.address);
        address.setText(vo.getAddress());
        TextView description=findViewById(R.id.description);
        description.setText(vo.getDescription());
        ImageView keep=findViewById(R.id.keep);
        if(vo.getKeep() == 1)keep.setImageResource(R.drawable.ic_keep_on);
        else keep.setImageResource(R.drawable.ic_keep_off);
        //지도위치이동
        SupportMapFragment map=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
        //위치보기 텍스트를 클릭한 경우
        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.getMapAsync(FoodInfoActivity.this);
            }
        });
        //전화번호를 클릭한 경우
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + vo.getTel()));
                startActivity(intent);
            }
        });
    } //onCreate
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        UiSettings settings=googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        LatLng latLng=new LatLng(vo.getLatitude(), vo.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title(vo.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    } //onMapReady
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    } //onOptionsItemSelected
}
