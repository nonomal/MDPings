package com.sekusarisu.mdpings.vpings.domain

import com.sekusarisu.mdpings.vpings.data.app_settings.MyPersistentListSerializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val activeInstance: Int = 0,
    @Serializable(with = MyPersistentListSerializer::class)
    val instances: PersistentList<Instance> = persistentListOf(),
    val refreshInterval: Int = 5000,
)

@Serializable
data class Instance(
    val name: String,
    val apiUrl: String,
    val apiToken: String
)