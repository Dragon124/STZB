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
    public static List<ResultBean> historyBeans = new ArrayList<>();

    public static void addBeans(List<ResultBean> beans) {
        historyBeans.addAll(beans);
    }

    public static void clear() {
        historyBeans.clear();
    }

    public static int size(){
        return historyBeans.size();
    }
}
