package co.idearun.game.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.collection.ArrayMap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.common.base.OnRvItemClickListener
import co.idearun.data.model.form.Fields
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import com.google.android.material.textfield.TextInputEditText
import timber.log.Timber

class FormFieldsAdapter(var vm: FormViewModel) :
    ListAdapter<Fields, FormFieldsAdapter.FormFieldsViewHolder>(FieldsDiffCallback) {

    public var disableField = false
    public var fieldSlugList = arrayListOf<Fields>()
    public var fieldTextList = arrayListOf<String>()
    var body = ArrayMap<String, String>()

    private var onRvItemClickListener: OnRvItemClickListener<Fields>? = null

    inner class FormFieldsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var fieldsEdt: TextInputEditText

        init {
            fieldsEdt = view.findViewById(R.id.fieldEdt)

        }

        fun bind(field: Fields) {
            //fieldsEdt.text = form.title,

            // if (field.type.equals("short_text"))
            fieldsEdt.hint = field.title
            Timber.i("slug in adapter ${field.slug}")
            fieldSlugList.add(field)
            if (disableField)
                disableEditText(fieldsEdt)

            fieldsEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    body.put(field.slug, p0.toString())
                    vm._body.postValue(body)
                    body.entries.forEach {
                        Timber.i("body size ${it.value}")
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormFieldsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_field_text, parent, false)
        return FormFieldsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormFieldsViewHolder, position: Int) {
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

    fun setOnRvItemClickListener(onRvItemClickListener: OnRvItemClickListener<Fields>) {
        this.onRvItemClickListener = onRvItemClickListener
    }

}

object FieldsDiffCallback : DiffUtil.ItemCallback<Fields>() {
    override fun areItemsTheSame(oldItem: Fields, newItem: Fields): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Fields, newItem: Fields): Boolean {
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

