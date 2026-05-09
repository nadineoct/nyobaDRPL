import os
import shutil
from ..utils.path_manager import PathManager

class Photo:
    """
    Domain model for a journal photo.
    """
    def __init__(self, photo_id: int = None, file_path: str = ""):
        self.__id = photo_id
        self.__file_path = file_path

    def upload_photo(self, source_path: str, entry_id: int, timestamp: str):
        """Copies photo to local app data."""
        ext = os.path.splitext(source_path)[1]
        filename = f"entry_{entry_id}_{timestamp}{ext}"
        dest_path = os.path.join(PathManager.get_photos_dir(), filename)
        
        try:
            shutil.copy2(source_path, dest_path)
            self.__file_path = dest_path
            return True
        except IOError:
            return False

    def delete_photo(self):
        """Deletes photo file from disk."""
        if self.__file_path and os.path.exists(self.__file_path):
            try:
                os.remove(self.__file_path)
                return True
            except OSError:
                return False
        return False

    # Getters
    def get_id(self): return self.__id
    def get_file_path(self): return self.__file_path
    
    # Setters
    def set_file_path(self, path): self.__file_path = path
