package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDataDto(
    val token: String,
    val expire: String
)