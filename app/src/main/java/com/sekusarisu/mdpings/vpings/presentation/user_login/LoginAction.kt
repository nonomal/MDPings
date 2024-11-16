package com.sekusarisu.mdpings.vpings.presentation.user_login

sealed interface LoginAction {
    data class OnTestClick(val apiURL: String, val apiTOKEN: String): LoginAction
    object OnCredentialsChange: LoginAction
    data class OnSaveClicked(val name: String, val apiURL: String, val apiTOKEN: String): LoginAction
}