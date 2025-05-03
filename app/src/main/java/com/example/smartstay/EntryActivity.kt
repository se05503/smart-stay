package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartstay.databinding.ActivityEntryBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

class EntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEntryBinding.inflate(layoutInflater)
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
                    put("성별 코드", etSexCode.text.toString())
                    put("나이", etAge.text.toString())
                    put("결혼여부코드", etMaritalStatus.text.toString())
                    put("자녀여부", etParentalStatus.text.toString())
                    put("가족유형명", etFamiliyType.text.toString())
                    put("직업분류명", etJobType.text.toString())
                    put("구성원 1인당 수익", etIndividualIncome.text.toString())
                    put("동행자 여부", etCompanionStatus.text.toString())
                    put("동행자 유형", etCompanionType.text.toString())
                }

                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

                val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level =
                    HttpLoggingInterceptor.Level.BODY }).build()
                val request = Request.Builder().url("https://5fcc-211-53-145-61.ngrok-free.app/receive").post(requestBody).build()

                okHttpClient.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("message", ""+ e.message)
                        Log.e("local message", ""+e.localizedMessage)
                        Log.e("stackTrace", ""+ e.stackTrace)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val response = response.body?.string()
                        val intent = Intent(this@EntryActivity, TestResultActivity::class.java).apply {
                            putExtra("recommend_stay", response)
                        }
                        startActivity(intent)
                    }
                })

            }
        }
    }
}