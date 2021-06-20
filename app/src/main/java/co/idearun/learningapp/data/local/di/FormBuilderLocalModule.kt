package co.idearun.learningapp.data.local.di

import co.idearun.formLocal.FormBuilderDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val formBuilderLocalModule = module {
    single() { FormBuilderDB.buildDatabase(androidContext()) }
    factory { (get() as FormBuilderDB).formDao() }
    factory { (get() as FormBuilderDB).formKeysDao() }
}