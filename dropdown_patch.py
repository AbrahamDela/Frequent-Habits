import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Replace filteredList declaration
old_filtered = """                                    val filteredList = remember(importedAudios, audioSearchQuery) {
                                        importedAudios.filter { audioSearchQuery.isBlank() || it.name.contains(audioSearchQuery, ignoreCase = true) }
                                    }"""

new_filtered = """                                    val filteredList = remember(importedAudios, audioSearchQuery) {
                                        importedAudios.filter { audioSearchQuery.isBlank() || it.name.contains(audioSearchQuery, ignoreCase = true) }
                                    }
                                    val dropdownRecentFiles = remember(recentFiles, audioSearchQuery) {
                                        if (audioSearchQuery.isBlank()) recentFiles else emptyList()
                                    }
                                    val dropdownOtherFiles = remember(filteredList, dropdownRecentFiles) {
                                        filteredList.filter { it !in dropdownRecentFiles }
                                    }"""
content = content.replace(old_filtered, new_filtered)

# Replace LazyColumn body
old_lazy = """                                            LazyColumn(
                                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.heightIn(max = 160.dp)
                                            ) {
                                                items(filteredList) { file ->
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
                                                            isAudioPickerExpanded = false
                                                        },
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = if (isSel) PrimaryViolet.copy(alpha = 0.2f) else AppCard,
                                                        border = BorderStroke(0.5.dp, if (isSel) PrimaryViolet else AppBorder),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = PrimaryViolet, modifier = Modifier.size(16.dp))
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Text(
                                                                    text = file.nameWithoutExtension,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = TextPrimary,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                                    maxLines = 1,
                                                                    overflow = TextOverflow.Ellipsis
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }"""

new_lazy = """                                            LazyColumn(
                                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.heightIn(max = 160.dp)
                                            ) {
                                                val renderItem = @Composable { file: java.io.File, isRecent: Boolean ->
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
                                                            isAudioPickerExpanded = false
                                                        },
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = if (isSel) PrimaryViolet.copy(alpha = 0.2f) else AppCard,
                                                        border = BorderStroke(0.5.dp, if (isSel) PrimaryViolet else AppBorder),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                                Icon(if (isRecent) Icons.Default.History else Icons.Default.MusicNote, contentDescription = null, tint = PrimaryViolet, modifier = Modifier.size(16.dp))
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Text(
                                                                    text = file.nameWithoutExtension,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = TextPrimary,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                                    maxLines = 1,
                                                                    overflow = TextOverflow.Ellipsis
                                                                )
                                                            }
                                                            if (isRecent) {
                                                                Text(
                                                                    text = if (language == "de") "Recent" else "Recent",
                                                                    style = MaterialTheme.typography.labelSmall,
                                                                    color = PrimaryViolet.copy(alpha = 0.8f),
                                                                    fontSize = 10.sp
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                if (dropdownRecentFiles.isNotEmpty()) {
                                                    item {
                                                        Text(
                                                            text = if (language == "de") "Zuletzt genutzt" else "Recent",
                                                            color = TextSecondary,
                                                            fontSize = 11.sp,
                                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                    items(dropdownRecentFiles) { file ->
                                                        renderItem(file, true)
                                                    }
                                                    if (dropdownOtherFiles.isNotEmpty()) {
                                                        item {
                                                            Divider(color = AppBorder, modifier = Modifier.padding(vertical = 4.dp))
                                                        }
                                                    }
                                                }

                                                items(dropdownOtherFiles) { file ->
                                                    renderItem(file, false)
                                                }
                                            }"""

content = content.replace(old_lazy, new_lazy)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
print("Dropdown patched")
