package co.idearun.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import co.idearun.common.UserInfoManager
import co.idearun.game.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userInfoManager = UserInfoManager(requireContext())

        imageView2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))


        btnHost.setOnClickListener {
            if (userInfoManager.sessionToken().isNullOrBlank()) {
                findNavController().navigate(R.id.action_mainFragment_to_authFragment)
            } else {
                findNavController().navigate(R.id.action_mainFragment_to_hostFragment)
            }

        }

        btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_playerCodeFragment)
        }

    }

}