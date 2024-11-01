package com.example.mdpings.vpings.presentation.user_login

import com.example.mdpings.core.domain.util.NetworkError

sealed interface LoginEvent {
    data class Error(val error: NetworkError): LoginEvent
}