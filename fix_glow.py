import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_func = """fun Modifier.animatedGlowingBorder(
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
}"""

new_func = """fun Modifier.animatedGlowingBorder(
    glowColor: Color,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 2.dp,
    baseColor: Color? = null
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    this.drawWithContent {
        drawContent()
        val radiusPx = cornerRadius.toPx()
        val widthPx = borderWidth.toPx()
        
        if (baseColor != null) {
            drawRoundRect(
                color = baseColor,
                size = size,
                cornerRadius = CornerRadius(radiusPx, radiusPx),
                style = Stroke(widthPx)
            )
        }
        
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        
        val shader = android.graphics.SweepGradient(
            centerX, 
            centerY, 
            intArrayOf(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
                glowColor.copy(alpha = 0.8f).toArgb(),
                Color.White.toArgb(),
                glowColor.copy(alpha = 0.8f).toArgb(),
                android.graphics.Color.TRANSPARENT
            ),
            floatArrayOf(0.0f, 0.7f, 0.9f, 0.95f, 0.98f, 1.0f)
        )
        val matrix = android.graphics.Matrix()
        matrix.postRotate(rotation, centerX, centerY)
        shader.setLocalMatrix(matrix)
        
        val brush = androidx.compose.ui.graphics.ShaderBrush(shader)
        
        drawRoundRect(
            brush = brush,
            size = size,
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style = Stroke(widthPx + 1.dp.toPx())
        )
    }
}"""

content = content.replace(old_func, new_func)

# Fix the Tagesfortschritt modifier call
content = content.replace(".animatedGlowingBorder(PrimaryViolet, 22.dp, 2.dp)", ".animatedGlowingBorder(PrimaryViolet, 22.dp, 2.dp, PrimaryViolet.copy(alpha=0.3f))")
# Fix the Support modifier call
content = content.replace(".animatedGlowingBorder(PrimaryViolet, 20.dp, 2.dp)", ".animatedGlowingBorder(PrimaryViolet, 20.dp, 2.dp, PrimaryViolet.copy(alpha=0.3f))")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
