with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_block1 = """                        activeDays++
                        val log = logsMap[dateStr]
                        val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)
                        if (status == "SUCCESS") {
                            completions++
                        }
                    }"""

new_block1 = """                        val log = logsMap[dateStr]
                        val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)
                        if (status == "PENDING" && dateStr == todayStr) {
                            // Do not penalize if today is pending
                        } else {
                            activeDays++
                            if (status == "SUCCESS") {
                                completions++
                            }
                        }
                    }"""
content = content.replace(old_block1, new_block1)

old_block2 = """                            activeDays++
                            val log = logsMap[habit.id to dateStr]?.firstOrNull()
                            val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)
                            if (status == "SUCCESS") {
                                completions++
                            }
                        }"""

new_block2 = """                            val log = logsMap[habit.id to dateStr]?.firstOrNull()
                            val status = getLogStatus(habit, log, dateStr, startSdfStr, todayStr)
                            if (status == "PENDING" && dateStr == todayStr) {
                                // Do not penalize if today is pending
                            } else {
                                activeDays++
                                if (status == "SUCCESS") {
                                    completions++
                                }
                            }
                        }"""
content = content.replace(old_block2, new_block2)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

