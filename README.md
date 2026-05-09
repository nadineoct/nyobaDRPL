# JuKi — Daily Journal

JuKi is a professional Python desktop application for daily journaling, self-care tracking, and mood visualization.

## Features
- **Daily Journaling:** Record your thoughts, triggers, and targets.
- **Mood Tracking:** Visualize your emotional trends over time with interactive charts.
- **Self-care Management:** Set and track your daily self-care goals.
- **Calendar View:** Review your journal history and self-care completions in a monthly grid.
- **Search & Filter:** Easily find specific entries by keyword, category, or date.
- **Privacy First:** Data is stored locally on your machine.

## Tech Stack
- **Language:** Python 3.10+
- **GUI:** CustomTkinter
- **Database:** SQLite3
- **Visualization:** Matplotlib
- **Image Processing:** Pillow

## Installation
1. Clone the repository.
2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the application:
   ```bash
   python run.py
   ```

## Project Structure
- `src/models/`: Domain data classes.
- `src/controllers/`: Business logic and database interaction.
- `src/views/`: UI components and page layouts.
- `src/database/`: SQLite schema and connection manager.
- `src/utils/`: Path management, configurations, and constants.

## Data Locations (Windows)
- **Database & Photos:** `%LocalAppData%/JuKi/`
- **Config:** `%AppData%/JuKi/`

## License
MIT
