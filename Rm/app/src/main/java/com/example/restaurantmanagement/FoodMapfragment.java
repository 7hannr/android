package com.example.restaurantmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodMapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    LocationManager lm;
    List<FoodVO> array = new ArrayList<>();
    List<FoodVO> arrayMarker = new ArrayList<>();
    MapAdapter adapter = new MapAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_map, container, false);
        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        FoodDAO dao = new FoodDAO();
        FoodHelper helper = new FoodHelper(getContext());
        array = dao.list(helper, false);

        RecyclerView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        Button btnList = view.findViewById(R.id.list_type);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getVisibility() == View.VISIBLE) {
                    list.setVisibility(View.GONE);
                    btnList.setText("목록보기");
                } else {
                    list.setVisibility(View.VISIBLE);
                    btnList.setText("목록닫기");
                }
            }
        });

        return view;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng).title("현재 위치")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_marker));
            gMap.addMarker(marker);

            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng).radius(500)
                    .fillColor(0x440000FF)
                    .strokeColor(0x110000FF)
                    .strokeWidth(4);
            gMap.addCircle(circleOptions);

            arrayMarker.clear();
            for (FoodVO vo : array) {
                float[] distance = new float[1];
                Location.distanceBetween(latitude, longitude, vo.getLatitude(), vo.getLongitude(), distance);
                if (distance[0] <= 500) {
                    vo.setDistance(distance[0]);
                    arrayMarker.add(vo);
                    LatLng markerLatLng = new LatLng(vo.getLatitude(), vo.getLongitude());
                    MarkerOptions markers = new MarkerOptions()
                            .position(markerLatLng).title(vo.getName());
                    gMap.addMarker(markers);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        UiSettings settings = googleMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        gMap = googleMap;

        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, 0);
        } else {
            lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }
    }

    @Override
    public void onPause() {
        lm.removeUpdates(locationListener);
        super.onPause();
    }

    private class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {
        @NonNull
        @Override
        public MapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_map, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MapAdapter.ViewHolder holder, int position) {
            FoodVO vo = arrayMarker.get(position);
            holder.image.setImageBitmap(BitmapFactory.decodeFile(vo.getImage()));
            holder.name.setText(vo.getName());
            holder.description.setText(vo.getDescription());

            DecimalFormat df = new DecimalFormat("#,##0.00Km");
            holder.distance.setText(df.format(vo.getDistance() / 1000));

            // 사진 이미지를 클릭한 경우
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FoodInfoActivity.class);
                    intent.putExtra("id", vo.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayMarker.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, description, distance;
            ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                description = itemView.findViewById(R.id.description);
                image = itemView.findViewById(R.id.image);
                distance = itemView.findViewById(R.id.distance);
            }
        }
    }
}
