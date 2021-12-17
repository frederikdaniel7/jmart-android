package com.FrederikDaniel_jmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.model.Product;
import com.FrederikDaniel_jmartMH.model.ProductCategory;
import com.FrederikDaniel_jmartMH.request.CreateProductRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {

    private EditText editNameProduct,editWeightProduct,editPriceProduct,editDiscountProduct;
    private RadioButton radiobuttonNew,radiobuttonUsed;
    private Spinner categorySpinner,shipmentSpinner;
    private Button createProductButton, cancelButton;
    private Product product;
    private static final Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        editNameProduct = findViewById(R.id.prodName);
        editWeightProduct = findViewById(R.id.prodWeight);
        editPriceProduct = findViewById(R.id.prodPrice);
        editDiscountProduct = findViewById(R.id.prodDiscount);
        createProductButton = findViewById(R.id.CreateProduct);
        cancelButton = findViewById(R.id.CancelProduct);
        radiobuttonNew = findViewById(R.id.newCondition);
        radiobuttonUsed = findViewById(R.id.usedCondition);

        List<String> productCategoryList = new ArrayList<>();
        for(ProductCategory category : ProductCategory.values()){
            productCategoryList.add(category.toString());
        }


        categorySpinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productCategoryList);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);

        shipmentSpinner = findViewById(R.id.spinnerShipment);
        ArrayAdapter<String> shipmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.shipmentPlans));
        shipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shipmentSpinner.setAdapter(shipmentAdapter);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateProductActivity.this,MainActivity.class);

                startActivity(i);
            }
        });

        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = LoginActivity.getLoggedAccount();
                String name = editNameProduct.getText().toString();
                String weight = editWeightProduct.getText().toString();
                String price = editPriceProduct.getText().toString();
                String discount = editDiscountProduct.getText().toString();

                int productWeight = Integer.valueOf(weight);
                double productPrice = Double.valueOf(price);
                double productDiscount = Double.valueOf(discount);
                boolean conditionUsed = checkButton(radiobuttonNew, radiobuttonUsed);
                ProductCategory category = getProductCategory(categorySpinner);
                byte shipmentPlan = getShipmentPlan(shipmentSpinner);

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(CreateProductActivity.this,"Create Product Success!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(CreateProductActivity.this,"Register Store First",Toast.LENGTH_SHORT).show();
                            }
                            product = gson.fromJson(response, Product.class);
                            Intent intent = new Intent(CreateProductActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateProductActivity.this,"Create Product Failed due to Connection!",Toast.LENGTH_SHORT).show();
                        Log.d("ERROR",error.toString());
                    }
                };
                CreateProductRequest createProductRequest = new CreateProductRequest(account.id, name, productWeight, conditionUsed, productPrice, productDiscount, category, shipmentPlan, listener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
                queue.add(createProductRequest);
            }
        });
    }


    public boolean checkButton(RadioButton radiobuttonNew, RadioButton radiobuttonUsed){
        boolean isUsed = false;
        if(radiobuttonNew.isChecked()){
            isUsed = true;
        }
        else if(radiobuttonUsed.isChecked()){
            isUsed = false;
        }
        return isUsed;
    }

    public ProductCategory getProductCategory(Spinner categorySpinner){
        ProductCategory category = ProductCategory.BOOK;
        String productCategory = categorySpinner.getSelectedItem().toString();
        for(ProductCategory prodCategory : ProductCategory.values()){
            if(prodCategory.toString().equals(productCategory)){
                category = prodCategory;
            }
        }
        return category;
    }

    public byte getShipmentPlan(Spinner shipmentSpinner){
        String shipmentPlan = shipmentSpinner.getSelectedItem().toString();
        if(shipmentPlan.equals("INSTANT")){
            return 1;
        }
        else if(shipmentPlan.equals("SAME DAY")){
            return 2;
        }
        else if(shipmentPlan.equals("NEXT DAY")){
            return 4;
        }
        else if(shipmentPlan.equals("REGULER")){
            return 8;
        }
        else if(shipmentPlan.equals("KARGO")){
            return 16;
        }
        else {
            return 0;
        }
    }
}