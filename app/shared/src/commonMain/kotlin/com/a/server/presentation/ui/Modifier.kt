package com.a.server.presentation.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.consumeStartSpace(amount: Dp): Modifier = this.layout { measurable, constraints ->
    val px = amount.roundToPx()
    // Ukur komponen dengan lebar maksimum ditambah agar konten di dalamnya tidak terpotong
    val looseConstraints = constraints.copy(
        maxWidth = constraints.maxWidth + px
    )
    val placeable = measurable.measure(looseConstraints)

    // Laporkan ukuran asli ke parent (Row) agar lebar Row tetap stabil sesuai weight (0.6f)
    layout(constraints.maxWidth, constraints.maxHeight) {
        // Geser penempatan layout ke kiri sejauh px (15.dp) untuk melakukan overlap
        placeable.place(x = -px, y = 0)
    }
}