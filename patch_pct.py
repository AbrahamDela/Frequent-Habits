import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""                                    Text\(
                                        text = progressText,
                                        style = MaterialTheme\.typography\.labelLarge,
                                        fontWeight = FontWeight\.Bold,
                                        color = Color\.White
                                    \)"""

replacement = """                                    Text(
                                        text = progressText,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )"""
# wait, wait, the text was:
# text = "${(animatedFraction * 100).toInt()}% ($progressText)",
# Ah, wait! I already changed it to `text = progressText` in a previous patch!!
# Let's verify what it currently says.
