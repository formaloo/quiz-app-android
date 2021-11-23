package co.idearun.game

import androidx.collection.ArrayMap
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem

data class ItemAdapter(
    val title:String? = null,
    var fieldList: List<TopFieldsItem?>? = null,
    var fieldValue: ArrayMap<String, FieldData>? = null
)
