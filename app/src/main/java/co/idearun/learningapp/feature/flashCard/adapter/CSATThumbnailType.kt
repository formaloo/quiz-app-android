package co.idearun.learningapp.feature.flashCard.adapter

import androidx.annotation.DrawableRes
import co.idearun.learningapp.R

enum class CSATThumbnailType(@DrawableRes val drawable: Int) {
    star(android.R.drawable.star_big_on),
    heart(android.R.drawable.star_big_on),
    monster(android.R.drawable.star_big_on),
    funny_face(android.R.drawable.star_big_on),
    flat_face(android.R.drawable.star_big_on),
    outlined(android.R.drawable.star_big_on)
}