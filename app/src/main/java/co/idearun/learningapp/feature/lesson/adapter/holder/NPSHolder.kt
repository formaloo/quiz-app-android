package co.idearun.learningapp.feature.lesson.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardNpsItemBinding
import co.idearun.learningapp.feature.lesson.listener.FieldsListener
import co.idearun.learningapp.feature.lesson.listener.LessonListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel


class NPSHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardNpsItemBinding.bind(view)
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
        binding.listener = listener
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        binding.viewmodel = uiViewModel
        binding.lifecycleOwner = binding.npsRec.context as LifecycleOwner
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel


        binding.npsRec.apply {
            adapter =
                NpsItemAdapter(item, form, uiViewModel, binding.errLay,lessonListener)
            layoutManager = GridLayoutManager(context, 11)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
        }

    }

}