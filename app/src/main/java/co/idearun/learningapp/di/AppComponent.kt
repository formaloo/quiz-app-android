package co.idearun.learningapp.di

import co.idearun.data.local.di.formBuilderLocalModule
import co.idearun.data.remote.di.createRemoteFormModule
import co.idearun.data.repository.di.formRepositoryModule
import co.idearun.feature.di.featureModule

const val xapikey="717ed4b1fb84033653eaf6a69da4eb3be1011657"
const val baseurl="https://api.formaloo.net/"

val appComponent = listOf(
    createRemoteFormModule(baseurl,xapikey),
    formBuilderLocalModule,
    formRepositoryModule,
    featureModule
)