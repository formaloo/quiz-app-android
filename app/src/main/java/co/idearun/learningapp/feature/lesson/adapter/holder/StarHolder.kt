package co.idearun.learningapp.feature.lesson.adapter.holder

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardStarItemBinding
import co.idearun.learningapp.feature.lesson.listener.FieldsListener
import co.idearun.learningapp.feature.lesson.listener.LessonListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class StarHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardStarItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        uiViewModel: UIViewModel,
        lessonListener: LessonListener
    ) {
        binding.field = item
        binding.form = form
        binding.viewmodel = uiViewModel
        binding.listener = listener
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel


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