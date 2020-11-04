package com.example.login.http

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

/**
Created by chene on @date 20-11-3 下午9:35
 **/
class Http {

    suspend fun newCall(body: RequestBody) = withContext(Dispatchers.IO){
        val response = StringBuilder()
            try {
                val url = URL(body.url);
                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = "POST"
                    connectTimeout = 8000
                    readTimeout = 8000
                    doInput = true
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json;charset=utf-8")
                }
                val out = DataOutputStream(connection.outputStream).apply {
                    writeBytes(toJson(body.params))
                }
                out.apply {
                    flush()
                    close()
                }
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                do {
                    line = reader.readLine()?.also {
                        response.append(it)
                    }
                } while (line != null)
                reader.close()
                connection.disconnect()
            }catch (e: Exception){
                e.printStackTrace()
            }
        response.toString()
    }


    private fun toJson(params: Map<String, String>?): String? {
        val jsonObject = JSONObject()
        params?.let {
            for ((k, v) in it){
                jsonObject.put(k, v)
            }
        }
        return jsonObject.toString()
    }
}