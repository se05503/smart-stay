package com.example.smartstay

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ActivityChatBinding
import com.example.smartstay.model.AccommodationInfo
import com.example.smartstay.model.ChatModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var chatBotMessage: ChatModel.ChatBotMessage
    private val chatItemList = mutableListOf<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userNickname = intent.getStringExtra("user_nickname")
        val userImage = intent.getStringExtra("user_image")

        // 대화 시작 전 문구 부분적으로 bold 로 만들기
        val fullText = "사용자님에게 적합한 숙소를 \n추천해드릴게요! \n원하시는 장소나 분위기를 \n말씀해주세요."
        val boldTextFirst = "적합한 숙소를 \n추천"
        val boldTextSecond = "원하시는 장소나 분위기"
        val spannable = SpannableString(fullText)
        val startIndexFirst = fullText.indexOf(boldTextFirst)
        val endIndexFirst = startIndexFirst + boldTextFirst.length
        val startIndexSecond = fullText.indexOf(boldTextSecond)
        val endIndexSecond = startIndexSecond + boldTextSecond.length
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndexFirst,
            endIndexFirst,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndexSecond,
            endIndexSecond,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvInduceChat.text = spannable

        linearLayoutManager = LinearLayoutManager(applicationContext)

        chatAdapter = ChatAdapter()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                linearLayoutManager.smoothScrollToPosition(binding.recyclerviewChat, null, chatAdapter.itemCount-1) // 아이템이 들어올 때마다 가장 마지막 위치로 이동하기
            }
        })

        binding.recyclerviewChat.apply {
            layoutManager = linearLayoutManager
            adapter = chatAdapter
        }

        binding.cvSend.setOnClickListener {
            // 유저 먼저 보내기
            val myText = binding.etMessage.text.toString()

            if(myText.isBlank()) {
                Toast.makeText(this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.tvInduceChat.isVisible = false
            chatItemList.add(ChatModel.UserMessage(profile = userImage, nickname = userNickname, message = myText))
            chatAdapter.submitList(chatItemList.toList())

//            communicateWithServer(myText)

            // 키보드 내리고 입력창 초기화
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: return@setOnClickListener
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            binding.etMessage.setText("")

            // 챗봇 임시 답변 (서버 응답 성공 시 삭제 예정)
            val chatMessages = listOf(
                "안녕하세요! 무엇을 도와드릴까요?",
                "오늘 하루도 좋은 하루 되세요!",
                "무엇을 찾고 계신가요?",
                "도움이 필요하시면 말씀해주세요!",
                "앱 사용 중 궁금한 점이 있으신가요?"
            )

            val chatbotRandomMessage = chatMessages.random()

            val lastUserMessage = chatItemList.last()

            if ((lastUserMessage as ChatModel.UserMessage).message.contains("추천")) {
                chatBotMessage = ChatModel.ChatBotMessage(
                    type = 1,
                    message = "숙소를 추천해드렸어요! 아래 숙소는 어떠세요?",
                    accommodationInfo = listOf(
                        AccommodationInfo(
                            name = "서울 센트럴 호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_1,
                            address = "서울특별시 중구 세종대로 110",
                            latitude = 37.5665f,
                            longitude = 126.9780f,
                            minimumPrice = 85000,
                            averagePrice = 100000,
                            maximumPrice = 120000,
                            starRating = "3성",
                            finalRating = 4,
                            isPetAvailable = "N",
                            isRestaurantExist = "Y",
                            isBarExist = "Y",
                            isCafeExist = "Y",
                            isFitnessCenterExist = "N",
                            isSwimmingPoolExist = "N",
                            isSpaExist = "N",
                            isSaunaExist = "N",
                            isReceptionCenterExist = "Y",
                            isBusinessCenterExist = "Y",
                            isOceanViewExist = "N"
                        ),
                        AccommodationInfo(
                            name = "강남 프리미엄 레지던스",
                            type = "레지던스",
                            image = R.drawable.img_stay_2,
                            address = "서울특별시 강남구 테헤란로 212",
                            latitude = 37.5013f,
                            longitude = 127.0396f,
                            minimumPrice = 110000,
                            averagePrice = 130000,
                            maximumPrice = 160000,
                            starRating = "4성",
                            finalRating = 5,
                            isPetAvailable = "Y",
                            isRestaurantExist = "Y",
                            isBarExist = "N",
                            isCafeExist = "Y",
                            isFitnessCenterExist = "Y",
                            isSwimmingPoolExist = "N",
                            isSpaExist = "N",
                            isSaunaExist = "N",
                            isReceptionCenterExist = "N",
                            isBusinessCenterExist = "Y",
                            isOceanViewExist = "N"
                        ),
                        AccommodationInfo(
                            name = "홍대 스타일 게스트하우스",
                            type = "게스트하우스",
                            image = R.drawable.img_stay_3,
                            address = "서울특별시 마포구 와우산로 29",
                            latitude = 37.5561f,
                            longitude = 126.9229f,
                            minimumPrice = 35000,
                            averagePrice = 50000,
                            maximumPrice = 60000,
                            starRating = "2성",
                            finalRating = 3,
                            isPetAvailable = "N",
                            isRestaurantExist = "N",
                            isBarExist = "Y",
                            isCafeExist = "Y",
                            isFitnessCenterExist = "N",
                            isSwimmingPoolExist = "N",
                            isSpaExist = "N",
                            isSaunaExist = "N",
                            isReceptionCenterExist = "N",
                            isBusinessCenterExist = "N",
                            isOceanViewExist = "N"
                        ),
                        AccommodationInfo(
                            name = "이태원 뷰 호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_4,
                            address = "서울특별시 용산구 이태원로 188",
                            latitude = 37.5349f,
                            longitude = 126.9948f,
                            minimumPrice = 90000,
                            averagePrice = 110000,
                            maximumPrice = 140000,
                            starRating = "3성",
                            finalRating = 4,
                            isPetAvailable = "Y",
                            isRestaurantExist = "Y",
                            isBarExist = "Y",
                            isCafeExist = "Y",
                            isFitnessCenterExist = "Y",
                            isSwimmingPoolExist = "N",
                            isSpaExist = "N",
                            isSaunaExist = "N",
                            isReceptionCenterExist = "Y",
                            isBusinessCenterExist = "N",
                            isOceanViewExist = "N"
                        ),
                        AccommodationInfo(
                            name = "한강 리버뷰 호텔",
                            type = "호텔",
                            image = R.drawable.img_stay_5,
                            address = "서울특별시 영등포구 여의대로 24",
                            latitude = 37.5219f,
                            longitude = 126.9246f,
                            minimumPrice = 130000,
                            averagePrice = 150000,
                            maximumPrice = 180000,
                            starRating = "5성",
                            finalRating = 5,
                            isPetAvailable = "N",
                            isRestaurantExist = "Y",
                            isBarExist = "Y",
                            isCafeExist = "Y",
                            isFitnessCenterExist = "Y",
                            isSwimmingPoolExist = "Y",
                            isSpaExist = "Y",
                            isSaunaExist = "Y",
                            isReceptionCenterExist = "Y",
                            isBusinessCenterExist = "Y",
                            isOceanViewExist = "Y"
                        )
                    )

                )
            } else {
                chatBotMessage = ChatModel.ChatBotMessage(
                    type = 0,
                    message = chatbotRandomMessage,
                    accommodationInfo = null
                )
            }

            chatItemList.add(chatBotMessage)
            chatAdapter.submitList(chatItemList.toList())
        }
    }

//    private fun communicateWithServer(myText: String) {
//        val jsonObject = JSONObject().apply {
//            put("user_id", "12345678")
//            put("message", myText)
//        }
//
//        //            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType()) 원래 형식
//        val requestBody = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
//
//        //            val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = // 원래 형식
//        //                HttpLoggingInterceptor.Level.BODY }).build()
//        val okHttpClient = OkHttpClient()
//        val request = Request.Builder().url("https://ab96-128-134-83-141.ngrok-free.app/receive").post(requestBody).build()
//
//        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                if (response.isSuccessful) {
//                    val chatBotMessage = response.body!!.string()
//                    Log.e("ChatActivity", chatBotMessage)
//                    chatItemList.add(ChatItem(nickname = "챗봇", message = chatBotMessage))
//                    chatAdapter.submitList(chatItemList.toList())
//                }
//            }
//
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                e.stackTraceToString()
//            }
//        })
//
//        //            RetrofitInstance.networkService.postChat(object: Callback<ChatRequest> {
//        //                override fun onResponse(
//        //                    call: Call<ChatRequest?>,
//        //                    response: Response<ChatRequest?>
//        //                ) {
//        //                    if(response.isSuccessful) {
//        //
//        //                    }
//        //                }
//        //
//        //                override fun onFailure(
//        //                    call: Call<ChatRequest?>,
//        //                    t: Throwable
//        //                ) {
//        //                    TODO("Not yet implemented")
//        //                }
//        //
//        //            })
//    }
}