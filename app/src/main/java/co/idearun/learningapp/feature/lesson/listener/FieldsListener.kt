package co.idearun.learningapp.feature.lesson.listener

import co.idearun.learningapp.data.model.form.Fields

interface FieldsListener {
    fun openDatePicker(field: Fields?)
    fun removeDatePicker()
    fun openFilePicker(field: Fields?, type: String, actionGetContent: String)
    fun openUrl(v: String)
}