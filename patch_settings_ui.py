with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Add state variable
var_old = """    val reviewNotificationsEnabled by viewModel.reviewNotificationsEnabled.collectAsStateWithLifecycle()"""
var_new = """    val reviewNotificationsEnabled by viewModel.reviewNotificationsEnabled.collectAsStateWithLifecycle()
    val insightNotificationsEnabled by viewModel.insightNotificationsEnabled.collectAsStateWithLifecycle()"""
content = content.replace(var_old, var_new)

# Add UI Option
ui_old = """                        HorizontalDivider(color = AppBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Option: Monatsrückblick"""
ui_new = """                        HorizontalDivider(color = AppBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Option: Smart Insights Notifications
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Insights",
                                    tint = PrimaryViolet,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (language == "de") "Smart Insights" else "Smart Insights",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = if (language == "de") "Wöchentliche Push-Nachrichten mit interessanten Statistiken" else "Weekly push notifications with interesting stats",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Switch(
                                checked = insightNotificationsEnabled && hasNotificationPermission,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        if (android.os.Build.VERSION.SDK_INT >= 33 && !hasNotificationPermission) {
                                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            viewModel.setInsightNotificationsEnabled(true)
                                        }
                                    } else {
                                        viewModel.setInsightNotificationsEnabled(false)
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = PrimaryViolet,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = ProgressTrack
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = AppBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Option: Monatsrückblick"""
content = content.replace(ui_old, ui_new)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
