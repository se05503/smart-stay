package com.example.smartstay

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartstay.databinding.ActivityChatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatItemList = mutableListOf<ChatItem>()

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

        val chatAdapter = ChatAdapter()
        binding.recyclerviewChat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }

        binding.btnSend.setOnClickListener {
            // 유저 먼저 보내기
            val myText = binding.etMyText.text.toString()
            chatItemList.add(ChatItem(nickname = "유저", message = myText))
            chatAdapter.submitList(chatItemList.toList())

            RetrofitInstance.networkService.postChat(object: Callback<ChatRequest> {
                override fun onResponse(
                    call: Call<ChatRequest?>,
                    response: Response<ChatRequest?>
                ) {
                    if(response.isSuccessful) {

                    }
                }

                override fun onFailure(
                    call: Call<ChatRequest?>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }

            })

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus ?: return@setOnClickListener
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            binding.etMyText.setText("")

            // 챗봇 답변
            val chatMessages = listOf(
                "안녕하세요! 무엇을 도와드릴까요?",
                "오늘 하루도 좋은 하루 되세요!",
                "무엇을 찾고 계신가요?",
                "도움이 필요하시면 말씀해주세요!",
                "앱 사용 중 궁금한 점이 있으신가요?"
            )

            val chatbotText = chatMessages.random()

            chatItemList.add(ChatItem(nickname = "챗봇", message = chatbotText)) // user_id, message
            chatAdapter.submitList(chatItemList.toList())
        }
    }
}