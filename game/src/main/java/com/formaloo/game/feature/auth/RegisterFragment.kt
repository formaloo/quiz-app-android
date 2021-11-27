package com.formaloo.game.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.formaloo.auth.model.register.RegisterInfo
import com.formaloo.auth.viewmodel.AuthViewModel
import com.formaloo.common.UserInfoManager
import com.formaloo.game.feature.Animation
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.R
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val userInfoManager = UserInfoManager(context!!)
        Animation.fadeInAnim(imageView,context)
        val authVm: AuthViewModel by viewModel()

        // register user
        registerBtn.setOnClickListener {
            authVm.registerUser(
                RegisterInfo(
                    nameEdt.text.toString(),
                    passEdt.text.toString(),
                    emailEdt.text.toString()
                )
            )
        }

        authVm.registerData.observe(this, {
            val token = it?.data?.profile?.sessionToken
            userInfoManager.saveSessionToken(token)
            authVm.authorizeUser(token!!)
        })

        // get authorization data with session token
        authVm.authorizeData.observe(this, {
            userInfoManager.saveAuthorizationToken(it.token)
            findNavController().navigate(R.id.action_authFragment_to_hostFragment)

        })

        // handle failure
        authVm.failure.observe(this, {
            authVm.hideLoading()
            checkFailureStatus(it)
        })

        // handle loading
        authVm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })
    }
}