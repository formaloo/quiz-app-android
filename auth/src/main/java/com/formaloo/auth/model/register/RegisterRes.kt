package com.formaloo.auth.model.register

import java.io.Serializable

data class RegisterRes(
    var data: RegisterData? = null,
    var status: Int? = null
): Serializable {
	companion object{
		fun empty() = RegisterRes(null,null)
	}
	fun toRegisterRes() = RegisterRes(data,status)
}

