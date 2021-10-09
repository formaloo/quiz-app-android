package co.idearun.learnandroid.di

import co.idearun.auth.remote.di.createAuthRemoteModule
import co.idearun.auth.repository.di.authRepositoryModule
import co.idearun.auth.viewmodel.di.authModule
import co.idearun.data.local.di.formBuilderLocalModule
import co.idearun.data.remote.di.createRemoteFormModule
import co.idearun.data.repository.di.formRepositoryModule
//import co.idearun.feature.di.featureModule
import co.idearun.game.di.gameAppModule
import co.idearun.learnandroid.BuildConfig.BASE_URL
import co.idearun.learnandroid.BuildConfig.X_API_KEY

val appComponent = listOf(
    createRemoteFormModule(BASE_URL, X_API_KEY),
    formBuilderLocalModule,
    formRepositoryModule,
   // featureModule,
    gameAppModule,
    createAuthRemoteModule("https://staging.icas.formaloo.com/","4638feb8378ac5799d6200889f97f2d3d88b9852"),
    authRepositoryModule,
    authModule
)