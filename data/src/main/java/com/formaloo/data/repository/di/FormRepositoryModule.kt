package com.formaloo.data.repository.di

import com.formaloo.data.remote.FormDatasource
import com.formaloo.data.repository.FormzRepo
import com.formaloo.data.repository.sharedRepo.SharedRepository
import com.formaloo.data.repository.sharedRepo.SharedRepositoryImpl
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