package co.idearun.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SubmitsResponse(
    val data: Data? = null,
    val status: Int? = null
) : Serializable {
    companion object {
        fun empty() = SubmitsResponse(null, 0)
    }

    fun toSubmitsResponse() = SubmitsResponse(data, status)
}

data class Data(
    val next: String? = null,
    val previous: Any? = null,
    val count: Int? = null,
    val rows: List<RowsItem?>? = null,
    val pageCount: Int? = null,
    @SerializedName("current_page")
    val currentPage: Int? = null,
    @SerializedName("page_size")
    val pageSize: Int? = null,
    @SerializedName("top_fields")
    val topFields: List<TopFieldsItem?>? = null
)

data class FieldData(
    @SerializedName("raw_value")
    val rawValue: String? = null,
    @SerializedName("json_value")
    val jsonValue: Any? = null,
    @SerializedName("json_key")
    val jsonKey: Any? = null,
    val position: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val value: String? = null,
    val alias: String? = null,
    @SerializedName("admin_only")
    val adminOnly: Boolean? = null,
    val slug: String? = null,
    @SerializedName("max_length")
    val maxLength: Int? = null

)

data class ChoiceItemsItem(
    val image: Any? = null,
    val isRandomSortable: Boolean? = null,
    val deleted: Any? = null,
    val updatedAt: String? = null,
    val isOtherChoice: Boolean? = null,
    val jsonKey: Any? = null,
    val createdAt: String? = null,
    val position: Int? = null,
    val title: String? = null,
    val slug: String? = null
)

data class TopFieldsItem(
    @SerializedName("choice_items")
    val choiceItems: List<ChoiceItemsItem?>? = null,
    val position: Int? = null,
    val type: String? = null,
    val title: String? = null,
    val slug: String? = null,
    val maxLength: Int? = null
)

data class RowsItem(
    @SerializedName("phone_verification_state")
    val phoneVerificationState: String? = null,
    @SerializedName("row_tags")
    val rowTags: List<Any?>? = null,
    val data: Map<String, String>? = null,
    @SerializedName("create_at")
    val createdAt: String? = null,
    @SerializedName("create_type")
    val createType: String? = null,
    val submitterRefererAddress: String? = null,
    val form: String? = null,
    @SerializedName("update_at")
    val updatedAt: String? = null,
    val id: Int? = null,
    val user: Any? = null,
    @SerializedName("rendered_data")
    val renderedData: Map<String, FieldData>? = null,
    @SerializedName("submit_code")
    val submitCode: String? = null,
    val slug: String? = null,
)

