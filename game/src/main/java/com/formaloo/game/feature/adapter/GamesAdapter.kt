package com.formaloo.game.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.common.base.OnRvItemClickListener
import com.formaloo.data.model.form.Form
import com.formaloo.game.R

class GamesAdapter : ListAdapter<Form, GamesAdapter.NewsViewHolder>(NewsDiffCallback) {

    private var onRvItemClickListener: OnRvItemClickListener<Form>? = null

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var titleGameTv : TextView
        var avatarGameIv : ImageView

        init {
            titleGameTv = view.findViewById(R.id.tvItemGame)
            avatarGameIv = view.findViewById(R.id.ivItemGame)
        }

        fun bind(form: Form) {
            titleGameTv.text = form.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))

        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.setDuration(1000)

        holder.itemView.animation = fadeIn

        if (onRvItemClickListener != null) {
            holder.itemView.setOnClickListener {
                onRvItemClickListener!!.onItemClick(getItem(position), position)
            }
        }

    }


    fun setOnRvItemClickListener(onRvItemClickListener: OnRvItemClickListener<Form>) {
        this.onRvItemClickListener = onRvItemClickListener
    }

}

object NewsDiffCallback : DiffUtil.ItemCallback<Form>() {
    override fun areItemsTheSame(oldItem: Form, newItem: Form): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Form, newItem: Form): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}