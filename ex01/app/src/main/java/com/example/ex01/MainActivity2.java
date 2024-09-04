package com.example.ex01;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    Button btnDecrease,btnIncrease;
    TextView count;
    int intCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setTitle("버튼연습");

        btnDecrease=findViewById(R.id.btn1);
        btnIncrease=findViewById(R.id.btn2);
        count=findViewById(R.id.text);

        btnDecrease.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                intCount--;
                count.setText("현재값"+intCount);
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intCount++;
                count.setText("현재값" + intCount);
            }
        });

        btnIncrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                intCount=100;
                count.setText("현재값" + intCount);
                return true;
            }
        });

        btnDecrease.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                intCount=0;
                count.setText("현재값" + intCount);
                return true;
            }
        });
    }//oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.jjajang){
            Toast.makeText(this, "짜장은 달콤해", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.jjambbong){
            Toast.makeText(this, "짬뽕은 매워", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.udong) {
            Toast.makeText(this, "우동은 시원해", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.mandoo) {
            Toast.makeText(this, "만두는 고소해", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == android.R.id.home) {
            finish(); }
        return super.onOptionsItemSelected(item);
    }

}//Activity

