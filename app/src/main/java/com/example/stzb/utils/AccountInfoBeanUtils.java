package com.example.stzb.utils;

import com.example.stzb.beans.AccountInfoBeans;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountInfoBeanUtils {
    public static List<AccountInfoBeans> AccountInfoBeans = new ArrayList<>();

    public static void clear() {
        AccountInfoBeans.clear();
    }

}
