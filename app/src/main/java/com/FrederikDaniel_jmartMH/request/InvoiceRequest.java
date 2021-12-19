package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class yang akan mengembalikan invoice dari backend
 */
public class InvoiceRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:5000/payment/%s?%s=%s";
    private final Map<String, String> params;

    /**
     * Method untuk melakukan request dan mendapatkan list dari invoice yang terdaftar pada JSon dan dilakukan GET berdasarkan id akun pembeli atau id akun store
     * @param id
     * @param byAccount
     * @param listener
     * @param errorListener
     */
    public InvoiceRequest(int id, boolean byAccount, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, byAccount ? "getByAccountId" : "getByStoreId", byAccount ? "buyerId" : "storeId", id), listener, errorListener);
        params = new HashMap<>();
        params.put(byAccount ? "buyerId" : "storeId", String.valueOf(id));
    }
    public Map<String, String> getParams(){
        return params;
    }
}
