package com.formaloo.feature.lesson.adapter

import androidx.annotation.DrawableRes
import com.formaloo.feature.R

enum class CSATThumbnailType(@DrawableRes val drawable: Int) {
    star(R.drawable.csat_star),
    heart(R.drawable.csat_star),
    monster(R.drawable.csat_star),
    funny_face(R.drawable.csat_star),
    flat_face(R.drawable.csat_star),
    outlined(R.drawable.csat_star)
}