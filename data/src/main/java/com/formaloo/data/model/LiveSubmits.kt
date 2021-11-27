package com.formaloo.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LiveSubmits(
    val data: LiveSubmitsData? = null,
    val status: Int? = null
) : Serializable {
    companion object {
        fun empty() = LiveSubmits(null, 0)
    }

    fun toLiveSubmits() = LiveSubmits(data, status)
}

data class LiveSubmitsData(
    @SerializedName("live_dashboard")
    val liveDashboard: LiveDashboard? = null
)

data class LiveDashboard(
    val address: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    val description: String? = null,
    @SerializedName("fields_list")
    val fieldsList: List<fieldList>? = null,
    @SerializedName("live_dashboard_address")
    val liveDashboardAddress: String? = null,
    val logo: String? = null,
    val title: String? = null,
    @SerializedName("total_submits_count")
    val totalSubmits: String? = null,
    val slug: String? = null
)

data class fieldList(
    val slug: String? = null,
    val type: String? = null,
    val title: String? = null,
    val value: String? = null,
    @SerializedName("admin_only")
    val adminOnly: Boolean? = null,
    val alias: String? = null
)
