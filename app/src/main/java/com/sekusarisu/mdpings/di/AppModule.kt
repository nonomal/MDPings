package com.sekusarisu.mdpings.di

import com.sekusarisu.mdpings.core.data.networking.HttpClientFactory
import com.sekusarisu.mdpings.vpings.data.app_settings.LocalAppSettingsDataSource
import com.sekusarisu.mdpings.vpings.data.networking.RemoteRealtimeServerDataClient
import com.sekusarisu.mdpings.vpings.data.networking.RemoteServerDataSource
import com.sekusarisu.mdpings.vpings.domain.AppSettingsDataSource
import com.sekusarisu.mdpings.vpings.domain.RealtimeServerDataClient
import com.sekusarisu.mdpings.vpings.domain.ServerDataSource
import com.sekusarisu.mdpings.vpings.presentation.app_settings.AppSettingsViewModel
import com.sekusarisu.mdpings.vpings.presentation.server_detail.ServerDetailViewModel
import com.sekusarisu.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.sekusarisu.mdpings.vpings.presentation.user_login.LoginViewModel
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        HttpClientFactory.create(
            OkHttp.create {
                config {
                    preconfigured = OkHttpClient.Builder()
                        .pingInterval(20, TimeUnit.SECONDS)
                        .build()
                }
            }
        )
    }
    singleOf(::RemoteServerDataSource).bind<ServerDataSource>()
    singleOf(::LocalAppSettingsDataSource).bind<AppSettingsDataSource>()
    singleOf(::RemoteRealtimeServerDataClient).bind<RealtimeServerDataClient>()

    viewModelOf(::LoginViewModel)
    viewModelOf(::ServerListViewModel)
    viewModelOf(::ServerDetailViewModel)
    viewModelOf(::AppSettingsViewModel)
}