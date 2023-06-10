package com.dicoding.storyapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding

class MainAdapter(private val listStory: List<ListStoryItem>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = listStory[position].photoUrl
        val name = listStory[position].name
        val desc = listStory[position].description
        val uploadDate = listStory[position].createdAt

        Glide.with(holder.itemView.context)
            .load(img)
            .into(holder.binding.img)
        holder.binding.etNamaUploader.text = name
        holder.binding.etDesc.text = desc
        holder.binding.etDate.text = uploadDate
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(listStory[holder.adapterPosition],holder)}
    }

    override fun getItemCount() = listStory.size

    class ViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem, holder: ViewHolder)
    }
}