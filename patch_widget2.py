import re

with open('app/src/main/java/com/example/HabitWidgetProvider.kt', 'r') as f:
    content = f.read()

target = """        val componentName = ComponentName(context, HabitWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(componentName)
        lastUpdateAllTime = System.currentTimeMillis()
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = true)
    }

    private suspend fun performDeltaHabit(context: Context, habitId: Int, delta: Float) {"""

replacement = """        val componentName = ComponentName(context, HabitWidgetProvider::class.java)
        val ids = appWidgetManager.getAppWidgetIds(componentName)
        lastUpdateAllTime = System.currentTimeMillis()
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = false)
    }

    private suspend fun performDeltaHabit(context: Context, habitId: Int, delta: Float) {"""

content = content.replace(target, replacement)

target2 = """        val ids = appWidgetManager.getAppWidgetIds(componentName)
        lastUpdateAllTime = System.currentTimeMillis()
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = true)
    }

    private fun updateAllWidgets("""

replacement2 = """        val ids = appWidgetManager.getAppWidgetIds(componentName)
        lastUpdateAllTime = System.currentTimeMillis()
        updateAllWidgetsSuspend(context, appWidgetManager, ids, isFullUpdate = false)
    }

    private fun updateAllWidgets("""
content = content.replace(target2, replacement2)

with open('app/src/main/java/com/example/HabitWidgetProvider.kt', 'w') as f:
    f.write(content)
