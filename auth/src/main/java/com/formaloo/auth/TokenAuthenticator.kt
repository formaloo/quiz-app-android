package com.formaloo.auth

import com.formaloo.auth.model.Token
import com.formaloo.auth.remote.AuthDataSource
import okhttp3.*
import retrofit2.Call
import timber.log.Timber
import retrofit2.Callback



class TokenAuthenticator(
    var appToken: String,
    var dataSource: AuthDataSource
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.i("TokenAuthenticator :D")
        getNewToken(appToken)
        return null
    }

    fun getNewToken(appToken: String){

        var call = dataSource.authorizeUserReq(appToken)

        call.enqueue(object: Callback<Token>{
            override fun onResponse(call: Call<Token>, response: retrofit2.Response<Token>) {
                Timber.i("TokenAuthenticator response ${response.body()?.token}")
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Timber.i("TokenAuthenticator error ${t.message} - ${t.cause}")
            }

        })
    }

}