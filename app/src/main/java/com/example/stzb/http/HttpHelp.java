package com.example.stzb.http;


import android.util.Log;

import com.example.stzb.MainCallBack;
import com.example.stzb.beans.AccountBeans;
import com.example.stzb.beans.AccountInfoBeans;
import com.example.stzb.utils.AccountBeanUtils;
import com.example.stzb.utils.AccountInfoBeanUtils;
import com.example.stzb.utils.Config;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    RetrofitService service;
    private static HttpHelp httpHelp = new HttpHelp();
    private MainCallBack mainCallBack;

    public void setMainCallBack(MainCallBack mainCallBack) {
        this.mainCallBack = mainCallBack;
    }

    public static HttpHelp getInstance() {
        return httpHelp;
    }

    public void getHistory() {
        AccountBeanUtils.clear();
        getHistory(1);
    }

    //获取前十页的账号详细
    private void getHistory(int page) {
        AccountInfoBeanUtils.clear();
        Call<AccountBeans> call = getRetrofit().getAccounts(System.currentTimeMillis() + "", page + "", Config.pageSessionId);
        call.enqueue(new Callback<AccountBeans>() {
            @Override
            public void onResponse(Call<AccountBeans> call, Response<AccountBeans> response) {
                AccountBeans beans = response.body();
                if (beans.status == 6) {
                    Log.e("123", "验证手机");
                    return;
                }
                AccountBeanUtils.addBeans(beans.result);
                if (page == 15 || beans.paging.is_last_page) {
                    getAccountInfoList();
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

    Gson gson = new Gson();

    //获取账号详细信息
    public void getAccountInfo(String ordersn) {
        Map<String, String> map = new HashMap();
        map.put("serverid", "1");
        map.put("ordersn", ordersn);
        map.put("view_loc", "search_cond");
        map.put("page_session_id", Config.pageSessionId);
        Call<AccountInfoBeans> call = getRetrofit().getAccountInfo(map);
        try {
            addAccountInfo(call.execute().body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private void addAccountInfo(AccountInfoBeans bean) {
        if (bean == null) {
            Log.e("stzb", "数据为空");
            return;
        }
        try {
            bean.time = simpleDateFormat.parse(bean.equip.fair_show_end_time).getTime();
            //小于当前时间跳过
            if (bean.time < System.currentTimeMillis()) {
                return;
            }
            JsonObject jsonObject = gson.fromJson(bean.equip.equip_desc, JsonObject.class).getAsJsonObject("tenure");
            bean.hufu = jsonObject.getAsJsonPrimitive("hufu").toString();
            bean.yufu = jsonObject.getAsJsonPrimitive("bind_yuan_bao").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AccountInfoBeanUtils.AccountInfoBeans.add(bean);
    }

    //遍历所有账号,获取信息信息
    public void getAccountInfoList() {
        List<Future<Object>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();
        for (AccountBeans.ResultBean historyBean : AccountBeanUtils.AccountBeans) {
            tasks.add(() -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getAccountInfo(historyBean.game_ordersn);
                return null;
            });
        }
        try {
            futures = executor.invokeAll(tasks);
            for (Future<Object> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Comparator<AccountInfoBeans> nameComparator = Comparator.comparing(AccountInfoBeans::getTime);
            AccountInfoBeanUtils.AccountInfoBeans.sort(nameComparator);
        }
        for (AccountInfoBeans bean : AccountInfoBeanUtils.AccountInfoBeans) {
            try {
                Log.e("信息", " 虎符:" + bean.hufu + " 玉符:" + bean.yufu + " 时间:" + bean.equip.fair_show_end_time + " 收藏:" + bean.equip.collect_num);
            } catch (Exception e) {
                Log.e("123", "出错");
            }
        }
        executor.shutdown();
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
                            .addHeader("X-Requested-With", "XMLHttpRequest")
                            .addHeader("Cookie", Config.cookie)
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
