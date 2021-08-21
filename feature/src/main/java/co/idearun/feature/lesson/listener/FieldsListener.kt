package co.idearun.feature.lesson.listener

import co.idearun.data.model.form.Fields

interface FieldsListener {
    fun openDatePicker(field: Fields?)
    fun removeDatePicker()
    fun openFilePicker(field: Fields?, type: String, actionGetContent: String)
    fun openUrl(v: String)
}