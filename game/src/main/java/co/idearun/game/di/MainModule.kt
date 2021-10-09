package co.idearun.game.di

import co.idearun.game.viewmodel.FormViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gameAppModule = module {
    viewModel { FormViewModel(get(named("FormzRepo"))) }
}