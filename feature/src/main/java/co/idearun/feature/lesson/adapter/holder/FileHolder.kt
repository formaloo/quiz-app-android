package co.idearun.feature.lesson.adapter.holder

import android.content.Intent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.feature.R
import co.idearun.common.Constants
import co.idearun.common.extension.visible
import co.idearun.data.model.form.Fields
import co.idearun.data.model.form.Form
import co.idearun.feature.databinding.LayoutFlashCardFileItemBinding
import co.idearun.feature.lesson.listener.FieldsListener
import co.idearun.feature.viewmodel.UIViewModel

class FileHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardFileItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: FieldsListener,
        form: Form,
        viewmodel: UIViewModel
    ) {
        binding.field = field
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


}