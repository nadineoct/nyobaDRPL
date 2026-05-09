from datetime import date as date_type, time as time_type
from .photo import Photo

class JournalEntry:
    """
    Domain model for a journal entry.
    """
    def __init__(self, 
                 entry_id: int = None, 
                 category: str = "", 
                 title: str = "", 
                 description: str = "", 
                 trigger: str = "", 
                 target: str = "", 
                 mood: str = "",
                 mood_score: int = 0,
                 date: str = "", 
                 time: str = "", 
                 photo: Photo = None):
        self.__id = entry_id
        self.__category = category
        self.__title = title
        self.__description = description
        self.__trigger = trigger
        self.__target = target
        self.__mood = mood
        self.__mood_score = mood_score
        self.__date = date
        self.__time = time
        self.__photo = photo

    # Factory method
    @classmethod
    def create_entry(cls, category, title, description, trigger, target, mood, mood_score, date, time, photo=None):
        return cls(None, category, title, description, trigger, target, mood, mood_score, date, time, photo)

    # Getters
    def get_id(self): return self.__id
    def get_category(self): return self.__category
    def get_title(self): return self.__title
    def get_description(self): return self.__description
    def get_trigger(self): return self.__trigger
    def get_target(self): return self.__target
    def get_mood(self): return self.__mood
    def get_mood_score(self): return self.__mood_score
    def get_date(self): return self.__date
    def get_time(self): return self.__time
    def get_photo(self): return self.__photo

    # Setters
    def set_category(self, val): self.__category = val
    def set_title(self, val): self.__title = val
    def set_description(self, val): self.__description = val
    def set_trigger(self, val): self.__trigger = val
    def set_target(self, val): self.__target = val
    def set_mood(self, val): self.__mood = val
    def set_mood_score(self, val): self.__mood_score = val
    def set_date(self, val): self.__date = val
    def set_time(self, val): self.__time = val
    def set_photo(self, val): self.__photo = val
