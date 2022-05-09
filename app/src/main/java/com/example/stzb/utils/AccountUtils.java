package com.example.stzb.utils;

import com.example.stzb.beans.AccountInfoBeans;

/**
 * Created by K on 2022/5/9
 * function:
 * other:
 */
public class AccountUtils {
    public static boolean checkInfo(AccountInfoBeans bean) {
        //小于当前时间跳过
        if (bean.time < System.currentTimeMillis()) {
            return false;
        }
        //收藏小于10||符小于1000
        if (bean.equip.collect_num <= 10 && (bean.yufu + bean.hufu) < 1000) {
            return false;
        }
        return true;
    }
}
