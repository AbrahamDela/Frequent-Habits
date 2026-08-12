import re

with open("app/src/main/java/com/example/NotificationHelper.kt", "r") as f:
    content = f.read()

# I will just grep the whole file and recreate it cleanly if needed. But it's easier to fetch the original and append
