with open("app/src/main/java/com/example/NotificationHelper.kt", "r") as f:
    content = f.read()

# find the last "    fun cancelSmartInsightNotifications(context: Context) {" and everything after it.
# Actually I'll just write a script to remove the junk.
import re

match = re.search(r'    fun cancelSmartInsightNotifications\(context: Context\) \{.*?\n    \}(.*?)\n\}', content, re.DOTALL)
if match:
    junk = match.group(1)
    content = content.replace(junk + "\n}", "\n}")
    with open("app/src/main/java/com/example/NotificationHelper.kt", "w") as f:
        f.write(content)
