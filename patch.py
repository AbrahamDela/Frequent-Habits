import re
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = r"""                val animatedFraction = animFraction\.value
                val streakScale = remember \{ Animatable\(1f\) \}
                LaunchedEffect\(animatedFraction\) \{
                    if \(animatedFraction >= 1\.0f && fraction >= 1\.0f && total > 0\) \{
                        streakScale\.animateTo\(
                            targetValue = 1\.4f,
                            animationSpec = spring\(
                                dampingRatio = Spring\.DampingRatioMediumBouncy,
                                stiffness = Spring\.StiffnessMedium
                            \)
                        \)
                        streakScale\.animateTo\(
                            targetValue = 1f,
                            animationSpec = spring\(
                                dampingRatio = Spring\.DampingRatioMediumBouncy,
                                stiffness = Spring\.StiffnessLow
                            \)
                        \)
                    \}
                \}

                Column\(
                    modifier = Modifier
                        \.fillMaxWidth\(\)
                        \.padding\(vertical = 6\.dp\)
                \) \{
                    Row\(
                        modifier = Modifier\.fillMaxWidth\(\),
                        verticalAlignment = Alignment\.CenterVertically
                    \) \{
                        Box\(
                            modifier = Modifier
                                \.weight\(1f\)
                                \.height\(36\.dp\)
                                \.clip\(RoundedCornerShape\(18\.dp\)\)
                                \.background\(ProgressTrack\)
                        \) \{
                            if \(animatedFraction > 0f\) \{
                                Box\(
                                    modifier = Modifier
                                        \.fillMaxHeight\(\)
                                        \.fillMaxWidth\(animatedFraction\.coerceIn\(0f, 1f\)\)
                                        \.background\(if \(animatedFraction >= 1\.0f\) SuccessGreen else PrimaryViolet\)
                                \)
                            \}

                            Row\(
                                modifier = Modifier
                                    \.fillMaxSize\(\)
                                    \.padding\(horizontal = 14\.dp\),
                                horizontalArrangement = Arrangement\.SpaceBetween,
                                verticalAlignment = Alignment\.CenterVertically
                            \) \{
                                Text\(
                                    text = if \(language == "de"\) "Tagesfortschritt" else "Daily Progress",
                                    style = MaterialTheme\.typography\.labelLarge,
                                    fontWeight = FontWeight\.Bold,
                                    color = Color\.White\.copy\(alpha = 0\.85f\)
                                \)
                                Text\(
                                    text = "\$\{\(animatedFraction \* 100\)\.toInt\(\)\}%\ \(\$progressText\)",
                                    style = MaterialTheme\.typography\.labelLarge,
                                    fontWeight = FontWeight\.Bold,
                                    color = Color\.White
                                \)
                            \}
                        \}
                        
                        Spacer\(modifier = Modifier\.width\(12\.dp\)\)
                        
                        Surface\(
                            color = DarkCard,
                            shape = RoundedCornerShape\(18\.dp\),
                            border = BorderStroke\(1\.dp, if \(fraction >= 1f\) HabitOrange\.copy\(alpha = 0\.5f\) else DarkBorder\),
                            modifier = Modifier
                                \.height\(36\.dp\)
                                \.scale\(streakScale\.value\)
                                \.testTag\("perfect_day_streak_badge"\)
                        \) \{
                            Row\(
                                modifier = Modifier\.padding\(horizontal = 12\.dp\),
                                verticalAlignment = Alignment\.CenterVertically,
                                horizontalArrangement = Arrangement\.spacedBy\(6\.dp\)
                            \) \{
                                Text\(
                                    text = "🔥",
                                    fontSize = 14\.sp
                                \)
                                Text\(
                                    text = "\$\{perfectDaysStats\.currentStreak\}",
                                    style = MaterialTheme\.typography\.titleSmall,
                                    fontWeight = FontWeight\.Bold,
                                    color = if \(fraction >= 1f\) HabitOrange else TextSecondary
                                \)
                            \}
                        \}"""

replacement = """                val animatedFraction = animFraction.value
                val streakScale = remember { Animatable(1f) }
                var displayedStreak by remember { mutableStateOf(perfectDaysStats.currentStreak) }
                
                LaunchedEffect(perfectDaysStats.currentStreak) {
                    if (fraction < 1.0f) {
                        displayedStreak = perfectDaysStats.currentStreak
                    }
                }
                
                LaunchedEffect(animatedFraction) {
                    if (animatedFraction >= 1.0f && fraction >= 1.0f && total > 0) {
                        displayedStreak = perfectDaysStats.currentStreak
                        streakScale.animateTo(
                            targetValue = 1.4f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                        streakScale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            color = DarkCard,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, DarkBorder)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (animatedFraction > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(animatedFraction.coerceIn(0f, 1f))
                                            .background(if (animatedFraction >= 1.0f) SuccessGreen else PrimaryViolet)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (language == "de") "Tagesfortschritt" else "Daily Progress",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = progressText,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Surface(
                            color = DarkCard,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, if (fraction >= 1f) HabitOrange.copy(alpha = 0.5f) else DarkBorder),
                            modifier = Modifier
                                .height(36.dp)
                                .scale(streakScale.value)
                                .testTag("perfect_day_streak_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${displayedStreak}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (fraction >= 1f) HabitOrange else TextSecondary
                                )
                            }
                        }"""

content = re.sub(target, replacement, content, count=1, flags=re.DOTALL)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
print("Patched daily progress and streak successfully")
