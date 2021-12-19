package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PaymentRequest extends StringRequest {
    private static final String CREATE_URL = "http://10.0.2.2:5000/payment/create";
    private static final String SUBMIT_URL = "http://10.0.2.2:5000/payment/%d/submit";
    private static final String ACCEPT_URL = "http://10.0.2.2:5000/payment/%d/accept";
    public static final String CANCEL_URL = "http://10.0.2.2:5000/payment/%d/cancel";

    private final Map<String, String> params;

    /**
     * Method Constructor untuk melakukan POST payment menggunakan detail dari product
     * @param buyerId
     * @param productId
     * @param productCount
     * @param shipmentAddress
     * @param shipmentPlan
     * @param storeId
     * @param listener
     * @param errorListener
     */

    public PaymentRequest(int buyerId, int productId, int productCount, String shipmentAddress, byte shipmentPlan, int storeId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, CREATE_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
        params.put("storeId", String.valueOf(storeId));
    }

    /**
     * Method untuk melakukan method POST terhadap payment yang akan di-accept/diterima
     * @param id
     * @param listener
     * @param errorListener
     */
    public PaymentRequest(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(ACCEPT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }

    /**
     * Method untuk melakukan method POST terhadap payment yang akan di-cancel/digagalkan
     * @param listener
     * @param id
     * @param errorListener
     */
    public PaymentRequest(Response.Listener<String> listener ,int id, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(CANCEL_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
    }


    /**
     * Method untuk melakukan method POST terhadap payment yang akan diSubmit
     * @param id
     * @param receipt
     * @param listener
     * @param errorListener
     */
    public PaymentRequest(int id, String receipt, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(SUBMIT_URL, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("receipt", receipt);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
