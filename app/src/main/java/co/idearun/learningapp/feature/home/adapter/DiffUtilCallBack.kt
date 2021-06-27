package co.idearun.learningapp.feature.home.adapter

import androidx.recyclerview.widget.DiffUtil
import co.idearun.learningapp.data.model.form.Form

class DiffUtilCallBack : DiffUtil.ItemCallback<Form>() {
    override fun areItemsTheSame(oldItem: Form, newItem: Form): Boolean {
        return oldItem.slug == newItem.slug
    }

    override fun areContentsTheSame(oldItem: Form, newItem: Form): Boolean {
        return oldItem.slug == newItem.slug
                && oldItem.title == newItem.title
                && oldItem.visit_count == newItem.visit_count
                && oldItem.submit_count == newItem.submit_count
    }
}