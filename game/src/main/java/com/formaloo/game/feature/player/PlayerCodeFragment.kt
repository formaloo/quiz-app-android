package com.formaloo.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.formaloo.game.feature.PlayerInfo
import com.formaloo.game.base.BaseFragment
import com.formaloo.game.R
import com.formaloo.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_player_code.*
import org.koin.android.viewmodel.ext.android.viewModel

class PlayerCodeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_code, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formVm: FormViewModel by viewModel()


        /* get form information with live dashboard code
        * and update PlayerInfo to use form address, slug in next fragments
        * then navigate to next fragment with form data */
        nextBtn.setOnClickListener {
            val liveCode = codeEdt.text.toString()
            if (checkCodeLength(liveCode)) {
                formVm.getFormDataWithLiveCode(liveCode)
            }

        }
        formVm.liveForm.observe(this, {
            PlayerInfo.updatePlayerFormInfo(it)
            findNavController().navigate(R.id.action_playerCodeFragment_to_playerNameFragment)
        })

        // handle failure
        formVm.failure.observe(this, {
            formVm.hideLoading()
            if (it?.msgRes?.contains("404")!!) {
                openAlert(getString(R.string.invalid_live_code))
            } else {
                checkFailureStatus(it)
            }
        })

        // handle loading
        formVm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })
    }

    fun checkCodeLength(code: String): Boolean {
        if (code.length == 6)
            return true
        openAlert(getString(R.string.code_length_error))
        return false
    }
}
