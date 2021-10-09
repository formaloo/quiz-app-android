package co.idearun.auth.repository.di

import co.idearun.auth.repository.AuthRepository
import co.idearun.auth.repository.AuthRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authRepositoryModule = module {
    factory(named("AuthRepositoryImpl")) {
        AuthRepositoryImpl(get(named("AuthDataSource")))
    }
}