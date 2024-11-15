package com.sekusarisu.mdpings.vpings.presentation.user_login

import com.sekusarisu.mdpings.core.domain.util.NetworkError

sealed interface LoginEvent {
    data class Error(val error: NetworkError): LoginEvent
}