package com.example.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.ArrayList

class ListAdapter( private val listener: NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {
    private val items: ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newsview, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]

        if (currentItem.imageUrl == ""){
            holder.thumbnail.setBackgroundResource(R.drawable.noimage)
        }
        else
        {
            Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.thumbnail)
        }
        holder.titleView.text = currentItem.title
        holder.description.text = currentItem.description

    }

    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)
        notifyItemChanged(0)
    }

}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val thumbnail: ImageView = itemView.findViewById(R.id.newsImage)
    val titleView: TextView = itemView.findViewById(R.id.news)
    val description: TextView = itemView.findViewById(R.id.description)
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}