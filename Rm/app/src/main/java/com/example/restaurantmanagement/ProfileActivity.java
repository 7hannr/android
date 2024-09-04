package com.example.restaurantmanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    EditText name, gender, phone, birthday;
    String strFile = "";
    CircleImageView image;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 액션 바 설정
        getSupportActionBar().setTitle("프로필설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI 요소 초기화
        name = findViewById(R.id.profile_name);
        gender = findViewById(R.id.profile_gender);
        phone = findViewById(R.id.profile_phone);
        birthday = findViewById(R.id.profile_birthday);
        image = findViewById(R.id.ic_person);

        // 저장된 프로필 정보 불러오기
        getProfile();

        // 성별 입력 상자 클릭 이벤트 처리
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] genders = {"여자", "남자"};
                AlertDialog.Builder box = new AlertDialog.Builder(ProfileActivity.this);
                box.setItems(genders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gender.setText(genders[which]);
                        dialog.dismiss();
                    }
                });
                box.show();
            }
        });

        // 생일 입력 상자 클릭 이벤트 처리
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar calendar = new GregorianCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new DatePickerDialog(
                        ProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String strBirthday = String.format("%4d-%02d-%02d", year, month + 1, dayOfMonth);
                                birthday.setText(strBirthday);
                            }
                        },
                        year, month, day
                );
                picker.show();
            }
        });

        // 프로필 아이콘 변경 버튼 클릭 이벤트 처리
        findViewById(R.id.profile_icon_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileIconActivity.class);
                startActivity(intent);
            }
        });
    }

    // 저장된 프로필 정보를 불러오는 메서드
    public void getProfile() {
        preferences = getSharedPreferences("profile", AppCompatActivity.MODE_PRIVATE);
        editor = preferences.edit();
        name.setText(preferences.getString("name", "홍길동"));
        gender.setText(preferences.getString("gender", ""));
        phone.setText(preferences.getString("phone", ""));
        birthday.setText(preferences.getString("birthday", ""));
        strFile = preferences.getString("image", "");
        if (!strFile.equals("")) {
            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getProfile(); // 화면이 다시 보일 때 프로필 정보를 갱신
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 뒤로가기 버튼을 누르면 현재 액티비티 종료
        } else if (item.getItemId() == R.id.submit) {
            // 저장 버튼을 눌렀을 때의 동작
            AlertDialog.Builder box = new AlertDialog.Builder(this);
            box.setTitle("질의");
            box.setMessage("프로필 정보를 저장하실래요?");
            box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putString("name", name.getText().toString());
                    editor.putString("gender", gender.getText().toString());
                    editor.putString("birthday", birthday.getText().toString());
                    editor.putString("phone", phone.getText().toString());
                    editor.commit();
                    finish();
                }
            });
            box.setNegativeButton("아니오", null);
            box.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
