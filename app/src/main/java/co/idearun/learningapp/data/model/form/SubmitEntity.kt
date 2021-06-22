package co.idearun.learningapp.data.model.form

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submit")
data class SubmitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uniqueId: Int,
    var hasFormError: Boolean?,
    var newRow: Boolean?,
    val rowSlug: String?,
    val formSlug: String?,
    var formReq: HashMap<String, String>,
    var files: HashMap<String, Fields>,
    var progressNumber:Int?
)