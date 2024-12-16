package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServerGroupResponsesDto(
    val data: List<ServerGroupDto> = emptyList<ServerGroupDto>()
)