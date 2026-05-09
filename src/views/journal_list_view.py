import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *

class JournalListView(BaseView):
    """
    High-fidelity Journal List View from Figma/HTML.
    """
    def __init__(self, master, controller, on_select_entry=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.on_select_entry = on_select_entry
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        # Top Container for Filter Button
        self.top_frame = ctk.CTkFrame(self, fg_color="transparent")
        self.top_frame.pack(fill="x", padx=100, pady=(52, 48))
        
        self.filter_btn = ctk.CTkButton(
            self.top_frame, text="Cari Berdasarkan Tanggal",
            font=(FONT_OUTFIT, 18, "normal"),
            text_color=COLOR_TEXT_PRIMARY,
            fg_color=COLOR_ACCENT_PALE,
            border_color="#A66502",
            border_width=1.25,
            corner_radius=12,
            height=50,
            command=self._on_filter_click
        )
        self.filter_btn.pack(side="right")

        # Scrollable Grid Area
        self.scroll = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.scroll.pack(fill="both", expand=True, padx=100)
        
        self.grid_container = ctk.CTkFrame(self.scroll, fg_color="transparent")
        self.grid_container.pack(fill="both", expand=True)
        self.grid_container.grid_columnconfigure((0, 1, 2), weight=1, pad=32)

        self.update_display()

    def update_display(self, entries=None):
        for child in self.grid_container.winfo_children():
            child.destroy()
            
        if entries is None:
            entries = self.controller.get_all_entries()
            
        if not entries:
            self._show_empty_state()
            return
            
        for i, entry in enumerate(entries):
            self._create_card(entry, i)

    def _create_card(self, entry, index):
        row = index // 3
        col = index % 3
        
        card = ctk.CTkFrame(
            self.grid_container, fg_color=COLOR_BG_WHITE, corner_radius=20
        )
        # Adding a fake shadow effect with a border since tkinter doesn't support real shadows well
        card.configure(border_width=1, border_color="#E0E0E0")
        card.grid(row=row, column=col, sticky="nsew", padx=16, pady=16)
        
        def on_click(e):
            if self.on_select_entry:
                self.on_select_entry(entry)

        card.bind("<Button-1>", on_click)

        # 1. Gray Image Placeholder
        img_box = ctk.CTkFrame(card, height=212, fg_color="#D6D6D6", corner_radius=20)
        img_box.pack(fill="x", padx=24, pady=(24, 0))
        img_box.bind("<Button-1>", on_click)
        
        # 2. Text Content
        text_inner = ctk.CTkFrame(card, fg_color="transparent")
        text_inner.pack(fill="both", expand=True, padx=24, pady=24)
        text_inner.bind("<Button-1>", on_click)
        
        title = ctk.CTkLabel(
            text_inner, text=entry.getTitle(),
            font=(FONT_OUTFIT, 22, "normal"), text_color="black",
            anchor="w", justify="left"
        )
        title.pack(fill="x")
        title.bind("<Button-1>", on_click)
        
        desc = ctk.CTkLabel(
            text_inner, text=entry.getDescription()[:120] + "...",
            font=(FONT_OUTFIT, 20, "normal"), text_color="black",
            anchor="w", justify="left", wraplength=400
        )
        desc.pack(fill="x", pady=(8, 24))
        desc.bind("<Button-1>", on_click)
        
        # 3. Yellow Date Tag
        date_pill = ctk.CTkFrame(text_inner, fg_color=COLOR_ACCENT_PALE, corner_radius=100)
        date_pill.pack(side="left")
        ctk.CTkLabel(
            date_pill, text=entry.getDate(),
            font=(FONT_OUTFIT, 15), text_color="#A66502"
        ).pack(padx=32, pady=8)

    def _show_empty_state(self):
        ctk.CTkLabel(self.grid_container, text="Belum ada jurnal ditemukan.", font=(FONT_OUTFIT, 22)).pack(pady=100)

    def _on_filter_click(self):
        # Trigger date filter dialog
        pass
