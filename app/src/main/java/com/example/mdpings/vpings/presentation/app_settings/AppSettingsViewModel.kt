package com.example.mdpings.vpings.presentation.app_settings

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mdpings.vpings.domain.AppSettingsDataSource
import com.example.mdpings.vpings.presentation.user_login.LoginAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.String

private const val USER_API_BACKEND = "user_api_backend"
private const val USER_API_TOKEN = "user_api_token"
private const val SERVER_LIST_REFRESH_INTERVAL = "server_list_refresh_interval"

class AppSettingsViewModel(
    private val appSettingsDataSource: AppSettingsDataSource
): ViewModel() {

    private val _state = MutableStateFlow(AppSettingsState())
    val state = _state

    // 点击coinList coin操作
    fun onAction(action: AppSettingsAction) {
        when(action) {
            is AppSettingsAction.OnInitLoadAppSettings -> {
                _state.update {
                    it.copy(
                        apiURL = getApiURL() ?: "https://your-api.example.com/",
                        apiTOKEN = getApiTOKEN() ?: "YOUR_NEZHA_MONITOR_API_TOKEN",
                        refreshInterval = getInterval()?.toInt() ?: 5000
                    )
                }
            }
            is AppSettingsAction.OnSaveClicked -> {
                when (action.dialogTitle) {
                    "API 地址" -> {
                        saveApiURL(action.value)
                    }
                    "TOKEN" -> {
                        saveApiTOKEN(action.value)
                    }
                    "更新间隔" -> {
                        saveInterval(action.value.toInt())
                    }
                }
            }
        }
    }

    fun saveApiURL(value: String) {
        viewModelScope.launch{
            appSettingsDataSource.putString(USER_API_BACKEND, value)
        }
    }
    fun getApiURL(): String? = runBlocking {
        appSettingsDataSource.getString(USER_API_BACKEND)
    }

    fun saveApiTOKEN(value: String) {
        viewModelScope.launch{
            appSettingsDataSource.putString(USER_API_TOKEN, value)
        }
    }
    fun getApiTOKEN(): String? = runBlocking {
        appSettingsDataSource.getString(USER_API_TOKEN)
    }

    fun saveInterval(value: Int) {
        viewModelScope.launch{
            appSettingsDataSource.putInt(SERVER_LIST_REFRESH_INTERVAL, value)
        }
    }
    fun getInterval(): Int? = runBlocking {
        appSettingsDataSource.getInt(SERVER_LIST_REFRESH_INTERVAL)
    }

}