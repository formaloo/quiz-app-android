package co.idearun.learningapp.feature.drawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutCategoryItemBinding
import co.idearun.learningapp.databinding.LayoutFormItemBinding
import kotlin.properties.Delegates

class SortedFormListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private var lastForm: Form? = null

    internal var collection: List<Form> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return when (viewType) {
            TYPE_HEADER -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_category_item, parent, false);
                CategoryViewHolder(itemView)
            }
            TYPE_ITEM -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_form_item, parent, false);
                FormViewHolder(itemView)
            }
            else -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_form_item, parent, false);
                FormViewHolder(itemView)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val form = collection[position]
        when (getItemViewType(position)) {
            TYPE_HEADER -> {
                (holder as CategoryViewHolder).bindItems(form)

            }
            TYPE_ITEM -> {
                (holder as FormViewHolder).bindItems(form)

            }
        }
        lastForm = form
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (lastForm == null) {
            TYPE_HEADER
        } else {
            val lastFormCatSlug = lastForm?.category?.slug
            val formSlug = collection[position].category?.slug
            if (lastFormCatSlug != null && formSlug != null && lastFormCatSlug != formSlug) {
                TYPE_HEADER

            } else {
                TYPE_ITEM

            }

        }

    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LayoutCategoryItemBinding.bind(itemView)
        fun bindItems(item: Form) {
            binding.category = item.category
            binding.lifecycleOwner = binding.titleTv.context as LifecycleOwner
        }
    }

    inner class FormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = LayoutFormItemBinding.bind(itemView)
        fun bindItems(item: Form) {
            binding.form = item
            binding.lifecycleOwner = binding.titleTv.context as LifecycleOwner

        }
    }

}