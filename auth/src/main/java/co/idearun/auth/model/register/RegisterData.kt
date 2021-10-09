package co.idearun.auth.model.register

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterData(
    var profile: Profile? = null
) : Serializable
