import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""                Text\(
                    modifier = Modifier
                        \.align\(Alignment\.Center\)
                        \.padding\(horizontal = 48\.dp\),
                    text = formattedDisplayDate,
                    style = MaterialTheme\.typography\.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight\.Bold,
                    textAlign = TextAlign\.Center
                \)"""

replacement = """                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 48.dp),
                    text = formattedDisplayDate,
                    style = MaterialTheme.typography.displayLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )"""

content = re.sub(target, replacement, content, count=1)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
