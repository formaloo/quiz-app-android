package co.idearun.game

import co.idearun.data.model.live.LiveDashboardCode

object PlayerInfo {
    var playerFormInfo: LiveDashboardCode? = null
    var playerName: String? = ""


    fun updatePlayerFormInfo(playerFormInfo: LiveDashboardCode?) {
        PlayerInfo.playerFormInfo = playerFormInfo
    }

    fun updatePlayerName(playerName: String?){
        PlayerInfo.playerName = playerName
    }
}