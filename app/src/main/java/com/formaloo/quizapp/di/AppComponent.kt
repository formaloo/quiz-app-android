package com.formaloo.quizapp.di

import com.formaloo.auth.remote.di.createAuthRemoteModule
import com.formaloo.auth.repository.di.authRepositoryModule
import com.formaloo.auth.viewmodel.di.authModule
import com.formaloo.data.local.di.formBuilderLocalModule
import com.formaloo.data.remote.di.createRemoteFormModule
import com.formaloo.data.repository.di.formRepositoryModule
//import co.idearun.feature.di.featureModule
import com.formaloo.game.di.gameAppModule

val appComponent = listOf(
    createRemoteFormModule("https://api.staging.formaloo.com", "4638feb8378ac5799d6200889f97f2d3d88b9852"),
    formBuilderLocalModule,
    formRepositoryModule,
   // featureModule,
    gameAppModule,
    createAuthRemoteModule("https://staging.icas.formaloo.com/","4638feb8378ac5799d6200889f97f2d3d88b9852"),
    authRepositoryModule,
    authModule
)