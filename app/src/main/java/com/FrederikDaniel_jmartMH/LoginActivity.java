package com.FrederikDaniel_jmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.request.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<String> {

    private static final Gson gson = new Gson();
    private static Account loggedAccount;
    private EditText ETPassword;
    private EditText ETEmail;
    private Button loginButton;
    private TextView registerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETEmail = findViewById(R.id.id_Email);
        ETPassword = findViewById(R.id.id_Password);
        loginButton = findViewById(R.id.id_LoginButton);
        registerButton = findViewById(R.id.id_RegisterButton);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick( View v) {
        if(v.getId()==R.id.id_LoginButton){
            String dataEmail = ETEmail.getText().toString().trim();
            String dataPassword = ETPassword.getText().toString().trim();
            LoginRequest loginRequest = new LoginRequest(dataEmail, dataPassword, this, this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(loginRequest);
        }
        else if(v.getId()==R.id.id_RegisterButton){
            Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(moveIntent);
        }

    }

    @Override
    public void onResponse(String response) {
        Intent moveIntent = new Intent(LoginActivity.this, MainActivity.class);
        try{
            JSONObject jsonObject = new JSONObject(response);
            loggedAccount = gson.fromJson(jsonObject.toString(), Account.class);
        }catch (Exception e){
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
        startActivity(moveIntent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(this, "Login Failed Connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static Account getLoggedAccount(){
        return loggedAccount;
    }
}


