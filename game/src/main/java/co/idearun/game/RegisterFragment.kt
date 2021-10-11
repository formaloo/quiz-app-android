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
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.imageView3
import kotlinx.android.synthetic.main.fragment_register.passEdt
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.zip.Inflater

class RegisterFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_register, container,false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView3.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))

        val vm: AuthViewModel by viewModel()
        registerBtn.setOnClickListener {
            if (nameEdt.text.toString() == "test"){
                findNavController().navigate(R.id.action_authFragment_to_hostFragment)
            }
            vm.registerUser(RegisterInfo(nameEdt.text.toString(),passEdt.text.toString(),emailEdt.text.toString(),phoneEdt.text.toString(),genderEdt.text.toString()))
        }

        vm.registerData.observe(this,{
            Log.i("TAG", "onViewCreated: ${it.data?.profile}")
            Toast.makeText(context,"خب یوزر ${it.data?.profile?.email} ثبت نام شده و الان توکن رو نشون میدم", Toast.LENGTH_LONG).show()
            Toast.makeText(context,"${it.data?.profile?.sessionToken}", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_authFragment_to_hostFragment)
        })
    }

}