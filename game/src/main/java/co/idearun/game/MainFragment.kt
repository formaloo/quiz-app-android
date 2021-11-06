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
import co.idearun.common.TokenContainer
import co.idearun.common.UserInfoManager
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.zip.Inflater

class MainFragment : Fragment() {

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


        TokenContainer.authorizationToken = userInfoManager.authorizationToken()
        TokenContainer.authorizationToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Im15YXBwLTAwMSJ9.eyJ0b2tlbiI6ImF1dGgiLCJzaWQiOiJjOTcxZmNkZC1iMTgwLTQ2MWYtOWZjOS1iN2ExMzliNjExNDkiLCJ1aWQiOjEwMTAyLCJlbWFpbCI6InRlc3Q1QGdtYWlsLmNvbSIsImZpcnN0X25hbWUiOiJ0ZXN0NSIsImxhc3RfbmFtZSI6IiIsInBob25lX251bWJlciI6IjA5MTEzMjEyMjExIiwidXNlcm5hbWUiOiI1NTFiNzc4Y2MwMzk0OGFjOWIwYzgwNTBhNGU4MDIiLCJ2ZXJpZmllZF9lbWFpbCI6ZmFsc2UsInZlcmlmaWVkX3Bob25lIjpmYWxzZSwibGFzdF91cGRhdGUiOiIyMDIxLTEwLTMxVDA3OjA0OjIyLjA4NFoiLCJncm91cHMiOiIiLCJpc3MiOiJpY2FzIiwiYXVkIjpbImljYXMiLCJjcm0iLCJmb3JteiIsImludm9pY2UiLCJwcm9qZWN0YW50IiwiYWN0aW9ucyJdLCJleHAiOjE2MzYyNDQ3NTEsImlhdCI6MTYzNjIxNDc1MX0.vLy5mkyuKQ5NbYUoetsI9lmpG7BUfsUF11n4-QZ0rjWCDvVZaCmKMIgOdGw9WH2mW8-PRvU4s5Ag1ZGjT7cU-e62Gqd8-Bfn135eUZRPLWtqea98zNctvaNb_qLXvFNod_q3IyjjF1rJv_BBF25wSS-P1t1r3cCAKBzCZdwPEeJjSF_XlouUEFHkAZsyGypLRenLZXIinvR5wuLkgjJDf3IPTTlZC-Y3qhMb69uizosYiPG6QHYRF4gsOcemvdllBcJk_TP2sbvQVVvKqYpGeAfzYqpQRHRc4U6os_Un8aUUhyvL5IZt_rLAbjL4D8rtxwEu-phypUnauiWyYOLY9Q"
        TokenContainer.sessionToken = userInfoManager.sessionToken()


        Timber.i("Token %s", TokenContainer.authorizationToken)
        Timber.i("Token %s", TokenContainer.sessionToken)

        Timber.i("Token %s", userInfoManager.sessionToken())
        Timber.i("Token %s", userInfoManager.authorizationToken())

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