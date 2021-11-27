package com.formaloo.data.model.form.createForm

import com.formaloo.data.model.form.Form
import java.io.Serializable

data class CreateFormData(
    var form: Form? = null
): Serializable