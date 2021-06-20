package co.idearun.learningapp.feature.flashCard.adapter.holder

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.R
import co.idearun.learningapp.data.model.form.ChoiceItem
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.databinding.LayoutFlashCardMatrixItemBinding
import co.idearun.learningapp.feature.Binding
import co.idearun.learningapp.feature.flashCard.FlashcardListener
import co.idearun.learningapp.feature.flashCard.ViewsListener
import co.idearun.learningapp.feature.viewmodel.UIViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class FlashCardMatrixHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = LayoutFlashCardMatrixItemBinding.bind(view)
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
        binding.viewmodel = uiViewModel
        binding.lifecycleOwner = binding.matrixLay.context as LifecycleOwner
        binding.fieldUiFooter.field = item
        binding.fieldUiFooter.viewmodel = uiViewModel

        flashcardListener.checkField(item, pos)

        var choices = arrayListOf<ChoiceItem>()
        var groups = arrayListOf<ChoiceItem>()
        item.choice_items?.let {
            choices = it
        }
        item.choice_groups?.let {
            groups = it
        }

        addRadioButtons(
            item,
            binding.matrixLay,
            choices,
            groups,
            form
        )
    }

    fun addRadioButtons(
        field: Fields,
        container: LinearLayout,
        choices: ArrayList<ChoiceItem>,
        groups: ArrayList<ChoiceItem>,
        form: Form
    ) {
        val context = container.context

        for (g in groups) {
            val value_rg = RadioGroup(context)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(16, 0, 16, 16)
            value_rg.layoutParams = layoutParams

            val title = TextView(context).apply {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.resources.getDimension(R.dimen.font_large)
                )

                setLineSpacing(0f, 1.33f)

                form.text_color?.let {
                    setTextColor(Color.parseColor(Binding.getHexColor(form.text_color)))

                }
                setTypeface(typeface, Typeface.BOLD)

                text = g.title
            }

            value_rg.addView(title)

            value_rg.orientation = LinearLayout.VERTICAL

            for (i in 1..choices.size) {
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.bottomMargin = 20

                val rdbtn = RadioButton(value_rg.context)
                rdbtn.layoutParams = layoutParams

                rdbtn.id = View.generateViewId()
                choices[i - 1].title?.let {
                    rdbtn.text = it
                }
                choices[i - 1].image?.let {

                    setRadioImage(it, rdbtn)
                }

                rdbtn.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.resources.getDimension(R.dimen.font_large)
                )

                Binding.getHexColor(form.text_color)?.let {
                    rdbtn.setTextColor(Color.parseColor(it))

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
                        rdbtn.buttonTintList = colorStateList
                    }
                }

                rdbtn.setOnClickListener {
                    if (g.slug != null && choices[i - 1].slug != null) {
//                        checkedAnswer[g.slug!!] = choices[i - 1].slug!!
//                        viewmodel.addKeyValueToReq(
//                            field.slug!!,
//                            getStr(checkedAnswer)
//                        )
                    }

                }



                value_rg.addView(rdbtn)
            }

            container.addView(value_rg)
            if (!groups.last().equals(g)) {
                val view = View(context)
                val borderParam =
                    RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3)
                borderParam.setMargins(16, 16, 16, 0)
                layoutParams.weight = 1f
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                view.layoutParams = borderParam
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.gray2))
                value_rg.addView(view)

            }
        }

    }

    private fun setRadioImage(it: String, rdbtn: RadioButton) {
        Picasso.get().load(it).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    val padding = rdbtn.resources.getDimensionPixelSize(R.dimen.padding_2xsmall)
                    val height = rdbtn.resources.getDimensionPixelSize(R.dimen.btn_h)
                    val width = rdbtn.resources.getDimensionPixelSize(R.dimen.btn_h_l)

                    val drawable = BitmapDrawable(
                        rdbtn.resources,
                        Bitmap.createScaledBitmap(bitmap, 150, height, false)
                    )
                    rdbtn.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

                    rdbtn.compoundDrawablePadding = padding

                }
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                Log.e("TAG", "onBitmapFailed: $e")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

        })

    }

}