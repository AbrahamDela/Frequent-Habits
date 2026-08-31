package com.example

fun tr(lang: String, de: String, en: String): String {
    if (lang == "de") return de
    if (lang == "es") return translateDynamicSpanish(en)
    if (lang == "ka") {
        return GEORGIAN_TRANSLATIONS[en]
            ?: GEORGIAN_TRANSLATIONS[de]
            ?: translateDynamicGeorgian(en, de)
    }
    if (lang == "zh") {
        return CHINESE_TRANSLATIONS[en]
            ?: CHINESE_TRANSLATIONS[de]
            ?: translateDynamicChinese(en, de)
    }
    return en
}

fun tr(lang: String, de: String, ka: String, zh: String, en: String): String {
    return when (lang) {
        "de" -> de
        "ka" -> ka
        "zh" -> zh
        "es" -> translateDynamicSpanish(en)
        else -> en
    }
}

private fun translateDynamicSpanish(en: String): String {
    val exact = SPANISH_TRANSLATIONS[en]
    if (exact != null) return exact
    var s = en
    val words = listOf(
        "Habits" to "Hábitos", "Habit" to "Hábito", "Today" to "Hoy", "Yesterday" to "Ayer", "Tomorrow" to "Mañana",
        "Profile" to "Perfil", "Stats" to "Estadísticas", "Statistics" to "Estadísticas", "Settings" to "Ajustes",
        "Achievements" to "Logros", "Achievement" to "Logro", "Progress" to "Progreso", "Streak" to "Racha",
        "Save" to "Guardar", "Cancel" to "Cancelar", "Delete" to "Eliminar", "Edit" to "Editar", "Add" to "Agregar",
        "Done" to "Listo", "Next" to "Siguiente", "Back" to "Atrás", "Language" to "Idioma", "Appearance" to "Apariencia",
        "Notifications" to "Notificaciones", "Reminder" to "Recordatorio", "Reminders" to "Recordatorios", "Daily" to "Diario",
        "Weekly" to "Semanal", "Monthly" to "Mensual", "Yearly" to "Anual", "Calendar" to "Calendario", "History" to "Historial",
        "Goal" to "Meta", "Goals" to "Metas", "Reward" to "Recompensa", "Rewards" to "Recompensas", "Timer" to "Temporizador",
        "Notes" to "Notas", "Note" to "Nota", "Category" to "Categoría", "Name" to "Nombre", "Description" to "Descripción",
        "Support" to "Soporte", "Email" to "Correo", "Search" to "Buscar", "Import" to "Importar", "Export" to "Exportar",
        "Backup" to "Copia de seguridad", "Restore" to "Restaurar", "Active" to "Activo", "Archived" to "Archivado",
        "Complete" to "Completar", "Completed" to "Completado", "Completions" to "Completados", "Days" to "Días", "Day" to "Día"
    )
    for ((a,b) in words) s=s.replace(a,b)
    return s
}

val SPANISH_TRANSLATIONS: Map<String,String> = mapOf(
    "Today" to "Hoy", "Profile" to "Perfil", "Stats" to "Estadísticas", "Habits" to "Hábitos",
    "Habit Hero" to "Héroe de hábitos", "MY PROFILE" to "MI PERFIL", "Total Completions" to "Total completado",
    "Total Check-ins" to "Total de registros", "Total Check-Ins" to "Total de registros", "Active Habits" to "Hábitos activos",
    "Days Longest Streak" to "Días de la racha más larga", "MONTHLY REVIEW" to "RESUMEN MENSUAL", "YEAR IN REVIEW" to "RESUMEN ANUAL",
    "Active Days" to "Días activos", "Top Habit" to "Mejor hábito", "Best Month" to "Mejor mes", "Best Streak" to "Mejor racha",
    "Profile & Account" to "Perfil y cuenta", "Appearance & Language" to "Apariencia e idioma", "Notifications & Reviews" to "Notificaciones y resúmenes",
    "Edit profile picture & name" to "Editar foto de perfil y nombre", "Haptic feedback & Archived habits" to "Vibración y hábitos archivados",
    "Habit updated!" to "¡Hábito actualizado!", "Habit added!" to "¡Hábito agregado!", "Create a habit first to view statistics." to "Crea primero un hábito para ver las estadísticas.",
    "Focus Audio Soundscapes" to "Sonidos para concentrarse", "Select & manage sounds" to "Seleccionar y administrar sonidos", "Search sound..." to "Buscar sonido...",
    "No Sound (Mute)" to "Sin sonido (silencio)", "No matching audio files found." to "No se encontraron archivos de audio.",
    "Import new audio (.mp3, .wav, .m4a)" to "Importar audio nuevo (.mp3, .wav, .m4a)", "Done" to "Listo",
    "Delete" to "Eliminar", "Edit" to "Editar", "Save" to "Guardar", "Cancel" to "Cancelar", "Add" to "Agregar", "Next" to "Siguiente", "Back" to "Atrás",
    "Language" to "Idioma", "Settings" to "Ajustes", "Statistics" to "Estadísticas", "Achievements" to "Logros", "Progress" to "Progreso", "Streak" to "Racha"
)

private fun translateDynamicGeorgian(en: String, de: String): String {
    var str = en
    str = str.replace("Habit updated!", "ჩვევა განახლდა!")
    str = str.replace("Habit added!", "ჩვევა დაემატა!")
    str = str.replace("Delete", "წაშლა")
    str = str.replace("Edit", "რედაქტირება")
    str = str.replace("Save", "შენახვა")
    str = str.replace("Cancel", "გაუქმება")
    str = str.replace("Done", "მზადაა")
    str = str.replace("Add", "დამატება")
    str = str.replace("Next", "შემდეგი")
    str = str.replace("Back", "უკან")
    str = str.replace("Today", "დღეს")
    str = str.replace("Yesterday", "გუშინ")
    str = str.replace("Tomorrow", "ხვალ")
    str = str.replace("Total Completions", "სულ დასრულებები")
    str = str.replace("Total Check-ins", "სულ ჩექინები")
    str = str.replace("Total Check-Ins", "სულ ჩექინები")
    str = str.replace("Active Habits", "აქტიური ჩვევები")
    str = str.replace("Days Longest Streak", "დღეების ყველაზე გრძელი სერია")
    str = str.replace("Streak", "სერია")
    str = str.replace("Progress", "პროგრესი")
    str = str.replace("Language", "ენა")
    str = str.replace("Settings", "პარამეტრები")
    str = str.replace("Statistics", "სტატისტიკა")
    str = str.replace("Achievements", "მიღწევები")
    str = str.replace("Habits", "ჩვევები")
    str = str.replace("soundscapes & focus audio", "ხმოვანი გარემო და ფოკუსის აუდიო")
    str = str.replace("Soundscapes & Fokus-Audio", "ხმოვანი გარემო და ფოკუსის აუდიო")
    str = str.replace("Support", "მხარდაჭერა")
    str = str.replace("Email", "ელ-ფოსტა")
    str = str.replace("E-Mail", "ელ-ფოსტა")
    return str
}

private fun translateDynamicChinese(en: String, de: String): String {
    var str = en
    str = str.replace("Habit updated!", "习惯已更新！")
    str = str.replace("Habit added!", "习惯已添加！")
    str = str.replace("Delete", "删除")
    str = str.replace("Edit", "编辑")
    str = str.replace("Save", "保存")
    str = str.replace("Cancel", "取消")
    str = str.replace("Done", "完成")
    str = str.replace("Add", "添加")
    str = str.replace("Next", "下一步")
    str = str.replace("Back", "返回")
    str = str.replace("Today", "今天")
    str = str.replace("Yesterday", "昨天")
    str = str.replace("Tomorrow", "明天")
    str = str.replace("Total Completions", "累计完成次数")
    str = str.replace("Total Check-ins", "累计打卡次数")
    str = str.replace("Total Check-Ins", "累计打卡次数")
    str = str.replace("Active Habits", "活跃习惯")
    str = str.replace("Days Longest Streak", "历史最高连续天数")
    str = str.replace("Streak", "连续达成")
    str = str.replace("Progress", "进度")
    str = str.replace("Language", "语言")
    str = str.replace("Settings", "设置")
    str = str.replace("Statistics", "数据统计")
    str = str.replace("Achievements", "成就勋章")
    str = str.replace("Habits", "习惯")
    str = str.replace("soundscapes & focus audio", "专注白噪音与背景音频")
    str = str.replace("Soundscapes & Fokus-Audio", "专注白噪音与背景音频")
    str = str.replace("Support", "支持与帮助")
    str = str.replace("Email", "电子邮件")
    str = str.replace("E-Mail", "电子邮件")
    return str
}

val GEORGIAN_TRANSLATIONS: Map<String, String> = mapOf(
    // Bottom Navigation & Core terms
    "Heute" to "დღეს",
    "Today" to "დღეს",
    "Profile" to "პროფილი",
    "Profil" to "პროფილი",
    "Stats" to "სტატისტიკა",
    "Statistik" to "სტატისტიკა",
    "Statistiken" to "სტატისტიკა",

    // Share & Profile Card
    "MEIN PROFIL" to "ჩემი პროფილი",
    "MY PROFILE" to "ჩემი პროფილი",
    "Gewohnheiten Held" to "ჩვევების გმირი",
    "Habit Hero" to "ჩვევების გმირი",
    "Abschlüsse insgesamt" to "სულ დასრულებები",
    "Total Completions" to "სულ დასრულებები",
    "Check-ins insgesamt" to "სულ ჩექინები",
    "Total Check-ins" to "სულ ჩექინები",
    "Total Check-Ins" to "სულ ჩექინები",
    "Aktive Gewohnheiten" to "აქტიური ჩვევები",
    "Active Habits" to "აქტიური ჩვევები",
    "Tage beste Serie" to "დღეების ყველაზე გრძელი სერია",
    "Days Longest Streak" to "დღეების ყველაზე გრძელი სერია",
    "MONATSRÜCKBLICK" to "ყოველთვიური მიმოხილვა",
    "MONTHLY REVIEW" to "ყოველთვიური მიმოხილვა",
    "JAHRESRÜCKBLICK" to "ყოველწლიური მიმოხილვა",
    "YEAR IN REVIEW" to "ყოველწლიური მიმოხილვა",
    "Abschlüsse" to "დასრულებები",
    "Completions" to "დასრულებები",
    "Aktive Tage" to "აქტიური დღეები",
    "Active Days" to "აქტიური დღეები",
    "Top Gewohnheit" to "საუკეთესო ჩვევა",
    "Top Habit" to "საუკეთესო ჩვევა",
    "Top-Gewohnheit" to "საუკეთესო ჩვევა",
    "Bester Monat" to "საუკეთესო თვე",
    "Best Month" to "საუკეთესო თვე",
    "Tage Serie" to "დღეების სერია",
    "Days Streak" to "დღეების სერია",
    "Beste Serie" to "საუკეთესო სერია",
    "Best Streak" to "საუკეთესო სერია",
    "Power Day" to "საუკეთესო დღე",
    "Bester Tag" to "საუკეთესო დღე",
    
    // Settings Categories & Items
    "Edit profile picture & name" to "პროფილის სურათის და სახელის რედაქტირება",
    "Profilbild & Nutzername bearbeiten" to "პროფილის სურათის და სახელის რედაქტირება",
    "Haptic feedback & Archived habits" to "ვიბრაცია და არქივირებული ჩვევები",
    "Haptischer Impuls beim Erledigen" to "მოკლე ვიბრაცია დასრულებისას",
    "SAF Backup, Restore & Danger zone" to "სარეზერვო ასლი, აღდგენა და საფრთხის ზონა",
    "SAF Backup, Wiederherstellen & Gefahrenbereich" to "სარეზერვო ასლი, აღდგენა და საფრთხის ზონა",
    "Profile & Account" to "პროფილი და ანგარიში",
    "Profil & Konto" to "პროფილი და ანგარიში",
    "Appearance & Language" to "იერსახე და ენა",
    "Erscheinungsbild & Sprache" to "იერსახე და ენა",
    "Notifications & Reviews" to "შეტყობინებები და მიმოხილვები",
    "Benachrichtigungen & Rückblicke" to "შეტყობინებები და მიმოხილვები",
    "Audio & Soundscapes" to "ხმები და რელაქსაცია",
    "Töne & Entspannung" to "ხმები და რელაქსაცია",
    "Habits & Archive" to "ჩვევები და არქივი",
    "Gewohnheiten & Archiv" to "ჩვევები და არქივი",
    "Data & Backup" to "მონაცემები და სარეზერვო ასლი",
    "Daten & Sicherung" to "მონაცემები და სარეზერვო ასლი",
    "Language" to "ენა",
    "Sprache" to "ენა",
    "General & Personalization" to "ზოგადი და პერსონალიზაცია",
    "Allgemein & Personalisierung" to "ზოგადი და პერსონალიზაცია",
    "Language, Info Cards & Intro" to "ენა, საინფორმაციო ბარათები და შესავალი",
    "Sprache, Infokarten & Einführung" to "ენა, საინფორმაციო ბარათები და შესავალი",
    "Monthly & Yearly Review settings" to "ყოველთვიური და ყოველწლიური მიმოხილვის პარამეტრები",
    "Monats- & Jahresrückblick Einstellungen" to "ყოველთვიური და ყოველწლიური მიმოხილვის პარამეტრები",
    "Support & About" to "მხარდაჭერა და აპლიკაციის შესახებ",
    "Support & Über die App" to "მხარდაჭერა და აპლიკაციის შესახებ",
    "Settings" to "პარამეტრები",
    "Einstellungen" to "პარამეტრები",

    // Bottom Navigation
    "Habits" to "ჩვევები",
    "Gewohnheiten" to "ჩვევები",
    "Statistics" to "სტატისტიკა",
    "Statistiken" to "სტატისტიკა",
    "Achievements" to "მიღწევები",
    "Erfolge" to "მიღწევები",

    // Common Actions & Buttons
    "Add" to "დამატება",
    "Hinzufügen" to "დამატება",
    "Save" to "შენახვა",
    "Speichern" to "შენახვა",
    "Cancel" to "გაუქმება",
    "Abbrechen" to "გაუქმება",
    "Delete" to "წაშლა",
    "Löschen" to "წაშლა",
    "Edit" to "რედაქტირება",
    "Bearbeiten" to "რედაქტირება",
    "Done" to "მზადაა",
    "Fertig" to "მზადაა",
    "Close" to "დახურვა",
    "Schließen" to "დახურვა",
    "Next" to "შემდეგი",
    "Weiter" to "შემდეგი",
    "Back" to "უკან",
    "Zurück" to "უკან",
    "Confirm" to "დადასტურება",
    "Bestätigen" to "დადასტურება",
    "Redeem" to "გამოყენება",
    "Einlösen" to "გამოყენება",
    "Restore" to "აღდგენა",
    "Wiederherstellen" to "აღდგენა",
    "Export" to "ექსპორტი",
    "Exportieren" to "ექსპორტი",
    "Import" to "იმპორტი",
    "Importieren" to "იმპორტი",

    // Time & Dates
    "Today" to "დღეს",
    "Heute" to "დღეს",
    "Yesterday" to "გუშინ",
    "Gestern" to "გუშინ",
    "Tomorrow" to "ხვალ",
    "Morgen" to "ხვალ",
    "Days" to "დღეები",
    "Tage" to "დღეები",
    "Weeks" to "კვირები",
    "Wochen" to "კვირები",
    "Months" to "თვეები",
    "Monate" to "თვეები",
    "Years" to "წლები",
    "Jahre" to "წლები",

    // Dialogs & Rewards
    "Define Reward" to "ჯილდოს განსაზღვრა",
    "Belohnung definieren" to "ჯილდოს განსაზღვრა",
    "Milestone Rewards" to "ეტაპის ჯილდოები",
    "Meilenstein-Belohnungen" to "ეტაპის ჯილდოები",
    "Reward" to "ჯილდო",
    "Belohnung" to "ჯილდო",
    "Condition:" to "პირობა:",
    "Bedingung:" to "პირობა:",
    "Trophy" to "თასი",
    "Trophäe" to "თასი",
    "Manual" to "ხელით",
    "Manuell" to "ხელით",
    "Target Value" to "მიზნობრივი მნიშვნელობა",
    "Ziel-Wert" to "მიზნობრივი მნიშვნელობა",
    "Habit Trophy:" to "ჩვევის თასი:",
    "Gewohnheitsspezifische Trophäe:" to "ჩვევის თასი:",
    "Streak (Days)" to "სერია (დღეები)",
    "Streak (Tage)" to "სერია (დღეები)",
    "Total (Times)" to "სულ (ჯერ)",
    "Gesamt (Mal)" to "სულ (ჯერ)",

    // Insights & Reviews
    "Smart Insights" to "ჭკვიანი ანალიტიკა",
    "Monthly Review" to "ყოველთვიური მიმოხილვა",
    "Monatsrückblick" to "ყოველთვიური მიმოხილვა",
    "Yearly Review" to "ყოველწლიური მიმოხილვა",
    "Jahresrückblick" to "ყოველწლიური მიმოხილვა",
    "Time Capsule" to "დროის კაფსულა",
    "Zeitkapsel" to "დროის კაფსულა",
    "Focus Timer" to "ფოკუსის ტაიმერი",
    "Fokus-Timer" to "ფოკუსის ტაიმერი",
    "Soundscapes" to "ხმოვანი გარემო",

    // Audio & Player
    "Focus Audio Soundscapes" to "ფოკუსის ხმოვანი გარემო",
    "Fokus-Audio Bibliothek" to "ფოკუსის ხმოვანი გარემო",
    "Select & manage sounds" to "ხმების არჩევა და მართვა",
    "Sound auswählen & verwalten" to "ხმების არჩევა და მართვა",
    "Search sound..." to "ხმის ძებნა...",
    "Sound suchen..." to "ხმის ძებნა...",
    "No Sound (Mute)" to "ხმის გარეშე (უხმო)",
    "Kein Sound (Stumm)" to "ხმის გარეშე (უხმო)",
    "Import new audio (.mp3, .wav, .m4a)" to "ახალი აუდიოს იმპორტი (.mp3, .wav, .m4a)",
    "Neues Audio importieren (.mp3, .wav, .m4a)" to "ახალი აუდიოს იმპორტი (.mp3, .wav, .m4a)",

    // Onboarding & Intro
    "Introduction" to "შესავალი",
    "Einführung" to "შესავალი",
    "Welcome to Everyday Habits" to "კეთილი იყოს თქვენი მობრძანება Everyday Habits-ში",
    "Willkommen bei Everyday Habits" to "კეთილი იყოს თქვენი მობრძანება Everyday Habits-ში",
    "Ready for your journey!" to "მზად ხართ თქვენი მოგზაურობისთვის!",
    "Bereit für deine Reise!" to "მზად ხართ თქვენი მოგზაურობისთვის!",

    // Settings page translations
    "Profil & Konto" to "პროფილი და ანგარიში",
    "Profile & Account" to "პროფილი და ანგარიში",
    "Erscheinungsbild & Sprache" to "იერსახე და ენა",
    "Appearance & Language" to "იერსახე და ენა",
    "Benachrichtigungen & Rückblicke" to "შეტყობინებები და მიმოხილვები",
    "Notifications & Reviews" to "შეტყობინებები და მიმოხილვები",
    "Töne & Entspannung" to "ხმები და რელაქსაცია",
    "Audio & Soundscapes" to "ხმები და რელაქსაცია",
    "Daten, Sicherung & Archiv" to "მონაცემები, სარეზერვო ასლი და არქივი",
    "Data, Backup & Archive" to "მონაცემები, სარეზერვო ასლი და არქივი",
    "Support & Über die App" to "მხარდაჭერა და აპლიკაციის შესახებ",
    "Support & About" to "მხარდაჭერა და აპლიკაციის შესახებ",

    "Profilbild & Nutzername bearbeiten" to "პროფილის სურათის და სახელის რედაქტირება",
    "Edit profile picture & name" to "პროფილის სურათის და სახელის რედაქტირება",
    "Sprache, Infokarten, Haptik & Einführung" to "ენა, საინფორმაციო ბარათები, ჰაპტიკა და შესავალი",
    "Language, Info Cards, Haptics & Intro" to "ენა, საინფორმაციო ბარათები, ჰაპტიკა და შესავალი",
    "Monats- & Jahresrückblick Einstellungen" to "ყოველთვიური და ყოველწლიური მიმოხილვის პარამეტრები",
    "Monthly & Yearly Review settings" to "ყოველთვიური და ყოველწლიური მიმოხილვის პარამეტრები",
    "Sicherung, Wiederherstellung & Archivierte Gewohnheiten" to "სარეზერვო ასლი, აღდგენა და არქივირებული ჩვევები",
    "Backup, Restore & Archived habits" to "სარეზერვო ასლი, აღდგენა და არქივირებული ჩვევები",

    "Profilbild" to "პროფილის სურათი",
    "Profile Picture" to "პროფილის სურათი",
    "Foto ändern" to "ფოტოს შეცვლა",
    "Change Photo" to "ფოტოს შეცვლა",
    "Entfernen" to "წაშლა",
    "Remove" to "წაშლა",
    "Nutzername" to "მომხმარებლის სახელი",
    "Username" to "მომხმარებლის სახელი",
    "Gib deinen Namen ein..." to "შეიყვანეთ თქვენი სახელი...",
    "Enter your name..." to "შეიყვანეთ თქვენი სახელი...",
    "Dein Name und Profilbild werden nur lokal auf deinem Gerät gespeichert und für deine personalisierte App-Erfahrung genutzt." to "თქვენი სახელი და პროფილის სურათი ინახება მხოლოდ თქვენს მოწყობილობაზე პერსონალიზებული გამოცდილებისთვის.",
    "Your name and profile picture are stored strictly locally on your device for your personalized app experience." to "თქვენი სახელი და პროფილის სურათი ინახება მხოლოდ თქვენს მოწყობილობაზე პერსონალიზებული გამოცდილებისთვის.",

    "Akzentfarbe" to "აქცენტის ფერი",
    "Accent Color" to "აქცენტის ფერი",
    "App Design" to "აპლიკაციის თემა",
    "App Theme" to "აპლიკაციის თემა",
    "Dunkel" to "ბნელი",
    "Dark" to "ბნელი",
    "Hell" to "ნათელი",
    "Light" to "ნათელი",
    "Infokarten & Hinweise anzeigen" to "საინფორმაციო ბარათების და რჩევების ჩვენება",
    "Show info cards & tips" to "საინფორმაციო ბარათების და რჩევების ჩვენება",
    "Blendet Erklärungen und Info-Buttons ein" to "განმარტებითი ბარათებისა და საინფორმაციო ხატულების ჩვენება",
    "Display explanatory cards and info icons" to "განმარტებითი ბარათებისა და საინფორმაციო ხატულების ჩვენება",
    "Vibration beim Abhaken" to "ჰაპტიკური გამოხმაურება",
    "Haptic feedback" to "ჰაპტიკური გამოხმაურება",
    "Haptischer Impuls beim Erledigen" to "მოკლე ვიბრაცია დასრულებისას",
    "Short vibration pulse on completion" to "მოკლე ვიბრაცია დასრულებისას",
    "Einführung" to "შესავალი",
    "Introduction" to "შესავალი",
    "Starte die Einführung erneut, um alle App-Tipps zu sehen." to "დაიწყეთ შესავალი თავიდან აპლიკაციის რჩევების სანახავად.",
    "Restart the introduction to review all app tips." to "დაიწყეთ შესავალი თავიდან აპლიკაციის რჩევების სანახავად.",
    "Einführung neu starten" to "შესავლის თავიდან დაწყება",
    "Restart Introduction" to "შესავლის თავიდან დაწყება",

    "Rückblicke & Berichte" to "მიმოხილვები და ანგარიშები",
    "Reviews & Reports" to "მიმოხილვები და ანგარიშები",
    "Benachrichtigungen" to "შეტყობინებები",
    "Notifications" to "შეტყობინებები",
    "Erinnerungen & Rückblick-Alarme erhalten" to "შეტყობინებებისა და მიმოხილვის სიგნალების მიღება",
    "Receive reminders & review alerts" to "შეტყობინებებისა და მიმოხილვის სიგნალების მიღება",
    "1x pro Woche eine Push-Benachrichtigung mit deinen spannendsten Insights & Highlights" to "ყოველკვირეული შეტყობინება თქვენი ყველაზე საინტერესო ანალიტიკითა და ჰაილაითებით",
    "Weekly push notification with your most interesting insights & highlights" to "ყოველკვირეული შეტყობინება თქვენი ყველაზე საინტერესო ანალიტიკითა და ჰაილაითებით",
    "Monatsrückblick" to "ყოველთვიური მიმოხილვა",
    "Monthly Review" to "ყოველთვიური მიმოხილვა",
    "Monatliche Statistiken & Highlights" to "ყოველთვიური სტატისტიკა და ჰაილაითები",
    "Analyze monthly statistics & highlights" to "ყოველთვიური სტატისტიკა და ჰაილაითები",
    "Jahresrückblick" to "ყოველწლიური მიმოხილვა",
    "Yearly Review" to "ყოველწლიური მიმოხილვა",
    "Großer Jahresrückblick mit Abzeichen & Highlights" to "დიდი ყოველწლიური მიმოხილვა სამკერდე ნიშნებითა და ჰაილაითებით",
    "Grand Year in Review with badges & highlights" to "დიდი ყოველწლიური მიმოხილვა სამკერდე ნიშნებითა და ჰაილაითებით",

    "Archivierte Gewohnheiten" to "არქივირებული ჩვევები",
    "Archived Habits" to "არქივირებული ჩვევები",
    "Inaktive Gewohnheiten reaktivieren" to "არააქტიური ჩვევების რეაქტივაცია",
    "Reactivate suspended habits" to "არააქტიური ჩვევების რეაქტივაცია",
    "Habit-Tracker Import (CSV / ZIP)" to "ჩვევების იმპორტი (CSV / ZIP)",
    "Habit Tracker Import (CSV / ZIP)" to "ჩვევების იმპორტი (CSV / ZIP)",
    "Importiere deine Historie direkt aus Loop Habit Tracker, Bull Habit Tracker (HabitBull) oder eigenen CSV/ZIP-Dateien." to "მოახდინეთ ჩვევების იმპორტი პირდაპირ Loop Habit Tracker-იდან, Bull Habit Tracker-იდან ან საკუთარი CSV/ZIP ფაილებიდან.",
    "Import habits and tracking history directly from Loop Habit Tracker, Bull Habit Tracker (HabitBull), or custom CSV/ZIP files." to "მოახდინეთ ჩვევების იმპორტი პირდაპირ Loop Habit Tracker-იდან, Bull Habit Tracker-იდან ან საკუთარი CSV/ZIP ფაილებიდან.",
    "CSV / ZIP Datei importieren" to "CSV / ZIP ფაილის იმპორტი",
    "Import CSV / ZIP File" to "CSV / ZIP ფაილის იმპორტი",
    "Lokales SAF Backup & Restore" to "ლოკალური SAF სარეზერვო ასლი და აღდგენა",
    "Local SAF Backup & Restore" to "ლოკალური SAF სარეზერვო ასლი და აღდგენა",
    "Wähle einen Ordner aus. Die App erstellt dort täglich automatisch ein Backup der letzten 3 Tage. Du kannst auch jederzeit manuell sichern oder wiederherstellen." to "აირჩიეთ ლოკალური საქაღალდე. აპლიკაცია ავტომატურად შეინახავს ყოველდღიურ ექსპორტს (მხოლოდ ბოლო 3-ს).",
    "Select a local folder. The app will automatically save daily JSON exports there (retaining only the 3 latest). You can also back up or restore manually." to "აირჩიეთ ლოკალური საქაღალდე. აპლიკაცია ავტომატურად შეინახავს ყოველდღიურ ექსპორტს (მხოლოდ ბოლო 3-ს).",
    "Ausgewählter Ordner:" to "არჩეული საქაღალდე:",
    "Selected Folder:" to "არჩეული საქაღალდე:",
    "Kein Ordner ausgewählt" to "საქაღალდე არ არის არჩეული",
    "No folder selected" to "საქაღალდე არ არის არჩეული",
    "Ordner auswählen" to "საქაღალდის არჩევა",
    "Select Folder" to "საქაღალდის არჩევა",
    "Sichern" to "რეზერვირება",
    "Backup" to "რეზერვირება",
    "Einspielen" to "აღდგენა",
    "Restore" to "აღდგენა",
    "Gefahrenbereich" to "საფრთხის ზონა",
    "Danger Zone" to "საფრთხის ზონა",
    "Hiermit werden alle Gewohnheiten unwiderruflich gelöscht." to "ეს სამუდამოდ წაშლის ყველა ჩვევას და ისტორიას.",
    "This permanently deletes all habits and tracking history." to "ეს სამუდამოდ წაშლის ყველა ჩვევას და ისტორიას.",
    "Alle Daten löschen" to "ყველა მონაცემის წაშლა",
    "Wipe All Data" to "ყველა მონაცემის წაშლა",

    "Fokus-Sounds & Hintergründe" to "ფოკუსის ხმები და ხმოვანი გარემო",
    "Focus Sounds & Soundscapes" to "ფოკუსის ხმები და ხმოვანი გარემო",
    "Verwalten" to "მართვა",
    "Manage" to "მართვა",

    // New additions
    "Support" to "მხარდაჭერა",
    "E-Mail Support" to "ელ-ფოსტით მხარდაჭერა",
    "Email Support" to "ელ-ფოსტით მხარდაჭერა",
    "E-Mail Kontakt" to "ელ-ფოსტით კონტაქტი",
    "Email Contact" to "ელ-ფოსტით კონტაქტი",
    "soundscapes & focus audio" to "ხმოვანი გარემო და ფოკუსის აუდიო",
    "Soundscapes & Fokus-Audio" to "ხმოვანი გარემო და ფოკუსის აუდიო",
    "Help make the app even better" to "დაგვეხმარეთ აპლიკაციის კიდევ უფრო გაუმჯობესებაში",
    "Hilf mit, die App zu verbessern" to "დაგვეხმარეთ აპლიკაციის კიდევ უფრო გაუმჯობესებაში",
    "Share with a friend" to "მეგობრისთვის გაზიარება",
    "Share with..." to "გაზიარება...",
    "Teilen mit..." to "გაზიარება...",
    "Support via Ko-fi" to "მხარდაჭერა Ko-fi-ს საშუალებით",
    "Unterstützen via Ko-fi" to "მხარდაჭერა Ko-fi-ს საშუალებით",
    "Recommendation: Frequent Habits" to "რეკომენდაცია: Frequent Habits",
    "Empfehlung: Frequent Habits" to "რეკომენდაცია: Frequent Habits",
    "Reward (e.g., New Book)" to "ჯილდო (მაგ., ახალი წიგნი)",
    "Tuesday" to "სამშაბათი",
    "Daily Target" to "ყოველდღიური სამიზნე",
    "Friday" to "პარასკევი",
    "e.g. cups" to "მაგ. ჭიქები",
    "Monday" to "ორშაბათი",
    "Click Increment" to "დააჭირეთ გაზრდას",
    "Thursday" to "ხუთშაბათი",
    "Saturday" to "შაბათი",
    "Wednesday" to "ოთხშაბათი",
    "Custom Unit" to "საბაჟო ერთეული",
    "Sunday" to "კვირა"
)

val CHINESE_TRANSLATIONS: Map<String, String> = mapOf(
    // Bottom Navigation & Core terms
    "Heute" to "今天",
    "Today" to "今天",
    "Profile" to "我的",
    "Profil" to "我的",
    "Stats" to "统计",
    "Statistik" to "统计",
    "Statistiken" to "统计",
    "Habits" to "习惯",
    "Gewohnheiten" to "习惯",
    "Achievements" to "成就",
    "Erfolge" to "成就",
    "Settings" to "设置",
    "Einstellungen" to "设置",

    // Share & Profile Card
    "MEIN PROFIL" to "我的主页",
    "MY PROFILE" to "我的主页",
    "Gewohnheiten Held" to "习惯达人",
    "Habit Hero" to "习惯达人",
    "Abschlüsse insgesamt" to "累计完成",
    "Total Completions" to "累计完成",
    "Check-ins insgesamt" to "累计打卡",
    "Total Check-ins" to "累计打卡",
    "Total Check-Ins" to "累计打卡",
    "Aktive Gewohnheiten" to "活跃习惯",
    "Active Habits" to "活跃习惯",
    "Tage beste Serie" to "最高连续天数",
    "Days Longest Streak" to "最高连续天数",
    "MONATSRÜCKBLICK" to "月度回顾",
    "MONTHLY REVIEW" to "月度回顾",
    "JAHRESRÜCKBLICK" to "年度回顾",
    "YEAR IN REVIEW" to "年度回顾",
    "Abschlüsse" to "完成次数",
    "Completions" to "完成次数",
    "Aktive Tage" to "活跃天数",
    "Active Days" to "活跃天数",
    "Top Gewohnheit" to "最佳习惯",
    "Top Habit" to "最佳习惯",
    "Top-Gewohnheit" to "最佳习惯",
    "Bester Monat" to "最佳月份",
    "Best Month" to "最佳月份",
    "Tage Serie" to "天连续",
    "Days Streak" to "天连续",
    "Beste Serie" to "最高连续",
    "Best Streak" to "最高连续",
    "Power Day" to "最高效日",
    "Bester Tag" to "最佳单日",

    // Settings Categories & Items
    "Edit profile picture & name" to "编辑头像与用户名",
    "Profilbild & Nutzername bearbeiten" to "编辑头像与用户名",
    "Haptic feedback & Archived habits" to "触觉震动与归档习惯",
    "Haptischer Impuls beim Erledigen" to "完成打卡时震动反馈",
    "SAF Backup, Restore & Danger zone" to "SAF 备份、恢复与危险区域",
    "SAF Backup, Wiederherstellen & Gefahrenbereich" to "SAF 备份、恢复与危险区域",
    "Profile & Account" to "个人与账户",
    "Profil & Konto" to "个人与账户",
    "Appearance & Language" to "外观与语言",
    "Erscheinungsbild & Sprache" to "外观与语言",
    "Notifications & Reviews" to "通知与回顾",
    "Benachrichtigungen & Rückblicke" to "通知与回顾",
    "Audio & Soundscapes" to "声音与白噪音",
    "Töne & Entspannung" to "声音与白噪音",
    "Habits & Archive" to "习惯与归档",
    "Gewohnheiten & Archiv" to "习惯与归档",
    "Data & Backup" to "数据与备份",
    "Daten & Sicherung" to "数据与备份",
    "Language" to "语言",
    "Sprache" to "语言",
    "General & Personalization" to "常规与个性化",
    "Allgemein & Personalisierung" to "常规与个性化",
    "Language, Info Cards & Intro" to "语言、提示卡片与简介",
    "Sprache, Infokarten & Einführung" to "语言、提示卡片与简介",
    "Language, Info Cards, Haptics & Intro" to "语言、提示卡片、触觉与引导",
    "Sprache, Infokarten, Haptik & Einführung" to "语言、提示卡片、触觉与引导",
    "Monthly & Yearly Review settings" to "月度与年度回顾设置",
    "Monats- & Jahresrückblick Einstellungen" to "月度与年度回顾设置",
    "Support & About" to "支持与关于",
    "Support & Über die App" to "支持与关于",
    "Daten, Sicherung & Archiv" to "数据、备份与归档",
    "Data, Backup & Archive" to "数据、备份与归档",
    "Sicherung, Wiederherstellung & Archivierte Gewohnheiten" to "备份、恢复与归档习惯",
    "Backup, Restore & Archived habits" to "备份、恢复与归档习惯",

    // Common Actions & Buttons
    "Add" to "添加",
    "Hinzufügen" to "添加",
    "Save" to "保存",
    "Speichern" to "保存",
    "Cancel" to "取消",
    "Abbrechen" to "取消",
    "Delete" to "删除",
    "Löschen" to "删除",
    "Edit" to "编辑",
    "Bearbeiten" to "编辑",
    "Done" to "完成",
    "Fertig" to "完成",
    "Close" to "关闭",
    "Schließen" to "关闭",
    "Next" to "下一步",
    "Weiter" to "下一步",
    "Back" to "返回",
    "Zurück" to "返回",
    "Confirm" to "确认",
    "Bestätigen" to "确认",
    "Redeem" to "兑换",
    "Einlösen" to "兑换",
    "Restore" to "恢复",
    "Wiederherstellen" to "恢复",
    "Export" to "导出",
    "Exportieren" to "导出",
    "Import" to "导入",
    "Importieren" to "导入",
    "Manage" to "管理",
    "Verwalten" to "管理",

    // Time & Dates
    "Yesterday" to "昨天",
    "Gestern" to "昨天",
    "Tomorrow" to "明天",
    "Morgen" to "明天",
    "Days" to "天",
    "Tage" to "天",
    "Weeks" to "周",
    "Wochen" to "周",
    "Months" to "月",
    "Monate" to "月",
    "Years" to "年",
    "Jahre" to "年",
    "Monday" to "周一",
    "Tuesday" to "周二",
    "Wednesday" to "周三",
    "Thursday" to "周四",
    "Friday" to "周五",
    "Saturday" to "周六",
    "Sunday" to "周日",

    // Dialogs & Rewards
    "Define Reward" to "设定奖励",
    "Belohnung definieren" to "设定奖励",
    "Milestone Rewards" to "里程碑奖励",
    "Meilenstein-Belohnungen" to "里程碑奖励",
    "Reward" to "奖励",
    "Belohnung" to "奖励",
    "Condition:" to "解锁条件：",
    "Bedingung:" to "解锁条件：",
    "Trophy" to "奖杯",
    "Trophäe" to "奖杯",
    "Manual" to "自定义数值",
    "Manuell" to "自定义数值",
    "Target Value" to "目标数值",
    "Ziel-Wert" to "目标数值",
    "Habit Trophy:" to "习惯关联奖杯：",
    "Gewohnheitsspezifische Trophäe:" to "习惯关联奖杯：",
    "Streak (Days)" to "连续天数",
    "Streak (Tage)" to "连续天数",
    "Total (Times)" to "累计次数",
    "Gesamt (Mal)" to "累计次数",
    "Reward (e.g., New Book)" to "奖励（例如：买一本新书）",

    // Insights & Reviews
    "Smart Insights" to "智能洞察",
    "Monthly Review" to "月度回顾",
    "Monatsrückblick" to "月度回顾",
    "Yearly Review" to "年度回顾",
    "Jahresrückblick" to "年度回顾",
    "Time Capsule" to "时间胶囊",
    "Zeitkapsel" to "时间胶囊",
    "Focus Timer" to "专注计时器",
    "Fokus-Timer" to "专注计时器",
    "Soundscapes" to "专注白噪音",
    "Focus Audio Soundscapes" to "专注背景白噪音",
    "Fokus-Audio Bibliothek" to "专注背景白噪音",
    "Select & manage sounds" to "选择并管理声音",
    "Sound auswählen & verwalten" to "选择并管理声音",
    "Search sound..." to "搜索声音...",
    "Sound suchen..." to "搜索声音...",
    "No Sound (Mute)" to "无声音（静音）",
    "Kein Sound (Stumm)" to "无声音（静音）",
    "Import new audio (.mp3, .wav, .m4a)" to "导入新音频 (.mp3, .wav, .m4a)",
    "Neues Audio importieren (.mp3, .wav, .m4a)" to "导入新音频 (.mp3, .wav, .m4a)",

    // Onboarding & Intro
    "Introduction" to "使用介绍",
    "Einführung" to "使用介绍",
    "Welcome to Everyday Habits" to "欢迎使用 Everyday Habits",
    "Willkommen bei Everyday Habits" to "欢迎使用 Everyday Habits",
    "Ready for your journey!" to "准备开启你的习惯之旅！",
    "Bereit für deine Reise!" to "准备开启你的习惯之旅！",

    // Profile & Settings
    "Profilbild" to "头像",
    "Profile Picture" to "头像",
    "Foto ändern" to "更换头像",
    "Change Photo" to "更换头像",
    "Entfernen" to "移除",
    "Remove" to "移除",
    "Nutzername" to "用户名",
    "Username" to "用户名",
    "Gib deinen Namen ein..." to "输入你的名字...",
    "Enter your name..." to "输入你的名字...",
    "Dein Name und Profilbild werden nur lokal auf deinem Gerät gespeichert und für deine personalisierte App-Erfahrung genutzt." to "你的姓名和头像仅保存在本地设备上，用于提供个性化的应用体验。",
    "Your name and profile picture are stored strictly locally on your device for your personalized app experience." to "你的姓名和头像仅保存在本地设备上，用于提供个性化的应用体验。",

    "Akzentfarbe" to "强调配色",
    "Accent Color" to "强调配色",
    "App Design" to "应用主题",
    "App Theme" to "应用主题",
    "Dunkel" to "深色模式",
    "Dark" to "深色模式",
    "Hell" to "浅色模式",
    "Light" to "浅色模式",
    "Infokarten & Hinweise anzeigen" to "显示提示卡片与说明",
    "Show info cards & tips" to "显示提示卡片与说明",
    "Blendet Erklärungen und Info-Buttons ein" to "在页面上显示帮助说明与提示按钮",
    "Display explanatory cards and info icons" to "在页面上显示帮助说明与提示按钮",
    "Vibration beim Abhaken" to "打卡触觉反馈",
    "Haptic feedback" to "打卡触觉反馈",
    "Short vibration pulse on completion" to "完成打卡时触发轻微震动",
    "Starte die Einführung erneut, um alle App-Tipps zu sehen." to "重新开启功能引导，查看全部使用技巧。",
    "Restart the introduction to review all app tips." to "重新开启功能引导，查看全部使用技巧。",
    "Einführung neu starten" to "重新启动介绍引导",
    "Restart Introduction" to "重新启动介绍引导",

    "Rückblicke & Berichte" to "回顾与统计报告",
    "Reviews & Reports" to "回顾与统计报告",
    "Benachrichtigungen" to "通知提醒",
    "Notifications" to "通知提醒",
    "Erinnerungen & Rückblick-Alarme erhalten" to "接收习惯提醒与回顾通知",
    "Receive reminders & review alerts" to "接收习惯提醒与回顾通知",
    "1x pro Woche eine Push-Benachrichtigung mit deinen spannendsten Insights & Highlights" to "每周推送一次精选的习惯洞察与数据亮点",
    "Weekly push notification with your most interesting insights & highlights" to "每周推送一次精选的习惯洞察与数据亮点",
    "Monatliche Statistiken & Highlights" to "月度习惯统计与高光时刻",
    "Analyze monthly statistics & highlights" to "月度习惯统计与高光时刻",
    "Großer Jahresrückblick mit Abzeichen & Highlights" to "年度大回顾：勋章成就与年度高光总结",
    "Grand Year in Review with badges & highlights" to "年度大回顾：勋章成就与年度高光总结",

    "Archivierte Gewohnheiten" to "已归档的习惯",
    "Archived Habits" to "已归档的习惯",
    "Inaktive Gewohnheiten reaktivieren" to "重新启用已归档的习惯",
    "Reactivate suspended habits" to "重新启用已归档的习惯",
    "Habit-Tracker Import (CSV / ZIP)" to "习惯数据导入 (CSV / ZIP)",
    "Habit Tracker Import (CSV / ZIP)" to "习惯数据导入 (CSV / ZIP)",
    "Importiere deine Historie direkt aus Loop Habit Tracker, Bull Habit Tracker (HabitBull) oder eigenen CSV/ZIP-Dateien." to "直接从 Loop Habit Tracker、HabitBull 或自定义 CSV/ZIP 文件导入你的习惯历史。",
    "Import habits and tracking history directly from Loop Habit Tracker, Bull Habit Tracker (HabitBull), or custom CSV/ZIP files." to "直接从 Loop Habit Tracker、HabitBull 或自定义 CSV/ZIP 文件导入你的习惯历史。",
    "CSV / ZIP Datei importieren" to "导入 CSV / ZIP 文件",
    "Import CSV / ZIP File" to "导入 CSV / ZIP 文件",
    "Lokales SAF Backup & Restore" to "本地 SAF 自动备份与恢复",
    "Local SAF Backup & Restore" to "本地 SAF 自动备份与恢复",
    "Wähle einen Ordner aus. Die App erstellt dort täglich automatisch ein Backup der letzten 3 Tage. Du kannst auch jederzeit manuell sichern oder wiederherstellen." to "选择一个本地文件夹。应用每天将自动保存最近 3 天的 JSON 备份。你也可以随时手动备份或恢复。",
    "Select a local folder. The app will automatically save daily JSON exports there (retaining only the 3 latest). You can also back up or restore manually." to "选择一个本地文件夹。应用每天将自动保存最近 3 天的 JSON 备份。你也可以随时手动备份或恢复。",
    "Ausgewählter Ordner:" to "已选文件夹：",
    "Selected Folder:" to "已选文件夹：",
    "Kein Ordner ausgewählt" to "未选择文件夹",
    "No folder selected" to "未选择文件夹",
    "Ordner auswählen" to "选择文件夹",
    "Select Folder" to "选择文件夹",
    "Sichern" to "立即备份",
    "Backup" to "立即备份",
    "Einspielen" to "恢复数据",
    "Gefahrenbereich" to "危险区域",
    "Danger Zone" to "危险区域",
    "Hiermit werden alle Gewohnheiten unwiderruflich gelöscht." to "这将永久清除所有习惯与历史打卡记录，不可撤销。",
    "This permanently deletes all habits and tracking history." to "这将永久清除所有习惯与历史打卡记录，不可撤销。",
    "Alle Daten löschen" to "清空所有数据",
    "Wipe All Data" to "清空所有数据",

    "Fokus-Sounds & Hintergründe" to "专注声音与背景音效",
    "Focus Sounds & Soundscapes" to "专注声音与背景音效",

    // More common phrases
    "Support" to "支持与帮助",
    "E-Mail Support" to "邮件支持",
    "Email Support" to "邮件支持",
    "E-Mail Kontakt" to "邮件联系",
    "Email Contact" to "邮件联系",
    "soundscapes & focus audio" to "专注白噪音与背景音频",
    "Soundscapes & Fokus-Audio" to "专注白噪音与背景音频",
    "Help make the app even better" to "帮助我们做得更好",
    "Hilf mit, die App zu verbessern" to "帮助我们做得更好",
    "Share with a friend" to "推荐给好友",
    "Share with..." to "分享至...",
    "Teilen mit..." to "分享至...",
    "Support via Ko-fi" to "通过 Ko-fi 赞助支持",
    "Unterstützen via Ko-fi" to "通过 Ko-fi 赞助支持",
    "Recommendation: Frequent Habits" to "推荐：Frequent Habits 习惯助手",
    "Empfehlung: Frequent Habits" to "推荐：Frequent Habits 习惯助手",
    "Daily Target" to "每日目标",
    "e.g. cups" to "例如：杯",
    "Click Increment" to "单次点击增量",
    "Custom Unit" to "自定义单位"
)
