package co.idearun.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import com.google.android.material.textfield.TextInputEditText


class ResultAdapter :
    ListAdapter<TopFieldsItem, ResultAdapter.ResultViewHolder>(ResultDiffCallback) {

    companion object {
        const val VIEW_TYPE_TEXT_FIELD = 0
        const val VIEW_TYPE_DROP_DOWN = 1
    }

    public var disableField = false
    public var fieldDataMap = ArrayMap<String, FieldData>()

    private var onRvItemClickListener: OnRvItemClickListener<TopFieldsItem>? = null


    internal class LayoutOneViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val btn: Button

        // method to set the views that will
        // be used further in onBindViewHolder method.
        private fun setView(text: String) {
            btn.text = text
        }

        init {
            btn = itemView.findViewById(R.id.button)
        }
    }


    inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var fieldsEdt: TextInputEditText

        init {
            fieldsEdt = view.findViewById(R.id.fieldEdt)

        }

        fun bind(field: TopFieldsItem) {

            fieldsEdt.hint = field.title
            fieldsEdt.setText(fieldDataMap.get(field.slug)?.value, TextView.BufferType.EDITABLE)

            if (field.type.equals("dropdown")) {
                field.choiceItems?.forEach { }
                fieldsEdt.setText("این فیلد وضعیت است", TextView.BufferType.EDITABLE)
            }

            if (disableField)
                disableEditText(fieldsEdt)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_field_text, parent, false)

        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
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


    fun getFieldsValue() {

    }

    fun setOnRvItemClickListener(onRvItemClickListener: OnRvItemClickListener<TopFieldsItem>) {
        this.onRvItemClickListener = onRvItemClickListener
    }

}

object ResultDiffCallback : DiffUtil.ItemCallback<TopFieldsItem>() {
    override fun areItemsTheSame(oldItem: TopFieldsItem, newItem: TopFieldsItem): Boolean {
        return oldItem.slug == newItem.slug
    }

    override fun areContentsTheSame(oldItem: TopFieldsItem, newItem: TopFieldsItem): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}

private fun disableEditText(editText: TextInputEditText) {
    editText.isFocusable = false
    editText.isEnabled = false
    editText.isCursorVisible = false
    editText.keyListener = null
    //editText.setBackgroundColor(Color.TRANSPARENT)
}

