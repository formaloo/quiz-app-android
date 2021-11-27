package com.formaloo.feature.lesson.adapter.holder

import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.formaloo.data.model.form.Fields
import com.formaloo.data.model.form.Form
import com.formaloo.feature.databinding.LayoutUiVideoItemBinding
import com.formaloo.feature.lesson.listener.LessonListener
import com.formaloo.feature.viewmodel.UIViewModel
import org.koin.core.KoinComponent
import timber.log.Timber
import java.util.regex.Pattern

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {

    private var link: String? = null
    val binding = LayoutUiVideoItemBinding.bind(itemView)

    fun bindItems(
        field: Fields,
        position_: Int,
        form: Form,
        viewmodel: UIViewModel,
        lessonListener: LessonListener

    ) {
        binding.field = field
        binding.form = form
        binding.fieldUiHeader.field = field
        binding.fieldUiHeader.form = form
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = binding.videoview.context as LifecycleOwner

        binding.openFullBtn.setOnClickListener {
            link?.let {
                lessonListener.openFullScreen(field, it)
            }
        }
        val webview = binding.videoview
        val url = field.video_link

        if (url != null) {
            webview.settings.userAgentString = "Android"
            webview.settings.loadWithOverviewMode = true
            webview.settings.setJavaScriptEnabled(true)
            webview.settings.useWideViewPort = true
            webview.settings.databaseEnabled = true
            webview.settings.allowContentAccess = true
            webview.settings.allowFileAccessFromFileURLs = true
            webview.settings.domStorageEnabled = true
            webview.settings.allowFileAccess = true
            webview.settings.setGeolocationEnabled(true)
            webview.settings.setAppCacheEnabled(true)
            webview.settings.setSupportMultipleWindows(true)
            webview.settings.cacheMode = WebSettings.LOAD_DEFAULT
            webview.setInitialScale(1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                webview.webChromeClient = WebChromeClient()
            }
            val youtuRegex = "v=([^\\\\s&#]*)"
//            val youtuRegex =
//                "/(?:https?://)?(?:www\\.)?(?:(?:youtu\\.be/)|(?:youtube\\.com)/(?:v/|u/\\w/|embed/|watch))(?:(?:\\?v=)?([^#&?=]*))?((?:[?&]\\w*=\\w*)*)/"
            val aparatRegex =
                "/(?:http[s]?://)?(?:www.)?aparat\\.com/v/([^/?&]+)/?/"

            val isAparat = url.contains("aparat")

            val pattern = when {
                isAparat -> {
                    Pattern.compile(
                        aparatRegex,
                        Pattern.CASE_INSENSITIVE
                    )
                }

                else -> {
                    Pattern.compile(
                        youtuRegex,
                        Pattern.CASE_INSENSITIVE
                    )
                }
            }

            val matcher = pattern.matcher(url)

            if (matcher.find()) {
                val group = matcher.group(1)
                Timber.e("group $group")
                link = when {
                    isAparat -> {
                        "https://www.aparat.com/video/video/embed/videohash/${group}/vt/frame"
                    }

                    else -> {
                        "https://www.youtube-nocookie.com/embed/${group}?autoplay=1&controls=1&fs=0&showinfo=1&frameborder=0"
//                        "https://www.youtube-nocookie.com/embed/${group}?controls=0&showinfo=0"
//                        "<iframe width=\"1131\" height=\"636\" src=\"https://www.youtube.com/embed/${group}\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"
                    }
                }

                webview.loadUrl(link ?: "")
                webview.webViewClient = WebViewClient()
            }


        } else {
            webview.invisible()
        }

        if (field.required == true) {
            viewmodel.reuiredField(field)

        } else {

        }
    }


}