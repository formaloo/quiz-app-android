package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.CustomTabView
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.zip.Inflater

class MainFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container,false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView2.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))

        btnHost.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_authFragment)
        }

        btnPlay.setOnClickListener {
            Toast.makeText(context,"فعلن رو بخش هاست فقط کار شده",Toast.LENGTH_LONG).show()
        }

    }

}