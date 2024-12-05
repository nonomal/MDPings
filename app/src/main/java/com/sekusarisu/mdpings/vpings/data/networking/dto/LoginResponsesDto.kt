package com.sekusarisu.mdpings.vpings.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponsesDto(
    val data: LoginDataDto? = LoginDataDto("", "")
)