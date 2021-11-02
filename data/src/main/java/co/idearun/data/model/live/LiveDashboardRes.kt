package co.idearun.data.model.live

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LiveDashboardRes(
    val data: Data? = null,
    val status: Int? = null
) : Serializable {
    companion object {
        fun empty() = LiveDashboardRes(null, 0)
    }

    fun toLiveDashboardRes() = LiveDashboardRes(data, status)
}

data class LiveDashboardForm(
    @SerializedName("live_dashboard_address")
    val liveDashboardAddress: String? = null,
    val address: String? = null,
    val title: String? = null,
    val slug: String? = null
)

data class LiveDashboardCode(
    val code: String? = null,
    val form: LiveDashboardForm? = null,
    @SerializedName("expiration_time")
    val expirationTime: Any? = null
)

data class Data(
    @SerializedName("live-dashboard-code")
    val liveDashboardCode: LiveDashboardCode? = null
)

