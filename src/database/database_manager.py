import sqlite3
import os
import logging
from ..utils.path_manager import PathManager

class DatabaseManager:
    """
    Manages SQLite database connections, initialization, and migrations.
    """
    
    def __init__(self):
        self.db_path = PathManager.get_db_path()
        self._setup_logging()

    def _setup_logging(self):
        log_file = os.path.join(PathManager.get_logs_dir(), "activity.log")
        logging.basicConfig(
            filename=log_file,
            level=logging.INFO,
            format='%(asctime)s - %(levelname)s - %(message)s'
        )

    def get_connection(self):
        try:
            conn = sqlite3.connect(self.db_path)
            conn.row_factory = sqlite3.Row
            return conn
        except sqlite3.Error as e:
            logging.error(f"Database connection error: {e}")
            return None

    def initialize(self):
        """Creates tables if they don't exist using schema.sql."""
        schema_path = os.path.join(os.path.dirname(__file__), "schema.sql")
        if not os.path.exists(schema_path):
            logging.error("schema.sql not found.")
            return

        conn = self.get_connection()
        if conn:
            try:
                with open(schema_path, 'r') as f:
                    schema_script = f.read()
                conn.executescript(schema_script)
                conn.commit()
                logging.info("Database initialized successfully.")
            except (sqlite3.Error, IOError) as e:
                logging.error(f"Database initialization error: {e}")
            finally:
                conn.close()

    def execute_query(self, query, params=(), commit=False):
        conn = self.get_connection()
        result = None
        if conn:
            try:
                cursor = conn.cursor()
                cursor.execute(query, params)
                if commit:
                    conn.commit()
                    result = cursor.lastrowid
                else:
                    result = cursor.fetchall()
            except sqlite3.Error as e:
                logging.error(f"Query execution error: {e}\nQuery: {query}\nParams: {params}")
            finally:
                conn.close()
        return result

    def execute_script(self, script):
        conn = self.get_connection()
        if conn:
            try:
                conn.executescript(script)
                conn.commit()
            except sqlite3.Error as e:
                logging.error(f"Script execution error: {e}")
            finally:
                conn.close()
