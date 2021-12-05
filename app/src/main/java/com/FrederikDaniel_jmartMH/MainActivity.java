package com.FrederikDaniel_jmartMH;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.model.Product;
import com.FrederikDaniel_jmartMH.model.ProductCategory;
import com.FrederikDaniel_jmartMH.request.FilterRequest;
import com.FrederikDaniel_jmartMH.request.RequestFactory;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<account> extends AppCompatActivity implements View.OnClickListener {
    public static List<Product> productList = new ArrayList<>();
    public static List<String> productnameList = new ArrayList<>();
    private TextView textView;
    private EditText editPage, editName, editLowestprice, editHighestprice;
    private ListView listView;
    public boolean applyFilter = false;
    private Account account = LoginActivity.getLoggedAccount();;
    private Button buttonGo, buttonNext, buttonPrev, buttonApply, buttonClear;
    ;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinnerCategory;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
    private static final Gson gson = new Gson();
    private int page;
    private Toolbar mTopToolbar;
    private ViewPager viewPager;
    private TabLayout tab_Layout;

    public static String[] productCategory = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listProd);
        editPage = findViewById(R.id.pageNum);
        buttonGo = findViewById(R.id.goButton);
        buttonNext = findViewById(R.id.nextButton);
        buttonPrev = findViewById(R.id.prevButton);
        editName = findViewById(R.id.name);
        editLowestprice = findViewById(R.id.lowestPrice);
        editHighestprice = findViewById(R.id.highestPrice);
        checkBoxNew = findViewById(R.id.chekNew);
        checkBoxUsed = findViewById(R.id.checkUsed);
        buttonApply = findViewById(R.id.applyButtonFilter);
        buttonClear = findViewById(R.id.clearBT);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerProduct);
        tabLayout = findViewById(R.id.tab_layout);
        productCardview = findViewById(R.id.productCardview);
        filterCardview = findViewById(R.id.filterCardView);
        buttonGo.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrev.setOnClickListener(this);
        buttonApply.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        productCardview.setVisibility(View.VISIBLE);
                        filterCardview.setVisibility(View.GONE);
                        break;
                    case 1:
                        filterCardview.setVisibility(View.VISIBLE);
                        productCardview.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, productCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        page = 0;

        ShowProductList(page, 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        setActivityMode(menuItem.getItemId());
        return super.onOptionsItemSelected(menuItem);
    }

    public void setActivityMode(int modeSelected){
        Intent moveIntent;
        switch (modeSelected){
            case R.id.search:
                break;
            case R.id.addbox:
                moveIntent = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(moveIntent);
                break;
            case R.id.person:
                moveIntent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(moveIntent);
                break;
            default:
                break;
        }
    }

    public void ShowProductList(int page, int pageSize){
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    productnameList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productlistType);
                    Log.d("ProductFragment", "" + productList.get(0).getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (Product product : productList) {
                    productnameList.add(product.getName());
                }
                Log.d("ProductFragment", "Array Size: " + productnameList.size());
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                listView.setAdapter(adapter);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ProductFragmentError", "" + error.toString());
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", page, pageSize, stringListener, errorListener));
    }

    public void ShowProductListFiltered(int page, Integer pageSize, String name, Integer highestPrice, Integer lowestPrice, ProductCategory productCategory){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    JSONArray object = new JSONArray(response);
                    Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, productListType);
                    for (Product product : productList) {
                        productnameList.add(product.getName());
                    }
                    Log.d("ProductFragment", "Array Size: " + productnameList.size());
                    ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        if (highestPrice == null && lowestPrice == null){
            Log.d("If condition 1", "1st");
            filterRequest = new FilterRequest(page, account.id, name, productCategory, listener, errorListener);
        }
        else if(highestPrice == null){
            Log.d("If condition 2", "2nd");
            filterRequest = new FilterRequest(page, account.id, name, lowestPrice, productCategory, listener, errorListener);
        }
        else if(lowestPrice == null){
            Log.d("If condition 3", "3rd");
            filterRequest = new FilterRequest(name, page, account.id, highestPrice, productCategory, listener, errorListener);
        }
        else {
            Log.d("If condition 4", "4th");
            filterRequest = new FilterRequest(page, account.id, name, lowestPrice, highestPrice, productCategory, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.goButton){
            page = Integer.valueOf(editPage.getText().toString());
            page--;
            ShowProductList(page, 1);
        }
        else if(v.getId() == R.id.nextButton){
            page++;
            ShowProductList(page, 1);
        }
        else if(v.getId() == R.id.prevButton){
            if(page == 0){
                Toast.makeText(MainActivity.this, "You are at the first page already", Toast.LENGTH_SHORT).show();
            }
            else{
                page--;
                ShowProductList(page, 1);
            }
        }
        else if(v.getId() == R.id.applyButtonFilter){
            String dataName = editName.getText().toString();
            String minPrice = editLowestprice.getText().toString();
            String maxPrice = editHighestprice.getText().toString();
            Integer dataHighestprice, dataLowestprice;
            if(TextUtils.isEmpty(minPrice)){
                dataLowestprice = null;
            }
            else{
                dataLowestprice = Integer.valueOf(editLowestprice.getText().toString());
            }
            if(TextUtils.isEmpty(maxPrice)){
                dataHighestprice = null;
            }
            else {
                dataHighestprice = Integer.valueOf(editLowestprice.getText().toString());
            }

            ProductCategory category = ProductCategory.BOOK;
            String dataSpinner = spinnerCategory.getSelectedItem().toString();
            for(ProductCategory productCategory : ProductCategory.values()){
                if(productCategory.toString().equals(dataSpinner)){
                    category = productCategory;
                }
            }
            ShowProductListFiltered(page, 1, dataName, dataHighestprice, dataLowestprice, category);
        }
    }
}