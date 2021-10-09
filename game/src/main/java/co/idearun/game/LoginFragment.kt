package co.idearun.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.idearun.auth.viewmodel.AuthViewModel
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
            vm.loginUser(userEdt.text.toString(), passEdt.text.toString())
        }
        //vm.loginUser("test@gmail.com", "ma%@#23ka")

        vm.loginData.observe(this,{
            Log.i("TAG", "onViewCreated: ${it.token}")
            Toast.makeText(context,"خب لاگین کار میکنه، الان توکن رو نشون میدم",Toast.LENGTH_LONG).show()
            Toast.makeText(context,it.token,Toast.LENGTH_LONG).show()
        })
    }

}