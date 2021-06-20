package co.idearun.learningapp.feature.flashCard

import co.idearun.learningapp.data.model.form.Fields


interface FlashcardListener {
    fun closePage()
    fun share()
    fun next()
    fun pre()
    fun checkField(field: Fields, pos: Int)
}