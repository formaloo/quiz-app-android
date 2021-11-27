package com.formaloo.auth.model.register

import java.io.Serializable

data class RegisterInfo(
    var name: String?,
    var pass: String?,
    var email: String?,
) : Serializable {
    companion object {
        fun empty() = RegisterInfo(null, null, null)
    }
}
