package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartstay.databinding.FragmentInitialSettingEndBinding
import com.example.smartstay.model.UserInput
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class InitialSettingEndFragment : Fragment(R.layout.fragment_initial_setting_end) {

    private lateinit var binding: FragmentInitialSettingEndBinding
    private val viewModel by activityViewModels<InitialSettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInitialSettingEndBinding.bind(view)

        // 직업 올바른 형태로 변환
        val job = viewModel.userInitialInfoMap["job"] as String
        var conversedJob = ""
        if (job.contains("\n")) conversedJob = job.replace("\n", "")

        binding.btnInitialSettingEnd.setOnClickListener {
            processWithoutServer(conversedJob)
            processWithServer(conversedJob)
        }
    }

    private fun processWithServer(conversedJob: String) {
        val jsonObject = JSONObject().apply {
            put("user_id", viewModel.userInfo.user_id)
            put("성별 코드", viewModel.userInitialInfoMap["gender"] as String)
            put("나이", viewModel.userInitialInfoMap["age"] as Int)
            put("직업분류명", conversedJob)
            put("결혼여부코드", viewModel.userInitialInfoMap["marriage_status"] as String)
            put("자녀여부", viewModel.userInitialInfoMap["children_status"] as String)
            put("가족유형", viewModel.userInitialInfoMap["family_count"].toString())
            put("구성원 1인당 수익", viewModel.userInitialInfoMap["income_per_person"] as Float)
            put("동행자여부", viewModel.userInitialInfoMap["companion_status"] as String)
        }
        val requestBody =
            jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val okHttpClient = OkHttpClient()
        val request =
            Request.Builder().url("${RetrofitInstance.BASE_URL}receive").post(requestBody)
                .build() // @POST("receive")
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("ttest(InitialSettingEndFragment)", "success")
                    val userInput = UserInput(
                        sexCode = viewModel.userInitialInfoMap["gender"] as String,
                        marriage = viewModel.userInitialInfoMap["marriage_status"] as String,
                        age = viewModel.userInitialInfoMap["age"] as Int,
                        familyCount = viewModel.userInitialInfoMap["family_count"].toString(),
                        job = conversedJob,
                        companionType = viewModel.userInitialInfoMap["companion"] as MutableList<String>,
                        children = viewModel.userInitialInfoMap["children_status"] as String,
                        isCompanionExist = viewModel.userInitialInfoMap["companion_status"] as String,
                        income = viewModel.userInitialInfoMap["income_per_person"] as Float
                    )
                    val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                        putExtra("user_nickname", viewModel.userInfo.nickname)
                        putExtra("user_image", viewModel.userInfo.imageUrl)
                        putExtra("user_id", viewModel.userInfo.user_id)
                        putExtra("user_info", userInput)
                    }
                    startActivity(intent)
                } else {
                    Log.d("ttest(InitialSettingEndFragment)", response.body!!.string())
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("ttest(InitialSettingEndFragment)", "" + e.stackTrace)
            }
        })
    }

    private fun processWithoutServer(conversedJob: String) {
        val userInput = UserInput(
            sexCode = viewModel.userInitialInfoMap["gender"] as String,
            marriage = viewModel.userInitialInfoMap["marriage_status"] as String,
            age = viewModel.userInitialInfoMap["age"] as Int,
            familyCount = viewModel.userInitialInfoMap["family_count"].toString(),
            job = conversedJob,
            companionType = viewModel.userInitialInfoMap["companion"] as MutableList<String>,
            children = viewModel.userInitialInfoMap["children_status"] as String,
            isCompanionExist = viewModel.userInitialInfoMap["companion_status"] as String,
            income = viewModel.userInitialInfoMap["income_per_person"] as Float
        )

        val intent = Intent(requireContext(), ChatActivity::class.java).apply {
            putExtra("user_nickname", viewModel.userInfo.nickname)
            putExtra("user_image", viewModel.userInfo.imageUrl)
            putExtra("user_id", viewModel.userInfo.user_id)
            putExtra("user_info", userInput)
        }

        startActivity(intent)
    }

}