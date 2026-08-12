import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Tagesfortschritt replacement
old_tagesfortschritt = """                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = AppCard),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(2.dp, PrimaryViolet)
                    ) {"""

new_tagesfortschritt = """                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .animatedGlowingBorder(PrimaryViolet, 22.dp, 2.dp),
                        colors = CardDefaults.cardColors(containerColor = AppCard),
                        shape = RoundedCornerShape(22.dp)
                    ) {"""
content = content.replace(old_tagesfortschritt, new_tagesfortschritt)

# Support & Feedback replacement
old_support = """                // Support, Feedback & Community (directly on main settings page)
                item {
                    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AppCard),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AppBorder, RoundedCornerShape(20.dp))
                    ) {"""

new_support = """                // Support, Feedback & Community (directly on main settings page)
                item {
                    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AppCard),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animatedGlowingBorder(PrimaryViolet, 20.dp, 2.dp)
                    ) {"""
content = content.replace(old_support, new_support)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

