package com.formaloo.feature.lesson.adapter.holder

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.common.Constants
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.feature.databinding.LayoutFlashCardStarItemBinding

import com.formaloo.feature.lesson.listener.LessonListener
import com.formaloo.feature.viewmodel.UIViewModel

class StarHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardStarItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,

        form: Form,
        uiViewModel: UIViewModel,
        lessonListener: LessonListener
    ) {
        binding.field = item
        binding.form = form
        binding.viewmodel = uiViewModel

        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form


        binding.starRating.setOnRatingBarChangeListener { ratingBar, fl, b ->
            uiViewModel.addKeyValueToReq(item.slug!!, fl)
            Handler(Looper.getMainLooper()).postDelayed({
                lessonListener.next()
            }, Constants.AUTO_NEXT_DELAY)
        }

        if (item.required == true) {
            uiViewModel.reuiredField(item)

        } else {

        }
    }



}