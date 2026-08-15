import sqlite3
import datetime

# We can't access the app's internal DB from here easily because we are not root.
# But wait, we can just use logcat to see what's happening!
