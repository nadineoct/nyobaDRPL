import json
import os
from .path_manager import PathManager

class ConfigManager:
    """
    Read and write user preferences (theme, period filter, chart color).
    Stored in AppData/Roaming/JuKi/config.json.
    """
    
    DEFAULT_CONFIG = {
        "theme": "light",
        "chart_color": "#EF73FF",
        "last_period_filter": "weekly"
    }

    @classmethod
    def load(cls):
        path = PathManager.get_config_path()
        if not os.path.exists(path):
            cls.save(cls.DEFAULT_CONFIG)
            return cls.DEFAULT_CONFIG
        
        try:
            with open(path, 'r') as f:
                return json.load(f)
        except (json.JSONDecodeError, IOError):
            return cls.DEFAULT_CONFIG

    @classmethod
    def save(cls, config_data):
        path = PathManager.get_config_path()
        try:
            with open(path, 'w') as f:
                json.dump(config_data, f, indent=4)
        except IOError:
            print(f"Error saving config to {path}")

    @classmethod
    def get(cls, key):
        config = cls.load()
        return config.get(key, cls.DEFAULT_CONFIG.get(key))

    @classmethod
    def set(cls, key, value):
        config = cls.load()
        config[key] = value
        cls.save(config)
