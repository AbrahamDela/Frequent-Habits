import re

with open("app/src/main/java/com/example/ui/HabitsViewModel.kt", "r") as f:
    content = f.read()

# Add state flow
prop_old = """    private val _monthlyReviewEnabled = MutableStateFlow(sharedPrefs.getBoolean("monthly_review_enabled", true))"""
prop_new = """    private val _insightNotificationsEnabled = MutableStateFlow(sharedPrefs.getBoolean("insight_notifications_enabled", true))
    val insightNotificationsEnabled: StateFlow<Boolean> = _insightNotificationsEnabled.asStateFlow()

    private val _monthlyReviewEnabled = MutableStateFlow(sharedPrefs.getBoolean("monthly_review_enabled", true))"""
content = content.replace(prop_old, prop_new)

# Add setter
setter_old = """    fun setMonthlyReviewEnabled(enabled: Boolean) {
        _monthlyReviewEnabled.value = enabled
        sharedPrefs.edit().putBoolean("monthly_review_enabled", enabled).apply()
        com.example.NotificationHelper.scheduleReviewNotifications(getApplication())
    }"""
setter_new = """    fun setMonthlyReviewEnabled(enabled: Boolean) {
        _monthlyReviewEnabled.value = enabled
        sharedPrefs.edit().putBoolean("monthly_review_enabled", enabled).apply()
        com.example.NotificationHelper.scheduleReviewNotifications(getApplication())
    }

    fun setInsightNotificationsEnabled(enabled: Boolean) {
        _insightNotificationsEnabled.value = enabled
        sharedPrefs.edit().putBoolean("insight_notifications_enabled", enabled).apply()
        if (enabled) {
            com.example.NotificationHelper.scheduleSmartInsightNotifications(getApplication())
        } else {
            com.example.NotificationHelper.cancelSmartInsightNotifications(getApplication())
        }
    }"""
content = content.replace(setter_old, setter_new)

with open("app/src/main/java/com/example/ui/HabitsViewModel.kt", "w") as f:
    f.write(content)
