package com.example.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.company.MyFirebaseMessagingService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class Login extends Activity {
    EditText userid, userpasswod;
    Button login;
    CheckBox id_store, auto_login;
    String user_id, user_password;
    String token;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        userid = (EditText)findViewById(R.id.userid);
        userpasswod =(EditText)findViewById(R.id.userpassword);
        login = (Button)findViewById(R.id.login);
        id_store = (CheckBox)findViewById(R.id.id_store);
        auto_login = (CheckBox)findViewById(R.id.auto_login);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        Toast.makeText(Login.this, token, Toast.LENGTH_SHORT).show();
                        System.out.println(token);
                    }
                });


        if(((CheckBox)auto_login).isChecked()){
            String userid2 = shared.getString("userid", "");
            String userpassword2 = shared.getString("userpassword", "");
            login(userid2, userpassword2);
        }

        id_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    user_id = userid.getText().toString();
                    SharedPreferences.Editor editor = shared.edit();
                    editor.clear();
                    editor.putString("userid", user_id);
                    editor.commit();
                }
            }
        });

        auto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    user_id = userid.getText().toString();
                    user_password = userpasswod.getText().toString();

                    SharedPreferences.Editor editor = shared.edit();
                    editor.clear();
                    editor.putString("userid", user_id);
                    editor.putString("userpassword", user_password);
                    editor.commit();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = userid.getText().toString();
                user_password = userpasswod.getText().toString();

                if(user_id.isEmpty()){
                    Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.isEmpty()){
                    Toast.makeText(Login.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(user_id, user_password);
                }
            }
        });
    }

    public void login(String user_id, String user_password){
        String url = "https://scv0319.cafe24.com/man/login.php?userid="+user_id+"&userpassword="+user_password+"";
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    String userid2 = jsonObject1.getString("userid");
                    String userpassword2 = jsonObject1.getString("userpassword");
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("userid",userid2);
                    editor.putString("userpassword",userpassword2);
                    editor.commit();

                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);

                }
                catch (JSONException e) {
                    Toast.makeText(Login.this, "아이디와 비밀번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed(){

    }

}
