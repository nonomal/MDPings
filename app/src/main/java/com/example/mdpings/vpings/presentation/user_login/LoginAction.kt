package com.example.mdpings.vpings.presentation.user_login

import androidx.navigation.NavController

sealed interface LoginAction {
    data class OnTestClick(val apiUrl: String, val token: String): LoginAction
}