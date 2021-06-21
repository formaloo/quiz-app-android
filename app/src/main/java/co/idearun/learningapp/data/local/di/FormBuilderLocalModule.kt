package co.idearun.learningapp.data.local.di

import co.idearun.learningapp.common.BaseMethod
import co.idearun.learningapp.data.local.FormBuilderDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val formBuilderLocalModule = module {
    single() { FormBuilderDB.buildDatabase(androidContext()) }
    factory { (get() as FormBuilderDB).formDao() }
    factory { (get() as FormBuilderDB).formKeysDao() }
    factory { (get() as FormBuilderDB).submitDao() }
    single { BaseMethod() }

}