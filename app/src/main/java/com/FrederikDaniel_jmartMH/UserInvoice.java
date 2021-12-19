package com.FrederikDaniel_jmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.model.Payment;
import com.FrederikDaniel_jmartMH.model.Store;
import com.FrederikDaniel_jmartMH.request.InvoiceRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Class yang akan menampilkan Invoice pada bagian Account
 */
public class UserInvoice extends AppCompatActivity {
    private RecyclerView recyclerViewInvoice;
    private static final Gson gson = new Gson();
    private ArrayList<Payment> userInvoiceList = new ArrayList<>();
    private ArrayList<Payment> storeInvoiceList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private MenuItem storeName, itemPerson, itemStore;
    private TextView Title;
    private TextView StoreName, UserName;
    public static boolean isUser = true;
    private Store store = new Store();

    /**
     * Function untuk menunjukkan List Card View Invoice
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_invoice);
        Title = findViewById(R.id.invoiceHeader);

        UserName = findViewById(R.id.UserInvoice);
        recyclerViewInvoice = findViewById(R.id.invoiceCardview);
        UserName.setText(account.name);
        InvoiceCardView.InvoiceConfirmation = false;
        getUserInvoiceList();
    }

    /**
     * Function untuk mengambil dan menyimpan data User yang dibeli productnya pada Invoice
     */
    private void getUserInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    userInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {

                    }.getType();
                    userInvoiceList = gson.fromJson(response, paymentListType);
                    Toast.makeText(UserInvoice.this,"testInvoice"+userInvoiceList.get(0).buyerId, Toast.LENGTH_SHORT).show();
                    recyclerViewInvoice.setLayoutManager(new LinearLayoutManager(UserInvoice.this));
                    InvoiceCardView invoiceCardViewAdapter = new InvoiceCardView(userInvoiceList);
                    recyclerViewInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserInvoice.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, true, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(UserInvoice.this);
        queue.add(invoiceRequest);
    }



}