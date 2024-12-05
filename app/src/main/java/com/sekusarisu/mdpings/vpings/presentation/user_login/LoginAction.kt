package com.sekusarisu.mdpings.vpings.presentation.user_login

sealed interface LoginAction {

    object OnInitLoadInstances: LoginAction
    object OnCredentialsChange: LoginAction

    data class OnTestClick(
        val baseUrl: String,
        val username: String,
        val password: String
    ): LoginAction

    data class OnSaveClicked(
        val name: String,
        val baseUrl: String,
        val username: String,
        val password: String
    ): LoginAction

    data class OnEditSaveClicked(
        val index: Int,
        val name: String,
        val baseUrl: String,
        val username: String,
        val password: String
    ): LoginAction

    data class OnDeleteClick(val index: Int): LoginAction
}