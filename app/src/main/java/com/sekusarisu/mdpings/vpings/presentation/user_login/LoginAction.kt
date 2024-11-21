package com.sekusarisu.mdpings.vpings.presentation.user_login

sealed interface LoginAction {
    object OnInitLoadInstances: LoginAction
    object OnCredentialsChange: LoginAction
    data class OnTestClick(val apiURL: String, val apiTOKEN: String): LoginAction
    data class OnSaveClicked(val name: String, val apiURL: String, val apiTOKEN: String): LoginAction
    data class OnEditSaveClicked(val index: Int, val name: String, val apiURL: String, val apiTOKEN: String): LoginAction
    data class OnDeleteClick(val index: Int): LoginAction
}