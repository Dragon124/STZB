package com.example.stzb.http;

import com.example.stzb.beans.AccountBeans;
import com.example.stzb.beans.AccountInfoBeans;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public interface RetrofitService {
    @GET("https://stzb.cbg.163.com/cgi/api/query?view_loc=search_cond&search_type=role&price_max=30000&order_by=collect_num%20DESC")
    Call<AccountBeans> getAccounts(@Query("_") String time, @Query("page") String page, @Query("page_session_id") String pageSessionId);

    @FormUrlEncoded
    @POST("https://stzb.cbg.163.com/cgi/api/get_equip_detail")
    Call<AccountInfoBeans> getAccountInfo(@FieldMap Map<String, String> map);
}
