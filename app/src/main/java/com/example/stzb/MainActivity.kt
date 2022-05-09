package com.example.stzb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stzb.http.HttpHelp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        HttpHelp.getInstance().getHistory()
//        HttpHelp.getInstance().getAccountInfo("202205020802116-1-F1WNZAHNO6YLO9");
    }
}