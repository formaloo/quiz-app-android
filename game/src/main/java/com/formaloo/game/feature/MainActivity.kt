package com.formaloo.game.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.formaloo.auth.viewmodel.AuthViewModel
import com.formaloo.common.TokenContainer
import com.formaloo.common.UserInfoManager
import com.formaloo.game.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userInfoManager = UserInfoManager(applicationContext)
        val authVm: AuthViewModel by viewModel()

        // get data from shared preferences
        TokenContainer.authorizationToken = userInfoManager.authorizationToken()
        TokenContainer.sessionToken = userInfoManager.sessionToken()

        // get new authorization token
        if (!TokenContainer.sessionToken.isNullOrBlank())
            authVm.authorizeUser(TokenContainer.sessionToken!!)

        authVm.authorizeData.observe(this,{
            userInfoManager.saveAuthorizationToken(it.token)
        })
    }
}