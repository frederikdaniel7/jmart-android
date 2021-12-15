package com.FrederikDaniel_jmartMH;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class MainActivity<account> extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
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
    private String shipmentPlan;
    private String condition;
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

        ShowProductList(page, 3);
        listView.setOnItemClickListener(this);
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
    public void GetshowFilterProductList(int page, int pageSize) {
        String filteredname = editName.getText().toString();
        Integer minP;
        Integer maxP;
        if (editLowestprice.getText().toString().equals("")) {
            minP = null;
        } else {
            minP = Integer.valueOf(editLowestprice.getText().toString());
        }

        if (editHighestprice.getText().toString().equals("")) {
            maxP = null;
        } else {
            maxP = Integer.valueOf(editHighestprice.getText().toString());
        }
        ProductCategory category = ProductCategory.BOOK;
        String productcategory = spinnerCategory.getSelectedItem().toString();
        for (ProductCategory p : ProductCategory.values()) {
            if (p.toString().equals(productcategory)) {
                category = p;
            }
        }

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    productList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type PListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productList = gson.fromJson(response, PListType);
                    if(!productList.isEmpty()){
                        List<String> productnameList = new ArrayList<>();
                        for (Product product : productList) {
                            productnameList.add(product.name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.product_list_view, productnameList);
                        listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error! Already in the last filter!", Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        if (maxP == null && minP == null) {
            filterRequest = new FilterRequest(page, pageSize, account.id, filteredname, category, listener, errorListener);
        } else if (maxP == null) {
            filterRequest = new FilterRequest(page, pageSize, account.id, minP, filteredname, category, listener, errorListener);
        } else if (minP == null) {
            filterRequest = new FilterRequest(filteredname, page, account.id, maxP, category, listener, errorListener);
        } else {
            filterRequest = new FilterRequest(page, account.id, filteredname, minP, maxP, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = productList.get(position);

        shipmentPlan = "REGULER";
        if (product.shipmentPlans == 1) {
            shipmentPlan = "INSTANT";
        } else if (product.shipmentPlans == 2) {
            shipmentPlan = "SAME DAY";
        } else if (product.shipmentPlans == 4) {
            shipmentPlan = "NEXT DAY";
        } else if (product.shipmentPlans == 8) {
            shipmentPlan = "REGULER";
        } else if (product.shipmentPlans == 16) {
            shipmentPlan = "KARGO";
        }

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_product_detail_dialog);


        final TextView productName = dialog.findViewById(R.id.productName);
        final TextView productWeight = dialog.findViewById(R.id.productWeight);
        final TextView productCondition = dialog.findViewById(R.id.productCondition);
        final TextView productPrice = dialog.findViewById(R.id.productPrice);
        final TextView productDiscount = dialog.findViewById(R.id.productDiscount);
        final TextView productShipmentplan = dialog.findViewById(R.id.productShipmentplan);
        final TextView productCategory = dialog.findViewById(R.id.productCategory);
        final Button buyButton = dialog.findViewById(R.id.buttonBuy);
        final ImageButton detailButton = dialog.findViewById(R.id.detailButton);
        final LinearLayout detailView = dialog.findViewById(R.id.detailView);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailView.getVisibility()== View.GONE)
                {
                    detailButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    detailView.setVisibility(View.VISIBLE);

                }
                else {
                    detailView.setVisibility(View.GONE);
                    detailButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
            }
        });

        condition = "New";
        if (product.getconditionUsed()) {
            condition = "Used";
        } else {
            condition = "New";
        }

        productName.setText(product.name);
        productPrice.setText("Rp. " + product.price);
        productWeight.setText(product.weight + " Kg");
        productCondition.setText(condition);

        productDiscount.setText(product.discount + " %");
        productShipmentplan.setText(shipmentPlan);
        productCategory.setText(product.category + "");

        dialog.show();
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.goButton)
        {
            page = Integer.valueOf(editPage.getText().toString());
            page--;
            if (applyFilter) {
                GetshowFilterProductList(page, 3);
            } else {
                ShowProductList(page,3);
            }
        }
        else if(v.getId() == R.id.nextButton){
            page++;
            if (applyFilter) {
                GetshowFilterProductList(page, 3);
            } else {
                ShowProductList(page,3);
            }
        }
        else if(v.getId() == R.id.prevButton){
            if (page == 0) {
                Toast.makeText(MainActivity.this, "You're already in Page 1", Toast.LENGTH_SHORT).show();
            } else if (page >= 1) {
                page--;
                if (applyFilter) {
                    GetshowFilterProductList(page, 3);
                } else {
                    ShowProductList(page,3);
                }
            }
        }
        else if (v.getId() == R.id.applyButtonFilter)
        {
            GetshowFilterProductList(0, 3);
            applyFilter = true;
            editPage.setText("" + 1);
            Toast.makeText(MainActivity.this, "Filter Success!", Toast.LENGTH_SHORT).show();
        }

        else if(v.getId() == R.id.clearBT)
        {
            page = 0;
            GetshowFilterProductList(page,3);
            applyFilter = false;
            editPage.setText("" + 1);
            Toast.makeText(MainActivity.this, "Filter Cleared!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}