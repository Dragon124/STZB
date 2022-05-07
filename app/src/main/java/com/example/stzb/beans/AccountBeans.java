package com.example.stzb.beans;

import java.util.List;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountBeans {
    public int status;
    public PagingBean paging;
    public List<ResultBean> result;

    public static class PagingBean {
        public boolean is_last_page;
    }

    public static class ResultBean {
        public String game_ordersn;
    }
}

