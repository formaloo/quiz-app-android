package co.idearun.data.model

import co.idearun.data.model.form.Form
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EditRomRes(
    var status: Int? = null,
    var data: EditRowData? = null
) : Serializable {
    companion object {
        fun empty() = EditRomRes(0, null)

    }

    fun toEditRomRes() = EditRomRes(status, data)
}

data class EditRowData(
    var row: Row? = null
): Serializable

data class Row(
    var id: String? = null,
    var form: String? = null,
    var slug: String? = null,
    @SerializedName("rendered_data")
    val renderedData: List<FieldData>? = null,
    @SerializedName("readable_data")
    val readableData: Map<String, String>? = null,
    val data: Map<String, String>? = null,
): Serializable
