import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
from ..filters.search_filter import SearchFilter

class SearchView(BaseView):
    """
    Interface for searching and filtering journal entries.
    """
    def __init__(self, master, controller, on_select_entry=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.on_select_entry = on_select_entry
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        # Search Bar Area
        search_container = ctk.CTkFrame(self, fg_color="transparent")
        search_container.pack(fill="x", padx=100, pady=(50, 20))
        
        self.search_input = ctk.CTkEntry(
            search_container, placeholder_text="Cari berdasarkan judul atau deskripsi...",
            height=60, font=(FONT_OUTFIT, 22), corner_radius=30,
            border_color=COLOR_BORDER
        )
        self.search_input.pack(side="left", fill="x", expand=True)
        
        search_btn = ctk.CTkButton(
            search_container, text="Cari",
            width=120, height=60, corner_radius=30,
            fg_color=COLOR_PRIMARY, font=(FONT_OUTFIT, 20, "bold"),
            command=self._on_search
        )
        search_btn.pack(side="left", padx=(20, 0))

        # Filter Panel
        filter_panel = ctk.CTkFrame(self, fg_color="#F9F9F9", corner_radius=20)
        filter_panel.pack(fill="x", padx=100, pady=(0, 40))
        
        ctk.CTkLabel(filter_panel, text="Filter:", font=(FONT_OUTFIT, 18, "bold")).pack(side="left", padx=20, pady=20)
        
        self.category_var = ctk.StringVar(value="Semua Kategori")
        self.category_menu = ctk.CTkOptionMenu(
            filter_panel, values=["Semua Kategori", "Personal", "Kesehatan", "Pekerjaan", "Sosial"],
            variable=self.category_var, fg_color=COLOR_BG_WHITE, text_color=COLOR_TEXT_PRIMARY,
            button_color=COLOR_BORDER, button_hover_color="#EEEEEE"
        )
        self.category_menu.pack(side="left", padx=10)
        
        # Results Area
        self.results_frame = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.results_frame.pack(fill="both", expand=True, padx=100, pady=(0, 50))

    def _on_search(self):
        keyword = self.search_input.get().strip()
        category = self.category_var.get()
        if category == "Semua Kategori":
            category = None
            
        filter_obj = SearchFilter(category=category, keyword=keyword)
        results = self.controller.search_entries(filter_obj)
        self.display_search_result(results)

    def display_search_result(self, results):
        for child in self.results_frame.winfo_children():
            child.destroy()
            
        if not results:
            self.display_empty_result_message()
            return
            
        for entry in results:
            self._create_result_row(entry)

    def _create_result_row(self, entry):
        row = ctk.CTkFrame(self.results_frame, fg_color=COLOR_BG_WHITE, corner_radius=15, border_width=1, border_color=COLOR_BORDER)
        row.pack(fill="x", pady=10)
        
        content = ctk.CTkFrame(row, fg_color="transparent")
        content.pack(fill="x", padx=20, pady=15)
        
        ctk.CTkLabel(content, text=entry.getDate(), font=(FONT_OUTFIT, 16), text_color=COLOR_TEXT_MUTED).pack(side="left")
        
        title = ctk.CTkButton(
            content, text=entry.getTitle(),
            font=(FONT_OUTFIT, 20, "bold"), text_color=COLOR_TEXT_PRIMARY,
            fg_color="transparent", hover_color="#F0F0F0", anchor="w",
            command=lambda e=entry: self.on_select_entry(e) if self.on_select_entry else None
        )
        title.pack(side="left", padx=20, fill="x", expand=True)
        
        ctk.CTkLabel(content, text=entry.getMood(), font=(FONT_OUTFIT, 16), text_color=COLOR_PRIMARY).pack(side="right")

    def display_empty_result_message(self):
        ctk.CTkLabel(self.results_frame, text="Tidak ada jurnal yang sesuai dengan pencarianmu.", font=(FONT_OUTFIT, 20)).pack(pady=50)

    def show_error_message(self, message: str):
        pass
