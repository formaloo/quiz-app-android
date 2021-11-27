package com.formaloo.feature.lesson.listener

import com.formaloo.data.model.form.Fields


interface LessonListener {
    fun closePage()
    fun share()
    fun next()
    fun pre()
    fun openFullScreen(field: Fields, link: String)
}