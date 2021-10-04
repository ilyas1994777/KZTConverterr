package com.example.kztconverter.retrofit.parsing

import android.annotation.SuppressLint
import android.os.HandlerThread
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.example.kztconverter.MainActivity
import com.example.kztconverter.retrofit.dataClass.dataExchange
import com.example.kztconverter.retrofit.dataClass.dataRasparseKZT
import com.example.kztconverter.retrofit.retrofit.apiRetrofit
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.logging.Handler
import kotlin.Unit.toString

class parsingExchange {
    var mainAct : MainActivity? = null
    var formExchange: Float? = null
     var qwe = 0
    var mHandler = android.os.Handler()
    fun parseApi(textView: TextView, textViewReconnect: TextView, editValue: EditText, buttonResult: Button): Boolean {
         var flag = false
        flag = false
//        var t = apiRetrofit().apiClient.getUpcomingEvent().execute()
//        Log.d("2222",t.isSuccessful.toString())
//
//        if(t.isSuccessful){
//            flag = true
//            val responseBody = t.body()!!
//            val responseString = responseBody.string()
//
//            var responseJsonObject = JSONObject(responseString)
//            var rasparseJsonObj = parse(responseJsonObject)
//
//            var decFormat = DecimalFormat("0.#")
//
//
//
//            mHandler.post(Runnable {
//                textView.text = decFormat.format(rasparseJsonObj.conversion_rates.KZT)
//            })
//
//
//            formExchange = rasparseJsonObj.conversion_rates.KZT!!.toFloat()
//
//
//        }else Toast.makeText(mainAct, "Connection error", Toast.LENGTH_SHORT)
//        .show()

        apiRetrofit().apiClient.getUpcomingEvent().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    flag = true
                    val responseBody = response.body()!!
                    val responseString = responseBody.string()

                    var responseJsonObject = JSONObject(responseString)
                    var rasparseJsonObj = parse(responseJsonObject)
                    var decFormat = DecimalFormat("0.#")

                    mHandler.post(Runnable {
                        textView.text = decFormat.format(rasparseJsonObj.conversion_rates.KZT)

                    })
                        formExchange = rasparseJsonObj.conversion_rates.KZT!!.toFloat()

                } else Toast.makeText(mainAct, "Connection error", Toast.LENGTH_SHORT)
                    .show()

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                textViewReconnect.isVisible = true
                editValue.isEnabled = false
                buttonResult.isEnabled = false
                Toast.makeText(mainAct, "проблемы с интернетом...", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        return flag
    }



  private  fun parse(branchJSONObject: JSONObject): dataExchange {
        var conversion_rates = branchJSONObject.getJSONObject("conversion_rates")
        return dataExchange(conversion_rates = rasparseDataKZT(conversion_rates))
    }

   private fun rasparseDataKZT(branchJSONObject: JSONObject): dataRasparseKZT {
        var KZT = branchJSONObject.getDouble("KZT")
        return dataRasparseKZT(KZT = KZT)
    }

    fun calculate(editValue: EditText, boolean: Boolean): Int {
//        var valueEdit = editValue.text.toString().toInt()
        var res : Int? = null
        if (boolean == false) {

             res = (formExchange!! * editValue.text.toString().toInt()).toInt()

        }
         if (boolean == true) {
            res = (editValue.text.toString().toInt() / formExchange!!).toInt()
        }

        return res!!
    }
}