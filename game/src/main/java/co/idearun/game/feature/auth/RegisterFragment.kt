package co.idearun.game.feature.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.idearun.auth.model.register.RegisterInfo
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.UserInfoManager
import co.idearun.game.Animation
import co.idearun.game.base.BaseFragment
import co.idearun.game.R
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

        authVm.authorizeData.observe(this, {
            userInfoManager.saveAuthorizationToken(it.token)
            findNavController().navigate(R.id.action_authFragment_to_hostFragment)

        })

        authVm.failure.observe(this, {
            authVm.hideLoading()
            checkFailureStatus(it)
        })


        authVm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })


    }

    fun checkEmail(email: String): Boolean{
        if(email.contains("@"))
            return true

        openAlert("your email is invalid")
        return false
    }


}