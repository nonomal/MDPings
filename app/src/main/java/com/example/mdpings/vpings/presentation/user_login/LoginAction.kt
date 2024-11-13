package com.example.mdpings.vpings.presentation.user_login

sealed interface LoginAction {
    data class OnTestClick(val apiURL: String, val apiTOKEN: String): LoginAction
    object OnCredentialsChange: LoginAction
    data class OnSaveClicked(val apiURL: String, val apiTOKEN: String): LoginAction
}