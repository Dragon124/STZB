package com.example.stzb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stzb.http.HttpHelp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        HttpHelp.getInstance().getHistory()
        HttpHelp.getInstance().setMainCallBack { code ->
            when (code) {

            }
        }
        HttpHelp.getInstance().getAccountInfo("01809D53-7548-7955-3A12-5383662DF22D");
    }
}