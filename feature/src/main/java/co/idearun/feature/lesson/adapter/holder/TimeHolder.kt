package co.idearun.feature.lesson.adapter.holder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.data.model.form.Fields
import co.idearun.data.model.form.Form
import co.idearun.feature.databinding.LayoutFlashCardTimeItemBinding
import co.idearun.feature.Binding
import co.idearun.feature.lesson.listener.FieldsListener
import co.idearun.feature.viewmodel.UIViewModel

class TimeHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardTimeItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        uiViewModel: UIViewModel
    ) {
        binding.field = item
        binding.form = form
        binding.viewmodel = uiViewModel
        binding.listener = listener
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel
        binding.lifecycleOwner = binding.keyTxv.context as LifecycleOwner


        val context = binding.keyTxv.context



        Binding.getHexColor(form.text_color)?.let {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    Color.parseColor(it) //disabled
                    , Color.parseColor(it) //enabled
                )
            )


            if (item.required == true) {
                uiViewModel.reuiredField(item)

            } else {

            }
        }

    }

}