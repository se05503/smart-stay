package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemChatBotRecommendationBinding
import com.example.smartstay.databinding.ItemChatBotTextBinding
import com.example.smartstay.databinding.ItemChatUserBinding

class ChatAdapter: ListAdapter<ChatModel, RecyclerView.ViewHolder>(differ) {

    inner class UserViewHolder(private val binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.UserMessage) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            binding.ivProfile.setImageResource(item.profile)
        }
    }

    inner class ChatBotTextViewHolder(private val binding: ItemChatBotTextBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.ChatBotMessage) {
            binding.tvMessage.text = item.message
        }
    }

    inner class ChatBotRecommendViewHolder(private val binding: ItemChatBotRecommendationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.ChatBotMessage) {
            binding.tvMessage.text = item.message
            val randomImages = listOf(
                R.drawable.img_stay_1,
                R.drawable.img_stay_2,
                R.drawable.img_stay_3,
                R.drawable.img_stay_4,
                R.drawable.img_stay_5,
                R.drawable.img_stay_6,
                R.drawable.img_stay_7,
                R.drawable.img_stay_8,
                R.drawable.img_stay_9,
                R.drawable.img_stay_10
            )
            binding.ivStayImage.setImageResource(randomImages.random())
            binding.tvStayName.text = item.accommodationInfo?.name
            binding.tvStayAddress.text = item.accommodationInfo?.address
            binding.tvStayPrice.text = "${item.accommodationInfo?.pricePerNight.toString()}원 / 박"
//            binding.recyclerviewChatStayRecommendations.adapter = ChatRecommendationAdapter()
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