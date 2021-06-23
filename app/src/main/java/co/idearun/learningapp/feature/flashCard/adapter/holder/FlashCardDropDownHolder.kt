package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardDropdownItemBinding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.flashCard.adapter.DropDownItemsAdapter
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class FlashCardDropDownHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardDropdownItemBinding.bind(view)
    fun bindItems(
        item: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form, viewmodel: UIViewModel
    ) {
        binding.field = item
        binding.form = form
        binding.fieldUiHeader.field = item
        binding.fieldUiHeader.form = form
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = binding.spinnerValueLay.context as LifecycleOwner
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = viewmodel

        val dropAdapter = DropDownItemsAdapter(form)

        binding.valueSpinner.apply {
            adapter = dropAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    dropAdapter.getItem(position)?.slug?.let { slug ->
                        viewmodel.addKeyValueToReq(item.slug!!, slug)
                    }
                }

            }

        }

        item.choice_items?.let { items ->
            dropAdapter.listItemsTxt = items

        }

        if (item.required == true) {
            viewmodel.reuiredField(item)

        } else {

        }
    }

}