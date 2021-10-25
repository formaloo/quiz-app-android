package co.idearun.common

object TokenContainer {
    var sessionToken: String? = ""
    var authorizationToken: String? = ""


    fun updateTokens(sessionToken: String, authorizationToken: String) {
        TokenContainer.sessionToken = sessionToken
        TokenContainer.authorizationToken = authorizationToken
    }

    fun updateSessionToken(sessionToken: String?){
        TokenContainer.sessionToken = sessionToken
    }

    fun updateAuthorizationToken(authorizationToken: String?){
        TokenContainer.authorizationToken = authorizationToken
    }
}