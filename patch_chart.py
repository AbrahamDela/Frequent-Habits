import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace('val text = "${trendPoint.score.toInt()}%"', 'val text = "${trendPoint.score.toInt()}"')
content = content.replace('listOf("100%", "75%", "50%", "25%", "0%")', 'listOf("100", "75", "50", "25", "0")')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

