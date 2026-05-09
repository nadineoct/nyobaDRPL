class SearchFilter:
    """
    Search and filter criteria for journal entries.
    """
    def __init__(self, category: str = None, keyword: str = "", date: str = ""):
        self.__category = category
        self.__keyword = keyword
        self.__date = date

    def is_valid_filter(self) -> bool:
        """Checks if at least one parameter is filled."""
        return bool(self.__category or self.__keyword or self.__date)

    def apply_filter(self):
        """Logic placeholder for applying filter."""
        pass

    # Getters
    def get_category(self): return self.__category
    def get_keyword(self): return self.__keyword
    def get_date(self): return self.__date
