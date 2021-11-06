package co.idearun.auth.remote

import okhttp3.RequestBody

class AuthDataSource(private val service: AuthService) {
    fun registerUser(body: RequestBody) = service.registerUser(body)
    fun loginUser(body: RequestBody) = service.loginUser(body)
    fun authorizeUser(token: String) = service.authorizeUser("JWT $token")
    fun authorizeUserReq(token: String) = service.authorizeUserReq("JWT $token")
}