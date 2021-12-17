package com.FrederikDaniel_jmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.model.Payment;
import com.FrederikDaniel_jmartMH.model.Store;
import com.FrederikDaniel_jmartMH.request.RegisterStoreRequest;
import com.FrederikDaniel_jmartMH.request.RequestFactory;
import com.FrederikDaniel_jmartMH.request.TopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutMeActivity extends AppCompatActivity {

    private Button registerstorebutton,registerbutton,cancelbutton,topupbutton, invoiceButton, accountInvoiceButton;
    private TextView tvName, tvEmail, tvBalance, tvStoreName, tvStoreAddress, tvStorePhoneNumber;
    private EditText editTopup, editName, editAddress, editPhoneNumber;
    private Account account;
    private ScrollView linearLayout1;
    private LinearLayout linearLayout2;
    private static final Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        topupbutton = findViewById(R.id.TopUpBT);
        registerstorebutton = findViewById(R.id.registerStoreBT);
        registerbutton = findViewById(R.id.RegisterBT);
        cancelbutton = findViewById(R.id.cancelBT);
        invoiceButton = findViewById(R.id.buttonInvoice);
        accountInvoiceButton = findViewById(R.id.AccountInvoiceButton);
        tvName = findViewById(R.id.nameAbt);
        tvEmail = findViewById(R.id.emailAbt);
        tvBalance = findViewById(R.id.balanceAbt);
        tvStoreName = findViewById(R.id.shopname);
        tvStoreAddress = findViewById(R.id.shopaddress);
        tvStorePhoneNumber = findViewById(R.id.shopphonenumber);
        editTopup = findViewById(R.id.editTopUp);
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editPhoneNumber = findViewById(R.id.editPhone);
        account = LoginActivity.getLoggedAccount();
        tvName.setText(account.name);
        tvEmail.setText(account.email);
//        tvBalance.setText("" + account.balance);
        takeBalance();
        linearLayout1 = findViewById(R.id.LLRegis);
        linearLayout2 = findViewById(R.id.LLinfo);


        if (account.store != null) {
            linearLayout2.setVisibility(View.VISIBLE);
            registerstorebutton.setVisibility(View.GONE);
            linearLayout1.setVisibility(View.GONE);
            tvStoreName.setText(account.store.name);
            tvStoreAddress.setText(account.store.address);
            tvStorePhoneNumber.setText(account.store.phoneNumber);
        }
        else {
            registerstorebutton.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
        }

        accountInvoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(AboutMeActivity.this, UserInvoice.class);;
                startActivity(moveIntent);
            }
        });

        invoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(AboutMeActivity.this, StoreInvoiceActivity.class);;
                startActivity(moveIntent);
            }
        });

        topupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountTopUp = editTopup.getText().toString().trim();
                double amount = Double.valueOf(amountTopUp);
                boolean isEmptyFields = false;
                if (TextUtils.isEmpty(editTopup.getText().toString())) {
                    isEmptyFields = true;
                    editTopup.setError("This bracket must be filled");
                }
                if(!isEmptyFields) {
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Boolean object = Boolean.valueOf(response);
                            if (object) {
                                Toast.makeText(AboutMeActivity.this, "Top Up Success!", Toast.LENGTH_SHORT).show();
                                takeBalance();
                                editTopup.getText().clear();
                            } else {
                                Toast.makeText(AboutMeActivity.this, "Top Up Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };


                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AboutMeActivity.this, "Top Up Failed due to Connection!", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", error.toString());
                        }
                    };

                    TopUpRequest topUpRequest = new TopUpRequest(amount, account.id, listener, errorListener);
                    RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                    queue.add(topUpRequest);
                }
            }
        });


        registerstorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                registerstorebutton.setVisibility(v.GONE);
                linearLayout1.setVisibility(v.VISIBLE);
                Toast.makeText(getApplicationContext(),"Register Store di click",Toast.LENGTH_SHORT).show();
            }
        });


        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeName = editName.getText().toString();
                String storeAddress = editAddress.getText().toString();
                String storePhoneNumber = editPhoneNumber.getText().toString();

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if (object != null) {
                                Toast.makeText(AboutMeActivity.this, "Store Successfully registered!", Toast.LENGTH_SHORT).show();
                            }
                            account.store = gson.fromJson(object.toString(), Store.class);
                            tvStoreName.setText(account.store.name);
                            tvStoreAddress.setText(account.store.address);
                            tvStorePhoneNumber.setText(account.store.phoneNumber);
                            linearLayout1.setVisibility(v.GONE);
                            linearLayout2.setVisibility(v.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Clicked Register", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.d("RESPONSE 1", response);
                            Toast.makeText(AboutMeActivity.this, "Store Fail to Register!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.d("Respons 2", response);
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AboutMeActivity.this, "Connection Error, Register Failed!", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.toString());
                    }
                };
                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(account.id, storeName, storeAddress, storePhoneNumber, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(registerStoreRequest);
            }
        });


        cancelbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(v.GONE);
                registerstorebutton.setVisibility(v.VISIBLE);
                Toast.makeText(getApplicationContext(), "Clicked Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void takeBalance() {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    tvBalance.setText("" + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AboutMeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }
}