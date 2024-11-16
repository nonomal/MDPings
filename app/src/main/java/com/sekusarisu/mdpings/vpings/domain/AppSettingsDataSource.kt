package com.sekusarisu.mdpings.vpings.domain

import kotlinx.collections.immutable.PersistentList

interface AppSettingsDataSource {
//    suspend fun putString(key: String, value: String)
//    suspend fun putInt(key: String, value: Int)
//    suspend fun getString(key: String): String?
//    suspend fun getInt(key: String): Int?
    suspend fun getInstances(): PersistentList<Instance>?
    suspend fun getActiveInstance(): Instance?
    suspend fun getActiveInstanceIndex(): Int?
    suspend fun putInstance(name: String, apiUrl: String, apiToken: String)
    suspend fun removeInstance(index: Int)
    suspend fun getInterval(): Int?
    suspend fun setInterval(interval: Int)
}
