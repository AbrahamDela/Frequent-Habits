with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.ui.graphics.Brush\nFilter\nMatrix", "import androidx.compose.ui.graphics.Brush\nimport androidx.compose.ui.graphics.ColorFilter\nimport androidx.compose.ui.graphics.ColorMatrix")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

