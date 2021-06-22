package co.idearun.learningapp.data.repository.di

import co.idearun.learningapp.data.remote.FormDatasource
import co.idearun.learningapp.data.repository.FormzRepo
import co.idearun.learningapp.data.repository.sharedRepo.SharedRepository
import co.idearun.learningapp.data.repository.sharedRepo.SharedRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val formRepositoryModule = module {
    single<FormzRepo>(named("FormzRepo")) {
        FormzRepo(get<FormDatasource>(named("FormzDatasource")),get(),get(),get())
    }

    factory(named("SharedRepositoryImpl")) {
        SharedRepositoryImpl(get()) as SharedRepository
    }
}