package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.data.model.form.ChoiceItem
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardSignleItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel

class FlashCardSingleHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardSignleItemBinding.bind(view)
    fun bindItems(
        field: Fields,
        pos: Int,
        listener: ViewsListener,
        form: Form,
        uiViewModel: UIViewModel
    ) {
        binding.field = field
        binding.form = form
        binding.viewmodel = uiViewModel
        binding.listener = listener
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.fieldUiFooter.field = field
        binding.fieldUiFooter.viewmodel = uiViewModel
        val context = binding.valueRg.context
        binding.lifecycleOwner = context as LifecycleOwner


        val type = field.type
        if (type != null && type.equals(Constants.YESNO)) {
            field.choice_items = arrayListOf(
                ChoiceItem(
                    context.resources.getString(R.string.yes),
                    null,
                    "yes"
                ), ChoiceItem(
                    context.resources.getString(
                        R.string.no
                    ), null, "no"
                )
            )
        }

        field.choice_items?.let {
            addRadioButtons(
                field,
                binding.valueRg,
                ArrayList(it),
                form,
                uiViewModel
            )

        }
    }

    fun addRadioButtons(
        field: Fields,
        value_rg: RadioGroup,
        items: ArrayList<ChoiceItem>,
        form: Form,
        uiViewModel: UIViewModel,
    ) {
        val context = value_rg.context
        val type = field.type
        value_rg.orientation = if (type != null && type.equals(Constants.YESNO)) {
            LinearLayout.HORIZONTAL
        } else {
            LinearLayout.VERTICAL
        }

        for (i in 1..items.size) {
            value_rg.addView(createRadioButton(form,uiViewModel,items,field,i,context))
        }
    }

    private fun createRadioButton(
        form: Form,
        uiViewModel: UIViewModel,
        items: java.util.ArrayList<ChoiceItem>,
        field: Fields,
        i: Int,
        context: Context
    ): RadioButton {


        val rdbtn = RadioButton(context)

        rdbtn.apply {
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = 48

            layoutParams = lp
            setPadding(48, 48, 48, 48)
            minLines=2

            Binding.getHexColor(form.field_color)?.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    setBackgroundColor(Color.parseColor(it))
                }
            }
            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.font_xlarge)
            )

            id = View.generateViewId()
            text = items[i - 1].title?:""

            items[i - 1].image?.let {

            }

            Binding.getHexColor(form.text_color)?.let {
                setTextColor(Color.parseColor(it))

                val colorStateList = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(android.R.attr.state_enabled)
                    ), intArrayOf(
                        Color.parseColor(it) //disabled
                        , Color.parseColor(it) //enabled
                    )
                )
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    buttonTintList = colorStateList
                }
            }

            setOnClickListener {
                items[i - 1].slug?.let { slug ->
                    uiViewModel.addKeyValueToReq(field.slug!!, slug)
                }

            }
        }

        return rdbtn
    }
}