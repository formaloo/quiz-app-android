package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardNpsItemBinding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel


class FlashCardNPSHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardNpsItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form,
        flashcardListener: FlashcardListener,
        uiViewModel: UIViewModel
    ) {
        binding.field = item
        binding.form = form
        binding.listener = listener
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        flashcardListener.checkField(item, pos)

        binding.viewmodel = uiViewModel
        binding.lifecycleOwner = binding.npsRec.context as LifecycleOwner
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel


        binding.npsRec.apply {
            adapter =
                FlashCardNpsItemAdapter(item, form, uiViewModel, binding.errLay, flashcardListener)
            layoutManager = GridLayoutManager(context, 11)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
        }

    }

}