package com.formaloo.auth.viewmodel.di

import com.formaloo.auth.viewmodel.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    viewModel { AuthViewModel(get(named("AuthRepositoryImpl"))) }
}