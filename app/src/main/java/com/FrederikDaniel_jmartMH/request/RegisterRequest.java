package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
//perbaikan CS9
public class RegisterRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:5000/account/register";
    private final Map <String,String> params ;

    public RegisterRequest(
            String name,
            String email,
            String password,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener
    )
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


