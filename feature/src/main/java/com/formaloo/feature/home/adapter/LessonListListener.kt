package com.formaloo.feature.home.adapter

import android.view.View
import com.formaloo.data.model.form.Form

interface LessonListListener {
    fun openLessonPage(form: Form?, formItemLay: View,progress:Int)}