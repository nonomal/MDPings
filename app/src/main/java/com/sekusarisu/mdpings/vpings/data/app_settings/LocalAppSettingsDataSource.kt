package com.sekusarisu.mdpings.vpings.data.app_settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sekusarisu.mdpings.vpings.domain.AppSettingsDataSource
import com.sekusarisu.mdpings.vpings.domain.Instance
import io.ktor.util.reflect.instanceOf
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable

private val Context.dataStore by dataStore("AppSettings.json", AppSettingsSerializer)

class LocalAppSettingsDataSource(
    private val context: Context
): AppSettingsDataSource {

    override suspend fun getInstances(): PersistentList<Instance> {
        return try {
            Log.d("InstanceManager", "Starting to fetch instances")
            val result = context.dataStore.data.first().instances
            Log.d("InstanceManager", "Fetched instances: $result")
            result
        } catch (e: Exception) {
            Log.e("InstanceManager", "Error getting instances", e)
            persistentListOf<Instance>()
        }
    }

    override suspend fun getActiveInstance(): Instance? {
        return try {
            context.dataStore.data.first().instances[context.dataStore.data.first().activeInstance]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getActiveInstanceIndex(): Int? {
        return try {
            context.dataStore.data.first().activeInstance
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun setActiveInstanceIndex(index: Int) {
        context.dataStore.updateData {
            it.copy(
                activeInstance = index
            )
        }
    }

    override suspend fun getInterval(): Int {
        return try {
            context.dataStore.data.first().refreshInterval
        } catch (e: Exception) {
            e.printStackTrace()
            5000
        }
    }

    override suspend fun setInterval(interval: Int) {
        context.dataStore.updateData {
            it.copy(
                refreshInterval = 5000
            )
        }
    }

    override suspend fun putInstance(name: String, apiUrl: String, apiToken: String) {
        try {
            Log.d("InstanceManager", "Starting to put instance: $name")
            context.dataStore.updateData { currentData ->
                Log.d("InstanceManager", "Current data before update: $currentData")
                val updatedData = currentData.copy(
                    instances = currentData.instances.mutate { mutableList ->
                        mutableList.add(
                            Instance(
                                name = name,
                                apiUrl = apiUrl,
                                apiToken = apiToken
                            )
                        )
                    }
                )
                Log.d("InstanceManager", "Updated data: $updatedData")
                updatedData
            }
        } catch (e: Exception) {
            Log.e("InstanceManager", "Error putting instance", e)
        }

    }

    override suspend fun removeInstance(index: Int) {
        context.dataStore.updateData {
            it.copy(
                instances = it.instances.removeAt(index)
            )
        }
    }
}