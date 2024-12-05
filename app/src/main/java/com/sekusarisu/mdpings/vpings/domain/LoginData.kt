package com.sekusarisu.mdpings.vpings.domain

data class LoginRequestData(
    val username: String,
    val password: String
)

data class LoginData(
    val token: String,
    val expire: String
)