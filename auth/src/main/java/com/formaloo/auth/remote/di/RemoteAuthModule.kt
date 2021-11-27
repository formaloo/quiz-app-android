package com.formaloo.auth.remote.di

import com.formaloo.auth.remote.AuthDataSource
import com.formaloo.auth.remote.AuthService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import org.koin.dsl.module
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


fun createAuthRemoteModule(
    baseUrl: String,
    appToken: String
) = module {

    factory(named("authInterceptor")) {
        Interceptor { chain ->
            val original = chain.request()

            val request =
                original.newBuilder()
                    .header("x-api-key", appToken)
                    .method(original.method, original.body)
                    .build()

            chain.proceed(request)
        }
    }

    single(named("authClient")) {
      //  val tokenAuthenticator by inject<TokenAuthenticator>()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(get(named("authInterceptor")) as Interceptor)
            //.addNetworkInterceptor()
          //  .authenticator(tokenAuthenticator)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }


    factory(named("authRetrofit")) {
        Retrofit.Builder()
            .client(get(named("authClient")))
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named("authService")) {
        get<Retrofit>(named("authRetrofit")).create(AuthService::class.java)
    }

    factory(named("AuthDataSource")) {
        AuthDataSource(get(named("authService")))
    }

/*    factory (named("TokenAuthenticator")){
        TokenAuthenticator(TokenContainer.sessionToken!!,get(named("AuthDataSource")))
    }*/
}
