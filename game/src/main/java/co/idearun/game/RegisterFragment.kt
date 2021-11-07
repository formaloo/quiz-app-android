package co.idearun.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.idearun.auth.model.register.RegisterInfo
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.UserInfoManager
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.imageView3
import kotlinx.android.synthetic.main.fragment_register.passEdt
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.alertdialog.appcompat.*
import timber.log.Timber
import java.util.zip.Inflater

class RegisterFragment : Fragment() {

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

        val vm: AuthViewModel by viewModel()
        registerBtn.setOnClickListener {
            if (nameEdt.text.toString() == "test") {
                findNavController().navigate(R.id.action_authFragment_to_hostFragment)
            }
            vm.registerUser(
                RegisterInfo(
                    nameEdt.text.toString(),
                    passEdt.text.toString(),
                    emailEdt.text.toString(),
                    phoneEdt.text.toString(),
                    genderEdt.text.toString()
                )
            )
        }

        vm.registerData.observe(this, {
            val token = it?.data?.profile?.sessionToken
            userInfoManager.saveSessionToken(token)
            vm.authorizeUser(token!!)

            Log.i("TAG", "onViewCreated: ${it.data?.profile}")
            Toast.makeText(context, "${it.data?.profile?.sessionToken}", Toast.LENGTH_LONG).show()
        })

        vm.authorizeData.observe(this, {
            userInfoManager.saveAuthorizationToken(it.token)
            findNavController().navigate(R.id.action_authFragment_to_hostFragment)

        })

        vm.failure.observe(this, {
            renderFailure(it.msgRes)
        })


    }

    private fun renderFailure(message: String?) {
        Timber.e("renderFailure $message")
        message?.let {
            try {

                val jObjError = JSONObject(message)
                setErrorsToViews(jObjError)


            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    e.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

                Timber.e("${e.localizedMessage}")

            }
        }
    }

    private fun setErrorsToViews(jObjError: JSONObject) {
        if (jObjError.has("errors")) {
            val jsonArray = jObjError.getJSONArray("errors")
            jsonArray?.let {
                if (jsonArray.length() > 0 && jsonArray[0] is JSONObject) {
                    Toast.makeText(
                        context,
                        (jsonArray[0] as JSONObject)["message"].toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }
}