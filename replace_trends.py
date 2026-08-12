with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    lines = f.readlines()

start_idx_habit = -1
end_idx_habit = -1
start_idx_overall = -1
end_idx_overall = -1

for i, line in enumerate(lines):
    if line.startswith("fun calculateHabitTrendPoints("):
        start_idx_habit = i
    elif start_idx_habit != -1 and line.startswith("fun calculateOverallTrendPoints("):
        end_idx_habit = i
        start_idx_overall = i
    elif start_idx_overall != -1 and line.startswith("@Composable"):
        # this might be `@Composable` before TimeframeSelectorPills
        if "fun TimeframeSelectorPills" in "".join(lines[i:i+2]):
            end_idx_overall = i
            break

if start_idx_habit != -1 and end_idx_overall != -1:
    new_habit_code = """fun calculateHabitTrendPoints(
    habit: Habit,
    logs: List<HabitLog>,
    timeframeIndex: Int
): List<TrendPoint> {
    val today = java.time.LocalDate.now()
    val points = mutableListOf<TrendPoint>()

    when (timeframeIndex) {
        0 -> { // Weekly
            for (i in 5 downTo 0) {
                val weekStart = today.minusWeeks(i.toLong()).with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                val weekEnd = weekStart.plusDays(6)
                val actualEnd = if (weekEnd.isAfter(today)) today else weekEnd
                val score = calculateHabitStrengthOnDate(habit, logs, actualEnd.toString()).toFloat()
                val weekNum = weekStart.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                val label = "KW$weekNum"
                points.add(TrendPoint(label, score, 0))
            }
        }
        1 -> { // Monthly
            for (i in 5 downTo 0) {
                val monthDate = today.minusMonths(i.toLong())
                val monthEnd = monthDate.withDayOfMonth(monthDate.lengthOfMonth())
                val actualEnd = if (monthEnd.isAfter(today)) today else monthEnd
                val score = calculateHabitStrengthOnDate(habit, logs, actualEnd.toString()).toFloat()
                val monthName = monthDate.month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault())
                points.add(TrendPoint(monthName, score, 0))
            }
        }
        2 -> { // Yearly
            for (i in 3 downTo 0) {
                val yearDate = today.minusYears(i.toLong())
                val yearEnd = yearDate.withDayOfYear(yearDate.lengthOfYear())
                val actualEnd = if (yearEnd.isAfter(today)) today else yearEnd
                val score = calculateHabitStrengthOnDate(habit, logs, actualEnd.toString()).toFloat()
                val yearLabel = yearDate.year.toString()
                points.add(TrendPoint(yearLabel, score, 0))
            }
        }
    }
    return points
}

fun calculateOverallTrendPoints(
    allHabits: List<Habit>,
    allLogs: List<HabitLog>,
    timeframeIndex: Int
): List<TrendPoint> {
    val today = java.time.LocalDate.now()
    val points = mutableListOf<TrendPoint>()
    val activeHabits = allHabits.filter { !it.isArchived }

    if (activeHabits.isEmpty()) {
        val labels = when (timeframeIndex) {
            0 -> (5 downTo 0).map { i ->
                val weekStart = today.minusWeeks(i.toLong()).with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                "KW${weekStart.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)}"
            }
            1 -> (5 downTo 0).map { i ->
                today.minusMonths(i.toLong()).month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault())
            }
            else -> (3 downTo 0).map { i ->
                today.minusYears(i.toLong()).year.toString()
            }
        }
        return labels.map { TrendPoint(it, 0f, 0) }
    }

    when (timeframeIndex) {
        0 -> { // Weekly
            for (i in 5 downTo 0) {
                val weekStart = today.minusWeeks(i.toLong()).with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                val weekEnd = weekStart.plusDays(6)
                val actualEnd = if (weekEnd.isAfter(today)) today else weekEnd
                val score = activeHabits.map { calculateHabitStrengthOnDate(it, allLogs, actualEnd.toString()) }.average().toFloat()
                val weekNum = weekStart.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                val label = "KW$weekNum"
                points.add(TrendPoint(label, score, 0))
            }
        }
        1 -> { // Monthly
            for (i in 5 downTo 0) {
                val monthDate = today.minusMonths(i.toLong())
                val monthEnd = monthDate.withDayOfMonth(monthDate.lengthOfMonth())
                val actualEnd = if (monthEnd.isAfter(today)) today else monthEnd
                val score = activeHabits.map { calculateHabitStrengthOnDate(it, allLogs, actualEnd.toString()) }.average().toFloat()
                val monthName = monthDate.month.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.getDefault())
                points.add(TrendPoint(monthName, score, 0))
            }
        }
        2 -> { // Yearly
            for (i in 3 downTo 0) {
                val yearDate = today.minusYears(i.toLong())
                val yearEnd = yearDate.withDayOfYear(yearDate.lengthOfYear())
                val actualEnd = if (yearEnd.isAfter(today)) today else yearEnd
                val score = activeHabits.map { calculateHabitStrengthOnDate(it, allLogs, actualEnd.toString()) }.average().toFloat()
                val yearLabel = yearDate.year.toString()
                points.add(TrendPoint(yearLabel, score, 0))
            }
        }
    }
    return points
}

"""
    lines = lines[:start_idx_habit] + [new_habit_code] + lines[end_idx_overall:]
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.writelines(lines)
    print("Replaced functions successfully")
else:
    print(f"Failed to find indices: start_habit={start_idx_habit}, end_habit={end_idx_habit}, start_overall={start_idx_overall}, end_overall={end_idx_overall}")

