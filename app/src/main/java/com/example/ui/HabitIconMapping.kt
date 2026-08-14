package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.filled.*
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
        "dumbbell" to R.drawable.ic_dumbbell,
        "run" to R.drawable.ic_run,
        "bicycle" to R.drawable.ic_bicycle,
        "pool" to R.drawable.ic_pool,
        "heart" to R.drawable.ic_heart,
        "meditation" to R.drawable.ic_meditation,
        "bed" to R.drawable.ic_bed,
        "spa" to R.drawable.ic_spa,
        "scale" to R.drawable.ic_scale,
        "fire" to R.drawable.ic_fire,
        "pill" to R.drawable.ic_pill,
        "sport" to R.drawable.ic_sport,
        "soccer" to R.drawable.ic_soccer,
        "trophy" to R.drawable.ic_trophy,
        "sparkle" to R.drawable.ic_sparkle,
        "sun" to R.drawable.ic_sun,
        "moon" to R.drawable.ic_moon,
        "water" to R.drawable.ic_water,
        "coffee" to R.drawable.ic_coffee,
        "tea" to R.drawable.ic_tea,
        "food" to R.drawable.ic_food,
        "apple" to R.drawable.ic_apple,
        "smile" to R.drawable.ic_smile,
        "smoke_free" to R.drawable.ic_smoke_free,
        "no_cell" to R.drawable.ic_no_cell,
        "plant" to R.drawable.ic_plant,
        "pet" to R.drawable.ic_pet,
        "leaf" to R.drawable.ic_leaf,
        "drink" to R.drawable.ic_drink,
        "cake" to R.drawable.ic_cake,
        "book" to R.drawable.ic_book,
        "school" to R.drawable.ic_school,
        "code" to R.drawable.ic_code,
        "work" to R.drawable.ic_work,
        "laptop" to R.drawable.ic_laptop,
        "pencil" to R.drawable.ic_pencil,
        "brush" to R.drawable.ic_brush,
        "palette" to R.drawable.ic_palette,
        "brain" to R.drawable.ic_brain,
        "language" to R.drawable.ic_language,
        "lightbulb" to R.drawable.ic_lightbulb,
        "camera" to R.drawable.ic_camera,
        "music" to R.drawable.ic_music,
        "headphones" to R.drawable.ic_headphones,
        "clean" to R.drawable.ic_clean,
        "home" to R.drawable.ic_home,
        "money" to R.drawable.ic_money,
        "piggy" to R.drawable.ic_piggy,
        "shopping" to R.drawable.ic_shopping,
        "clock" to R.drawable.ic_clock,
        "timer" to R.drawable.ic_timer,
        "calendar" to R.drawable.ic_calendar,
        "task" to R.drawable.ic_task,
        "phone" to R.drawable.ic_phone,
        "car" to R.drawable.ic_car,
        "travel" to R.drawable.ic_travel,
        "bell" to R.drawable.ic_bell,
        "party" to R.drawable.ic_party,
        "star" to R.drawable.ic_star,
        "shield" to R.drawable.ic_shield
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

    @Composable
    fun getIcon(name: String): ImageVector {
        return when (name.lowercase()) {
            "dumbbell" -> Icons.Rounded.FitnessCenter
            "run" -> Icons.Rounded.DirectionsRun
            "bicycle" -> Icons.Rounded.DirectionsBike
            "pool" -> Icons.Rounded.Pool
            "heart" -> Icons.Rounded.Favorite
            "meditation" -> Icons.Rounded.SelfImprovement
            "bed" -> Icons.Rounded.Bed
            "spa" -> Icons.Rounded.Spa
            "scale" -> Icons.Rounded.MonitorWeight
            "fire" -> Icons.Rounded.LocalFireDepartment
            "pill" -> Icons.Rounded.Medication
            "sport" -> Icons.Rounded.SportsBasketball
            "soccer" -> Icons.Rounded.SportsSoccer
            "trophy" -> Icons.Rounded.EmojiEvents
            "sparkle" -> Icons.Rounded.AutoAwesome
            "sun" -> Icons.Rounded.WbSunny
            "moon" -> Icons.Rounded.Nightlight
            "water" -> Icons.Rounded.WaterDrop
            "coffee" -> Icons.Rounded.Coffee
            "tea" -> Icons.Rounded.EmojiFoodBeverage
            "food" -> Icons.Rounded.Restaurant
            "apple" -> Icons.Rounded.Fastfood
            "smile" -> Icons.Rounded.SentimentSatisfied
            "smoke_free" -> Icons.Rounded.SmokeFree
            "no_cell" -> Icons.Rounded.NoCell
            "plant" -> Icons.Rounded.Grass
            "pet" -> Icons.Rounded.Pets
            "leaf" -> Icons.Rounded.Eco
            "drink" -> Icons.Rounded.LocalDrink
            "cake" -> Icons.Rounded.Cake
            "book" -> Icons.Rounded.MenuBook
            "school" -> Icons.Rounded.School
            "code" -> Icons.Rounded.Code
            "work" -> Icons.Rounded.Work
            "laptop" -> Icons.Rounded.Laptop
            "pencil" -> Icons.Rounded.Edit
            "brush" -> Icons.Rounded.Brush
            "palette" -> Icons.Rounded.Palette
            "brain" -> Icons.Rounded.Psychology
            "language" -> Icons.Rounded.Language
            "lightbulb" -> Icons.Rounded.Lightbulb
            "camera" -> Icons.Rounded.PhotoCamera
            "music" -> Icons.Rounded.MusicNote
            "headphones" -> Icons.Rounded.Headphones
            "clean" -> Icons.Rounded.CleaningServices
            "home" -> Icons.Rounded.Home
            "money" -> Icons.Rounded.AttachMoney
            "piggy" -> Icons.Rounded.Savings
            "shopping" -> Icons.Rounded.ShoppingCart
            "clock" -> Icons.Rounded.Schedule
            "timer" -> Icons.Rounded.Timer
            "calendar" -> Icons.Rounded.CalendarToday
            "task" -> Icons.Rounded.Task
            "phone" -> Icons.Rounded.Smartphone
            "car" -> Icons.Rounded.DirectionsCar
            "travel" -> Icons.Rounded.Flight
            "bell" -> Icons.Rounded.Notifications
            "party" -> Icons.Rounded.Celebration
            "star" -> Icons.Rounded.Star
            "shield" -> Icons.Rounded.Security
            else -> Icons.Rounded.AutoAwesome
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
            "dumbbell" -> R.drawable.ic_dumbbell
            "run" -> R.drawable.ic_run
            "bicycle" -> R.drawable.ic_bicycle
            "pool" -> R.drawable.ic_pool
            "heart" -> R.drawable.ic_heart
            "meditation" -> R.drawable.ic_meditation
            "bed" -> R.drawable.ic_bed
            "spa" -> R.drawable.ic_spa
            "scale" -> R.drawable.ic_scale
            "fire" -> R.drawable.ic_fire
            "pill" -> R.drawable.ic_pill
            "sport" -> R.drawable.ic_sport
            "soccer" -> R.drawable.ic_soccer
            "trophy" -> R.drawable.ic_trophy
            "sparkle" -> R.drawable.ic_sparkle
            "sun" -> R.drawable.ic_sun
            "moon" -> R.drawable.ic_moon
            "water" -> R.drawable.ic_water
            "coffee" -> R.drawable.ic_coffee
            "tea" -> R.drawable.ic_tea
            "food" -> R.drawable.ic_food
            "apple" -> R.drawable.ic_apple
            "smile" -> R.drawable.ic_smile
            "smoke_free" -> R.drawable.ic_smoke_free
            "no_cell" -> R.drawable.ic_no_cell
            "plant" -> R.drawable.ic_plant
            "pet" -> R.drawable.ic_pet
            "leaf" -> R.drawable.ic_leaf
            "drink" -> R.drawable.ic_drink
            "cake" -> R.drawable.ic_cake
            "book" -> R.drawable.ic_book
            "school" -> R.drawable.ic_school
            "code" -> R.drawable.ic_code
            "work" -> R.drawable.ic_work
            "laptop" -> R.drawable.ic_laptop
            "pencil" -> R.drawable.ic_pencil
            "brush" -> R.drawable.ic_brush
            "palette" -> R.drawable.ic_palette
            "brain" -> R.drawable.ic_brain
            "language" -> R.drawable.ic_language
            "lightbulb" -> R.drawable.ic_lightbulb
            "camera" -> R.drawable.ic_camera
            "music" -> R.drawable.ic_music
            "headphones" -> R.drawable.ic_headphones
            "clean" -> R.drawable.ic_clean
            "home" -> R.drawable.ic_home
            "money" -> R.drawable.ic_money
            "piggy" -> R.drawable.ic_piggy
            "shopping" -> R.drawable.ic_shopping
            "clock" -> R.drawable.ic_clock
            "timer" -> R.drawable.ic_timer
            "calendar" -> R.drawable.ic_calendar
            "task" -> R.drawable.ic_task
            "phone" -> R.drawable.ic_phone
            "car" -> R.drawable.ic_car
            "travel" -> R.drawable.ic_travel
            "bell" -> R.drawable.ic_bell
            "party" -> R.drawable.ic_party
            "star" -> R.drawable.ic_star
            "shield" -> R.drawable.ic_shield
            else -> R.drawable.ic_sparkle
        }
    }
}
