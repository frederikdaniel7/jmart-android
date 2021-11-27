package com.FrederikDaniel_jmartMH.request;



import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestFactory {

    private static final String URL_FORMAT_ID = "http://10.0.2.2:<port>/%s/%d";
    private static final String URL_FORMAT_PAGE = "http://10.0.2.2:<port>%s/page";

    public static StringRequest getById (String parentURI, int id, Response.Listener<String>listener,
    Response.ErrorListener errorListener)
    {
        String url = String.format(URL_FORMAT_ID,parentURI,id);
        return new StringRequest(Request.Method.GET, url,listener,errorListener);
    }
    public final StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String>listener,
                                       Response.ErrorListener errorListener) {
        String url = String.format(URL_FORMAT_PAGE, parentURI);
        Map<String, String> params = new HashMap<>();
        params.put("page", String.format(URL_FORMAT_PAGE, parentURI));
        params.put("pageSize", String.valueOf(pageSize));
        return new StringRequest(Request.Method.GET, url, listener, errorListener) {
            public Map<String, String> getParams() {
                return params;
            }
        };
    }
}
