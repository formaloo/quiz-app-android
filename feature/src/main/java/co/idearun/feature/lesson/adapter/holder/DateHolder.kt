package co.idearun.feature.lesson.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.data.model.form.Fields
import co.idearun.data.model.form.Form
import co.idearun.feature.databinding.LayoutFlashCardDateItemBinding
import co.idearun.feature.lesson.listener.FieldsListener
import co.idearun.feature.viewmodel.UIViewModel

class DateHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardDateItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        uiViewModel: UIViewModel
    ) {
        binding.field = field
        binding.form = form
        binding.listener = listener
        binding.viewmodel = uiViewModel
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.lifecycleOwner = binding.keyTxv.context as LifecycleOwner

        if (field.required == true) {
            uiViewModel.reuiredField(field)

        } else {

        }
    }

}