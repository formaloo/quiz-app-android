package com.formaloo.auth.repository.di

import com.formaloo.auth.repository.AuthRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authRepositoryModule = module {
    factory(named("AuthRepositoryImpl")) {
        AuthRepositoryImpl(get(named("AuthDataSource")))
    }
}