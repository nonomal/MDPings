package com.example.mdpings.vpings.presentation.server_list


interface ServerListAction {
    data class OnExpandClick(val id: Int): ServerListAction
    data class OnShrinkClick(val id: Int): ServerListAction
}