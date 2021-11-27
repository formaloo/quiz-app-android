package com.formaloo.feature.lesson.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.feature.databinding.LayoutStackSectionItemBinding

class StackHolder(view: View) {
    val binding = LayoutStackSectionItemBinding.bind(view)
    fun bindItems(item: Fields, pos: Int, form: Form) {
        binding.field = item
        binding.form = form
        binding.lifecycleOwner = binding.keyTxv.context as LifecycleOwner


    }


}