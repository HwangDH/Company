package com.example.company;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Single_Customer extends AppCompatActivity {
    TextView txt1, txt2;
    Button send_single_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single__customer);

        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        send_single_info = (Button)findViewById(R.id.send_info_check);

        Intent intent = getIntent();

        txt1.setText(intent.getStringExtra("name"));
        txt2.setText(intent.getStringExtra("age"));

        send_single_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Single_Customer.this,SendInfo.class);
                startActivity(intent);
            }
        });
    }
}
