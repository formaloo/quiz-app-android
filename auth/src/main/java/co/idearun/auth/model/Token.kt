package co.idearun.auth.model

import java.io.Serializable

data class Token(
    var token: String? = null
) : Serializable {
    companion object {
        fun empty() = Token(null)
    }

    fun toToken() = Token(token)
}
