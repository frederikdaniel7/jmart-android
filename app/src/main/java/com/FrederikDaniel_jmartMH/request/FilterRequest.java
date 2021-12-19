package com.FrederikDaniel_jmartMH.request;

import com.FrederikDaniel_jmartMH.model.ProductCategory;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Class yang akan bertanggung jawab dalam memfilter product pada main activity sesuai dengan parameter yang diisikan.
 */
public class FilterRequest extends StringRequest {
    private static final String URL_FORMAT = "http://10.0.2.2:5000/product/getFiltered?page=%s&pageSize=%s&accountId=%s&search=%s&minPrice=%s&maxPrice=%s&category=%s";
    private final Map<String, String> params;

    /**
     * Method Constructor untuk memfilter semua parameter yang dimasukkan
     * @param page
     * @param pageSize
     * @param accountId
     * @param search
     * @param minPrice
     * @param maxPrice
     * @param category
     * @param listener
     * @param errorListener
     */
    public FilterRequest(int page, int pageSize, int accountId, String search, int minPrice, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali page size karena tidak diisi
     * @param page
     * @param accountId
     * @param search
     * @param minPrice
     * @param maxPrice
     * @param category
     * @param listener
     * @param errorListener
     */
    public FilterRequest(int page, int accountId, String search, int minPrice, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, minPrice, maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali minPrice dan maxPrice karena tidak terisi
     * @param page
     * @param pageSize
     * @param accountId
     * @param search
     * @param category
     * @param listener
     * @param errorListener
     */

    public FilterRequest(int page, int pageSize, int accountId, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali  maxPrice karena tidak terisi
     * @param page
     * @param pageSize
     * @param accountId
     * @param minPrice
     * @param search
     * @param category
     * @param listener
     * @param errorListener
     */
    public FilterRequest(int page, int pageSize, int accountId, int minPrice, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali minPrice karena tidak terisi
     * @param category
     * @param page
     * @param pageSize
     * @param accountId
     * @param maxPrice
     * @param search
     * @param listener
     * @param errorListener
     */
    public FilterRequest(ProductCategory category, int page, int pageSize, int accountId, int maxPrice, String search, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, pageSize, accountId, search, "", maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali pageSize, minPrice dan maxPrice karena tidak terisi
     * @param page
     * @param accountId
     * @param search
     * @param category
     * @param listener
     * @param errorListener
     */
    public FilterRequest(int page, int accountId, String search, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, "", "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali pageSize dan maxPrice karena tidak terisi
     * @param page
     * @param accountId
     * @param search
     * @param category
     * @param listener
     * @param errorListener
     */

    public FilterRequest(int page, int accountId, String search, int minPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, minPrice, "", category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", String.valueOf(minPrice));
        params.put("maxPrice", "");
        params.put("category", category.toString());
    }

    /**
     * Method Constructor untuk memfilter semua parameter kecuali pageSize dan minPrice karena tidak terisi
     * @param page
     * @param accountId
     * @param search
     * @param category
     * @param listener
     * @param errorListener
     */
    public FilterRequest(String search, int page, int accountId, int maxPrice, ProductCategory category, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL_FORMAT, page, "", accountId, search, "", maxPrice, category.toString()), listener, errorListener);
        params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("pageSize", "");
        params.put("accountId", String.valueOf(accountId));
        params.put("search", search);
        params.put("minPrice", "");
        params.put("maxPrice", String.valueOf(maxPrice));
        params.put("category", category.toString());
    }

    public Map<String, String> getParams(){
        return params;
    }

}
