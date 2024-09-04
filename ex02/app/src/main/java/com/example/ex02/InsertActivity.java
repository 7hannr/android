package com.example.ex02;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.widgets.Helper;


public class InsertActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("상품등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        EditText name=findViewById(R.id.name);
        EditText price=findViewById(R.id.price);

        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName=name.getText().toString();
                String strPrice=price.getText().toString();
                if(strName.equals("") || strPrice.equals(""))
                    new AlertDialog.Builder(InsertActivity.this)
                            .setTitle("알림")
                            .setMessage("상품명과 가격을 입력하세요!").setPositiveButton("확인", null).show();
                else{
                    String sql="insert into product(name, price) values('" + strName + "'," + strPrice + ")";
                    db.execSQL(sql);
                    finish();
                }
            }
        });
    } //onCreate
} //InsertActivity
