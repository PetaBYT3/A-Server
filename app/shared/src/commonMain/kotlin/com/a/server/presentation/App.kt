package com.a.server.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.a.server.navigation.ScreenTab
import com.a.server.presentation.ui.theme.AServerTheme

@Composable
@Preview
fun App() {
    AServerTheme {
        Surface {
            ScreenTab()
        }
    }
}