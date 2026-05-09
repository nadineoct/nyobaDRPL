import os
import sys

class PathManager:
    """
    Handles AppData paths for JuKi, ensuring cross-user compatibility.
    Primary target is Windows, with fallbacks for development on Linux/macOS.
    """
    
    APP_NAME = "JuKi"

    @classmethod
    def get_app_data_roaming(cls):
        """Returns the roaming AppData path (config)."""
        if sys.platform == "win32":
            base = os.environ.get("APPDATA", os.path.expanduser("~\\AppData\\Roaming"))
        else:
            base = os.path.expanduser("~/.config")
        
        path = os.path.join(base, cls.APP_NAME)
        return path

    @classmethod
    def get_app_data_local(cls):
        """Returns the local AppData path (database, photos, logs)."""
        if sys.platform == "win32":
            base = os.environ.get("LOCALAPPDATA", os.path.expanduser("~\\AppData\\Local"))
        else:
            base = os.path.expanduser("~/.local/share")
        
        path = os.path.join(base, cls.APP_NAME)
        return path

    @classmethod
    def get_db_path(cls):
        return os.path.join(cls.get_app_data_local(), "juki.db")

    @classmethod
    def get_photos_dir(cls):
        return os.path.join(cls.get_app_data_local(), "photos")

    @classmethod
    def get_logs_dir(cls):
        return os.path.join(cls.get_app_data_local(), "logs")

    @classmethod
    def get_config_path(cls):
        return os.path.join(cls.get_app_data_roaming(), "config.json")

    @classmethod
    def initialize(cls):
        """Creates necessary directories on first run."""
        os.makedirs(cls.get_app_data_roaming(), exist_ok=True)
        os.makedirs(cls.get_app_data_local(), exist_ok=True)
        os.makedirs(cls.get_photos_dir(), exist_ok=True)
        os.makedirs(cls.get_logs_dir(), exist_ok=True)
