package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.common.extension.invisible
import co.idearun.learningapp.common.extension.visible
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardFileItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel
import timber.log.Timber

class FlashCardFileHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardFileItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form,
        viewmodel: UIViewModel
    ) {
        binding.field = field
//        binding.holder = this
        binding.form = form
        binding.listener = listener
        binding.viewmodel = viewmodel

        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form

        binding.fieldUiFooter.field = field
        binding.fieldUiFooter.viewmodel = viewmodel

        val context = binding.keyTxv.context
        binding.lifecycleOwner = context as LifecycleOwner

        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }


        field.file_type?.let {
            when (it) {
                Constants.image -> {
                    binding.descTxv.text = context.getString(R.string.upload_file_image_Desc)
                    binding.descTxv.visible()
                }
                Constants.document -> {
                    binding.descTxv.visible()
                    binding.descTxv.text = context.getString(R.string.upload_file_image_doc)
                }
                Constants.all -> {
                    ""
                }
                else -> {
                    ""
                }
            }
        }

        binding.fileBtn.setOnClickListener {
            hideErr(binding, viewmodel)
            Timber.e("setOnClickListener file")
            when (field.file_type) {
                Constants.image -> {
                    listener.openFilePicker(field, "image/*", Intent.ACTION_GET_CONTENT)
                }
                Constants.document -> {
                    listener.openFilePicker(field, "application/*", Intent.ACTION_OPEN_DOCUMENT)
                }
                Constants.all -> {
                    listener.openFilePicker(field, "*/*", Intent.ACTION_GET_CONTENT)

                }
                else -> {
                    listener.openFilePicker(field, "*/*", Intent.ACTION_GET_CONTENT)
                }
            }

            binding.removeFileBtn.visible()
        }

        Binding.getHexColor(form.text_color)?.let {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled)
                ), intArrayOf(
                    Color.parseColor(it) //disabled
                    , Color.parseColor(it) //enabled
                )
            )
//            binding.starRating.progressTintList = colorStateList
        }


        viewmodel.fieldFielName.observe(context as LifecycleOwner, {
            it?.let {
                if (it.slug == field.slug) {
                    binding.fielTxv.visible()
                    binding.fielTxv.text = it.title
                } else {

                }
            }
        })


    }

    private fun hideErr(binding: LayoutFlashCardFileItemBinding, viewmodel: UIViewModel) {
        binding.errLay.invisible()
        viewmodel.setErrorToField(Fields(), "")
    }

}