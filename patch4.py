import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target1 = r"""@Composable
fun HabitItemRow\(
    habit: Habit,"""

replacement1 = """@Composable
fun HabitItemRow(
    modifier: Modifier = Modifier,
    habit: Habit,"""

content = re.sub(target1, replacement1, content, count=1)

target2 = r"""    Card\(
        colors = CardDefaults\.cardColors\(containerColor = Color\.Transparent\),
        shape = RoundedCornerShape\(16\.dp\),
        modifier = Modifier
            \.fillMaxWidth\(\)"""

replacement2 = """    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()"""

content = re.sub(target2, replacement2, content, count=1)

target3 = r"""                        HabitItemRow\(
                            habit = currentHabit,"""

replacement3 = """                        HabitItemRow(
                            modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(stiffness = Spring.StiffnessMediumLow)),
                            habit = currentHabit,"""

content = re.sub(target3, replacement3, content, count=1)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
print("Patched HabitItemRow successfully")
