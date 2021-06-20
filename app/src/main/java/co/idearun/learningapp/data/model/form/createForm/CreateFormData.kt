package co.idearun.learningapp.data.model.form.createForm

import co.idearun.learningapp.data.model.form.Form
import java.io.Serializable

data class CreateFormData(
    var form: Form? = null
): Serializable