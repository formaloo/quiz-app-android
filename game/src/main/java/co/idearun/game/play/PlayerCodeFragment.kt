package co.idearun.game.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import co.idearun.game.BaseFragment
import co.idearun.game.R
import co.idearun.game.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_player_code.*

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
                vm.getFormDataWithLiveCode(liveCode)
            }

        }

        vm.liveForm.observe(this, {
            vm.userForm.value = it
            findNavController().navigate(R.id.action_playerCodeFragment_to_playerNameFragment)
        })

        vm.failure.observe(this, {
            vm.hideLoading()
            if (it?.msgRes?.contains("404")!!) {
                openAlert("invalid live code")
            } else {
                checkFailureStatus(it)
            }
        })

        vm.isLoading.observe(this,{
            if (it) loading.visibility = View.VISIBLE else loading.visibility = View.GONE
        })
    }

    fun checkCodeLength(code: String): Boolean {
        if (code.length == 6)
            return true
        openAlert("you code length must be 6 digit number")
        return false
    }
}
