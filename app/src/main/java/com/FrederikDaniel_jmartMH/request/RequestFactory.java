package com.FrederikDaniel_jmartMH.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class untuk melakukan request dan mendapatkan object berdasarkan idnya atau halamannya.
 */
public class RequestFactory {

    private static final String URL_FORMAT_ID = "http://10.0.2.2:5000/%s/%d";
    public static final String URL_FORMAT_PAGE = "http://10.0.2.2:5000/%s/page?page=%s&pageSize=%s";

    /**
     * Method StringRequest untuk mendapatkan object berdasarkan id melalui penggunaan GET
     * @param parentURI
     * @param id
     * @param listener
     * @param errorListener
     * @return
     */
    public static final StringRequest getById(String parentURI, int id, Response.Listener<String>listener, Response.ErrorListener errorListener)
    {
        String url = String.format(URL_FORMAT_ID,parentURI,id);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }

    /**
     * Method StringRequest untuk mendapatkan object berdasarkan halaman atau ukuran halaman melalui penggunaan GET
     * @param parentURI
     * @param page
     * @param pageSize
     * @param listener
     * @param errorListener
     * @return
     */
    public static final StringRequest getPage(String parentURI, int page, int pageSize, Response.Listener<String>listener, Response.ErrorListener errorListener)
    {
        String url = String.format(URL_FORMAT_PAGE, parentURI, page, pageSize);
        return new StringRequest(Request.Method.GET, url, listener, errorListener);
    }
}
