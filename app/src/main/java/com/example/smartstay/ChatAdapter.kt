package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemChatBotBinding
import com.example.smartstay.databinding.ItemChatUserBinding

class ChatAdapter: ListAdapter<ChatModel, RecyclerView.ViewHolder>(differ) {

    inner class UserViewHolder(private val binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.UserMessage) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            binding.ivProfile.setImageResource(item.profile)
        }
    }

    inner class ChatBotViewHolder(private val binding: ItemChatBotBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatModel.ChatBotMessage) {
            binding.tvMessage.text = item.message
            // TODO: 숙소 추천 view 띄우기
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is ChatModel.ChatBotMessage -> VIEW_TYPE_CHATBOT
            is ChatModel.UserMessage -> VIEW_TYPE_USER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_CHATBOT) {
            val binding = ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChatBotViewHolder(binding)
        } else {
            val binding = ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ChatBotViewHolder -> holder.bind(getItem(position) as ChatModel.ChatBotMessage)
            is UserViewHolder -> holder.bind(getItem(position) as ChatModel.UserMessage)
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

        private const val VIEW_TYPE_CHATBOT = 0
        private const val VIEW_TYPE_USER = 1
    }
}