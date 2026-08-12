import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.ui.Modifier", "")
content = content.replace("import androidx.compose.ui.composed", "")
content = content.replace("import androidx.compose.animation.core.*", "")
content = content.replace("import androidx.compose.ui.draw.drawWithContent", "")
content = content.replace("import androidx.compose.ui.graphics.Color", "")
content = content.replace("import androidx.compose.ui.graphics.toArgb", "")
content = content.replace("import androidx.compose.ui.graphics.drawscope.Stroke", "")
content = content.replace("import androidx.compose.ui.geometry.CornerRadius", "")
content = content.replace("import androidx.compose.ui.unit.Dp", "")
content = content.replace("import androidx.compose.ui.unit.dp", "")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

