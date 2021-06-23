package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardLikeDislikeItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class FlashCardLikeDislikeHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardLikeDislikeItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form,
        viewmodel: UIViewModel
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


        Binding.getHexColor(form.text_color)?.let {
            binding.dislikeBtn.setColorFilter(Color.parseColor(it))
            binding.likeBtn.setColorFilter(Color.parseColor(it))
        }

        binding.dislikeBtn.setOnClickListener {
            changeBtnActivity(binding.dislikeBtn, binding.likeBtn, form, false)
            viewmodel.addKeyValueToReq(field.slug!!, -1)
            hideErr(binding, viewmodel)

        }

        binding.likeBtn.setOnClickListener {
            changeBtnActivity(binding.likeBtn, binding.dislikeBtn, form, true)
            viewmodel.addKeyValueToReq(field.slug!!, 1)
            hideErr(binding, viewmodel)

        }


        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }
    }

    private fun hideErr(binding: LayoutFlashCardLikeDislikeItemBinding, viewmodel: UIViewModel) {
        binding.errLay.invisible()
        viewmodel.setErrorToField(Fields(), "")

    }

    private fun changeBtnActivity(
        activeBtn: ImageButton,
        deActiveBtn: ImageButton,
        form: Form,
        likeBtn: Boolean
    ) {

        activeBtn.apply {
            if (likeBtn) {
                setImageResource(R.drawable.ic_like)

            } else {
                setImageResource(R.drawable.ic_dislike)

            }
            Binding.getHexColor(form.text_color)?.let {
                setColorFilter(Color.parseColor(it))
            }

        }

        deActiveBtn.apply {
            if (likeBtn) {
                setImageResource(R.drawable.ic_dislike_border)

            } else {
                setImageResource(R.drawable.ic_like_border)

            }
            Binding.getHexColor(form.text_color)?.let {
                setColorFilter(Color.parseColor(it))
            }
        }
    }

}