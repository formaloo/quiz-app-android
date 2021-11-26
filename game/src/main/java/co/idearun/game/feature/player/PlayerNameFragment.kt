package co.idearun.game.feature.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import co.idearun.game.feature.PlayerInfo
import co.idearun.game.base.BaseFragment
import co.idearun.game.R
import kotlinx.android.synthetic.main.fragment_player_name.*

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

        /*** get player name from user and save it in [PlayerInfo] object
         * to send in hidden field when user submit form*/
        playBtn.setOnClickListener {
            val playerName = nameEdt.text.toString()
            if(!playerName.isBlank()) {
                PlayerInfo.updatePlayerName(playerName)
                findNavController().navigate(R.id.action_playerNameFragment_to_playerFormFragment)
            } else {
                openAlert(getString(R.string.empty_name_msg))
            }
        }
    }
}
