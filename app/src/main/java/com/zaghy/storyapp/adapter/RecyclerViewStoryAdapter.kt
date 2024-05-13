package com.zaghy.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zaghy.storyapp.databinding.CardStoryBinding

class RecyclerViewStoryAdapter<T>(
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val bindView: (T, CardStoryBinding) -> Unit,
    private val onClick: (T) -> Unit
) : ListAdapter<T, RecyclerViewStoryAdapter<T>.MyViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewStoryAdapter<T>.MyViewHolder {
        val binding = CardStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewStoryAdapter<T>.MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(private val binding: CardStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: T) {
            bindView(data, binding)
            binding.root.setOnClickListener {
                onClick(data)
            }

        }
    }
}