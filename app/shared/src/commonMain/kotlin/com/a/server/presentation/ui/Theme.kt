package com.a.server.presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.googlesans_bold
import aserver.app.shared.generated.resources.googlesans_bolditalic
import aserver.app.shared.generated.resources.googlesans_italic
import aserver.app.shared.generated.resources.googlesans_medium
import aserver.app.shared.generated.resources.googlesans_mediumitalic
import aserver.app.shared.generated.resources.googlesans_regular
import org.jetbrains.compose.resources.Font

@Composable
fun rememberGoogleSansTypography(): Typography {
    val googleSansFamily = FontFamily(
        Font(Res.font.googlesans_regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.googlesans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.googlesans_medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.googlesans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.googlesans_bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.googlesans_bolditalic, FontWeight.Bold, FontStyle.Italic)
    )

    return Typography(
        displayLarge = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        displayMedium = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        displaySmall = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        headlineLarge = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal),
        headlineMedium = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal),
        headlineSmall = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal),
        titleLarge = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal),
        titleMedium = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal),
        titleSmall = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal),
        bodyLarge = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        bodyMedium = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        bodySmall = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal),
        labelLarge = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal),
        labelMedium = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal),
        labelSmall = TextStyle(fontFamily = googleSansFamily, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Normal)
    )
}