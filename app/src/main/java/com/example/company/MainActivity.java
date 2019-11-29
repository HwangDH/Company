package com.example.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {
    ListView listview;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    String myJSON;
    ListAdapter adapter;
    int count = 0;
    String [] bankid = new String[30];
    String [] productID = new String[30];
    String [] productName = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        listview = (ListView) findViewById(R.id.list);
        personList = new ArrayList<HashMap<String, String>>();

        getData("https://scv0319.cafe24.com/termProject/depositProduct.php");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Single_Customer.class);
                intent.putExtra("bankid", bankid[position]);
                intent.putExtra("productID", productID[position]);
                intent.putExtra("productName", productName[position]);
                startActivity(intent);
            }
        });
    }


    public void showList(){
        try{
            JSONObject jsonObject = new JSONObject(myJSON);
            peoples = jsonObject.getJSONArray("result");
            //JSON 배열 길이만큼 반복문을 실행
            while(count < peoples.length()){
                JSONObject object = peoples.getJSONObject(count);
                bankid[count] = object.getString("Bank_ID4");
                productID[count] = object.getString("DepositProduct_ID");
                productName[count] = object.getString("DepositProduct_Name");
                HashMap<String, String> persons = new HashMap<>();
                persons.put("Bank_ID4", bankid[count]);
                persons.put("DepositProduct_ID", productID[count]);
                persons.put("DepositProduct_Name", productName[count]);
                personList.add(persons);
                adapter = new SimpleAdapter(
                        MainActivity.this, personList, R.layout.customer_list,
                        new String[] {"Bank_ID4", "DepositProduct_ID", "DepositProduct_Name"},
                        new int[] {R.id.bank, R.id.depositid, R.id.depositname}
                );
                listview.setAdapter(adapter);
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    @Override
    public void onClick(View v){

    }

    @Override
    public void onBackPressed(){

    }
}
