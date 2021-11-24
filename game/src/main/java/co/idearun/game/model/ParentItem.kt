package co.idearun.game.model

import androidx.collection.ArrayMap
import co.idearun.data.model.FieldData
import co.idearun.data.model.TopFieldsItem
import co.idearun.data.model.fieldList

data class ParentItem(
    val title:String? = null,
    val rowSlug:String? = null,
    var fieldList: List<List<TopFieldsItem?>>? = null,
    var fieldValue: List<ArrayMap<String, FieldData>>? = null
)


data class ParentItemPlayer(
    val title:String? = null,
    var fieldList: List<List<fieldList?>>? = null,
    var fieldValue: List<ArrayMap<String, String>>? = null
)


