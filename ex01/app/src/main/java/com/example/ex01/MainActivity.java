package com.example.ex01;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit1 = findViewById(R.id.edit1);
                String str= edit1.getText().toString();
                Toast.makeText(MainActivity.this,
                        "안녕하세요!"+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btn=findViewById(R.id.btn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.red){
            btn.setTextColor(Color.RED);
        }else if(item.getItemId() == R.id.blue)
        {
            btn.setTextColor(Color.BLUE);
        }else if(item.getItemId() == R.id.green){
            btn.setTextColor(Color.GREEN);
        }else if(item.getItemId() == R.id.big) {
            if(item.isChecked()){
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);
            }else{
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, 200);
            }
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int color = btn.getTextColors().getDefaultColor();
        if(color == Color.RED){
            menu.findItem(R.id.red).setChecked(true);
        }else if(color == Color.BLUE){
            menu.findItem(R.id.blue).setChecked(true);
        }else if(color == Color.GREEN){
            menu.findItem(R.id.green).setChecked(true);
        }
        if(btn.getTextSize() == 200) {
            menu.findItem(R.id.big).setChecked(true);
        }
        else{
            menu.findItem(R.id.big).setChecked(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

}