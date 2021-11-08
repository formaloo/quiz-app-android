package co.idearun.game.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.idearun.auth.viewmodel.AuthViewModel
import co.idearun.common.TokenContainer
import co.idearun.data.model.live.LiveDashboardRes
import co.idearun.game.BaseFragment
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_formeditor.*
import kotlinx.android.synthetic.main.fragment_games.*
import kotlinx.android.synthetic.main.fragment_player_code.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PlayerCodeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player_code, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: FormViewModel by activityViewModels()


        nextBtn.setOnClickListener {
            val liveCode = codeEdt.text.toString()
            if (checkCodeLength(liveCode)) {
                vm.getFormDataWithLiveCode(
                    "JWT ${TokenContainer.authorizationToken}",
                    liveCode
                )
            }

        }

        vm.liveForm.observe(this, {
            vm.userForm.value = it
            findNavController().navigate(R.id.action_playerCodeFragment_to_playerNameFragment)
        })

        vm.failure.observe(this, {
            if (it?.msgRes?.contains("404")!!){
                openAlert("invalid live code")
            }
            checkFailureStatus(it)
        })
    }

    fun checkCodeLength(code: String): Boolean {
        if (code.length == 6)
            return true
        openAlert("you code length must be 6 digit number")
        return false
    }
}
