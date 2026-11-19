package com.a.server.presentation.database.scheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.NavigationState
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DatabaseSchemeScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: SchemeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: SchemeState,
    onAction: (SchemeAction) -> Unit
) {
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    // 2. State posisi & ukuran masing-masing kartu
    var usersPos by remember { mutableStateOf(Offset(50f, 100f)) }
    var postsPos by remember { mutableStateOf(Offset(500f, 300f)) }

    var usersSize by remember { mutableStateOf(IntSize.Zero) }
    var postsSize by remember { mutableStateOf(IntSize.Zero) }

    // Fungsi helper untuk menahan posisi agar tidak keluar batas parent
    fun clampPosition(pos: Offset, cardSize: IntSize): Offset {
        val maxX = (canvasSize.width - cardSize.width).coerceAtLeast(0).toFloat()
        val maxY = (canvasSize.height - cardSize.height).coerceAtLeast(0).toFloat()
        return Offset(
            x = pos.x.coerceIn(0f, maxX),
            y = pos.y.coerceIn(0f, maxY)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .clipToBounds() // Memastikan tidak ada elemen yang bocor keluar layar
            .onSizeChanged { canvasSize = it } // Ukur luas area parent
    ) {
        // LAYER 1: Menggambar Garis Lurus dengan Sudut Rounded 16.dp
        Canvas(modifier = Modifier.fillMaxSize()) {
            val start = Offset(
                x = usersPos.x + usersSize.width,
                y = usersPos.y + (usersSize.height / 2f)
            )
            val end = Offset(
                x = postsPos.x,
                y = postsPos.y + (postsSize.height / 2f)
            )

            val cornerRadius = 16.dp.toPx()
            val path = Path().apply {
                moveTo(start.x, start.y)

                if (end.x >= start.x + 40f) {
                    // Kondisi normal: kartu target di sebelah kanan kartu asal
                    val midX = (start.x + end.x) / 2f
                    lineTo(midX, start.y) // Lurus ke kanan
                    lineTo(midX, end.y)   // Lurus vertikal (naik/turun)
                    lineTo(end.x, end.y)  // Lurus ke target
                } else {
                    // Kondisi saat kartu target digeser melewati sisi kiri kartu asal
                    val stub = 24.dp.toPx()
                    val midY = (start.y + end.y) / 2f
                    lineTo(start.x + stub, start.y)
                    lineTo(start.x + stub, midY)
                    lineTo(end.x - stub, midY)
                    lineTo(end.x - stub, end.y)
                    lineTo(end.x, end.y)
                }
            }

            drawPath(
                path = path,
                color = Color(0xFF6200EE),
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    // Membuat setiap belokan siku 90 derajat otomatis rounded 16.dp
                    pathEffect = PathEffect.cornerPathEffect(cornerRadius)
                )
            )
        }

        // LAYER 2: Kartu Tabel Pertama ("users")
        SimpleTableCard(
            title = "users",
            columns = listOf("id (PK)", "email", "password"),
            position = usersPos,
            onDrag = { delta ->
                usersPos = clampPosition(usersPos + delta, usersSize)
            },
            onSizeMeasured = { size -> usersSize = size }
        )

        // LAYER 3: Kartu Tabel Kedua ("posts")
        SimpleTableCard(
            title = "posts",
            columns = listOf("id (PK)", "user_id (FK)", "title"),
            position = postsPos,
            onDrag = { delta ->
                postsPos = clampPosition(postsPos + delta, postsSize)
            },
            onSizeMeasured = { size -> postsSize = size }
        )
    }
}

@Composable
private fun SimpleTableCard(
    title: String,
    columns: List<String>,
    position: Offset,
    onDrag: (Offset) -> Unit,
    onSizeMeasured: (IntSize) -> Unit
) {
    Surface(
        modifier = Modifier
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .width(180.dp)
            .onSizeChanged { size -> onSizeMeasured(size) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount)
                }
            },
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6200EE))
                    .padding(10.dp)
            )

            columns.forEach { column ->
                Text(
                    text = column,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Content(
        navigationState = NavigationState(),
        state = SchemeState(),
        onAction = {}
    )
}