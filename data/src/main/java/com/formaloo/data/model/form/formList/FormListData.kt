package com.formaloo.data.model.form.formList

import com.formaloo.data.model.form.Form
import java.io.Serializable

data class FormListData(
    var forms: List<Form>? = null,
    var previous: String? = null,
    var next: String? = null,
    var count: Int? = null,
    var current_page: Int? = null,
    var page_count: Int? = null,
    var page_size: Int? = null
): Serializable