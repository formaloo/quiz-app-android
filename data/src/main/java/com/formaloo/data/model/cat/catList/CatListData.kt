package com.formaloo.data.model.cat.catList

import com.formaloo.data.model.cat.Category
import java.io.Serializable

data class CatListData(
    var categorys: List<Category>? = null
): Serializable
