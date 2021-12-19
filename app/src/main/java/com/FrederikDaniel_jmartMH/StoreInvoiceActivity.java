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
 * Class yang akan menampilkan Invoice pada bagian Store
 */
public class StoreInvoiceActivity extends AppCompatActivity {
    private RecyclerView recyclerViewInvoice;
    private static final Gson gson = new Gson();
    private ArrayList<Payment> userInvoiceList = new ArrayList<>();
    private ArrayList<Payment> storeInvoiceList = new ArrayList<>();
    private Account account = LoginActivity.getLoggedAccount();
    private MenuItem storeName, itemPerson, itemStore;
    private TextView Title;
    private TextView StoreName;
    public static boolean isUser = true;
    private Store store = new Store();

    /**
     * Function untuk menunjukkan List Card View Invoice
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_invoice);
        Title = findViewById(R.id.invoiceHeader);

        StoreName = findViewById(R.id.StoreInvoice);
        recyclerViewInvoice = findViewById(R.id.invoiceCardview);
        StoreName.setText(account.store.name);
        InvoiceCardView.InvoiceConfirmation = true;
        getStoreInvoiceList();
    }

    /**
     * Function untuk mengambil dan menyimpan data store yang dibeli productnya pada Invoice
     */
    private void getStoreInvoiceList() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    storeInvoiceList.clear();
                    JSONArray object = new JSONArray(response);
                    Type paymentListType = new TypeToken<ArrayList<Payment>>() {
                    }.getType();
                    storeInvoiceList = gson.fromJson(response, paymentListType);
                    recyclerViewInvoice.setLayoutManager(new LinearLayoutManager(StoreInvoiceActivity.this));
                    InvoiceCardView invoiceCardViewAdapter = new InvoiceCardView(storeInvoiceList);
                    recyclerViewInvoice.setAdapter(invoiceCardViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoreInvoiceActivity.this, "Get List Failed due to Connection", Toast.LENGTH_SHORT).show();
            }
        };

        InvoiceRequest invoiceRequest = new InvoiceRequest(account.id, false, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(StoreInvoiceActivity.this);
        queue.add(invoiceRequest);
    }

}