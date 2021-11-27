package com.formaloo.quizapp.di

import com.formaloo.auth.remote.di.createAuthRemoteModule
import com.formaloo.auth.repository.di.authRepositoryModule
import com.formaloo.auth.viewmodel.di.authModule
import com.formaloo.data.local.di.formBuilderLocalModule
import com.formaloo.data.remote.di.createRemoteFormModule
import com.formaloo.data.repository.di.formRepositoryModule
//import co.idearun.feature.di.featureModule
import com.formaloo.game.di.gameAppModule
import com.formaloo.quizapp.BuildConfig

val appComponent = listOf(
    createRemoteFormModule(BuildConfig.BASE_URL, BuildConfig.X_API_KEY),
    formBuilderLocalModule,
    formRepositoryModule,
   // featureModule,
    gameAppModule,
    createAuthRemoteModule(BuildConfig.ICAS_BASE_URL, BuildConfig.X_API_KEY),
    authRepositoryModule,
    authModule
)