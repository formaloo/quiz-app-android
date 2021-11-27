package com.formaloo.data.model.search

import com.formaloo.data.model.form.Form
import java.io.Serializable

data class SearchData(
    var forms: List<Form>? = null,
) : Serializable