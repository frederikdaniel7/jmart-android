package com.FrederikDaniel_jmartMH.request;
import com.FrederikDaniel_jmartMH.model.ProductCategory;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class yang akan bertanggung jawab dalam pembuatan dan penyimpanan product
 */
public class CreateProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:5000/product/create";
    private final Map<String, String> params;

    /**
     * Constructor yang akan menyimpan detail product yang diisikan
     * @param id
     * @param NameProduct
     * @param WeightProduct
     * @param ConditionProduct
     * @param priceProduct
     * @param discountProduct
     * @param productCategory
     * @param shipmentPlans
     * @param listener
     * @param errorListener
     */
    public CreateProductRequest(int id, String NameProduct, int WeightProduct, boolean ConditionProduct, double priceProduct, double discountProduct, ProductCategory productCategory, byte shipmentPlans, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST,URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(id));
        params.put("name", NameProduct);
        params.put("weight", String.valueOf(WeightProduct));
        params.put("conditionUsed", String.valueOf(ConditionProduct));
        params.put("price", String.valueOf(priceProduct));
        params.put("discount", String.valueOf(discountProduct));
        params.put("category", productCategory.toString());
        params.put("shipmentPlans", String.valueOf(shipmentPlans));
    }

    public Map<String, String> getParams(){

        return params;
    }
}
