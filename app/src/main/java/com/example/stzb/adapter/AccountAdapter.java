package com.example.stzb.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.stzb.R;
import com.example.stzb.beans.AccountInfoBeans;

import org.jetbrains.annotations.NotNull;

/**
 * Created by K on 2022/5/7
 * function:
 * other:
 */
public class AccountAdapter extends BaseQuickAdapter<AccountInfoBeans, BaseViewHolder> {
    public AccountAdapter(int layosId) {
        super(R.layout.account_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AccountInfoBeans accountInfoBeans) {

    }
}
