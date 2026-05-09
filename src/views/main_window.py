import customtkinter as ctk
import os
import tkinter.font as tkfont
from .navbar import NavBar
from .home_view import HomeView
from .journal_list_view import JournalListView
from .calendar_view import CalendarView
from .write_journal_view import WriteJournalView
from .registration_form_view import RegistrationFormView
from ..controllers.entry_controller import EntryController
from ..controllers.analytics_controller import AnalyticsController
from ..controllers.search_controller import SearchController
from ..controllers.registration_form_controller import RegistrationFormController

class MainWindow(ctk.CTk):
    """
    Main application window using a frame-switching pattern.
    """
    def __init__(self, db_manager):
        super().__init__()
        
        self.title("JuKi — Daily Journal")
        self.geometry("1280x800")
        self.minsize(1024, 600)
        
        self.db = db_manager
        self.current_user = None
        
        # 2. Load assets
        self._load_fonts()
        
        # 3. Initialize Controllers
        self.entry_controller = EntryController(self.db)
        self.analytics_controller = AnalyticsController(self.db)
        self.search_controller = SearchController(self.db)
        self.reg_controller = RegistrationFormController(self.db)
        
        # 4. Setup Container
        self.container = ctk.CTkFrame(self)
        self.container.pack(side="top", fill="both", expand=True)
        self.container.grid_rowconfigure(1, weight=1)
        self.container.grid_columnconfigure(0, weight=1)
        
        self.navbar = None
        self.frames = {}
        
        # 5. Launch first page
        self._check_first_run()

    def _load_fonts(self):
        """Attempts to load brand fonts from assets/fonts."""
        from ..utils.font_manager import FontManager
        FontManager.load_all_fonts()

    def _check_first_run(self):
        query = "SELECT * FROM users LIMIT 1"
        result = self.db.execute_query(query)
        if not result:
            self.switch_page("Registration")
        else:
            self.current_user = result[0]
            self.switch_page("Beranda")

    def get_current_user_id(self):
        return self.current_user['id'] if self.current_user else 1

    def switch_page(self, page_name, entry=None, keyword=None):
        # Destroy current navbar if exists
        if self.navbar:
            self.navbar.destroy()
            
        # Add Navbar for most pages
        if page_name not in ["Registration"]:
            self.navbar = NavBar(self.container, current_page=page_name, on_navigate=self.switch_page)
            self.navbar.grid(row=0, column=0, sticky="ew")
        
        # Instantiate and show the page
        if page_name == "Registration":
            frame = RegistrationFormView(self.container, self.reg_controller, on_success=lambda: self.switch_page("Beranda"))
        elif page_name == "Beranda":
            frame = HomeView(self.container, self.analytics_controller, self.entry_controller)
        elif page_name == "Jurnal":
            frame = JournalListView(self.container, self.entry_controller, on_select_entry=lambda e: self.switch_page("EntryDetail", entry=e))
        elif page_name == "Kalendar":
            frame = CalendarView(self.container, self.entry_controller)
        elif page_name == "WriteJournal":
            frame = WriteJournalView(self.container, self.entry_controller, entry=entry, on_close=lambda: self.switch_page("Beranda"))
        elif page_name == "EntryDetail":
            from .journal_detail_view import EntryDetailView
            frame = EntryDetailView(self.container, self.entry_controller, entry=entry, on_back=lambda: self.switch_page("Jurnal"))
        elif page_name == "Search":
            from .search_view import SearchView
            frame = SearchView(self.container, self.search_controller, on_select_entry=lambda e: self.switch_page("EntryDetail", entry=e))
            if keyword:
                frame.search_input.insert(0, keyword)
                frame._on_search()
        elif page_name == "Visualisasi":
            from .visualizer_view import VisualizerView
            frame = VisualizerView(self.container, self.analytics_controller)
        
        # Ensure the frame has access to the main window for switching
        frame.master = self.container
        
        frame.grid(row=1, column=0, sticky="nsew")
        frame.tkraise()
