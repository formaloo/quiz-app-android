package com.formaloo.game.feature.adapter.model

import androidx.collection.ArrayMap
import com.formaloo.data.model.FieldData
import com.formaloo.data.model.TopFieldsItem

data class ParentItem(
    val rowSlug:String? = null,
    var fieldList: List<List<TopFieldsItem?>>? = null,
    var fieldValue: List<ArrayMap<String, FieldData>>? = null
)

