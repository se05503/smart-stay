package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemChatBotBinding
import com.example.smartstay.databinding.ItemChatUserBinding

class ChatAdapter: ListAdapter<ChatItem, RecyclerView.ViewHolder>(differ) {

    inner class UserViewHolder(private val binding: ItemChatUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            binding.ivProfile.setImageResource(item.profile)
        }
    }

    inner class ChatBotViewHolder(private val binding: ItemChatBotBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            binding.ivProfile.setImageResource(item.profile)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position].nickname=="챗봇") VIEW_TYPE_CHATBOT else VIEW_TYPE_USER
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
            is ChatBotViewHolder -> holder.bind(currentList[position])
            is UserViewHolder -> holder.bind(currentList[position])
        }
    }

    companion object {
        val differ = object: DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return oldItem == newItem
            }

        }

        private const val VIEW_TYPE_CHATBOT = 0
        private const val VIEW_TYPE_USER = 1
    }
}