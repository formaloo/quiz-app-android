package com.formaloo.auth.model.register

import java.io.Serializable

data class RegisterData(
    var profile: Profile? = null
) : Serializable
