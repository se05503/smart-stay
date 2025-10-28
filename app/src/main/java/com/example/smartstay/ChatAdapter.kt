package com.example.smartstay

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.ItemChatBotRecommendationBinding
import com.example.smartstay.databinding.ItemChatBotTextBinding
import com.example.smartstay.databinding.ItemChatUserBinding
import com.example.smartstay.model.ChatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChatAdapter(
    private val clickListener: ItemClickListener
): ListAdapter<ChatModel, RecyclerView.ViewHolder>(differ) {

    inner class UserViewHolder(private val binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.UserMessage)  {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            Glide.with(binding.ivProfile).load(item.profile).into(binding.ivProfile)
        }
    }

    inner class ChatBotTextViewHolder(private val binding: ItemChatBotTextBinding): RecyclerView.ViewHolder(binding.root) {

        private var typingJob: Job? = null
        private var loadingJob: Job? = null

        fun bind(item: ChatModel) {

            when(item) {
                is ChatModel.ChatBotLoading -> {
                    loadingJob?.cancel()
                    loadingJob = CoroutineScope(Dispatchers.Main).launch {
                        var dotCount = 0
                        while(isActive) {
                            var dots = ".".repeat(dotCount%4) // 0 ~ 3
                            binding.tvMessage.text = "챗봇이 생각하는 중입니다. 조금만 기다려주세요${dots}"
                            dotCount++
                            delay((300..500).random().toLong())
                        }
                    }
                }

                is ChatModel.ChatBotMessage -> {
                    typingJob = null
                    loadingJob?.cancel()
                    binding.tvMessage.text = ""

                    typingJob = CoroutineScope(Dispatchers.Main).launch {
                        val message = item.message
                        for(i in message.indices) {
                            binding.tvMessage.append(message[i].toString())
                            delay((30..80).random().toLong())
                        }
                    }
                }

                is ChatModel.UserMessage -> {}
            }

        }

        fun onViewRecycled() { loadingJob?.cancel() }
    }

    inner class ChatBotRecommendViewHolder(private val binding: ItemChatBotRecommendationBinding): RecyclerView.ViewHolder(binding.root) {

        private var typingJob: Job? = null

        fun bind(item: ChatModel.ChatBotMessage) {

            typingJob = null
            binding.tvMessage.text = ""

            typingJob = CoroutineScope(Dispatchers.Main).launch {
                val message = item.message
                for(i in message.indices) {
                    binding.tvMessage.append(message[i].toString())
                    delay((30..80).random().toLong())
                }
            }

            binding.viewpager2RecommendStays.adapter = ChatRecommendStayAdapter(item.accommodationInfo ?: emptyList(), clickListener) // TODO: [refactor] 5개만 표시할건데 recyclerview는 조금 적절하지 않을 수도 있겠다
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if(holder is ChatBotTextViewHolder) {
            holder.onViewRecycled()
        }
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return when(val item = getItem(position)) {
            is ChatModel.UserMessage -> {
                VIEW_TYPE_USER
            }
            is ChatModel.ChatBotMessage -> {
                when(item.type) {
                    0 -> VIEW_TYPE_CHATBOT_TEXT
                    1 -> VIEW_TYPE_CHATBOT_RECOMMEND
                    else -> VIEW_TYPE_NONE
                }
            }
            is ChatModel.ChatBotLoading -> {
                VIEW_TYPE_CHATBOT_LOADING
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }
            VIEW_TYPE_CHATBOT_TEXT, VIEW_TYPE_CHATBOT_LOADING -> {
                val binding = ItemChatBotTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ChatBotTextViewHolder(binding)
            }
            else -> {
                val binding = ItemChatBotRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ChatBotRecommendViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when(holder) {
            is UserViewHolder -> holder.bind(item as ChatModel.UserMessage)
            is ChatBotTextViewHolder -> holder.bind(item)
            is ChatBotRecommendViewHolder -> holder.bind(item as ChatModel.ChatBotMessage)
        }
    }

    companion object {
        val differ = object: DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(
                oldItem: ChatModel,
                newItem: ChatModel
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatModel,
                newItem: ChatModel
            ): Boolean {
                return oldItem == newItem
            }

        }

        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_CHATBOT_TEXT = 1
        private const val VIEW_TYPE_CHATBOT_RECOMMEND = 2
        private const val VIEW_TYPE_CHATBOT_LOADING = 3
        private const val VIEW_TYPE_NONE = 4 // 예외 처리용
    }
}