package co.idearun.auth.viewmodel.di

import co.idearun.auth.viewmodel.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    viewModel { AuthViewModel(get(named("AuthRepositoryImpl"))) }
}