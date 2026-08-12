with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

bad_str = "import androidx.compose.ui.graphics.Brush\n\n\nFilter\nMatrix"
good_str = "import androidx.compose.ui.graphics.Brush\nimport androidx.compose.ui.graphics.ColorFilter\nimport androidx.compose.ui.graphics.ColorMatrix"

content = content.replace(bad_str, good_str)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

