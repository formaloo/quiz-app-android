package co.idearun.game.feature.adapter

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
import co.idearun.data.model.form.Fields
import co.idearun.game.R
import co.idearun.game.feature.viewmodel.FormViewModel
import com.google.android.material.textfield.TextInputEditText

class FormFieldsAdapter(var vm: FormViewModel) :
    ListAdapter<Fields, FormFieldsAdapter.FormFieldsViewHolder>(FieldsDiffCallback) {

    var disableField = false
    var fieldSlugList = arrayListOf<Fields>()
    var body = ArrayMap<String, String>()

    inner class FormFieldsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var fieldsEdt: TextInputEditText

        init {
            fieldsEdt = view.findViewById(R.id.fieldEdt)
        }

        fun bind(field: Fields) {
            fieldsEdt.hint = field.title
            fieldSlugList.add(field)
            if (disableField)
                disableEditText(fieldsEdt)

            fieldsEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    body.put(field.slug, p0.toString())
                    vm._body.postValue(body)
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

