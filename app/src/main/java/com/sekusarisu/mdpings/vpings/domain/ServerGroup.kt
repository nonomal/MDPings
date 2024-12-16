package com.sekusarisu.mdpings.vpings.domain

data class ServerGroup(
    val group: Group,
    val servers: List<Int>
)

data class Group(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val name: String
)