package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class MonitorsResponsesDto(
    val data: List<MonitorDto>? = emptyList<MonitorDto>()
)
