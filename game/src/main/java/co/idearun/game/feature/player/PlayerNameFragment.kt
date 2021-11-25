package co.idearun.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.idearun.game.feature.PlayerInfo
import co.idearun.game.base.BaseFragment
import co.idearun.game.R
import co.idearun.game.feature.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_player_name.*
import org.koin.android.viewmodel.ext.android.viewModel

class PlayerNameFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_player_name, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vm: FormViewModel by viewModel()

        playBtn.setOnClickListener {
            val playerName = nameEdt.text.toString()
            if(!playerName.isBlank()) {
                PlayerInfo.updatePlayerName(playerName)
                findNavController().navigate(R.id.action_playerNameFragment_to_playerFormFragment)
            } else {
                openAlert("we need to know your name, please enter your name :)")
            }
        }

        vm.failure.observe(this, {
            checkFailureStatus(it)
        })
    }
}
