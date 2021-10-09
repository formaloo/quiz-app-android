package co.idearun.auth.model

import java.io.Serializable

data class LoginRes(
	var token: String? = null,
	var signUp: Boolean? = false
): Serializable {
	companion object{
		fun empty() = LoginRes(null, false)
	}
	fun toLoginRes() = LoginRes(token, signUp)
}

