import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace("""        item(key = "today_top_row") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {""", """        item(key = "today_top_row") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .defaultMinSize(minHeight = 44.dp)
            ) {""")

content = content.replace("""            item(key = "profile_top_row") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {""", """            item(key = "profile_top_row") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .defaultMinSize(minHeight = 44.dp)
                ) {""")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
