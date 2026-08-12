import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.animatedGlowingBorder(
    glowColor: Color,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 2.dp
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    this.drawWithContent {
        drawContent()
        val radiusPx = cornerRadius.toPx()
        val widthPx = borderWidth.toPx()
        
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        
        val shader = android.graphics.SweepGradient(
            centerX, 
            centerY, 
            intArrayOf(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
                glowColor.toArgb(),
                android.graphics.Color.TRANSPARENT
            ),
            floatArrayOf(0.0f, 0.5f, 0.95f, 1.0f)
        )
        val matrix = android.graphics.Matrix()
        matrix.postRotate(rotation, centerX, centerY)
        shader.setLocalMatrix(matrix)
        
        val brush = androidx.compose.ui.graphics.ShaderBrush(shader)
        
        drawRoundRect(
            brush = brush,
            size = size,
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style = Stroke(widthPx)
        )
    }
}
