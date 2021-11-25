package co.idearun.game.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.TokenContainer
import co.idearun.common.UserInfoManager
import co.idearun.game.R
import co.idearun.game.feature.viewmodel.FormViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userInfoManager = UserInfoManager(applicationContext)
        val authVm: AuthViewModel by viewModel()

        TokenContainer.authorizationToken = userInfoManager.authorizationToken()
        TokenContainer.sessionToken = userInfoManager.sessionToken()
        
        if (!TokenContainer.sessionToken.isNullOrBlank())
            authVm.authorizeUser(TokenContainer.sessionToken!!)

        authVm.authorizeData.observe(this,{
            userInfoManager.saveAuthorizationToken(it.token)
        })

        val vm: FormViewModel by viewModel()
        Log.i("TAG", "onCreate in quiz app")

        vm.form.observe(this, {
            Timber.i("$it")
        })


    }
}