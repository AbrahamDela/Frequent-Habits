with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

for i in range(85, 95):
    print(f"{i+1}: {repr(lines[i])}")
