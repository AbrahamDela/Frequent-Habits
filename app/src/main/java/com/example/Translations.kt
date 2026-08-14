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
    str = str.replace("soundscapes & focus audio", "ხმოვანი გარემო და ფოკუსის აუდიო")
    str = str.replace("Soundscapes & Fokus-Audio", "ხმოვანი გარემო და ფოკუსის აუდიო")
    str = str.replace("Support", "მხარდაჭერა")
    str = str.replace("Email", "ელ-ფოსტა")
    str = str.replace("E-Mail", "ელ-ფოსტა")
    return str
}

val GEORGIAN_TRANSLATIONS: Map<String, String> = mapOf(
    // Bottom Navigation & Core terms
    "Profile" to "პროფილი",
    "Profil" to "პროფილი",
    "Stats" to "სტატისტიკა",
    "Statistik" to "სტატისტიკა",
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
    "Import" to "იმპორტი",
    "Import" to "იმპორტი",

    // New additions
    "Support" to "მხარდაჭერა",
    "E-Mail Support" to "ელ-ფოსტით მხარდაჭერა",
    "Email Support" to "ელ-ფოსტით მხარდაჭერა",
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
    "Sunday" to "კვირა",
)
