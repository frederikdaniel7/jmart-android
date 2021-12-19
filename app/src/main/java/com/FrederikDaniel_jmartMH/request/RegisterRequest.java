package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Class yang akan melakukan request terhadap registrasi akun yang dimasukkan detailnya pada Reqister Activity
 */
public class RegisterRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:5000/account/register";
    private final Map <String,String> params ;

    /**
     * Method Constructor yang akan melakukan POST untuk meregistrasi akun ke dalam backend
     * @param name
     * @param email
     * @param password
     * @param listener
     * @param errorListener
     */
    public RegisterRequest(
            String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String, String> getParams(){
        return params;
    }
}


