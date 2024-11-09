package com.example.mdpings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mdpings.core.data.networking.HttpClientFactory
import com.example.mdpings.vpings.data.StoreSettings
import com.example.mdpings.vpings.data.networking.RemoteServerDataSource
import com.example.mdpings.vpings.domain.ServerDataSource
import com.example.mdpings.vpings.presentation.server_detail.ServerDetailViewModel
import com.example.mdpings.vpings.presentation.server_list.ServerListViewModel
import com.example.mdpings.vpings.presentation.user_login.LoginViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

val appModule = module {
    single { StoreSettings(get()) }
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteServerDataSource).bind<ServerDataSource>()

    viewModelOf(::LoginViewModel)
    viewModelOf(::ServerListViewModel)
    viewModelOf(::ServerDetailViewModel)
}