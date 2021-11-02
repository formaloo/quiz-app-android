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
        TokenContainer.authorizationToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Im15YXBwLTAwMSJ9.eyJ0b2tlbiI6ImF1dGgiLCJzaWQiOiJjOTcxZmNkZC1iMTgwLTQ2MWYtOWZjOS1iN2ExMzliNjExNDkiLCJ1aWQiOjEwMTAyLCJlbWFpbCI6InRlc3Q1QGdtYWlsLmNvbSIsImZpcnN0X25hbWUiOiJ0ZXN0NSIsImxhc3RfbmFtZSI6IiIsInBob25lX251bWJlciI6IjA5MTEzMjEyMjExIiwidXNlcm5hbWUiOiI1NTFiNzc4Y2MwMzk0OGFjOWIwYzgwNTBhNGU4MDIiLCJ2ZXJpZmllZF9lbWFpbCI6ZmFsc2UsInZlcmlmaWVkX3Bob25lIjpmYWxzZSwibGFzdF91cGRhdGUiOiIyMDIxLTEwLTMxVDA3OjA0OjIyLjA4NFoiLCJncm91cHMiOiIiLCJpc3MiOiJpY2FzIiwiYXVkIjpbImljYXMiLCJjcm0iLCJmb3JteiIsImludm9pY2UiLCJwcm9qZWN0YW50IiwiYWN0aW9ucyJdLCJleHAiOjE2MzU4NDE1MzUsImlhdCI6MTYzNTgxMTUzNX0.NOWbWAz6dUz4k6TClYYHBUP2_FF7pOVNl_WVhz4dZ_2C0ukjfdLTeBiPUWTmAV2cmBEIKzBmBT6w2HJtaY1HC9pg1A4z9PK1Z2jPvjv4F91TKNR3GHCwzmSAWyxbxtUFgmb7564Vyo9Spjd7YJkf4dh-8kbyoBL-lSxWcBss1Yv9yLE0X_pI9UbM7-DumsyQwIrEGbr04nAe8siMV3nEA6NaMLZDNdtQpIkC2ML8xY4ALOkbkEZKGCsSJ19h5LacpJp4bwk9YotNcXk_km9xwf8hLd2LLChCq_oeVWMZppVCLZZbXPDlU5kD3AYejOIL23OQOHkjX9Q1ysHFTbqYsA"
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