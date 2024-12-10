package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionResponsesDto(
    val data: SessionDataDto = SessionDataDto("", 0, "")
)