package com.lonewalkerstudios.tiptatious

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

public const val TAG = "MainActivity"
public const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var baseEditAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tipPercentage: TextView
    private lateinit var total: TextView
    private lateinit var tipAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        baseEditAmount = findViewById(R.id.baseEdit)
        seekBarTip = findViewById(R.id.tipPercentageBar)
        tipPercentage = findViewById(R.id.tipPercentage)
        total = findViewById(R.id.tipTotal)
        tipAmount = findViewById(R.id.tipText)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tipPercentage.text = "$INITIAL_TIP_PERCENT%"


        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tipPercentage.text = "$progress%"
                calculate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        baseEditAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                calculate()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        calculate()
    }


    private fun calculate() {
        val baseText = baseEditAmount.text.toString()
        if (baseText.isEmpty()) {
            tipAmount.text = ""
            total.text = ""
            return
        }

        val baseAmount = baseText.toDoubleOrNull() ?: 0.0
        val tipPercent = seekBarTip.progress

        val tipAmountFinal = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmountFinal


        tipAmount.text = String.format(Locale.US, "%.2f", tipAmountFinal)
        total.text = String.format(Locale.US, "%.2f", totalAmount)
    }
}