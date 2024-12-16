package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerGroupDto(
    val group: GroupDto,
    val servers: List<Int>
)

@Serializable
data class GroupDto(
    val id: Int,
    @SerialName(value = "created_at") val createdAt: String,
    @SerialName(value = "updated_at") val updatedAt: String,
    val name: String
)