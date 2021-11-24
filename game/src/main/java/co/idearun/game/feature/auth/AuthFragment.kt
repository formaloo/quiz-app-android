package co.idearun.game.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.idearun.common.CustomTabView
import co.idearun.game.R

class AuthFragment: Fragment() {
    lateinit var tabView: CustomTabView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabView = CustomTabView(
            requireActivity(),
            childFragmentManager,
            R.id.tabAuth,
            R.id.viewPagerAuth
        )
        tabView.add(RegisterFragment().javaClass, "Register")
        tabView.add(LoginFragment().javaClass, "Login")

    }

}