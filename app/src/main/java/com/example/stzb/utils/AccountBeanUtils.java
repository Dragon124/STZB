package com.example.stzb.utils;

import com.example.stzb.beans.AccountBeans.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountBeanUtils {
    public static List<ResultBean> AccountBeans = new ArrayList<>();

    public static void addBeans(List<ResultBean> beans) {
        AccountBeans.addAll(beans);
    }

    public static void clear() {
        AccountBeans.clear();
    }

}
