package com.sekusarisu.mdpings.vpings.presentation.app_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sekusarisu.mdpings.vpings.domain.AppSettings
import com.sekusarisu.mdpings.vpings.domain.AppSettingsDataSource
import com.sekusarisu.mdpings.vpings.domain.Instance
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.String

const val USER_API_BACKEND = "user_api_backend"
const val USER_API_TOKEN = "user_api_token"
const val SERVER_LIST_REFRESH_INTERVAL = "server_list_refresh_interval"

class AppSettingsViewModel(
    private val appSettingsDataSource: AppSettingsDataSource
): ViewModel() {

    private val _state = MutableStateFlow(AppSettingsState())
    val state = _state

    // 点击coinList coin操作
    fun onAction(action: AppSettingsAction) {
        when(action) {
            is AppSettingsAction.OnInitLoadAppSettings -> {
                viewModelScope.launch {
                    val instances = getInstances() ?: persistentListOf()
                    val activeInstance = getActiveInstanceIndex() ?: 0
                    val refreshInterval = getInterval() ?: 5000
                    _state.update {
                        it.copy(
                            appSettings = AppSettings(
                                activeInstance = activeInstance,
                                instances = instances,
                                refreshInterval = refreshInterval,
                            )
                        )
                    }
                }
            }
            is AppSettingsAction.OnSaveInstanceClicked -> {
                saveInstance(action.name, action.apiUrl, action.apiToken)
            }
            is AppSettingsAction.OnSaveIntervalClicked -> {
                saveInterval(action.interval)
            }
//            is AppSettingsAction.OnSaveClicked -> {
//                when (action.dialogTitle) {
//                    "API 地址" -> {
//                        saveApiURL(action.value)
//                    }
//                    "TOKEN" -> {
//                        saveApiTOKEN(action.value)
//                    }
//                    "更新间隔" -> {
//                        saveInterval(action.value.toInt())
//                    }
//                }
//            }
        }
    }

//    fun saveApiURL(value: String) {
//        viewModelScope.launch{
//            appSettingsDataSource.putString(USER_API_BACKEND, value)
//        }
//    }
//    fun getApiURL(): String? = runBlocking {
//        appSettingsDataSource.getString(USER_API_BACKEND)
//    }
//
//    fun saveApiTOKEN(value: String) {
//        viewModelScope.launch{
//            appSettingsDataSource.putString(USER_API_TOKEN, value)
//        }
//    }
//    fun getApiTOKEN(): String? = runBlocking {
//        appSettingsDataSource.getString(USER_API_TOKEN)
//    }
//
//    fun saveInterval(value: Int) {
//        viewModelScope.launch{
//            appSettingsDataSource.putInt(SERVER_LIST_REFRESH_INTERVAL, value)
//        }
//    }
//    fun getInterval(): Int? = runBlocking {
//        appSettingsDataSource.getInt(SERVER_LIST_REFRESH_INTERVAL)
//    }

//    fun saveApiURL(value: String) {
//        viewModelScope.launch{
//            appSettingsDataSource.putString(USER_API_BACKEND, value)
//        }
//    }
//    fun saveApiTOKEN(value: String) {
//        viewModelScope.launch{
//            appSettingsDataSource.putString(USER_API_TOKEN, value)
//        }
//    }

    fun getActiveInstanceIndex(): Int? = runBlocking {
        appSettingsDataSource.getActiveInstanceIndex()
    }

    fun getInstances(): PersistentList<Instance>? = runBlocking {
        appSettingsDataSource.getInstances()
    }

    fun saveInstance(name: String, apiUrl: String, apiToken: String) {
        viewModelScope.launch{
            appSettingsDataSource.putInstance(name, apiUrl, apiToken)
        }
    }

    fun getApiURL(): String? = runBlocking {
        appSettingsDataSource.getActiveInstance()?.apiUrl
    }

    fun getApiTOKEN(): String? = runBlocking {
        appSettingsDataSource.getActiveInstance()?.apiToken
    }

    fun saveInterval(interval: Int) {
        viewModelScope.launch{
            appSettingsDataSource.setInterval(interval)
        }
    }

    fun getInterval(): Int? = runBlocking {
        appSettingsDataSource.getInterval()
    }

}