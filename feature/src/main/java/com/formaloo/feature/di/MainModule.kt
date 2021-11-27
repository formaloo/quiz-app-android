package com.formaloo.feature.di

import com.formaloo.feature.viewmodel.FormViewModel
import com.formaloo.feature.viewmodel.SharedViewModel
import com.formaloo.feature.viewmodel.UIViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureModule = module {
    viewModel { FormViewModel(get(named("FormzRepo"))) }
    viewModel { UIViewModel(get(named("FormzRepo"))) }
    viewModel { SharedViewModel(get(named("SharedRepositoryImpl"))) }

}
