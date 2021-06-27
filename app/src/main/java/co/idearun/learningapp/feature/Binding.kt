package co.idearun.learningapp.feature

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import co.idearun.learningapp.common.BaseMethod
import co.idearun.learningapp.common.Constants
import co.idearun.learningapp.data.model.form.Fields
import co.idearun.learningapp.data.model.form.Form
import co.idearun.learningapp.feature.drawer.SortedLessonListAdapter
import co.idearun.learningapp.feature.lesson.adapter.LessonFieldsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


object Binding : KoinComponent {
    val baseMethod: BaseMethod by inject()

    const val TAG = "Binding"

    @BindingAdapter("app:imageUrlRounded")
    @JvmStatic
    fun loadImageRounded(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view.context).load(url).apply(RequestOptions.circleCropTransform())
                .into(view)
        }

    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view.context).load(url).into(view)
        }

    }

    @BindingAdapter("app:htmlTxt")
    @JvmStatic
    fun setHtmlTxt(txv: TextView, txt: String?) {
        txt?.let {
            txv.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(txt, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(txt)
            }
        }

    }


    @JvmStatic
    @BindingAdapter("app:isVisisble", "app:form")
    fun isVisible(view: ViewGroup, progress: Int?, form: Form?) {
        val show = progress ?: 0 > 0 && form?.slug?.isNotEmpty() == true

        val transition: Transition = Fade()
        transition.duration = 1000
        transition.addTarget(view)
        TransitionManager.beginDelayedTransition(view, transition)

        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("field_desc")
    fun field_desc(view: TextView, field: Fields) {
        val context = view.context

        var description = ""
        field.description?.let {
            description = it
        }
        val type = if (field.sub_type != null) {
            field.sub_type
        } else {
            field.type
        }

        val descPlus = when (type) {

            Constants.FILE -> {
                if (field.max_size != null) {
                    context.getString(co.idearun.learningapp.R.string.max_file_size) + " : " + field.max_size

                } else {
                    ""
                }
            }

            Constants.TIME -> {
                "${context.getString(co.idearun.learningapp.R.string.from)} : ${field?.from_time ?: "-"}" +
                        "  " +
                        "${context.getString(co.idearun.learningapp.R.string.to)} : ${field?.to_time ?: "-"}"

            }

            Constants.DATE -> {
                "${context.getString(co.idearun.learningapp.R.string.from_date)} : ${
                    getDateOnLocale(
                        field.from_date ?: ""
                    ) ?: "-"
                }" +
                        "  " +
                        "${context.getString(co.idearun.learningapp.R.string.to_date)} : ${
                            getDateOnLocale(
                                field.to_date ?: ""
                            ) ?: "-"
                        }"
            }
            Constants.SHORT_TEXT -> {
                if (field.max_length != null) {
                    context.getString(co.idearun.learningapp.R.string.max_char_lenght) + " : " + field.max_length

                } else {
                    ""
                }
            }
            else -> {

                ""
            }
        }

        view.text = if (descPlus.isNotEmpty()) {
            "$description ( $descPlus )"

        } else {
            description
        }
    }

    fun getDateOnLocale(time: String): String? {
        return convertStringToDate(time)

    }

    fun convertStringToDate(time: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date: Date? = null
        var datestr: String? = null

        if (time.isNotEmpty())
            try {
                date = sdf.parse(time)
                datestr = sdf2.format(date)

            } catch (e: Exception) {


            }

        return datestr

    }

    @JvmStatic
    @BindingAdapter("text_color")
    fun setTextColor(view: TextView, color: String?) {
        val txtColor = getHexColor(color) ?: convertRgbToHex("55", "55", "55")
        view.setTextColor(Color.parseColor(txtColor))

    }


//    @JvmStatic
//    @BindingAdapter("penColor")
//    fun setPenColor(view: SignaturePad, color: String?) {
//        val txtColor = getHexColor(color) ?: convertRgbToHex("55", "55", "55")
//        view.setPenColor(Color.parseColor(txtColor))

//    }

    @JvmStatic
    @BindingAdapter("nps_color", "nps_data")
    fun nps_color(view: AppCompatButton, form: Form, status: Boolean?) {

        status?.let {
            val txtColor = getHexColor(form.text_color) ?: convertRgbToHex("55", "55", "55")
            val fieldColor = getHexColor(form.field_color) ?: convertRgbToHex("242", "242", "242")
            val background_color = getHexColor(form.background_color) ?: convertRgbToHex("242", "242", "242")

            if (it) {
                view.background = ColorDrawable(Color.parseColor(txtColor))
                view.setTextColor(Color.parseColor(fieldColor))

            } else {
                view.background = ColorDrawable(Color.parseColor(background_color))
                view.setTextColor(Color.parseColor(txtColor))

            }
        }

    }

//    @JvmStatic
//    @BindingAdapter("pin_color")
//    fun pin_color(view: PinView?, form: Form?) {
//        val txtColor = getHexColor(form?.text_color) ?: convertRgbToHex("55", "55", "55")
//        val fieldColor = getHexColor(form?.field_color) ?: convertRgbToHex("242", "242", "242")
//
////        val shapedrawable = GradientDrawable()
////        shapedrawable.setStroke(4, Color.parseColor(txtColor))
////        shapedrawable.cornerRadius = 3f
////        view.background = shapedrawable
//
//        view?.setTextColor(Color.parseColor(txtColor))
//        view?.setLineColor(Color.parseColor(txtColor))
//        view?.cursorColor = Color.parseColor(txtColor)
//
//    }

//    @JvmStatic
//    @BindingAdapter("progress_color")
//    fun progress_color(view: ProgressBar, form: Form?) {
//        val txtColor = getHexColor(form?.text_color) ?: convertRgbToHex("55", "55", "55")
//
//        val colorList = ColorStateList(
//            arrayOf(
//                intArrayOf(-R.attr.state_enabled),
//                intArrayOf(R.attr.state_enabled)
//            ), intArrayOf(
//                Color.parseColor(txtColor) //disabled
//                , Color.parseColor(txtColor) //enabled
//            )
//        )
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            view.progressTintList = colorList
//        }
//    }

    @JvmStatic
    @BindingAdapter("divider_background")
    fun divider_background(view: View, color: String?) {
        val txtColor = getHexColor(color) ?: convertRgbToHex("55", "55", "55")
        view.setBackgroundColor(Color.parseColor(txtColor))

    }

    @JvmStatic
    @BindingAdapter("text_color")
    fun setTextColor(view: AppCompatButton, color: String?) {
        val txtColor = getHexColor(color) ?: convertRgbToHex("55", "55", "55")
        view.setTextColor(Color.parseColor(txtColor))

    }

    @JvmStatic
    @BindingAdapter("edt_background")
    fun fieldBackground(view: TextInputEditText, color: String?) {
        val backColor = getHexColor(color) ?: convertRgbToHex("242", "242", "242")
        view.setBackgroundColor(Color.parseColor(backColor))
    }

    @JvmStatic
    @BindingAdapter("app:imageTintList")
    fun imageTintList(view: ImageButton, color: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.imageTintList = baseMethod.getColorStateList(color)
        }
    }

    @JvmStatic
    @BindingAdapter("app:progressTint")
    fun progressTint(view: LinearProgressIndicator, color: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.progressTintList = baseMethod.getColorStateList(color)
        }
    }

    @JvmStatic
    @BindingAdapter("app:backgroundTintList")
    fun backgroundTintList(view: View, color: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.backgroundTintList = baseMethod.getColorStateList(color)
        }
    }

    @JvmStatic
    @BindingAdapter("textCursorDrawable")
    fun textCursorDrawable(view: TextInputEditText, color: String?) {
        val txtColor = getHexColor(color) ?: convertRgbToHex("242", "242", "242")
        setCursorColor(view, Color.parseColor(txtColor))
    }

    @JvmStatic
    @BindingAdapter("field_background")
    fun fieldBackgroundShape(view: View?, form: Form?) {
        val shapedrawable = GradientDrawable()

        val fieldColor = getHexColor(form?.field_color) ?: convertRgbToHex("242", "242", "242")
        shapedrawable.setColor(Color.parseColor(fieldColor))

        val borderColor = getHexColor(form?.border_color) ?: convertRgbToHex("255", "255", "255")
        shapedrawable.setStroke(4, Color.parseColor(borderColor))

        shapedrawable.cornerRadius = 3f

        view?.background = shapedrawable

    }

    @JvmStatic
    @BindingAdapter("field_background", "field_has_err")
    fun fieldBackground(view: View, form: Form?, hasErr: Boolean?) {
        val shapedrawable = GradientDrawable()
        val errdrawable = GradientDrawable()

        form?.field_color?.let {
            val fieldColor = getHexColor(it) ?: convertRgbToHex("242", "242", "242")
            shapedrawable.setColor(Color.parseColor(fieldColor))

        }
        form?.border_color?.let {
            val borderColor = getHexColor(it) ?: convertRgbToHex("255", "255", "255")
            shapedrawable.setStroke(4, Color.parseColor(borderColor))

        }


        shapedrawable.cornerRadius = 3f

        errdrawable.setColor(Color.parseColor("#1BFB9B9B"))
        errdrawable.setStroke(4, Color.parseColor("#F43A3B"))

        if (hasErr == true) {
            view.background = errdrawable

        } else {
            view.background = shapedrawable
        }


    }

    @JvmStatic
    @BindingAdapter("field_background", "field_has_err")
    fun selectedFieldBackground(view: View, form: Form?, hasErr: Boolean?) {
        val shapedrawable = GradientDrawable()
        val errdrawable = GradientDrawable()

        form?.text_color?.let {
            val fieldColor = getHexColor(it) ?: convertRgbToHex("242", "242", "242")
            shapedrawable.setColor(Color.parseColor(fieldColor))

        }
        form?.text_color?.let {
            val borderColor = getHexColor(it) ?: convertRgbToHex("255", "255", "255")
            shapedrawable.setStroke(4, Color.parseColor(borderColor))

        }


        shapedrawable.cornerRadius = 3f

        errdrawable.setColor(Color.parseColor("#1BFB9B9B"))
        errdrawable.setStroke(4, Color.parseColor("#F43A3B"))

        if (hasErr == true) {
            view.background = errdrawable

        } else {
            view.background = shapedrawable
        }


    }
    @JvmStatic
    @BindingAdapter("text_color")
    fun setSelectedTextColor(view: TextView,form: Form?) {
        val txtColor = getHexColor(form?.background_color) ?: convertRgbToHex("55", "55", "55")
        view.setTextColor(Color.parseColor(txtColor))

    }

    @JvmStatic
    @BindingAdapter("btn_background")
    fun btnBackgroundShape(view: View, color: String?) {
        val shapedrawable = GradientDrawable()

        val fieldColor = getHexColor(color) ?: convertRgbToHex("242", "242", "242")
        shapedrawable.setColor(Color.parseColor(fieldColor))

        shapedrawable.cornerRadius = 3f

        view.background = shapedrawable

    }

    @JvmStatic
    @BindingAdapter("nps_background")
    fun npsBackgroundShape(view: View, color: String?) {
        val shapedrawable = GradientDrawable()

        val fieldColor = getHexColor(color) ?: convertRgbToHex("242", "242", "242")
        shapedrawable.setColor(Color.parseColor(fieldColor))

        view.background = shapedrawable

    }

    @JvmStatic
    @BindingAdapter("section_background")
    fun sectionBackgroundShape(view: View, form: Form?) {
        val shapedrawable = GradientDrawable()

        val borderColor = getHexColor(form?.border_color) ?: convertRgbToHex("255", "255", "255")
        shapedrawable.setStroke(4, Color.parseColor(borderColor))

        shapedrawable.cornerRadius = 3f

        view.background = shapedrawable

    }

    @JvmStatic
    @BindingAdapter("TextInputLayout_style")
    fun TextInputLayoutStyle(view: TextInputLayout, form: Form?) {

        val borderColor = getHexColor(form?.border_color) ?: convertRgbToHex("255", "255", "255")
        val borderColorList = ColorStateList(
            arrayOf(
                intArrayOf(-R.attr.state_enabled),
                intArrayOf(R.attr.state_enabled)
            ), intArrayOf(
                Color.parseColor(borderColor) //disabled
                , Color.parseColor(borderColor) //enabled
            )
        )


        val fieldColor = getHexColor(form?.field_color) ?: convertRgbToHex("242", "242", "242")
        val fieldColorList = ColorStateList(
            arrayOf(
                intArrayOf(-R.attr.state_enabled),
                intArrayOf(R.attr.state_enabled)
            ), intArrayOf(
                Color.parseColor(fieldColor) //disabled
                , Color.parseColor(fieldColor) //enabled
            )
        )


        view.setBoxStrokeColorStateList(borderColorList)
        view.boxBackgroundColor = Color.parseColor(fieldColor)
    }

    @JvmStatic
    @BindingAdapter("app:text_color")
    fun setTextColor(view: TextInputEditText, color: String?) {
        val txtColor = getHexColor(color) ?: convertRgbToHex("55", "55", "55")
        view.setTextColor(Color.parseColor(txtColor))

    }

    fun getHexColor(color_: String?): String? {

        var hexString: String? = null
        var color = color_
        try {
            if (color != null) {
                color = color.replace("{\"", "")
                color = color.replace("\"", "")

                val a = color.substring(color.indexOf("a:") + 2, color.indexOf("}"))
                val r = color.substring(color.indexOf("r:") + 2, color.indexOf(",g"))
                val g = color.substring(color.indexOf("g:") + 2, color.indexOf(",b"))
                val b = color.substring(color.indexOf("b:") + 2, color.indexOf(",a"))

                hexString = convertRgbToHex(r, g, b)

            }
        } catch (e: Exception) {
        }

        return hexString
    }

    fun convertRgbToHex(r: String, g: String, b: String): String {
        return "#${
            Integer.toHexString(
                Color.rgb(
                    Integer.parseInt(r),
                    Integer.parseInt(g),
                    Integer.parseInt(b)
                )
            )
        }"
    }

    fun setCursorColor(view: TextInputEditText, color: Int) {
        try {
            // Get the cursor resource id
            var field: Field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            field.setAccessible(true)
            val drawableResId: Int = field.getInt(view)

            // Get the editor
            field = TextView::class.java.getDeclaredField("mEditor")
            field.isAccessible = true
            val editor: Any = field.get(view)

            // Get the drawable and set a color filter
            val drawable: Drawable? = AppCompatResources.getDrawable(view.context, drawableResId)
            drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            val drawables = arrayOf(drawable, drawable)

            // Set the drawables
            field = editor.javaClass.getDeclaredField("mCursorDrawable")
            field.setAccessible(true)
            field.set(editor, drawables)
        } catch (ignored: java.lang.Exception) {
        }
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setFormItems(recyclerView: RecyclerView, resource: ArrayList<HashMap<Int, Form>>?) {
        if (recyclerView.adapter is SortedLessonListAdapter)
            with(recyclerView.adapter as SortedLessonListAdapter) {
                resource?.let {
                    collection = it
                }
            }
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setFieldItems(recyclerView: RecyclerView, resource: ArrayList<Fields>?) {
        if (recyclerView.adapter is LessonFieldsAdapter)
            with(recyclerView.adapter as LessonFieldsAdapter) {
                resource?.let {
                    collection = it
                }
            }
    }

    @ColorInt fun darkenColor(@ColorInt color: Int): Int {
        return Color.HSVToColor(FloatArray(3).apply {
            Color.colorToHSV(color, this)
            this[2] *= 0.8f
        })
    }
}