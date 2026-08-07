import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""            item\(key = "stats_top_row"\) \{
                Box\(
                    modifier = Modifier
                        \.fillMaxWidth\(\)
                        \.padding\(vertical = 12\.dp\),
                    contentAlignment = Alignment\.Center
                \) \{"""

replacement = """            item(key = "stats_top_row") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .defaultMinSize(minHeight = 44.dp),
                    contentAlignment = Alignment.Center
                ) {"""

content = re.sub(target, replacement, content, count=1)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
