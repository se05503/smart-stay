package com.example.smartstay

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemChatBinding

class ChatAdapter: ListAdapter<ChatItem, ChatAdapter.ViewHolder>(differ) {
    inner class ViewHolder(private val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            binding.tvMessage.text = item.message
            binding.tvNickname.text = item.nickname
            if(item.nickname == "유저") {
                binding.tvMessage.gravity = Gravity.END
                binding.tvNickname.gravity = Gravity.END
            } else if(item.nickname == "챗봇") {
                binding.tvMessage.gravity = Gravity.START
                binding.tvNickname.gravity = Gravity.START
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
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
    }
}