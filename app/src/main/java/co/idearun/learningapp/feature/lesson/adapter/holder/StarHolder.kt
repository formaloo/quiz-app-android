package co.idearun.learningapp.feature.lesson.adapter.holder

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardStarItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.lesson.listener.FieldsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class StarHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardStarItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        uiViewModel: UIViewModel
    ) {
        binding.field = item
        binding.form = form
        binding.viewmodel = uiViewModel
        binding.listener = listener
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel

        Binding.getHexColor(form.text_color)?.let {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-R.attr.state_enabled),
                    intArrayOf(R.attr.state_enabled)
                ), intArrayOf(
                    Color.parseColor(it) //disabled
                    , Color.parseColor(it) //enabled
                )
            )
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                binding.starRating.progressTintList = colorStateList
            }
        }

        binding.starRating.setOnRatingBarChangeListener { ratingBar, fl, b ->
            uiViewModel.addKeyValueToReq(item.slug!!, fl)
        }

        if (item.required == true) {
            uiViewModel.reuiredField(item)

        } else {

        }
    }



}