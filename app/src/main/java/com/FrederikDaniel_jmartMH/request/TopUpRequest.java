package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class request yang akan menambahkan balance ketika melakukan topup pada account
 */
public class TopUpRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:5000/account/%d/topUp";
    private final Map<String, String> params;

    /**
     * Method Constructor request yang akan melakukan POST jumlah top up yang ingin dimasukkan ke balance berdasarkan id account
     * @param balance
     * @param id
     * @param listener
     * @param errorListener
     */
    public TopUpRequest(double balance, int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("balance", String.valueOf(balance));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
