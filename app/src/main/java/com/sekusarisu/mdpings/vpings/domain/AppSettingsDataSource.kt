package com.sekusarisu.mdpings.vpings.domain

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.Flow

interface AppSettingsDataSource {

    val appSettingsFlow: Flow<AppSettings>

    suspend fun getInstances(): PersistentList<Instance>?
    suspend fun getActiveInstance(): Instance?
    suspend fun getActiveInstanceIndex(): Int?
    suspend fun setActiveInstanceIndex(index: Int)
    suspend fun putInstance(name: String, apiUrl: String, apiToken: String)
    suspend fun editInstance(index: Int, name: String, apiUrl: String, apiToken: String)
    suspend fun removeInstance(index: Int)
    suspend fun getInterval(): Int?
    suspend fun setInterval(interval: Int)
}
