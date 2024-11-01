package com.example.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServersResponsesDto(
    val result: List<ServerDto>
)