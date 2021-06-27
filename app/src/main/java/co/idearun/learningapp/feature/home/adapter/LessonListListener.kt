package co.idearun.learningapp.feature.home.adapter

import android.view.View
import co.idearun.learningapp.data.model.form.Form

interface LessonListListener {
    fun openLesson(form: Form?, formItemLay: View)}