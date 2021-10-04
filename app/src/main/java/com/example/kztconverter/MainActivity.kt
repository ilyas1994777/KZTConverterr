package com.example.kztconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.kztconverter.retrofit.parsing.parsingExchange
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var textViewKZT: TextView? = null
    private var editValue: EditText? = null
    private var textViewResultat: TextView? = null
    private var parsExchange = parsingExchange()
    private var progressBar: ProgressBar? = null
    private var buttonResult: Button? = null
    private var textViewReconnect : TextView? = null
    private var switchBoolean : Switch? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        parsExchange.mainAct = this
        progressBar?.isVisible = false

        globalScopeStart()

        switchBoolean!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                textViewResultat!!.text = ""
                editValue!!.text.clear()
                editValue!!.hint = "N-Тенге равно $"

            } else {
                textViewResultat!!.text = ""
                editValue!!.text.clear()
                editValue!!.hint = "N-долларов равно ТГ"
            }

        }


        textViewReconnect!!.setOnClickListener {
            globalScopeStart()
        }

        buttonResult!!.setOnClickListener {
            if (editValue?.text!!.isNotEmpty()) {
                if (switchBoolean!!.isChecked){
                    switchCalculate(true)
                } else switchCalculate(false)
            } else Toast.makeText(this, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
        }

    }


    fun globalScopeStart() {
        var flag = false

        var time = 0
        GlobalScope.async(Dispatchers.Main) {
            progressBar?.isVisible = true
            withContext(Dispatchers.IO) {

                do {
                    delay(500)
                    flag = async { parsExchange.parseApi(textViewKZT!!, textViewReconnect!!, editValue!!, buttonResult!!) }.await()
                    if (time < 3) {
                        time++
//                        Log.d("mmm", time.toString())
                    } else break
                } while (!flag)

            }
            progressBar?.isVisible = false
            editValue?.isEnabled = true
            buttonResult?.isEnabled = true
            textViewReconnect!!.isVisible = false
        }
    }

    fun init(){
        switchBoolean = findViewById(R.id.switchBoolean)
        textViewReconnect = findViewById(R.id.textViewReconnect)
        buttonResult = findViewById(R.id.buttonResult)
        textViewResultat = findViewById(R.id.textViewResultat)
        textViewKZT = findViewById(R.id.textViewKZT)
        editValue = findViewById(R.id.editValue)
        progressBar = findViewById(R.id.progressBar)
    }

    @SuppressLint("SetTextI18n")
    fun switchCalculate(boolean: Boolean){
        if (boolean == false) {
            textViewResultat!!.text =
                "${
                    editValue?.text.toString().toInt()
                } $ = ${
                    parsExchange.calculate(editValue!!, false).toDouble().toString()
                        .replace(".0", "")
                } Тенге"
        }
        if (boolean == true)  {
            textViewResultat!!.text =
                "${
                    editValue?.text.toString().toInt()
                } Тенге = ${
                    parsExchange.calculate(editValue!!, true).toDouble().toString()
                        .replace(".0", "")
                } $"
        }
    }

}