import ctypes
import os
import sys

class FontManager:
    """
    Utility to load custom fonts on Windows using GDI.
    """
    FR_PRIVATE = 0x10
    FR_NOT_ENUM = 0x20

    @classmethod
    def load_font(cls, font_path):
        """
        Loads a font file into the process memory for use by the application.
        Targeted for Windows.
        """
        if sys.platform != "win32":
            return False
            
        if not os.path.exists(font_path):
            return False

        # AddFontResourceEx is the Win32 API to load a font for the current process
        path_buf = ctypes.create_unicode_buffer(font_path)
        res = ctypes.windll.gdi32.AddFontResourceExW(path_buf, cls.FR_PRIVATE, 0)
        return res > 0

    @classmethod
    def load_all_fonts(cls):
        """Loads all .ttf fonts from the assets/fonts directory."""
        base_dir = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
        fonts_dir = os.path.join(base_dir, "assets", "fonts")
        
        if not os.path.exists(fonts_dir):
            return
            
        for file in os.listdir(fonts_dir):
            if file.endswith(".ttf"):
                cls.load_font(os.path.join(fonts_dir, file))
