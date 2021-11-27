package com.formaloo.game.di

import com.formaloo.game.feature.viewmodel.AuthViewModel
import com.formaloo.game.feature.viewmodel.FormViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gameAppModule = module {
    viewModel { FormViewModel(get(named("FormzRepo"))) }
    viewModel { AuthViewModel(get(named("AuthRepositoryImpl"))) }
}