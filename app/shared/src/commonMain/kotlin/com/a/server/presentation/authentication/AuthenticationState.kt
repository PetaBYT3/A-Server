package com.a.server.presentation.authentication

import com.a.server.presentation.model.authentication.AuthenticationUi

data class AuthenticationState(
    val search: String = "",

    val isAuthenticationListLoading: Boolean = true,
    val isAuthenticationListError: String? = null,
    val authenticationList: List<AuthenticationUi> = emptyList(),
    val filteredAuthenticationList: List<AuthenticationUi> = emptyList(),
    val selectedAuthenticationId: Set<String> = emptySet(),

    val isDeleteDialogVisible: Boolean = false
)
