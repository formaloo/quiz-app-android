package co.idearun.learningapp.feature

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object Binding {

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
}