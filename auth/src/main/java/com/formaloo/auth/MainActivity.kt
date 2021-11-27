package com.formaloo.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.formaloo.auth.viewmodel.AuthViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("onCreate from auth module")
        val vm: AuthViewModel by viewModel()



        /*

        //sample request to register user
        val user = RegisterInfo.empty()

        user.name = "test"
        user.pass = "ma%@#23ka"
        user.phone = "09117778855"
        user.email = "test2@gmail.com"
        user.gender = "male"

        vm.registerUser(user)
        vm.registerData.observe(this, {
            Timber.i(" login $it")
        })


        // sample request to login user
        vm.loginUser("test@gmail.com", "ma%@#23ka")
        vm.loginData.observe(this, {
            Timber.i(" login $it")
        })


        // sample request to authorize user
        vm.authorizeUser("$token")
        vm.authorizeData.observe(this, {
            Timber.i("${it.token}")
        })

*/

    }
}