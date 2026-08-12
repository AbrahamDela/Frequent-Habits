#!/bin/bash

# Fix in calculateHabitTrendPoints
sed -i 's/activeDays++\n                        val log = logsMap\[dateStr\]/val log = logsMap[dateStr]/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/val log = logsMap\[dateStr\]\n                        val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)\n                        if (status == "SUCCESS") {/val log = logsMap[dateStr]\n                        val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)\n                        if (status == "PENDING" \&\& dateStr == todayStr) {\n                        } else {\n                            activeDays++\n                            if (status == "SUCCESS") {/g' app/src/main/java/com/example/MainActivity.kt

# Fix in calculateOverallTrendPoints
sed -i 's/activeDays++\n                            val log = logsMap\[habit.id to dateStr\]?.firstOrNull()/val log = logsMap[habit.id to dateStr]?.firstOrNull()/g' app/src/main/java/com/example/MainActivity.kt
sed -i 's/val log = logsMap\[habit.id to dateStr\]?.firstOrNull()\n                            val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)\n                            if (status == "SUCCESS") {/val log = logsMap[habit.id to dateStr]?.firstOrNull()\n                            val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)\n                            if (status == "PENDING" \&\& dateStr == todayStr) {\n                            } else {\n                                activeDays++\n                                if (status == "SUCCESS") {/g' app/src/main/java/com/example/MainActivity.kt

