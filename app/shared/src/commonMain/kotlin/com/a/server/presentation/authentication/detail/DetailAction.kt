package com.a.server.presentation.authentication.detail

sealed interface DetailAction {
    data object DialogAlertDeleteAuthentication: DetailAction
    data object DeleteAuthentication: DetailAction
}