package co.idearun.learningapp.data.model.cat

import java.io.Serializable

data class Category(
    var id: Int? = null,
    var slug: String? = null,
    var title: String? = null
) : Serializable