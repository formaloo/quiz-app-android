package co.idearun.game.feature

import co.idearun.data.model.live.LiveDashboardCode

object PlayerInfo {
    var playerFormInfo: LiveDashboardCode? = null
    var playerName: String? = "player"
    var playerNameSlug: String? = ""


    fun updatePlayerFormInfo(playerFormInfo: LiveDashboardCode?) {
        PlayerInfo.playerFormInfo = playerFormInfo
    }

    fun updatePlayerName(playerName: String?) {
        PlayerInfo.playerName = playerName
    }

    fun updatePlayerNameSlug(playerNameSlug: String?) {
        PlayerInfo.playerNameSlug = playerNameSlug
    }
}