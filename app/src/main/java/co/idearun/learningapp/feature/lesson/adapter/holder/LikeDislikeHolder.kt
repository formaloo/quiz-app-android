package co.idearun.learningapp.feature.lesson.adapter.holder

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardLikeDislikeItemBinding
import co.idearun.learningapp.feature.lesson.listener.FieldsListener
import co.idearun.learningapp.feature.lesson.listener.LessonListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class LikeDislikeHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardLikeDislikeItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        viewmodel: UIViewModel,
        lessonListener: LessonListener
    ) {
        binding.field = field
        binding.form = form
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.listener = listener
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = binding.dislikeBtn.context as LifecycleOwner
        binding.fieldUiFooter.field = field
        binding.fieldUiFooter.viewmodel = viewmodel

        binding.dislikeBtn.setOnClickListener {
            it.isSelected = true
            binding.likeBtn.isSelected = false
            viewmodel.addKeyValueToReq(field.slug?:"",-1)
            Handler(Looper.getMainLooper()).postDelayed({
                lessonListener.next()
            }, Constants.AUTO_NEXT_DELAY)
        }

        binding.likeBtn.setOnClickListener {
            it.isSelected = true
            binding.dislikeBtn.isSelected = false

            viewmodel.addKeyValueToReq(field.slug?:"",1)

            Handler(Looper.getMainLooper()).postDelayed({
                lessonListener.next()
            }, Constants.AUTO_NEXT_DELAY)
        }


        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }
    }


}