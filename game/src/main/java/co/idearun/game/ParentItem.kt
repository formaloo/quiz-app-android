package co.idearun.game

import androidx.collection.ArrayMap
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem

data class ParentItem(
    val title:String? = null,
    var fieldList: List<List<TopFieldsItem?>>? = null,
    var fieldValue: List<ArrayMap<String, FieldData>>? = null
)
