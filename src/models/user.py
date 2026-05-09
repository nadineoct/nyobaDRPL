class User:
    """
    Domain model for JuKi user.
    """
    def __init__(self, user_id: int = None, name: str = "", avatar: str = None):
        self.__id = user_id
        self.__name = name
        self.__avatar = avatar

    # Getters
    def get_id(self) -> int:
        return self.__id

    def get_name(self) -> str:
        return self.__name

    def get_avatar(self) -> str:
        return self.__avatar

    # Setters
    def set_name(self, name: str):
        self.__name = name

    def set_avatar(self, avatar: str):
        self.__avatar = avatar

    # Logic placeholders as per CD-01
    def fill_form(self):
        pass

    def press_save(self):
        pass

    def press_cancel(self):
        pass
