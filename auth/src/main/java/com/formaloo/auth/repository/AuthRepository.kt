package com.formaloo.auth.repository

import android.util.Log
import com.formaloo.auth.model.LoginRes
import com.formaloo.auth.model.Token
import com.formaloo.auth.model.register.RegisterRes
import com.formaloo.auth.remote.AuthDataSource
import com.formaloo.common.exception.Failure
import com.formaloo.common.exception.ViewFailure
import com.formaloo.common.functional.Either
import com.formaloo.data.repository.TAG
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface AuthRepository {
    suspend fun registerUser(body: RequestBody): Either<Failure, RegisterRes>
    suspend fun loginUser(body: RequestBody): Either<Failure, LoginRes>
    suspend fun authorizeUser(token: String): Either<Failure, Token>
}

class AuthRepositoryImpl(private val service: AuthDataSource) : AuthRepository {

    override suspend fun registerUser(body: RequestBody): Either<Failure, RegisterRes> {
        val call = service.registerUser(body)
        return try {
            request(call, { it }, RegisterRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun loginUser(body: RequestBody): Either<Failure, LoginRes> {
        val call = service.loginUser(body)
        return try {
            request(call, { it }, LoginRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }

    override suspend fun authorizeUser(token: String): Either<Failure, Token> {
        val call = service.authorizeUser(token)
        return try {
            request(call, { it.toToken() }, Token.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }


}

private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
    return try {
        val response = call.execute()
        var jObjError: JSONObject? = null
        Log.e(TAG, "request: " + response.raw())
        Log.e(TAG, "request: " + response.body())
        try {
            jObjError = JSONObject(response.errorBody()?.string())
            Log.e("request", "Repo responseErrorBody jObjError-> $jObjError")

        } catch (e: Exception) {

        }

        when (response.code()) {
            200 -> Either.Right(transform((response.body() ?: default)))
            201 -> Either.Right(transform((response.body() ?: default)))
            400 -> Either.Left(ViewFailure.responseError("$jObjError"))
            401 -> Either.Left(Failure.UNAUTHORIZED_Error)
            500 -> Either.Left(Failure.ServerError)
            else -> Either.Left(ViewFailure.responseError("$jObjError"))
        }

    } catch (exception: Throwable) {
        if (exception is UnknownHostException || exception is SocketTimeoutException) {
            Either.Left(Failure.NetworkConnection)

        } else {
            Either.Left(ViewFailure.responseError("exception++>  $exception"))

        }
    }

}