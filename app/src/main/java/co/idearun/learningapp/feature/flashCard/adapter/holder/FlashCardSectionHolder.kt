package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.common.extension.visible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardSectionItemBinding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.SwipeStackAdapter
import co.idearun.learningapp.feature.flashCard.SwipeStackListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel
import link.fls.swipestack.SwipeStack

class FlashCardSectionHolder(view: View, private val swipeStackListener: SwipeStackListener) :
    RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardSectionItemBinding.bind(view)
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
        binding.lifecycleOwner = binding.swipeStack.context as LifecycleOwner
        flashcardListener.checkField(item, pos)

        // Prepare the View for the animation
        binding.swipeStack.visible()
        binding.swipeStack.alpha = 0.0f

        // Start the animation
        binding.swipeStack.animate()
            .translationY(binding.swipeStack.height.toFloat())
            .alpha(1.0f)
            .setListener(null);

        setSwipeStack(arrayListOf(item,item,item,item), form, swipeStackListener, pos)

    }

    private fun setSwipeStack(
        fields: ArrayList<Fields>?,
        form: Form,
        listener: SwipeStackListener,
        fieldsPos: Int
    ) {
        val swipeStackAdapter = SwipeStackAdapter(fields ?: arrayListOf(), form)
        binding.swipeStack.adapter = swipeStackAdapter
        swipeStackAdapter.notifyDataSetChanged()

        binding.swipeStack.setListener(object : SwipeStack.SwipeStackListener {
            override fun onViewSwipedToLeft(position: Int) {

            }

            override fun onViewSwipedToRight(position: Int) {

            }

            override fun onStackEmpty() {
                swipeStackListener.onSwipeEnd(fieldsPos)

            }


        })
    }

}