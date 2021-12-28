package com.onyx.tippy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SaveBillActivity : AppCompatActivity() {
    private lateinit var billInfoTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_bill)
        billInfoTextView = findViewById(R.id.receiveBillTextView)
        val intent : Intent = getIntent()

        billInfoTextView.text
    }
}

