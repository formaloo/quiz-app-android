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
import co.idearun.game.base.BaseFragment
import co.idearun.game.R
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.imageView3
import kotlinx.android.synthetic.main.fragment_register.passEdt
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    AdapterView.OnItemClickListener {

    var gender = RegisterInfo.GENDER_MALE
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val userInfoManager = UserInfoManager(context!!)
        imageView3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))

        genderSpinner.onItemSelectedListener = this
        val authVm: AuthViewModel by viewModel()
        registerBtn.setOnClickListener {
            authVm.registerUser(
                RegisterInfo(
                    nameEdt.text.toString(),
                    passEdt.text.toString(),
                    emailEdt.text.toString(),
                    phoneEdt.text.toString(),
                    gender
                )
            )
        }

        authVm.registerData.observe(this, {
            val token = it?.data?.profile?.sessionToken
            userInfoManager.saveSessionToken(token)
            authVm.authorizeUser(token!!)

            Log.i("TAG", "onViewCreated: ${it.data?.profile}")
            Toast.makeText(context, "${it.data?.profile?.sessionToken}", Toast.LENGTH_LONG).show()
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2==0) gender = RegisterInfo.GENDER_MALE else gender = RegisterInfo.GENDER_FEMALE
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    fun checkEmail(email: String): Boolean{
        if(email.contains("@"))
            return true

        openAlert("your email is invalid")
        return false
    }


}