package com.example.mdpings.vpings.presentation.user_login

sealed interface LoginAction {
    data class OnTestClick(val apiUrl: String, val token: String): LoginAction
    data class OnCredentialsChange(val apiUrl: String, val token: String): LoginAction
}