package com.example.company;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.company.Adapter.Customer_Adapter;

public class MainActivity extends Activity implements View.OnClickListener {
    private ListView listview;
    private Customer_Adapter adapter;
    private String[] customer_name = {"황동현", "홍길동"};
    private String[] customer_age = {"12","25"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new Customer_Adapter();
        listview = (ListView) findViewById(R.id.list);

        listview.setAdapter(adapter);

        for (int i = 0; i < customer_name.length; i++) {
            adapter.addVO(customer_name[i], customer_age[i]);
        }
    }

    @Override
    public void onClick(View v){

    }
}
