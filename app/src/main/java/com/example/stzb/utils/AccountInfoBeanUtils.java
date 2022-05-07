package com.example.stzb.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.stzb.beans.AccountBeans.ResultBean;
import com.example.stzb.beans.AccountInfoBeans;
import com.example.stzb.http.HttpHelp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountInfoBeanUtils {
    private List<AccountInfoBeans> historyBeans = new ArrayList<>();

    public void addBean(AccountInfoBeans beans) {
        historyBeans.add(beans);
    }

    public void clear() {
        historyBeans.clear();
    }

    public int size() {
        return historyBeans.size();
    }

    public void getAccountInfo() {
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (ResultBean historyBean : AccountBeanUtils.historyBeans) {
//            executor.execute();
            futures.add(executor.submit(() -> {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpHelp.getInstance().getAccountInfo(historyBean.game_ordersn);
            }));
        }

        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (AccountInfoBeans historyBean : historyBeans) {
            try {
//                Log.e("123", "玉符:" + historyBean.equip.equip_desc.tenure.hufu + "  虎符" + historyBean.equip.equip_desc.tenure.yuan_bao);
            } catch (Exception e) {
                Log.e("123", "出错");
            }

        }
        executor.shutdown();
    }
}
