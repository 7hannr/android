package com.example.restaurantmanagement;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_permission);
        getSupportActionBar().hide();
        if(checkAndRequestPermission()){
            Intent intent = new Intent(PermissionActivity.this, IndexActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //권한을 확인하고 권한이 부여되어 있지 않으면 권한을 요청한다.
    private boolean checkAndRequestPermission() {
    String[] permissions={
            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
    };
    ArrayList<String> permissionsNeeded =new ArrayList<>();
    for(String permission:permissions){
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            permissionsNeeded.add(permission);
        }
    }
 if(!permissionsNeeded.isEmpty()){
        ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]),100);
        return false;
 }
 return true;
}
//권한 요청 결과를 받은 메서드
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    boolean isAllGranted = true; for (int i=0; i<permissions.length; i++){
        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
            isAllGranted = false;
        }
    }
    if (isAllGranted){ //모든 권한이 부여된 경우 IndexActivity로 이동한다.
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
    }else{ //모든 권한이 부여되어 있지 않은 경우 권한 설정화면으로 이등할지를 선택한다.
        AlertDialog.Builder box=new AlertDialog.Builder(this); box.setTitle("질의"); box.setMessage("권한 설정 화면으로 이동하실래요?");
        box.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish(); Intent intent = new Intent(); intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri= Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri); startActivity(intent);
        }
    });
box.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    });
    box.show();
    }
}
}
