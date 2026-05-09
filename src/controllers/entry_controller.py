from ..models.journal_entry import JournalEntry
from ..models.photo import Photo
from ..database.database_manager import DatabaseManager

class EntryController:
    """
    Handles business logic for journal entries.
    """
    def __init__(self, db_manager: DatabaseManager):
        self.__db = db_manager
        self.__journal = []

    def get_all_entries(self):
        query = "SELECT * FROM journal_entries ORDER BY date DESC, time DESC"
        rows = self.__db.execute_query(query)
        self.__journal = [self._row_to_entry(row) for row in rows]
        return self.__journal

    def get_entry_detail(self, entry_id: int):
        query = "SELECT * FROM journal_entries WHERE id = ?"
        rows = self.__db.execute_query(query, (entry_id,))
        if rows:
            return self._row_to_entry(rows[0])
        return None

    def is_data_empty(self) -> bool:
        query = "SELECT COUNT(*) FROM journal_entries"
        result = self.__db.execute_query(query)
        return result[0][0] == 0

    def add_entry(self, entry: JournalEntry, user_id: int = 1):
        photo_id = None
        temp_photo = entry.get_photo()
        
        # Step 1: Insert entry first to get the entry_id
        query = """
            INSERT INTO journal_entries (user_id, category, title, description, trigger, target, mood, mood_score, date, time)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
        params = (
            user_id,
            entry.get_category(),
            entry.getTitle(),
            entry.getDescription(),
            entry.getTrigger(),
            entry.getTarget(),
            entry.getMood(),
            entry.getMood_score(),
            entry.getDate(),
            entry.getTime()
        )
        entry_id = self.__db.execute_query(query, params, commit=True)

        # Step 2: Handle photo if exists
        if temp_photo and temp_photo.get_file_path():
            from datetime import datetime
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            
            # Copy file to AppData
            if temp_photo.upload_photo(temp_photo.get_file_path(), entry_id, timestamp):
                # Insert photo record
                photo_query = "INSERT INTO photos (file_path, entry_id) VALUES (?, ?)"
                photo_id = self.__db.execute_query(photo_query, (temp_photo.get_file_path(), entry_id), commit=True)
                
                # Update journal entry with photo_id
                self.__db.execute_query("UPDATE journal_entries SET photo_id = ? WHERE id = ?", (photo_id, entry_id), commit=True)
                return entry_id
        return entry_id

    def delete_entry(self, entry_id: int):
        # Get photo first to delete file
        try:
            entry = self.get_entry_detail(entry_id)
            if entry and entry.get_photo():
                entry.get_photo().delete_photo()
                self.__db.execute_query("DELETE FROM photos WHERE entry_id = ?", (entry_id,), commit=True)

            self.__db.execute_query("DELETE FROM journal_entries WHERE id = ?", (entry_id,), commit=True)
            return True
        except Exception:
            return False

    def update_entry(self, entry: JournalEntry):
        query = """
            UPDATE journal_entries 
            SET category = ?, title = ?, description = ?, trigger = ?, target = ?, mood = ?, mood_score = ?, date = ?, time = ?, updated_at = datetime('now')
            WHERE id = ?
        """
        params = (
            entry.get_category(),
            entry.getTitle(),
            entry.getDescription(),
            entry.getTrigger(),
            entry.getTarget(),
            entry.getMood(),
            entry.getMood_score(),
            entry.getDate(),
            entry.getTime(),
            entry.get_id()
        )
        self.__db.execute_query(query, params, commit=True)

    def cancel_entry(self, entry_id: int):
        pass

    def get_current_streak(self) -> int:
        """Calculates the current consecutive days with at least one entry."""
        query = "SELECT DISTINCT date FROM journal_entries ORDER BY date DESC"
        rows = self.__db.execute_query(query)
        if not rows:
            return 0
        
        from datetime import datetime, timedelta
        dates = [datetime.strptime(row['date'], "%Y-%m-%d").date() for row in rows]
        
        streak = 0
        current_date = datetime.now().date()
        
        # Check if the latest entry was today or yesterday to continue streak
        if dates[0] < current_date - timedelta(days=1):
            return 0
            
        for i in range(len(dates)):
            expected_date = dates[0] - timedelta(days=i)
            if dates[i] == expected_date:
                streak += 1
            else:
                break
        return streak

    # --- Self-care Targets ---

    def get_daily_targets(self, user_id: int, target_date: str):
        """Returns all targets and their completion status for a specific date."""
        query = """
            SELECT t.id, t.label, COALESCE(c.completed, 0) as completed
            FROM self_care_targets t
            LEFT JOIN self_care_completions c ON t.id = c.target_id AND c.date = ?
            WHERE t.user_id = ? AND t.is_active = 1
        """
        return self.__db.execute_query(query, (target_date, user_id))

    def toggle_target_completion(self, user_id: int, target_id: int, target_date: str, completed: bool):
        """Inserts or updates completion status for a target."""
        val = 1 if completed else 0
        query = """
            INSERT INTO self_care_completions (target_id, user_id, date, completed)
            VALUES (?, ?, ?, ?)
            ON CONFLICT(target_id, date) DO UPDATE SET completed = excluded.completed
        """
        self.__db.execute_query(query, (target_id, user_id, target_date, val), commit=True)

    def add_self_care_target(self, user_id: int, label: str):
        query = "INSERT INTO self_care_targets (user_id, label) VALUES (?, ?)"
        self.__db.execute_query(query, (user_id, label), commit=True)

    def get_month_moods(self, year_month: str):
        """Returns a dict mapping date string to mood label for a specific month (YYYY-MM)."""
        query = "SELECT date, mood FROM journal_entries WHERE date LIKE ?"
        rows = self.__db.execute_query(query, (f"{year_month}-%",))
        return {row['date']: row['mood'] for row in rows}

    def _row_to_entry(self, row):
        photo = None
        if row['photo_id']:
            photo_query = "SELECT * FROM photos WHERE id = ?"
            photo_row = self.__db.execute_query(photo_query, (row['photo_id'],))
            if photo_row:
                photo = Photo(photo_row[0]['id'], photo_row[0]['file_path'])

        return JournalEntry(
            row['id'], row['category'], row['title'], row['description'],
            row['trigger'], row['target'], row['mood'], row['mood_score'],
            row['date'], row['time'], photo
        )
