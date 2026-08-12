import re

with open("app/src/main/AndroidManifest.xml", "r") as f:
    content = f.read()

if "SmartInsightNotificationReceiver" not in content:
    content = content.replace("</application>", "    <receiver android:name=\".SmartInsightNotificationReceiver\" android:exported=\"false\" />\n    </application>")
    with open("app/src/main/AndroidManifest.xml", "w") as f:
        f.write(content)
