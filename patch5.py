import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""    // Always add a fallback
    options\.add\(InsightOption\(
        text = if \(language == "de"\) "\$\{greeting\}jeder Check-in heute bringt dich deinem Ziel einen Schritt näher\. ⚡" else "\$\{greeting\}every check-in today brings you one step closer to your goal\. ⚡",
        icon = Icons\.Default\.Bolt,
        color = PrimaryViolet
    \)\)"""

replacement = """    // Always add fallbacks to ensure variety
    options.add(InsightOption(
        text = if (language == "de") "${greeting}jeder Check-in heute bringt dich deinem Ziel einen Schritt näher. ⚡" else "${greeting}every check-in today brings you one step closer to your goal. ⚡",
        icon = Icons.Default.Bolt,
        color = PrimaryViolet
    ))
    options.add(InsightOption(
        text = if (language == "de") "${greeting}ein neuer Tag, eine neue Chance! Lass uns loslegen. 🚀" else "${greeting}a new day, a new chance! Let's get started. 🚀",
        icon = Icons.Default.AutoAwesome,
        color = SuccessGreen
    ))
    options.add(InsightOption(
        text = if (language == "de") "${greeting}Konstanz ist der Schlüssel zum Erfolg. Bleib fokussiert! 🎯" else "${greeting}Consistency is the key to success. Stay focused! 🎯",
        icon = Icons.Default.TrackChanges,
        color = HabitOrange
    ))
    options.add(InsightOption(
        text = if (language == "de") "${greeting}mach diesen Tag zu einem perfekten Tag. Du schaffst das! 🌟" else "${greeting}make this day a perfect day. You got this! 🌟",
        icon = Icons.Default.Star,
        color = HabitYellow
    ))"""

content = re.sub(target, replacement, content, count=1)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
print("Patched fallbacks successfully")
