package com.example.company.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.company.List.CustomerVO;
import com.example.company.MainActivity;
import com.example.company.R;
import com.example.company.SendInfo;
import com.example.company.Single_Customer;


import java.util.ArrayList;

public class Customer_Adapter extends BaseAdapter {
    private ArrayList<CustomerVO> listVO = new ArrayList<CustomerVO>();
    SharedPreferences shared;

    @Override
    public int getCount() {
        return listVO.size();
    }

    @Override
    public Object getItem(int position) {
        return listVO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customer_list, parent, false);
        }

        TextView customer_name = (TextView) convertView.findViewById(R.id.customer_name);
        TextView customer_age = (TextView) convertView.findViewById(R.id.customer_age);

        CustomerVO listViewItem = listVO.get(position);

        customer_name.setText(listViewItem.getCustomer_name());
        customer_age.setText(listViewItem.getCustomer_age());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, (pos + 1) + "번째가 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), Single_Customer.class);
                intent.putExtra("name", listVO.get(position).getCustomer_name());
                intent.putExtra("age", listVO.get(position).getCustomer_age());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addVO(String customer_name, String customer_age) {
        CustomerVO item = new CustomerVO();

        item.setCustomer_name(customer_name);
        item.setCustomer_age(customer_age);

        listVO.add(item);
    }
}
