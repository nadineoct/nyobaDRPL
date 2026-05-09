"""
JuKi — Daily Journal Application
Entry point. Initializes paths, database, and launches the main window.
"""
from src.utils.path_manager import PathManager
from src.database.database_manager import DatabaseManager
from src.views.main_window import MainWindow

def main():
    # Initialize application paths
    PathManager.initialize()
    
    # Initialize database
    db = DatabaseManager()
    db.initialize()
    
    # Launch main window
    app = MainWindow(db)
    app.mainloop()

if __name__ == "__main__":
    main()
