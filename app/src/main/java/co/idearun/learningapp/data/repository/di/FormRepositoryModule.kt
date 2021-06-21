package co.idearun.learningapp.data.repository.di

import co.idearun.learningapp.data.remote.FormDatasource
import co.idearun.learningapp.data.repository.FormzRepo
import org.koin.core.qualifier.named
import org.koin.dsl.module

val formRepositoryModule = module {
    single<FormzRepo>(named("FormzRepo")) {
        FormzRepo(get<FormDatasource>(named("FormzDatasource")),get(),get(),get())
    }
}