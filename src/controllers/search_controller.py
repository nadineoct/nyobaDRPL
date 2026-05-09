from ..database.database_manager import DatabaseManager
from ..filters.search_filter import SearchFilter
from ..models.journal_entry import JournalEntry
from ..models.photo import Photo

class SearchController:
    """
    Handles searching and filtering of journal entries.
    """
    def __init__(self, db_manager: DatabaseManager):
        self.__db = db_manager

    def search_entries(self, filter_obj: SearchFilter):
        query = "SELECT * FROM journal_entries WHERE 1=1"
        params = []

        if filter_obj.get_keyword():
            query += " AND (title LIKE ? OR description LIKE ?)"
            params.extend([f"%{filter_obj.get_keyword()}%", f"%{filter_obj.get_keyword()}%"])
        
        if filter_obj.get_category():
            query += " AND category = ?"
            params.append(filter_obj.get_category())
            
        if filter_obj.get_date():
            query += " AND date = ?"
            params.append(filter_obj.get_date())

        query += " ORDER BY date DESC, time DESC"
        rows = self.__db.execute_query(query, tuple(params))
        return [self._row_to_entry(row) for row in rows]

    def validate_search_input(self, filter_obj: SearchFilter) -> bool:
        return filter_obj.is_valid_filter()

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
