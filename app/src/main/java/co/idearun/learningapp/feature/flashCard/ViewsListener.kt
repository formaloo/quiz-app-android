package co.idearun.learningapp.feature.flashCard

import co.idearun.learningapp.data.model.form.Fields

interface ViewsListener {
    fun openDatePicker(field: Fields?)
    fun removeDatePicker()
    fun openFilePicker(field: Fields?, type: String, actionGetContent: String)
    fun openUrl(v: String)
}