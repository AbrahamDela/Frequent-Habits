package com.example.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.tr
import com.example.data.Habit
import com.example.data.HabitLog
import com.example.MonthlyReviewData
import com.example.YearlyReviewData
import com.example.ui.theme.PrimaryViolet
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

fun isNewYearReviewPeriod(): Boolean {
    val today = LocalDate.now()
    val month = today.monthValue
    return month == 1
}

fun isNewYearPopupPeriod(): Boolean {
    val today = LocalDate.now()
    val month = today.monthValue
    val day = today.dayOfMonth
    // Popup appears a few days before New Year until Jan 5: Dec 27 to Jan 5
    return (month == 12 && day >= 27) || (month == 1 && day <= 5)
}

// Theme presets for share cards
data class ShareThemePreset(
    val name: String,
    val bgColors: List<Color>,
    val accentColor: Color,
    val textColor: Color
)

val habitShareThemes = listOf(
    ShareThemePreset(
        name = "Violet Glow",
        bgColors = listOf(Color(0xFF2E1065), Color(0xFF0F172A)),
        accentColor = Color(0xFFA855F7),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Emerald Mint",
        bgColors = listOf(Color(0xFF064E3B), Color(0xFF022C22)),
        accentColor = Color(0xFF10B981),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Sunset Orange",
        bgColors = listOf(Color(0xFF7C2D12), Color(0xFF18181B)),
        accentColor = Color(0xFFF97316),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Electric Cyber",
        bgColors = listOf(Color(0xFF1E1B4B), Color(0xFF311042)),
        accentColor = Color(0xFF06B6D4),
        textColor = Color.White
    )
)

val profileShareThemes = listOf(
    ShareThemePreset(
        name = "Midnight Cyber",
        bgColors = listOf(Color(0xFF1E1B4B), Color(0xFF0F172A)),
        accentColor = Color(0xFFA855F7),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Emerald Gold",
        bgColors = listOf(Color(0xFF064E3B), Color(0xFF14532D)),
        accentColor = Color(0xFFF59E0B),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Sunset Royal",
        bgColors = listOf(Color(0xFF831843), Color(0xFF4C1D95)),
        accentColor = Color(0xFFF43F5E),
        textColor = Color.White
    ),
    ShareThemePreset(
        name = "Obsidian Dark",
        bgColors = listOf(Color(0xFF18181B), Color(0xFF09090B)),
        accentColor = Color(0xFF38BDF8),
        textColor = Color.White
    )
)

/**
 * Helper to share a bitmap image card via Android Native Share Sheet
 */
fun shareBitmapImage(context: Context, bitmap: Bitmap, title: String, textSummary: String) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "share_card_${System.currentTimeMillis()}.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "com.example.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, textSummary)
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, title)
        context.startActivity(chooser)
    } catch (e: Exception) {
        Log.e("ShareDialogs", "Error sharing image", e)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$textSummary")
        }
        context.startActivity(Intent.createChooser(intent, title))
    }
}

// ==========================================
// HABIT SHARE DIALOG
// ==========================================
@Composable
fun HabitShareDialog(
    habit: Habit,
    habitLogs: List<HabitLog>,
    userName: String,
    language: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Calculate Habit Stats
    val habitSpecificLogs = remember(habit.id, habitLogs) { habitLogs.filter { it.habitId == habit.id } }
    val totalCompletions = remember(habitSpecificLogs) { habitSpecificLogs.size }
    
    // Calculate streak
    val streakDays = remember(habitSpecificLogs) {
        if (habitSpecificLogs.isEmpty()) 0
        else {
            val logDates = habitSpecificLogs.map { it.date }.toSet()
            var current = LocalDate.now()
            var streak = 0
            while (logDates.contains(current.toString())) {
                streak++
                current = current.minusDays(1)
            }
            if (streak == 0 && logDates.contains(LocalDate.now().minusDays(1).toString())) {
                current = LocalDate.now().minusDays(1)
                while (logDates.contains(current.toString())) {
                    streak++
                    current = current.minusDays(1)
                }
            }
            streak
        }
    }

    // Completion rate in last 30 days
    val last30DaysRate = remember(habitSpecificLogs) {
        val today = LocalDate.now()
        val last30Set = (0 until 30).map { today.minusDays(it.toLong()).toString() }.toSet()
        val count = habitSpecificLogs.count { last30Set.contains(it.date) }
        ((count / 30f) * 100).toInt().coerceIn(0, 100)
    }

    // Density / Option Toggles
    var showStreak by remember { mutableStateOf(true) }
    var showCompletionRate by remember { mutableStateOf(true) }
    var showTarget by remember { mutableStateOf(true) }
    var showTotalCount by remember { mutableStateOf(true) }
    var showUserName by remember { mutableStateOf(true) }
    var showCustomMotto by remember { mutableStateOf(true) }
    var customMottoText by remember {
        mutableStateOf(if (language == "de") "Fokus & Disziplin jeden Tag! 💪" else "Focus & Consistency every day! 💪")
    }
    var selectedThemeIndex by remember { mutableIntStateOf(0) }

    val currentTheme = habitShareThemes[selectedThemeIndex % habitShareThemes.size]

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF12121A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color(0xFFA855F7),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == "de") "Gewohnheit teilen" else "Share Habit",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // LIVE PREVIEW CARD
                    Text(
                        text = if (language == "de") "VORSCHAU" else "PREVIEW",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.verticalGradient(currentTheme.bgColors)
                            )
                            .border(1.5.dp, currentTheme.accentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Category Tag
                            Surface(
                                color = currentTheme.accentColor.copy(alpha = 0.2f),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.accentColor.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = habit.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = currentTheme.accentColor,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Habit Name
                            Text(
                                text = habit.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Target / Unit info
                            if (showTarget && habit.type == "NUMBER" && habit.unit.isNotEmpty()) {
                                Text(
                                    text = if (language == "de") "Ziel: ${habit.targetValue} ${habit.unit}" else "Target: ${habit.targetValue} ${habit.unit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Metrics Row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (showStreak) {
                                    MetricChip(
                                        icon = "🔥",
                                        label = "$streakDays ${if (language == "de") "Tage Streak" else "Days Streak"}",
                                        accent = currentTheme.accentColor
                                    )
                                }
                                if (showCompletionRate) {
                                    MetricChip(
                                        icon = "📈",
                                        label = "$last30DaysRate% ${if (language == "de") "30T Erfolge" else "30d Success"}",
                                        accent = currentTheme.accentColor
                                    )
                                }
                                if (showTotalCount) {
                                    MetricChip(
                                        icon = "✅",
                                        label = "$totalCompletions ${if (language == "de") "mal absolviert" else "times done"}",
                                        accent = currentTheme.accentColor
                                    )
                                }
                            }

                            // Custom Motto Quote
                            if (showCustomMotto && customMottoText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "\"$customMottoText\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // User Badge
                            if (showUserName && userName.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "— $userName",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = currentTheme.accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // INFORMATION DENSITY & CUSTOMIZATION CONTROLS
                    Text(
                        text = if (language == "de") "INFORMATIONSDICHTE ANPASSEN" else "CUSTOMIZE INFORMATION DENSITY",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )

                    // Theme selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        habitShareThemes.forEachIndexed { idx, theme ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Brush.horizontalGradient(theme.bgColors))
                                    .border(
                                        width = if (selectedThemeIndex == idx) 2.dp else 1.dp,
                                        color = if (selectedThemeIndex == idx) theme.accentColor else Color.White.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedThemeIndex = idx },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = theme.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = if (selectedThemeIndex == idx) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Toggles
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1E1E2A))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DensityToggleRow(
                            label = if (language == "de") "🔥 Serie / Streak anzeigen" else "🔥 Show Streak",
                            checked = showStreak,
                            onCheckedChange = { showStreak = it }
                        )
                        DensityToggleRow(
                            label = if (language == "de") "📈 Erfolgsquote (30 Tage) anzeigen" else "📈 Show Success Rate",
                            checked = showCompletionRate,
                            onCheckedChange = { showCompletionRate = it }
                        )
                        DensityToggleRow(
                            label = if (language == "de") "✅ Gesamtzahl der Abschlüsse anzeigen" else "✅ Show Total Completions",
                            checked = showTotalCount,
                            onCheckedChange = { showTotalCount = it }
                        )
                        if (habit.type == "NUMBER" && habit.unit.isNotEmpty()) {
                            DensityToggleRow(
                                label = if (language == "de") "🎯 Tagesziel & Einheit anzeigen" else "🎯 Show Target & Unit",
                                checked = showTarget,
                                onCheckedChange = { showTarget = it }
                            )
                        }
                        DensityToggleRow(
                            label = if (language == "de") "👤 Deinen Profilnamen anzeigen" else "👤 Show Profile Name",
                            checked = showUserName,
                            onCheckedChange = { showUserName = it }
                        )
                        DensityToggleRow(
                            label = if (language == "de") "💬 Eigenen Spruch / Motto anzeigen" else "💬 Show Custom Quote",
                            checked = showCustomMotto,
                            onCheckedChange = { showCustomMotto = it }
                        )
                    }

                    // Editable Quote / Motto input field
                    if (showCustomMotto) {
                        OutlinedTextField(
                            value = customMottoText,
                            onValueChange = { customMottoText = it },
                            label = { Text(if (language == "de") "Persönliche Notiz / Motto" else "Personal Quote") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = currentTheme.accentColor,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Button: Share
                Button(
                    onClick = {
                        val bitmap = renderHabitShareBitmap(
                            habit = habit,
                            streakDays = if (showStreak) streakDays else null,
                            completionRate = if (showCompletionRate) last30DaysRate else null,
                            totalCompletions = if (showTotalCount) totalCompletions else null,
                            showTarget = showTarget,
                            userName = if (showUserName) userName else null,
                            customMotto = if (showCustomMotto) customMottoText else null,
                            theme = currentTheme,
                            language = language
                        )
                        val textSummary = buildString {
                            append("🔥 ")
                            append(habit.name)
                            if (showStreak) append(" • $streakDays Tage Streak")
                            if (showCompletionRate) append(" • $last30DaysRate% Erfolgsquote")
                            append("\nShared via Everyday Habits App")
                        }
                        shareBitmapImage(context, bitmap, habit.name, textSummary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "de") "Gewohnheitskarte teilen" else "Share Habit Card",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// PROFILE SHARE DIALOG
// ==========================================
@Composable
fun ProfileShareDialog(
    userName: String,
    profileImageUri: String,
    habits: List<Habit>,
    logs: List<HabitLog>,
    language: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = PrimaryViolet

    // Calculate metrics
    val totalCompletions = remember(logs) { logs.size }
    val activeHabitsCount = remember(habits) { habits.count { !it.isArchived } }
    val longestStreak = remember(habits, logs) {
        habits.maxOfOrNull { h ->
            val hLogs = logs.filter { it.habitId == h.id }.map { it.date }.toSet()
            var streak = 0
            var curr = LocalDate.now()
            while (hLogs.contains(curr.toString())) {
                streak++
                curr = curr.minusDays(1)
            }
            streak
        } ?: 0
    }
    val userLevel = remember(totalCompletions) { (totalCompletions / 15) + 1 }

    // Render bitmap
    val bitmap = remember(userName, profileImageUri, userLevel, totalCompletions, activeHabitsCount, longestStreak, accentColor, language) {
        renderProfileShareBitmap(
            context = context,
            userName = userName,
            profileImageUri = profileImageUri,
            userLevel = userLevel,
            totalCompletions = totalCompletions,
            activeHabits = activeHabitsCount,
            longestStreak = longestStreak,
            accentColor = accentColor,
            language = language
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF12121A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == "de") "Profilkarte teilen" else "Share Profile",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Preview (using the exact rendered Bitmap!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1080f / 1350f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Profile Share Card Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Share
                Button(
                    onClick = {
                        val title = if (language == "de") "Frequent Habits Profil" else "Frequent Habits Profile"
                        val summaryText = getSocialShareText(language)
                        shareBitmapImage(context, bitmap, title, summaryText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "de") "Profilkarte teilen" else "Share Profile Card",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyReviewShareDialog(
    year: Int,
    reviewData: MonthlyReviewData,
    language: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = PrimaryViolet

    // Render bitmap
    val bitmap = remember(year, reviewData, accentColor, language) {
        renderMonthlyReviewShareBitmap(
            context = context,
            year = year,
            reviewData = reviewData,
            language = language,
            accentColor = accentColor
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF12121A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == "de") "Monatsrückblick teilen" else "Share Monthly Review",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Preview (using the exact rendered Bitmap!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1080f / 1350f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Monthly Review Card Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Share
                Button(
                    onClick = {
                        val title = if (language == "de") "Monatsrückblick ${reviewData.monthName} $year" else "${reviewData.monthName} $year Review"
                        val summaryText = getSocialShareText(language)
                        shareBitmapImage(context, bitmap, title, summaryText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "de") "Rückblick jetzt teilen" else "Share Review Now",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun YearlyReviewShareDialog(
    year: Int,
    reviewData: YearlyReviewData,
    language: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = PrimaryViolet

    // Render bitmap
    val bitmap = remember(year, reviewData, accentColor, language) {
        renderYearlyReviewShareBitmap(
            context = context,
            year = year,
            reviewData = reviewData,
            language = language,
            accentColor = accentColor
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF12121A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == "de") "Jahresrückblick teilen" else "Share Yearly Review",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card Preview (using the exact rendered Bitmap!)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1080f / 1350f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Yearly Review Card Preview",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Share
                Button(
                    onClick = {
                        val title = if (language == "de") "Jahresrückblick $year" else "$year Review"
                        val summaryText = getSocialShareText(language)
                        shareBitmapImage(context, bitmap, title, summaryText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "de") "Rückblick jetzt teilen" else "Share Review Now",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// NEW YEAR REVIEW POPUP DIALOG
// ==========================================
@Composable
fun NewYearReviewPopupDialog(
    reviewYear: Int,
    language: String,
    onStartReview: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4C1D95),
                            Color(0xFF1E1B4B),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .border(2.dp, Color(0xFFA855F7), RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Festive Header Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        .border(2.dp, Color(0xFFF59E0B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🎆", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = if (language == "de") "Dein $reviewYear Rückblick ist da! 🎉" else "Your $reviewYear Review is Ready! 🎉",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Teaser Text
                Text(
                    text = if (language == "de")
                        "Entdecke deine beeindruckenden Erfolge, Strakes und Top-Gewohnheiten des vergangenen Jahres in deiner persönlichen Story!"
                    else
                        "Discover your highlights, streaks and top habits from the past year in your personal interactive story!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Button(
                    onClick = {
                        onDismiss()
                        onStartReview()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA855F7),
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == "de") "Rückblick jetzt ansehen" else "View Story Now",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == "de") "Später" else "Later",
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Helper composables
@Composable
private fun MetricChip(icon: String, label: String, accent: Color) {
    Surface(
        color = accent.copy(alpha = 0.2f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DensityToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFA855F7)
            )
        )
    }
}

// ==========================================
// BITMAP RENDERING HELPER FUNCTIONS
// ==========================================

fun renderHabitShareBitmap(
    habit: Habit,
    streakDays: Int?,
    completionRate: Int?,
    totalCompletions: Int?,
    showTarget: Boolean,
    userName: String?,
    customMotto: String?,
    theme: ShareThemePreset,
    language: String
): Bitmap {
    val width = 1080
    val height = 1350
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Background Gradient Fill
    val bgPaint = Paint().apply {
        isAntiAlias = true
        shader = android.graphics.LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            theme.bgColors.first().toArgb(),
            theme.bgColors.last().toArgb(),
            android.graphics.Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Border Card
    val cardPaint = Paint().apply {
        isAntiAlias = true
        color = theme.accentColor.toArgb()
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    val cardRect = RectF(60f, 60f, width - 60f, height - 60f)
    canvas.drawRoundRect(cardRect, 48f, 48f, cardPaint)

    var currentY = 220f

    // Category
    val catPaint = Paint().apply {
        isAntiAlias = true
        color = theme.accentColor.toArgb()
        textSize = 38f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(habit.category.uppercase(), width / 2f, currentY, catPaint)
    currentY += 120f

    // Habit Title
    val titlePaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 72f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(habit.name, width / 2f, currentY, titlePaint)
    currentY += 90f

    if (showTarget && habit.type == "NUMBER" && habit.unit.isNotEmpty()) {
        val targetPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.LTGRAY
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
        val targetText = if (language == "de") "Ziel: ${habit.targetValue} ${habit.unit}" else "Target: ${habit.targetValue} ${habit.unit}"
        canvas.drawText(targetText, width / 2f, currentY, targetPaint)
        currentY += 80f
    }

    currentY += 60f

    // Metrics
    val metricPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 44f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    if (streakDays != null) {
        val text = "🔥 $streakDays ${if (language == "de") "Tage Streak" else "Days Streak"}"
        canvas.drawText(text, width / 2f, currentY, metricPaint)
        currentY += 80f
    }

    if (completionRate != null) {
        val text = "📈 $completionRate% ${if (language == "de") "Erfolgsquote" else "Success Rate"}"
        canvas.drawText(text, width / 2f, currentY, metricPaint)
        currentY += 80f
    }

    if (totalCompletions != null) {
        val text = "✅ $totalCompletions ${if (language == "de") "mal geschafft" else "times completed"}"
        canvas.drawText(text, width / 2f, currentY, metricPaint)
        currentY += 80f
    }

    if (!customMotto.isNullOrBlank()) {
        currentY += 40f
        val mottoPaint = Paint().apply {
            isAntiAlias = true
            color = theme.accentColor.toArgb()
            textSize = 42f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("\"$customMotto\"", width / 2f, currentY, mottoPaint)
        currentY += 80f
    }

    if (!userName.isNullOrBlank()) {
        val userPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.WHITE
            textSize = 38f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("— $userName", width / 2f, height - 140f, userPaint)
    }

    // App Branding Footer
    val footerPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Everyday Habits App", width / 2f, height - 80f, footerPaint)

    return bitmap
}

fun getSocialShareText(language: String): String {
    return if (language == "de") {
        "Ich baue bessere Routinen auf und verfolge meine täglichen Ziele mit Frequent Habits! 🚀 Werde auch du produktiver und gestalte deine perfekte Routine. Lade die App hier herunter: https://ais-pre-lcaq5stuvgcre6e7salzdk-873513281263.europe-west2.run.app"
    } else {
        "I'm building better routines and tracking my daily goals with Frequent Habits! 🚀 Join me in shaping positive habits every day. Download the app here: https://ais-pre-lcaq5stuvgcre6e7salzdk-873513281263.europe-west2.run.app"
    }
}

fun mixColorWithBlack(color: Int, ratio: Float): Int {
    val a = android.graphics.Color.alpha(color)
    val r = (android.graphics.Color.red(color) * ratio).toInt()
    val g = (android.graphics.Color.green(color) * ratio).toInt()
    val b = (android.graphics.Color.blue(color) * ratio).toInt()
    return android.graphics.Color.argb(a, r, g, b)
}

fun loadBitmapFromUri(context: Context, uriString: String): Bitmap? {
    if (uriString.isBlank()) return null
    return try {
        val uri = Uri.parse(uriString)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        Log.e("ShareDialogs", "Error loading bitmap from URI: $uriString", e)
        null
    }
}

fun drawCircularAvatar(canvas: Canvas, bitmap: Bitmap, centerX: Float, centerY: Float, radius: Float, borderPaint: Paint?) {
    try {
        val size = (radius * 2).toInt()
        val scaled = Bitmap.createScaledBitmap(bitmap, size, size, true)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val outputCanvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
        }
        outputCanvas.drawARGB(0, 0, 0, 0)
        paint.color = 0xff424242.toInt()
        outputCanvas.drawCircle(radius, radius, radius, paint)
        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        val rect = android.graphics.Rect(0, 0, size, size)
        outputCanvas.drawBitmap(scaled, rect, rect, paint)
        
        canvas.drawBitmap(output, centerX - radius, centerY - radius, null)
        if (borderPaint != null) {
            canvas.drawCircle(centerX, centerY, radius, borderPaint)
        }
    } catch (e: Exception) {
        Log.e("ShareDialogs", "Failed to draw circular avatar", e)
    }
}

fun renderProfileShareBitmap(
    context: Context,
    userName: String,
    profileImageUri: String,
    userLevel: Int,
    totalCompletions: Int,
    activeHabits: Int,
    longestStreak: Int,
    accentColor: Color,
    language: String
): Bitmap {
    val width = 1080
    val height = 1350
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val primaryInt = accentColor.toArgb()
    val bgStart = mixColorWithBlack(primaryInt, 0.15f)
    val bgEnd = mixColorWithBlack(primaryInt, 0.04f)

    val bgPaint = Paint().apply {
        isAntiAlias = true
        shader = android.graphics.LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            bgStart,
            bgEnd,
            android.graphics.Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Rounded card border
    val borderPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    val cardRect = RectF(60f, 60f, width - 60f, height - 60f)
    canvas.drawRoundRect(cardRect, 48f, 48f, borderPaint)

    var currentY = 160f

    // App Header Brand
    val headerPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        textSize = 42f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        letterSpacing = 0.15f
    }
    canvas.drawText("FREQUENT HABITS", width / 2f, currentY, headerPaint)
    currentY += 160f

    // Profile Pic / Avatar
    val displayName = userName.ifBlank { tr(language, "Gewohnheiten Held", "Habit Hero") }
    val avatarRadius = 110f
    val avatarPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    val avatarBitmap = loadBitmapFromUri(context, profileImageUri)
    if (avatarBitmap != null) {
        drawCircularAvatar(canvas, avatarBitmap, width / 2f, currentY, avatarRadius, avatarPaint)
    } else {
        // Placeholder
        val placeholderPaint = Paint().apply {
            isAntiAlias = true
            color = mixColorWithBlack(primaryInt, 0.35f)
        }
        canvas.drawCircle(width / 2f, currentY, avatarRadius, placeholderPaint)
        canvas.drawCircle(width / 2f, currentY, avatarRadius, avatarPaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.WHITE
            textSize = 100f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val letter = if (displayName.isNotEmpty()) displayName.substring(0, 1).uppercase() else "H"
        val textBounds = android.graphics.Rect()
        textPaint.getTextBounds(letter, 0, 1, textBounds)
        val yOffset = textBounds.height() / 2f - textBounds.bottom
        canvas.drawText(letter, width / 2f, currentY + yOffset, textPaint)
    }
    currentY += 190f

    // User Name
    val namePaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 72f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(displayName, width / 2f, currentY, namePaint)
    currentY += 80f

    // Level Badge
    val levelText = "⚡ LEVEL $userLevel"
    val levelPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        textSize = 42f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText(levelText, width / 2f, currentY, levelPaint)
    currentY += 110f

    // Horizontal Separator
    val sepPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(64, 255, 255, 255)
        strokeWidth = 3f
    }
    canvas.drawLine(150f, currentY, width - 150f, currentY, sepPaint)
    currentY += 110f

    // Stats
    val statPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 46f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val compText = "🏆  $totalCompletions  ${tr(language, "Abschlüsse insgesamt", "Total Completions")}"
    canvas.drawText(compText, width / 2f, currentY, statPaint)
    currentY += 85f

    val habitsText = "📌  $activeHabits  ${tr(language, "Aktive Gewohnheiten", "Active Habits")}"
    canvas.drawText(habitsText, width / 2f, currentY, statPaint)
    currentY += 85f

    val streakText = "🔥  $longestStreak  ${tr(language, "Tage beste Serie", "Days Longest Streak")}"
    canvas.drawText(streakText, width / 2f, currentY, statPaint)

    // Footer Branding
    val footerPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 34f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Frequent Habits App", width / 2f, height - 100f, footerPaint)

    return bitmap
}

fun renderMonthlyReviewShareBitmap(
    context: Context,
    year: Int,
    reviewData: MonthlyReviewData,
    language: String,
    accentColor: Color
): Bitmap {
    val width = 1080
    val height = 1350
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val primaryInt = accentColor.toArgb()
    val bgStart = mixColorWithBlack(primaryInt, 0.15f)
    val bgEnd = mixColorWithBlack(primaryInt, 0.04f)

    val bgPaint = Paint().apply {
        isAntiAlias = true
        shader = android.graphics.LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            bgStart,
            bgEnd,
            android.graphics.Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    val borderPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    val cardRect = RectF(60f, 60f, width - 60f, height - 60f)
    canvas.drawRoundRect(cardRect, 48f, 48f, borderPaint)

    var currentY = 160f

    // Header
    val headerPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        textSize = 42f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        letterSpacing = 0.15f
    }
    canvas.drawText(tr(language, "MONATSRÜCKBLICK", "MONTHLY REVIEW"), width / 2f, currentY, headerPaint)
    currentY += 150f

    // Giant Display Date
    val datePaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 80f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("✨ ${reviewData.monthName.uppercase()} $year ✨", width / 2f, currentY, datePaint)
    currentY += 150f

    // Stats Grid Divider
    val sepPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(64, 255, 255, 255)
        strokeWidth = 3f
    }
    canvas.drawLine(150f, currentY, width - 150f, currentY, sepPaint)
    currentY += 110f

    // Stats rows
    val statPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 46f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val totalText = "🏆  ${reviewData.totalCompletions}  ${tr(language, "Check-ins insgesamt", "Total Check-ins")}"
    canvas.drawText(totalText, width / 2f, currentY, statPaint)
    currentY += 90f

    val deltaSign = if (reviewData.scoreDelta >= 0) "+" else ""
    val strengthText = "📈  Score: ${reviewData.endScore} ($deltaSign${reviewData.scoreDelta})  ${tr(language, "Routine-Stärke", "Routine Strength")}"
    canvas.drawText(strengthText, width / 2f, currentY, statPaint)
    currentY += 90f

    if (reviewData.mvpHabit != null) {
        val mvpText = "👑  ${reviewData.mvpHabit.icon} ${reviewData.mvpHabit.name}  ${tr(language, "Gewohnheits-MVP", "Habit MVP")}"
        canvas.drawText(mvpText, width / 2f, currentY, statPaint)
        currentY += 90f
    }

    val powerText = "⚡  ${reviewData.bestDayOfWeekName}  ${tr(language, "Power-Tag", "Power Day")}"
    canvas.drawText(powerText, width / 2f, currentY, statPaint)

    // Footer Branding
    val footerPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 34f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Frequent Habits App", width / 2f, height - 100f, footerPaint)

    return bitmap
}

fun renderYearlyReviewShareBitmap(
    context: Context,
    year: Int,
    reviewData: YearlyReviewData,
    language: String,
    accentColor: Color
): Bitmap {
    val width = 1080
    val height = 1350
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val primaryInt = accentColor.toArgb()
    val bgStart = mixColorWithBlack(primaryInt, 0.15f)
    val bgEnd = mixColorWithBlack(primaryInt, 0.04f)

    val bgPaint = Paint().apply {
        isAntiAlias = true
        shader = android.graphics.LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            bgStart,
            bgEnd,
            android.graphics.Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    val borderPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    val cardRect = RectF(60f, 60f, width - 60f, height - 60f)
    canvas.drawRoundRect(cardRect, 48f, 48f, borderPaint)

    var currentY = 160f

    // Header
    val headerPaint = Paint().apply {
        isAntiAlias = true
        color = primaryInt
        textSize = 42f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        letterSpacing = 0.15f
    }
    canvas.drawText(tr(language, "JAHRESRÜCKBLICK", "YEAR IN REVIEW"), width / 2f, currentY, headerPaint)
    currentY += 150f

    // Giant Display Date
    val datePaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 80f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("✨ $year ✨", width / 2f, currentY, datePaint)
    currentY += 150f

    // Stats Grid Divider
    val sepPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(64, 255, 255, 255)
        strokeWidth = 3f
    }
    canvas.drawLine(150f, currentY, width - 150f, currentY, sepPaint)
    currentY += 110f

    // Stats rows
    val statPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        textSize = 46f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    val totalText = "🏆  ${reviewData.totalCompletions}  ${tr(language, "Abschlüsse insgesamt", "Total Completions")}"
    canvas.drawText(totalText, width / 2f, currentY, statPaint)
    currentY += 90f

    val activeText = "📅  ${reviewData.activeDaysCount} (${reviewData.activeDaysPercentage}%)  ${tr(language, "Aktive Tage", "Active Days")}"
    canvas.drawText(activeText, width / 2f, currentY, statPaint)
    currentY += 90f

    if (reviewData.topHabit != null) {
        val topText = "👑  ${reviewData.topHabit.icon} ${reviewData.topHabit.name}  ${tr(language, "Top Gewohnheit", "Top Habit")}"
        canvas.drawText(topText, width / 2f, currentY, statPaint)
        currentY += 90f
    }

    val monthText = "🗓️  ${reviewData.bestMonthName}  ${tr(language, "Bester Monat", "Best Month")}"
    canvas.drawText(monthText, width / 2f, currentY, statPaint)
    currentY += 90f

    val streakText = "🔥  ${reviewData.longestStreak} ${tr(language, "Tage Serie", "Days Streak")}  ${tr(language, "Beste Serie", "Best Streak")}"
    canvas.drawText(streakText, width / 2f, currentY, statPaint)

    // Footer Branding
    val footerPaint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.argb(160, 255, 255, 255)
        textSize = 34f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Frequent Habits App", width / 2f, height - 100f, footerPaint)

    return bitmap
}
