from ..database.database_manager import DatabaseManager

class RegistrationFormController:
    """
    Handles user registration and data validation.
    """
    def __init__(self, db_manager: DatabaseManager):
        self.__db = db_manager
        self.__validation_status = False

    def validate_data(self, data: dict) -> bool:
        name = data.get('name', '').strip()
        if not name:
            self.__validation_status = False
            return False
        self.__validation_status = True
        return True

    def submit_entry(self, data: dict):
        if not self.validate_data(data):
            return False
            
        avatar_path = data.get('avatar')
        if avatar_path:
            import shutil
            from ..utils.path_manager import PathManager
            import os
            
            ext = os.path.splitext(avatar_path)[1]
            dest = os.path.join(PathManager.get_app_data_local(), f"user_avatar{ext}")
            try:
                shutil.copy2(avatar_path, dest)
                avatar_path = dest
            except IOError:
                pass

        query = "INSERT INTO users (name, avatar) VALUES (?, ?)"
        self.__db.execute_query(query, (data['name'], avatar_path), commit=True)
        return True

    def show_notification(self):
        pass
