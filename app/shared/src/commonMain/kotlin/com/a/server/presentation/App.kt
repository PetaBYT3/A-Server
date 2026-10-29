package com.a.server.presentation

import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import com.a.server.navigation.ScreenRoot
import com.a.server.presentation.ui.rememberGoogleSansTypography

@Composable
@Preview
fun App() {
    MaterialExpressiveTheme(
        motionScheme = MotionScheme.expressive()
    ) {
        Surface {
            ScreenRoot()
        }
    }
}