import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

match = re.search(r'// 1\. TOP ROW.*?val recentFiles = remember.*?\}', content, re.DOTALL)
if match:
    recent_logic = match.group(0)
    match2 = re.search(r'if \(recentFiles\.isNotEmpty\(\)\) \{.*?// 2\. SEARCH BAR', content, re.DOTALL)
    if match2:
        new_content = content.replace(match2.group(0), '// 2. SEARCH BAR')
        with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
            f.write(new_content)
        print("Success")
    else:
        print("Failed to find UI block")
else:
    print("Failed to find logic block")
