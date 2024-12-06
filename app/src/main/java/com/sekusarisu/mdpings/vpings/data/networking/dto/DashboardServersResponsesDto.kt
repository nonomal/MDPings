package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardServersResponsesDto(
    val data: List<DashboardServerDto> = emptyList<DashboardServerDto>()
)

