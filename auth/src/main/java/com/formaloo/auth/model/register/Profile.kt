package com.formaloo.auth.model.register

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Profile(
    @SerializedName("session_token")
    var sessionToken: String? = null,
    var companies: List<Any?>? = null,
    var gender: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    @SerializedName("first_name")
    var firstName: String? = null,
    var email: String? = null,
    var username: String? = null
) : Serializable
