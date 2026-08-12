package com.example

fun tr(lang: String, de: String, en: String): String {
    if (lang == "de") return de
    if (lang == "ka") {
        return GEORGIAN_TRANSLATIONS[en]
            ?: GEORGIAN_TRANSLATIONS[de]
            ?: translateDynamicGeorgian(en, de)
    }
    return en
}

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
    str = str.replace("Streak", "სერია")
    str = str.replace("Progress", "პროგრესი")
    str = str.replace("Language", "ენა")
    str = str.replace("Settings", "პარამეტრები")
    str = str.replace("Statistics", "სტატისტიკა")
    str = str.replace("Achievements", "მიღწევები")
    str = str.replace("Habits", "ჩვევები")
    return str
}

val GEORGIAN_TRANSLATIONS: Map<String, String> = mapOf(
    // Settings Categories & Items
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
    "Bereit für deine Reise!" to "მზად ხართ თქვენი მოგზაურობისთვის!"
)
