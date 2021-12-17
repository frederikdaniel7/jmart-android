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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.FrederikDaniel_jmartMH.model.Payment;
import com.FrederikDaniel_jmartMH.model.Product;
import com.FrederikDaniel_jmartMH.model.ProductCategory;
import com.FrederikDaniel_jmartMH.request.FilterRequest;
import com.FrederikDaniel_jmartMH.request.PaymentRequest;
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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Activity berupa menu utama, menampilkan semua produk sesuai dengan file JSon dan opsi untuk menggunakan Filter
 * @author Frederik Daniel Joshua H
 * @version 15 Desember 2021
 */
public class MainActivity<account> extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static List<Product> productList = new ArrayList<>();
    public static List<Product> productListdummy = new ArrayList<>();
    public static List<String> productnameList = new ArrayList<>();
    private TextView textView;
    private EditText editPage, editName, editLowestprice, editHighestprice;
    private ListView listView;
    public boolean isEmpty = false;
    public boolean applyFilter = false;
    private Account account = LoginActivity.getLoggedAccount();;
    private Button buttonGo, buttonNext, buttonPrev, buttonApply, buttonClear, pageMark;
    ;
    private CheckBox checkBoxNew, checkBoxUsed;
    private Spinner spinnerCategory;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
    private static final Gson gson = new Gson();
    private int page, totalItem;
    private double totalPrice;
    private Toolbar mTopToolbar;
    private ViewPager viewPager;
    private TabLayout tab_Layout;
    private String shipmentPlan;
    private String condition;
    private Payment payment = new Payment();
    public static String[] productCategory = {"BOOK", "KITCHEN", "ELECTRONIC", "FASHION", "GAMING", "GADGET", "MOTHERCARE", "COSMETICS",
            "HEALTHCARE", "FURNITURE", "JEWELRY", "TOYS", "FNB", "STATIONERY", "SPORTS", "AUTOMOTIVE",
            "PETCARE", "ART_CRAFT", "CARPENTRY", "MISCELLANEOUS", "PROPERTY", "TRAVEL", "WEDDING"};

    /**
     * Mehod inisialisasi variabel untuk menyimpan id layout dan event handler activitynya
     * @param savedInstanceState
     */
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

    /**
     * Method untuk menampilkan list view berisikan produk dengan jumlah yang disesuaikan
     * @param page
     * @param pageSize
     */
    public void ShowProductList(int page, int pageSize){
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    productnameList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>(){}.getType();
                    productListdummy = gson.fromJson(response, productlistType);
                    if(productListdummy!= null)
                    {
                        productList.clear();
                        productList = productListdummy;
                    }
                    else{
                        Toast.makeText(MainActivity.this, "next Page is empty", Toast.LENGTH_SHORT).show();
                    }

                    if (productList == null)
                    {
                       isEmpty = true;
                    }
//                    Log.d("ProductFragment", "" + productList.get(0).getName());
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

    /**
     * Method untuk menampilkan produk berdasarkan Filter yang dimasukkan
     * @param page
     * @param pageSize
     */
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
                Toast.makeText(MainActivity.this, "You are already in the last page!", Toast.LENGTH_SHORT).show();
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

    /**
     * Method yang akan menampilkan detail produk melalui dialog dan juga disertai dengan konfirmasi pembelian produk
     * @param parent
     * @param view
     * @param position
     * @param id
     */
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
            /**
             * function untuk menunjukkan detail product
             */
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
        if (product.isConditionUsed()) {
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
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_payment_dialog);
                final TextView productName = dialog.findViewById(R.id.productName);
                final TextView productWeight = dialog.findViewById(R.id.productWeight);
                final TextView productCondition = dialog.findViewById(R.id.productCondition);
                final TextView productPrice = dialog.findViewById(R.id.productPrice);
                final TextView productDiscount = dialog.findViewById(R.id.productDiscount);
                final TextView productShipmentplan = dialog.findViewById(R.id.productShipmentplan);
                final TextView productCategory = dialog.findViewById(R.id.productCategory);

                final TextView balance = dialog.findViewById(R.id.AccountBalance);
                final TextView finalPrice = dialog.findViewById(R.id.finalPrice);
                final ImageButton buttonAdd = dialog.findViewById(R.id.plusButton);
                final ImageButton buttonRemove = dialog.findViewById(R.id.removeButton);
                final EditText editAmount = dialog.findViewById(R.id.editAmount);
                final EditText editAddress = dialog.findViewById(R.id.editAddress);
                final Button buyNow = dialog.findViewById(R.id.buttonBuyNow);
                final Button cancel = dialog.findViewById(R.id.buttonCancel);
                takeBalance(balance);
                totalItem = 1;

                totalPrice = product.price;
                editAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (editAmount.getText().toString().equals("")) {

                        } else {
                            totalItem = Integer.valueOf(editAmount.getText().toString());
                            totalPrice = (product.price - (product.price * (product.discount/100))) * totalItem ;
                            finalPrice.setText("Rp. " + totalPrice);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                productName.setText(product.name);
                productWeight.setText(product.weight + " Kg");
                productCondition.setText(condition);
                productDiscount.setText(product.discount + " %");
                productShipmentplan.setText(shipmentPlan);
                productCategory.setText(product.category + "");
                balance.setText("Rp" + account.balance);
                totalPrice = (product.price - (product.price * (product.discount/100))) * totalItem ;
                finalPrice.setText("Rp" + totalPrice);

                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (totalItem > 1) {
                            totalItem--;
                            editAmount.setText(Integer.toString(totalItem));
                        }
                    }
                });
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        totalItem++;
                        editAmount.setText(Integer.toString(totalItem));
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                buyNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isEmptyFields = false;
                        if (TextUtils.isEmpty(editAmount.getText().toString())) {
                            isEmptyFields = true;
                            editAmount.setError("This bracket must be filled");
                        }
                        if (TextUtils.isEmpty(editAddress.getText().toString())) {
                            isEmptyFields = true;
                            editAddress.setError("This bracket must be filled");
                        }
                        if (!isEmptyFields) {
                            if (totalPrice < account.balance) {
                                Response.Listener<String> listenerCreatePayment = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject object = null;
                                        try {
                                            object = new JSONObject(response);
                                            if (object != null) {
                                                Toast.makeText(MainActivity.this, "Payment Success!", Toast.LENGTH_SHORT).show();
                                                refreshData();
                                                dialog.dismiss();
                                            }
                                            payment = gson.fromJson(response, Payment.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                Response.ErrorListener errorListenerCreatePayment = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", error.toString());
                                    }
                                };
                                PaymentRequest createPaymentRequest = new PaymentRequest(account.id, product.id, totalItem, editAddress.getText().toString(), product.shipmentPlans, product.accountId, listenerCreatePayment, errorListenerCreatePayment);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                queue.add(createPaymentRequest);
                            } else {
                                Toast.makeText(MainActivity.this, "Balance Insufficient", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
        dialog.show();
    }

    public void refreshData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    Log.d("MainActivity(Rfresh)", "Data: " + account.balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorResponse", "Error: " + error);
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));
    }

    /**
     * Method yang digunakan untuk mengeset respon dari button yang ada pada Main Activity
     * @param v
     */
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
            if (!isEmpty) {
                page++;
            }

            if (applyFilter) {
                GetshowFilterProductList(page, 3);
            } else {
                ShowProductList(page, 3);
            }
        }
        else if(v.getId() == R.id.prevButton){
            isEmpty=false;
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

    /**
     * Method untuk mengupdate balance yang akan ditampilkan pada dialog pembelian Produk
     * @param balance
     */
    public void takeBalance(TextView balance) {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                    balance.setText("Rp" + account.balance);
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
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getById("account", account.id, listener, errorListener));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}