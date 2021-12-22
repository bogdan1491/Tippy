package com.onyx.tippy

import android.animation.ArgbEvaluator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var numberOfFriends: EditText
    private lateinit var splitBillButton: Button
    private lateinit var splitBillAmount: TextView
    private lateinit var sendBillButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tipDescritpionTextView)
        numberOfFriends = findViewById(R.id.numberOfFrindsTextView)
        splitBillButton = findViewById(R.id.splitBillButton)
        splitBillAmount = findViewById(R.id.splitBillTextView)
        sendBillButton = findViewById(R.id.sendBillButton)
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercent.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!numberOfFriends.text.isEmpty()) {
                    val compute =
                        tvTotalAmount.text.toString().toDouble() / numberOfFriends.text.toString()
                            .toDouble()
                    splitBillAmount.text = compute.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

        })
        splitBillButton.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                if (numberOfFriends.text.isEmpty() || tvTotalAmount.text.isEmpty()) {
                    val text = "Please add the bill amount and the number of friends"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                } else {
                    val splitBill: Double =
                        tvTotalAmount.text.toString().toDouble() / numberOfFriends.text.toString()
                            .toDouble()
                    splitBillAmount.text = "%.2f".format(splitBill)
                }
            }
        })
        sendBillButton.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, SaveBillActivity::class.java)
                intent.putExtra("Bill amount", etBaseAmount.text.toString())
                    .putExtra("Tip percent", tvTipPercent.text)
                    .putExtra("Tip amount", tvTipAmount.text)
                    .putExtra("Number of friends", numberOfFriends.text.toString())
                    .putExtra("Split bill", splitBillAmount.text)

                startActivity(intent)

            }
        })


    }

    /**
     * Update tip description based on the percentage and change the color in range of red to green
     *
     */
    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.worst_tip),
            ContextCompat.getColor(this, R.color.best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    /**
     * Compute the tip amount based on the selected percent and the final amount
     *
     */
    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}