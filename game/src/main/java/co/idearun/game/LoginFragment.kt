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
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.zip.Inflater

class LoginFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container,false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: AuthViewModel by viewModel()
        loginBtn.setOnClickListener {
            if (userEdt.text.toString() == "test"){
                findNavController().navigate(R.id.action_authFragment_to_hostFragment)
            }

            vm.loginUser(userEdt.text.toString(), passEdt.text.toString())
        }
        //vm.loginUser("test@gmail.com", "ma%@#23ka")

        imageView3.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))



        vm.loginData.observe(this,{
            Log.i("TAG", "onViewCreated: ${it.token}")
            Toast.makeText(context,"خب لاگین کار میکنه، الان توکن رو نشون میدم",Toast.LENGTH_LONG).show()
            Toast.makeText(context,it.token,Toast.LENGTH_LONG).show()
          //  findNavController().navigate(R.id.action_authFragment_to_hostFragment)

            vm.authorizeUser(it.token!!)

        })

        val vm1: FormViewModel by viewModel()

        vm.authorizeData.observe(this,{
            Toast.makeText(context,"توکن دوم" + it.token,Toast.LENGTH_LONG).show()
            vm1.copyForm("bTOdLEfK", "JWT ${it.token}")

        })

        vm1.form.observe(this,{
            if (!it.title.isNullOrEmpty()){
                Toast.makeText(context,"فرم کپی شده $it",Toast.LENGTH_LONG).show()
            }
        })



    }

}