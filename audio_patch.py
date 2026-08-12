import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Remove the TOP ROW block
old_top_row = """                                // 1. TOP ROW: 3 RECENT SOUNDS BUTTONS WITH LABEL
                                val recentFileNames = remember(importedAudios, selectedAudioFile) {
                                    AudioSoundscapeManager.getRecentAudios(context)
                                }
                                val recentFiles = remember(recentFileNames, importedAudios) {
                                    val found = recentFileNames.mapNotNull { name -> importedAudios.find { it.name == name } }
                                    if (found.isNotEmpty()) found.take(3)
                                    else importedAudios.take(3)
                                }

                                if (recentFiles.isNotEmpty()) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = if (language == "de") "Zuletzt genutzt:" else "Recent:",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            recentFiles.forEach { file ->
                                                val isSel = selectedAudioFile?.absolutePath == file.absolutePath
                                                Surface(
                                                    onClick = {
                                                        selectedAudioFile = file
                                                        AudioSoundscapeManager.setLastSelectedAudio(context, file.name)
                                                        if (isTimerRunning) {
                                                            try {
                                                                mediaPlayer?.stop()
                                                                mediaPlayer?.release()
                                                                mediaPlayer = MediaPlayer().apply {
                                                                    setDataSource(file.absolutePath)
                                                                    isLooping = true
                                                                    prepare()
                                                                    start()
                                                                }
                                                            } catch (e: Exception) { e.printStackTrace() }
                                                        }
                                                    },
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = if (isSel) PrimaryViolet.copy(alpha = 0.25f) else AppCard,
                                                    border = BorderStroke(1.dp, if (isSel) PrimaryViolet else AppBorder),
                                                    modifier = Modifier.weight(1f).height(36.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.MusicNote,
                                                            contentDescription = null,
                                                            tint = if (isSel) PrimaryViolet else TextSecondary,
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = file.nameWithoutExtension,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = if (isSel) TextPrimary else TextSecondary,
                                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            fontSize = 11.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // 2. SEARCH BAR, CLEAR & UPLOAD BUTTONS"""

new_top_row = """                                val recentFileNames = remember(importedAudios, selectedAudioFile) {
                                    AudioSoundscapeManager.getRecentAudios(context)
                                }
                                val recentFiles = remember(recentFileNames, importedAudios) {
                                    val found = recentFileNames.mapNotNull { name -> importedAudios.find { it.name == name } }
                                    if (found.isNotEmpty()) found.take(3)
                                    else importedAudios.take(3)
                                }
                                // 2. SEARCH BAR, CLEAR & UPLOAD BUTTONS"""

content = content.replace(old_top_row, new_top_row)
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
