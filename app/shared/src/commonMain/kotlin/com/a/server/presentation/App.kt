package com.a.server.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import com.a.server.navigation.ScreenRoot

@Composable
@Preview
fun App() {
    MaterialTheme {
        ScreenRoot()
    }
}