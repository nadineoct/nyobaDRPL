"""
Design Tokens and App-wide constants for JuKi.
Mapped directly from the provided Figma/HTML specifications.
"""

# Brand Colors
COLOR_PRIMARY       = "#A114AC"     # Navbar Jurnal/Kalendar, Buttons
COLOR_PRIMARY_DARK  = "#8D1395"     # Navbar Beranda
COLOR_ACCENT_YELLOW = "#FFE341"     # Today Highlight, CTA Buttons
COLOR_ACCENT_PALE   = "#FFFAC1"     # Journal Cards, Date Tags
COLOR_ACCENT_GREEN  = "#82DD55"     # Completed Targets
COLOR_STREAK_ORANGE = "#FFA930"     # Streak indicator, Mood accents
COLOR_PURPLE_LIGHT  = "#FAE7FF"     # Calendar Sidebar
COLOR_RED_ALERT     = "#DC2626"     # Delete/Danger
COLOR_BLUE_INFO      = "#2563EB"     # Posting/Info
COLOR_BROWN_ACCENT   = "#74400F"     # Greeting Text

# Text Colors
COLOR_TEXT_PRIMARY   = "#292929"
COLOR_TEXT_SECONDARY = "#434343"
COLOR_TEXT_MUTED     = "#767676"
COLOR_TEXT_DISABLED  = "rgba(0,0,0,0.20)" # Using #CCCCCC for tkinter
COLOR_BORDER         = "#D6D6D6"
COLOR_BG_WHITE       = "#FFFFFF"

# Fonts
FONT_OUTFIT          = "Outfit"
FONT_MONTSERRAT      = "Montserrat"
FONT_PLUS_JAKARTA    = "Plus Jakarta Sans"
FONT_INTER           = "Inter"

# Font Sizes (Mapping px to pt roughly 1:0.75 or 1:1 depending on system DPI)
# We will use the sizes provided in the text style sheets.
SIZE_XL = 75   # Journal Titles (Detail)
SIZE_LG = 50   # Greetings / Streaks
SIZE_MD = 30   # Section Titles
SIZE_SM = 20   # Body Text
SIZE_XS = 15   # Tags / Labels

# Mood Data
MOODS = [
    {"label": "Excited", "score": 5, "emoji": "🤩", "color": "#FFA930"},
    {"label": "Happy",   "score": 4, "emoji": "😊", "color": "#82DD55"},
    {"label": "Neutral", "score": 3, "emoji": "😐", "color": "#D6D6D6"},
    {"label": "Sad",     "score": 2, "emoji": "😢", "color": "#00A3FE"},
    {"label": "Angry",   "score": 1, "emoji": "😠", "color": "#DC2626"}
]
