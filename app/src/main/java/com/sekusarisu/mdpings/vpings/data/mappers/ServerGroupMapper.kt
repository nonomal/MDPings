package com.sekusarisu.mdpings.vpings.data.mappers

import com.sekusarisu.mdpings.vpings.data.networking.dto.GroupDto
import com.sekusarisu.mdpings.vpings.data.networking.dto.ServerGroupDto
import com.sekusarisu.mdpings.vpings.domain.Group
import com.sekusarisu.mdpings.vpings.domain.ServerGroup

fun ServerGroupDto.toServerGroup(): ServerGroup {
    return ServerGroup(
        group = group.toGroup(),
        servers = servers
    )
}

fun GroupDto.toGroup(): Group {
    return Group(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = name
    )
}