package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutStackSectionItemBinding

class FlashCardStackHolder(view: View) {
    val binding = LayoutStackSectionItemBinding.bind(view)
    fun bindItems(item: Fields, pos: Int, form: Form) {
        binding.field = item
        binding.form = form
        binding.lifecycleOwner = binding.keyTxv.context as LifecycleOwner


    }


}