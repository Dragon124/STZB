package com.example.stzb.beans;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountInfoBeans {
    public EquipBean equip;
    public long time;
    public class EquipBean {
        public String equip_desc;
        public String fair_show_end_time;
        public int collect_num;
    }

    public long getTime() {
        return time;
    }

    public int hufu;
    public int yufu;
}
