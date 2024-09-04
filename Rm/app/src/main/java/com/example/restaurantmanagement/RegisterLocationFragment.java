package com.example.restaurantmanagement;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class RegisterLocationFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    ProgressBar bar;
    TextView txtAddress;
    LocationManager lm;
    FoodVO food = new FoodVO();
    Marker marker; // 지도의 특정 영역 클릭 시 마커를 저장할 변수

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_location, container, false);
        bar = view.findViewById(R.id.progressBar);
        txtAddress = view.findViewById(R.id.address);

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        // '다음' 버튼 클릭 시 다른 프래그먼트로 전환
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", food);
                RegisterActivity registerActivity = (RegisterActivity) getActivity();
                registerActivity.replaceFragment("input", bundle);
            }
        });

        return view;
    }

    LocationListener locationListener = new LocationListener() {
        // GPS 현재 위치로 이동
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            gMap.clear(); // 이전 마커 제거
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            gMap.addMarker(new MarkerOptions().position(latLng).title("현재 위치")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker))); // 마커 생성

            txtAddress.setText(getAddress(getContext(), latitude, longitude));
            food.setAddress(txtAddress.getText().toString());
            food.setLatitude(latitude);
            food.setLongitude(longitude);

            bar.setVisibility(View.GONE);
        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        gMap = googleMap;

        String permission = android.Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 0);
        } else {
            lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }

        // 지도의 특정 영역을 클릭한 경우
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                txtAddress.setText(getAddress(getContext(), latitude, longitude));

                // 기존 마커 제거 후 새로운 마커 추가
                if (marker != null) marker.remove();
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = gMap.addMarker(options);

                // FoodVO 객체에 선택된 위치 정보 저장
                food.setAddress(txtAddress.getText().toString());
                food.setLatitude(latitude);
                food.setLongitude(longitude);
            }
        });
    }

    public String getAddress(Context context, double latitude, double longitude) {
        // 위도, 경도 값으로 주소 가져오기
        String strAddress = "현재 위치를 확인할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
            if (address != null && !address.isEmpty()) {
                strAddress = address.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            System.out.println("에러:" + e.toString());
        }
        return strAddress;
    }

    @Override
    public void onPause() {
        super.onPause();
        lm.removeUpdates(locationListener);
    }
}
