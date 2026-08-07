import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target1 = r"""    if \(smartInsightDismissedDate == todayDateString\) return

    // Only show if the user has a bit of history \(e\.g\. at least 7 completed check-ins overall\)
    if \(perfectDaysStats\.totalCompletedHabits < 7\) return"""

replacement1 = """    // Only show if the user has a bit of history (e.g. at least 7 completed check-ins overall)
    if (perfectDaysStats.totalCompletedHabits < 7) return
    
    val coroutineScope = rememberCoroutineScope()
    // Local state to hide it instantly if already dismissed on load,
    // but keep it in tree during animation if dismissed just now.
    val isAlreadyDismissed = remember { smartInsightDismissedDate == todayDateString }
    if (isAlreadyDismissed) return"""

content = re.sub(target1, replacement1, content, count=1)

target2 = r"""                            Box\(
                                modifier = Modifier
                                    \.size\(24\.dp\)
                                    \.clickable \{
                                        visible = false
                                        onDismiss\(todayDateString\)
                                    \},
                                contentAlignment = Alignment\.Center
                            \) \{"""

replacement2 = """                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        if (visible) {
                                            visible = false
                                            coroutineScope.launch {
                                                kotlinx.coroutines.delay(500)
                                                onDismiss(todayDateString)
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {"""

content = re.sub(target2, replacement2, content, count=1)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
print("Patched SmartInsightCard successfully")
