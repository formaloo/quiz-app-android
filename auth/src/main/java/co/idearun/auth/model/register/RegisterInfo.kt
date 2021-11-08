package co.idearun.auth.model.register

import java.io.Serializable

data class RegisterInfo(
    var name: String?,
    var pass: String?,
    var email: String?,
    var phone: String?,
    var gender: String?
) : Serializable {
    companion object {
        const val GENDER_MALE = "male"
        const val GENDER_FEMALE = "female"
        fun empty() = RegisterInfo(null, null, null, null, null)
    }
}
