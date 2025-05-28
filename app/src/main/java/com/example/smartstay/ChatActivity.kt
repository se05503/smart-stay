package com.example.smartstay

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ActivityChatBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        linearLayoutManager = LinearLayoutManager(applicationContext)

        chatAdapter = ChatAdapter()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                linearLayoutManager.smoothScrollToPosition(binding.recyclerviewChat, null, chatAdapter.itemCount-1)
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

            chatItemList.add(ChatModel.UserMessage(profile = R.drawable.ic_user, nickname = "유저", message = myText))
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

            if((lastUserMessage as ChatModel.UserMessage).message.contains("추천")) {
                chatBotMessage = ChatModel.ChatBotMessage(
                    type = 1,
                    message = "숙소를 추천해드렸어요! 아래 숙소는 어떠세요?",
                    accommodationInfo = AccommodationInfo(
                        name = "라비드아틀란 호텔",
                        pricePerNight = 58000,
                        address = "해운대해수욕장 도보 13분"
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