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
import kotlinx.coroutines.launch

class ChatAdapter(
    private val context: Context
): ListAdapter<ChatModel, RecyclerView.ViewHolder>(differ) {

    inner class UserViewHolder(private val binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.UserMessage) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            Glide.with(binding.ivProfile).load(item.profile).into(binding.ivProfile)
        }
    }

    inner class ChatBotTextViewHolder(private val binding: ItemChatBotTextBinding): RecyclerView.ViewHolder(binding.root) {

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
        }
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

            binding.viewpager2RecommendStays.adapter = ChatRecommendStayAdapter(item.accommodationInfo ?: emptyList(), context)
            binding.btnNaverMap.setOnClickListener {
                // 네이버 지도 이동
                val intent = Intent(context, NaverMapActivity::class.java)
                intent.putExtra("accommodation_list", ArrayList(item.accommodationInfo))
                context.startActivity(intent)
            }
        }
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_USER) {
            val binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding)
        } else if(viewType == VIEW_TYPE_CHATBOT_TEXT) {
            val binding = ItemChatBotTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatBotTextViewHolder(binding)
        } else {
            val binding = ItemChatBotRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatBotRecommendViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is UserViewHolder -> holder.bind(getItem(position) as ChatModel.UserMessage)
            is ChatBotTextViewHolder -> holder.bind(getItem(position) as ChatModel.ChatBotMessage)
            is ChatBotRecommendViewHolder -> holder.bind(getItem(position) as ChatModel.ChatBotMessage)
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
        private const val VIEW_TYPE_NONE = 3 // 예외 처리용
    }
}