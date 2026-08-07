import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""                // Center Title Column \(Title \+ Perfect Day Streak Badge under it for perfect symmetry\)
                Column\(
                    modifier = Modifier
                        \.align\(Alignment\.Center\)
                        \.padding\(horizontal = 48\.dp\),
                    horizontalAlignment = Alignment\.CenterHorizontally
                \) \{
                    Text\(
                        text = formattedDisplayDate,
                        style = MaterialTheme\.typography\.displayLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight\.Bold,
                        textAlign = TextAlign\.Center
                    \)
                \}"""

replacement = """                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 48.dp),
                    text = formattedDisplayDate,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )"""

new_content = re.sub(target, replacement, content, count=1, flags=re.DOTALL)
if content != new_content:
    with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
        f.write(new_content)
    print("Patched successfully")
else:
    print("Target not found using regex")
