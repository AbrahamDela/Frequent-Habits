#!/bin/bash
sed -i 's/if (isHabitActiveOnDate(habit, dateStr)) {/if (isHabitActiveOnDate(habit, dateStr)) {/g' app/src/main/java/com/example/MainActivity.kt
