package co.idearun.feature.lesson.listener

import co.idearun.data.model.form.Fields


interface LessonListener {
    fun closePage()
    fun share()
    fun next()
    fun pre()
    fun openFullScreen(field: Fields, link: String)
}