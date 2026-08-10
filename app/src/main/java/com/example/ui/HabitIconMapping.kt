package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.*
import com.frequent.habits.R

data class IconCategory(
    val id: String,
    val nameDe: String,
    val nameEn: String,
    val keys: List<String>
)

object HabitIconMapping {

    val categories = listOf(
        IconCategory(
            id = "all",
            nameDe = "Alle",
            nameEn = "All",
            keys = emptyList()
        ),
        IconCategory(
            id = "health_sport",
            nameDe = "Sport & Gesundheit",
            nameEn = "Sport & Health",
            keys = listOf("dumbbell", "run", "bicycle", "pool", "heart", "meditation", "bed", "spa", "scale", "fire", "pill", "sport", "soccer", "trophy")
        ),
        IconCategory(
            id = "daily_nature",
            nameDe = "Alltag & Natur",
            nameEn = "Daily & Nature",
            keys = listOf("sparkle", "sun", "moon", "water", "coffee", "tea", "food", "apple", "smile", "smoke_free", "no_cell", "plant", "pet", "leaf", "drink", "cake")
        ),
        IconCategory(
            id = "learning_work",
            nameDe = "Lernen & Arbeit",
            nameEn = "Learning & Work",
            keys = listOf("book", "school", "code", "work", "laptop", "pencil", "brush", "palette", "brain", "language", "lightbulb", "camera", "music", "headphones")
        ),
        IconCategory(
            id = "home_money",
            nameDe = "Haushalt & Finanzen",
            nameEn = "Home & Finance",
            keys = listOf("clean", "home", "money", "piggy", "shopping", "clock", "timer", "calendar", "task", "phone", "car", "travel", "bell", "party", "star", "shield")
        )
    )

    val iconList = listOf(
        // Health & Sport
        "dumbbell" to Icons.Default.FitnessCenter,
        "run" to Icons.Default.DirectionsWalk,
        "bicycle" to Icons.Default.DirectionsBike,
        "pool" to Icons.Default.Pool,
        "heart" to Icons.Default.Favorite,
        "meditation" to Icons.Default.SelfImprovement,
        "bed" to Icons.Default.Bed,
        "spa" to Icons.Default.Spa,
        "scale" to Icons.Default.MonitorWeight,
        "fire" to Icons.Default.LocalFireDepartment,
        "pill" to Icons.Default.Medication,
        "sport" to Icons.Default.SportsEsports,
        "soccer" to Icons.Default.SportsSoccer,
        "trophy" to Icons.Default.EmojiEvents,

        // Daily Life & Nature
        "sparkle" to Icons.Default.AutoAwesome,
        "sun" to Icons.Default.WbSunny,
        "moon" to Icons.Default.NightsStay,
        "water" to Icons.Default.WaterDrop,
        "coffee" to Icons.Default.Coffee,
        "tea" to Icons.Default.FreeBreakfast,
        "food" to Icons.Default.Restaurant,
        "apple" to Icons.Default.LocalDining,
        "smile" to Icons.Default.SentimentSatisfiedAlt,
        "smoke_free" to Icons.Default.SmokeFree,
        "no_cell" to Icons.Default.PhonelinkErase,
        "plant" to Icons.Default.LocalFlorist,
        "pet" to Icons.Default.Pets,
        "leaf" to Icons.Default.Eco,
        "drink" to Icons.Default.LocalDrink,
        "cake" to Icons.Default.Cake,

        // Learning, Creative & Work
        "book" to Icons.Default.MenuBook,
        "school" to Icons.Default.School,
        "code" to Icons.Default.Code,
        "work" to Icons.Default.Work,
        "laptop" to Icons.Default.Laptop,
        "pencil" to Icons.Default.Edit,
        "brush" to Icons.Default.Brush,
        "palette" to Icons.Default.Palette,
        "brain" to Icons.Default.Psychology,
        "language" to Icons.Default.Language,
        "lightbulb" to Icons.Default.Lightbulb,
        "camera" to Icons.Default.PhotoCamera,
        "music" to Icons.Default.MusicNote,
        "headphones" to Icons.Default.Headphones,

        // Organization, Money & Home
        "clean" to Icons.Default.CleaningServices,
        "home" to Icons.Default.Home,
        "money" to Icons.Default.AttachMoney,
        "piggy" to Icons.Default.Savings,
        "shopping" to Icons.Default.ShoppingCart,
        "clock" to Icons.Default.Alarm,
        "timer" to Icons.Default.Timer,
        "calendar" to Icons.Default.CalendarToday,
        "task" to Icons.Default.CheckCircle,
        "phone" to Icons.Default.Phone,
        "car" to Icons.Default.DirectionsCar,
        "travel" to Icons.Default.Flight,
        "bell" to Icons.Default.Notifications,
        "party" to Icons.Default.Celebration,
        "star" to Icons.Default.Star,
        "shield" to Icons.Default.Shield
    )

    val colorList = listOf(
        "blue" to HabitBlue,
        "purple" to HabitPurple,
        "cyan" to HabitCyan,
        "green" to HabitGreen,
        "yellow" to HabitYellow,
        "orange" to HabitOrange,
        "red" to HabitRed,
        "pink" to HabitPink,
        "teal" to HabitTeal,
        "rose" to HabitRose,
        "indigo" to HabitIndigo
    )

    fun getIcon(name: String): ImageVector {
        val lower = name.lowercase()
        return iconList.find { it.first == lower }?.second ?: when (lower) {
            "sparkle" -> Icons.Default.AutoAwesome
            "moon" -> Icons.Default.NightsStay
            "sun" -> Icons.Default.WbSunny
            "water" -> Icons.Default.WaterDrop
            "heart" -> Icons.Default.Favorite
            "dumbbell" -> Icons.Default.FitnessCenter
            "book" -> Icons.Default.MenuBook
            "coffee" -> Icons.Default.Coffee
            "run" -> Icons.Default.DirectionsWalk
            "code" -> Icons.Default.Code
            "music" -> Icons.Default.MusicNote
            "phone" -> Icons.Default.Phone
            "meditation" -> Icons.Default.SelfImprovement
            "clock" -> Icons.Default.Alarm
            "food" -> Icons.Default.Restaurant
            "money" -> Icons.Default.AttachMoney
            "work" -> Icons.Default.Work
            "clean" -> Icons.Default.CleaningServices
            else -> Icons.Default.AutoAwesome
        }
    }

    fun getColor(name: String): Color {
        if (name.startsWith("#")) {
            try { return Color(android.graphics.Color.parseColor(name)) } catch (e: Exception) {}
        }
        if (name.startsWith("0x") || name.startsWith("0X")) {
            try { return Color(name.substring(2).toLong(16) or 0xFF000000) } catch (e: Exception) {}
        }
        return when (name.lowercase()) {
            "blue", "electric_blue" -> HabitBlue
            "purple" -> HabitPurple
            "cyan", "neon_teal" -> HabitCyan
            "green", "emerald_green" -> HabitGreen
            "yellow" -> HabitYellow
            "orange", "sunset_orange" -> HabitOrange
            "red", "crimson_red" -> HabitRed
            "pink", "rose_pink" -> HabitPink
            "slate", "grey", "gray" -> HabitSlate
            "teal" -> HabitTeal
            "rose" -> HabitRose
            "indigo" -> HabitIndigo
            else -> {
                try {
                    val hex = if (name.startsWith("#")) name else "#$name"
                    Color(android.graphics.Color.parseColor(hex))
                } catch (e: Exception) {
                    PrimaryViolet
                }
            }
        }
    }

    fun getIconDrawableId(name: String): Int {
        return when (name.lowercase()) {
            "sparkle", "plant", "pet", "smile", "leaf", "bell", "party", "star", "shield" -> R.drawable.ic_sparkle
            "moon", "bed" -> R.drawable.ic_moon
            "sun", "fire", "lightbulb" -> R.drawable.ic_sun
            "water", "drink" -> R.drawable.ic_water
            "heart", "spa" -> R.drawable.ic_heart
            "dumbbell", "bicycle", "pool", "sport", "scale", "soccer", "trophy" -> R.drawable.ic_dumbbell
            "book", "school", "language", "brain" -> R.drawable.ic_book
            "coffee", "tea" -> R.drawable.ic_coffee
            "run" -> R.drawable.ic_run
            "code", "laptop" -> R.drawable.ic_code
            "music", "headphones" -> R.drawable.ic_music
            "phone", "no_cell" -> R.drawable.ic_phone
            "meditation" -> R.drawable.ic_meditation
            "clock", "timer", "calendar" -> R.drawable.ic_clock
            "food", "apple", "cake" -> R.drawable.ic_food
            "money", "piggy", "shopping" -> R.drawable.ic_money
            "work", "pencil", "brush", "palette", "camera", "car", "travel" -> R.drawable.ic_work
            "clean", "home", "task" -> R.drawable.ic_clean
            else -> R.drawable.ic_sparkle
        }
    }
}
