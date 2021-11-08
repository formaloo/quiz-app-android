package co.idearun.game

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.UserInfoManager
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userInfoManager = UserInfoManager(context!!)
        imageView3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))

        val authVm: AuthViewModel by viewModel()

        loginBtn.setOnClickListener {
            authVm.loginUser(userEdt.text.toString(), passEdt.text.toString())
        }

        authVm.loginData.observe(this, {
            userInfoManager.saveSessionToken(it.token)
            authVm.authorizeUser(it.token!!)
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

}