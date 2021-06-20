package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardDateItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class FlashCardDateHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardDateItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form,
        flashcardListener: FlashcardListener,
        uiViewModel: UIViewModel
    ) {
        binding.field = field
        binding.form = form
        binding.listener = listener
        binding.viewmodel = uiViewModel
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.lifecycleOwner = binding.keyTxv.context as LifecycleOwner
        flashcardListener.checkField(field, pos)

        val context = binding.keyTxv.context

        Binding.getHexColor(form.text_color)?.let { it ->
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-R.attr.state_enabled),
                    intArrayOf(R.attr.state_enabled)
                ), intArrayOf(
                    Color.parseColor(it) //disabled
                    , Color.parseColor(it) //enabled
                )
            )

            if (field.required == true) {
                uiViewModel.reuiredField(field)

            } else {

            }
        }
    }

}