package co.idearun.common

import okhttp3.*
import timber.log.Timber

class TokenAuthenticator(
    var appToken: String
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.i("TokenAuthenticator :D")
        return null
    }

    fun getNewToken(){

    }

}