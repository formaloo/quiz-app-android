package co.idearun.learningapp.data.model.search

import co.idearun.learningapp.data.model.form.Form
import java.io.Serializable

data class SearchData(
    var forms: List<Form>? = null,
) : Serializable