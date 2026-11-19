package com.a.server.presentation.authentication.detail

import com.a.server.presentation.model.authentication.AuthenticationUi

data class DetailState(
    val isAuthLoading: Boolean = true,
    val isAuthError: String? = null,
    val authentication: AuthenticationUi = AuthenticationUi(),

    val isDialogAlertDeleteAuthenticationVisible: Boolean = false
)
