from ..database.database_manager import DatabaseManager
from datetime import datetime, timedelta

class AnalyticsController:
    """
    Handles mood trend analysis and self-care statistics.
    """
    def __init__(self, db_manager: DatabaseManager):
        self.__db = db_manager
        self.__raw_entries = []
        self.__success_rate = 0.0
        self.__mood_trend = {}
        self.__total_target_count = 0

    def calculate_average_mood(self):
        if not self.__raw_entries: return 0.0
        total_score = sum(e['mood_score'] for e in self.__raw_entries)
        return total_score / len(self.__raw_entries)

    def calculate_self_care_rate(self):
        query = "SELECT COUNT(*) as total, SUM(completed) as completed FROM self_care_completions"
        result = self.__db.execute_query(query)
        if result and result[0]['total'] > 0:
            self.__success_rate = (result[0]['completed'] / result[0]['total']) * 100
        return self.__success_rate

    def filter_by_date(self, start_date: str, end_date: str):
        query = "SELECT * FROM journal_entries WHERE date BETWEEN ? AND ? ORDER BY date"
        self.__raw_entries = self.__db.execute_query(query, (start_date, end_date))
        # Update trend immediately
        self.get_mood_trend()

    def aggregate_data(self):
        pass

    def get_mood_trend(self):
        trend = {}
        for row in self.__raw_entries:
            trend[row['date']] = row['mood_score']
        self.__mood_trend = trend
        return self.__mood_trend

    def get_journal_entry(self):
        return self.__raw_entries

    def get_visualization(self):
        # Formats data for matplotlib
        dates = list(self.__mood_trend.keys())
        scores = list(self.__mood_trend.values())
        return dates, scores
