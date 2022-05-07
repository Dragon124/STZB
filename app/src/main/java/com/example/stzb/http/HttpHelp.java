package com.example.stzb.http;


import android.util.Log;

import com.example.stzb.beans.AccountBeans;
import com.example.stzb.beans.AccountInfoBeans;
import com.example.stzb.utils.AccountBeanUtils;
import com.example.stzb.utils.AccountInfoBeanUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Thread.sleep;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class HttpHelp {
    public String pageSessionId = "01809D05-259B-0089-D0EF-858F52582C50&";
    public AccountInfoBeanUtils accountInfoBeanUtils = new AccountInfoBeanUtils();
    RetrofitService service;
    private static HttpHelp httpHelp = new HttpHelp();

    public static HttpHelp getInstance() {
        return httpHelp;
    }

    public void getHistory() {
        AccountBeanUtils.clear();
        getHistory(1);
    }

    private void getHistory(int page) {
        accountInfoBeanUtils.clear();
        Call<AccountBeans> call = getRetrofit().getAccounts(System.currentTimeMillis() + "", page + "", pageSessionId);
        call.enqueue(new Callback<AccountBeans>() {
            @Override
            public void onResponse(Call<AccountBeans> call, Response<AccountBeans> response) {
                AccountBeans beans = response.body();
                if (beans.status == 6) {
                    Log.e("123", "验证手机");
                }
                AccountBeanUtils.addBeans(beans.result);
                if (page == 10 || beans.paging.is_last_page) {
                    accountInfoBeanUtils.getAccountInfo();
                    return;
                }
                try {
                    sleep(2000);
                    getHistory(page + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AccountBeans> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
 Gson gson=new Gson();
    public void getAccountInfo(String id) {
        Map<String, String> map = new HashMap();
        map.put("serverid", "1");
        map.put("ordersn", "202204241402116-1-UXRRDTXFK7OEKY");
        map.put("view_loc", "search_cond");
        map.put("page_session_id", id);
        Call<AccountInfoBeans> call = getRetrofit().getAccountInfo(map);
//        try {
//            accountInfoBeanUtils.addBean(call.execute().body());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        call.enqueue(new Callback<AccountInfoBeans>() {
            @Override
            public void onResponse(Call<AccountInfoBeans> call, Response<AccountInfoBeans> response) {
//                accountInfoBeanUtils.addBean(response.body());
                Log.e("123",gson.fromJson(response.body().equip.equip_desc, JsonObject.class).getAsJsonObject("tenure").toString());
            }

            @Override
            public void onFailure(Call<AccountInfoBeans> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private RetrofitService getRetrofit() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://stzb.cbg.163.com/")
                    .client(getOkHttpClient())
                    .build();
            service = retrofit.create(RetrofitService.class);
        }
        return service;
    }

    /**
     * 构造okhttp头部
     */
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                            .addHeader("Accept-Encoding", "gzip, deflate, br")
                            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36")
                            .addHeader("Cookie", "UM_distinctid=1805ac294eb935-0697bbb4300909-521e311e-1fa400-1805ac294ecd51; fingerprint=rnplk4vitulj7euo; _ga=GA1.2.1287286381.1651027817; _ns=NS1.2.258598176.1651037439; mp_MA-B9D6-269DF3E58055_hubble=%7B%22sessionReferrer%22%3A%20%22https%3A%2F%2Fepay.163.com%2Faccount%2FsecureSetting.htm%22%2C%22updatedTime%22%3A%201651108721760%2C%22sessionStartTime%22%3A%201651108628261%2C%22sendNumClass%22%3A%20%7B%22allNum%22%3A%206%2C%22errSendNum%22%3A%200%7D%2C%22deviceUdid%22%3A%20%22bc3498fae70d2c46d5ac6177fd539dc0e20dd1fd%22%2C%22persistedTime%22%3A%201651038954588%2C%22LASTEVENT%22%3A%20%7B%22eventId%22%3A%20%22da_screen%22%2C%22time%22%3A%201651108721761%7D%2C%22currentReferrer%22%3A%20%22https%3A%2F%2Fepay.163.com%2FpcMain%2Faccount%2Fsafe-question%2Fstep1%22%2C%22sessionUuid%22%3A%20%228ea7205a04ef303741f6b52fd6e6b74f64ed78c4%22%7D; trace_session_id=01809C1C-EB41-CC9C-1114-1800ED955B7D; _flow_group=g9; _external_mark=direct; is_log_active_stat=1; NTES_YD_SESS=9T8eR5rFfO8l4cjL8X_4tBCR3eVw4ipHnKAH79mEGMDErPh.QAcwBDLPat4I06epNFeawtzOp65HJOdH9StTqWAftJpnlQtZ6DAcePidEnody7.ftOFCIvcaGXREmgxNojgK2X.x3L8jKCtpWrPz3Y3IV0aOt25xmBABhZ6o5S.0b6gN1IM.ko7XeHemMasD9RkAF0GwuJs4qW3Wu5P57MGehfNZ.5JYa2heMKlXqDtcT; NTES_YD_PASSPORT=qAI6JqzAg02UE_546EcRzFl3BAZgaQeQNeGd9UUIiHwxRYNr78A0QW3Y9SIVoOZqP5Z90SKcqOynkcRPpUR0nHcJhAHvoOoXXrDeJHPGh3J1ai6If3AkB78ldyrc.nHfAmjHf_qSVG_Dn86FNHp8TfpK40kqqvNyitUloTTs1olN_q8iJLY6uD4eHBxEv_kPCL_KpOPGx2ankeOolknQJXTLo; S_INFO=1651901748|0|0&60##|13672708263; P_INFO=13672708263|1651901748|1|cbg|00&99|sic&1651898153&g10_client#sic&510100#10#0#0|&0|null|13672708263; sid=zG6gBpJk5O6bJmU3VfOx22sSDwf3ir_L7c1LT7IE; urs_share_login_token=yd.3db8930ec6e5482fb@163.com$9d90849611843cd8620e0d3d7fc0fd98; urs_share_login_token_h5=yd.3db8930ec6e5482fb@163.com$9d90849611843cd8620e0d3d7fc0fd98; login_id=8d4b7e33-cdc7-11ec-9765-b9700a3c7b38")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(getLogInterceptor())
                .build();
        return httpClient;
    }

    public static HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        return loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}
