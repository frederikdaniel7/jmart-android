package com.FrederikDaniel_jmartMH.request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class yang akan melakukan request terhadap registrasi Store yang dimasukkan detailnya pada Reqister Activity
 */
public class RegisterStoreRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:5000/account/%d/registerStore";
    private final Map<String, String> params;

    /**
     * Method Constructor yang akan melakukan POST untuk meregistrasi Store ke dalam backend
     * @param id
     * @param name
     * @param address
     * @param phoneNumber
     * @param listener
     * @param errorListener
     */
    public RegisterStoreRequest(int id, String name, String address, String phoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, String.format(URL_FORMAT, id), listener, errorListener);
        params = new HashMap<>();
        params.put("id",String.valueOf(id));
        params.put("name", name);
        params.put("address", address);
        params.put("phoneNumber", phoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}