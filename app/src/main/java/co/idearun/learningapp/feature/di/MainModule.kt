package co.idearun.learningapp.feature.di

import co.idearun.learningapp.feature.viewmodel.FormViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val featureModule = module {
    viewModel(){ FormViewModel(get(named("FormzRepo"))) }
}
