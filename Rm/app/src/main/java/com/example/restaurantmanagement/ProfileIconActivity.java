package com.example.restaurantmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileIconActivity extends AppCompatActivity {
    CircleImageView image;
    String strFile = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_icon);

        // 액션 바 설정
        getSupportActionBar().setTitle("프로필 이미지 변경");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI 요소 초기화
        image = findViewById(R.id.profile_icon);
        preferences = getSharedPreferences("profile", Activity.MODE_PRIVATE);
        strFile = preferences.getString("image", "");
        if (!strFile.equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
        }

        // 카메라 버튼 클릭 이벤트 처리
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file = File.createTempFile("img_", ".jpg", storageDir);
                    strFile = file.getAbsolutePath();
                    Uri uriFile = FileProvider.getUriForFile(ProfileIconActivity.this, getPackageName(), file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);
                    activityResultCamera.launch(intent);
                } catch (Exception e) {
                    System.out.println("카메라 오류: " + e.toString());
                }
            }
        });

        // 앨범 버튼 클릭 이벤트 처리
        findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultAlbum.launch(intent);
            }
        });
    }

    // 카메라 결과 처리
    ActivityResultLauncher<Intent> activityResultCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        image.setImageBitmap(BitmapFactory.decodeFile(strFile));
                    }
                }
            }
    );

    // 앨범 선택 결과 처리
    ActivityResultLauncher<Intent> activityResultAlbum = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Cursor cursor = getContentResolver().query(result.getData().getData(), null, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            strFile = cursor.getString(index);
                            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
                            cursor.close();
                        }
                    }
                }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.submit) {
            AlertDialog.Builder box = new AlertDialog.Builder(this);
            box.setTitle("질의");
            box.setMessage("프로필 사진을 변경하실래요?");
            box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor = preferences.edit();
                    editor.putString("image", strFile);
                    editor.commit();
                    finish();
                }
            });
            box.setNegativeButton("아니오", null);
            box.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
