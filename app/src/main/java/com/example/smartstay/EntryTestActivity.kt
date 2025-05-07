package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartstay.databinding.ActivityEntryTestBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class EntryTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEntryTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            Log.d("ttest", "${systemBars.left}, ${systemBars.top}, ${systemBars.right}, ${systemBars.bottom}")
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            btnRecommendStay.setOnClickListener {

                val jsonObject = JSONObject().apply {
                    put("성별 코드", "1")
                    put("나이", "25")
                    put("결혼여부코드", "2")
                    put("자녀여부", "0")
                    put("가족유형명", "1인가구")
                    put("직업분류명", "학생")
                    put("구성원 1인당 수익", "1800000")
                    put("동행자 여부", "1")
                    put("동행자 유형", "친구")
                }

                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

                val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level =
                    HttpLoggingInterceptor.Level.BODY }).build()
                val request = Request.Builder().url("https://af26-61-33-26-201.ngrok-free.app/receive").post(requestBody).build()

                okHttpClient.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("message", ""+ e.message)
                        Log.e("local message", ""+e.localizedMessage)
                        Log.e("stackTrace", ""+ e.stackTrace)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val response = response.body?.string()
                        val jsonResponse = JSONArray(response)
                        val recommendStayList = arrayListOf<StayItem>()
                        for (i in 0 until jsonResponse.length()) {
                            val item = jsonResponse.getJSONObject(i)
                            val name = item.getString("숙박업명")
                            val price = item.getInt("숙박업평균가격")
                            val type = item.getString("숙박유형명")
                            val recommendStayItem = StayItem(name, price, type)
                            recommendStayList.add(recommendStayItem)
                        }
                        val intent = Intent(this@EntryTestActivity, TestResultActivity::class.java).apply {
                            putParcelableArrayListExtra("stays", recommendStayList)
                        }
                        startActivity(intent)
                    }
                })

            }
        }
    }
}