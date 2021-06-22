package co.idearun.learningapp.feature.adapter

import android.view.View
import co.idearun.learningapp.data.model.form.Form

interface FormListListener {
    fun openForm(form: Form?, formItemLay: View)
}