package co.idearun.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.form.Form
import timber.log.Timber

class GamesAdapter : ListAdapter<Form, GamesAdapter.NewsViewHolder>(NewsDiffCallback) {

    private var onRvItemClickListener: OnRvItemClickListener<Form>? = null

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var titleGameTv : TextView
        lateinit var avatarGameIv : ImageView

        init {
            titleGameTv = view.findViewById(R.id.tvItemGame)
            avatarGameIv = view.findViewById(R.id.ivItemGame)
        }

        fun bind(form: Form) {
            titleGameTv.text = form.title

            /*val url = Uri.parse(form.logo)
            avatarGameIv.setImageURI(url)
*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))

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