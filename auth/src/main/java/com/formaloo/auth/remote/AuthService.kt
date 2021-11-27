package com.formaloo.auth.remote

import com.formaloo.auth.model.LoginRes
import com.formaloo.auth.model.register.RegisterRes
import com.formaloo.auth.model.Token
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    companion object {
        private const val VERSION1 = "v1/"
        private const val VERSION2 = "v2/"
        private const val VERSION3 = "v3/"

        private const val REGISTER = "${VERSION1}profiles/profile/"
        private const val LOGIN = "${VERSION1}profiles/session/"
        private const val AUTHORIZE = "${VERSION1}profiles/authorize/"
    }


    @POST(REGISTER)
    fun registerUser(
        @Body body: RequestBody
    ): Call<RegisterRes>

    @POST(LOGIN)
    fun loginUser(
        @Body body: RequestBody
    ): Call<LoginRes>


    @POST(AUTHORIZE)
    fun authorizeUser(@Header("Authorization") token: String
    ): Call<Token>

    @POST(AUTHORIZE)
    fun authorizeUserReq(@Header("Authorization") token: String
    ): Call<Token>

}