import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
from ..models.journal_entry import JournalEntry
from datetime import datetime
import os
from tkinter import filedialog, messagebox

class WriteJournalView(BaseView):
    """
    High-fidelity Write/Edit Journal View from Figma/HTML.
    """
    def __init__(self, master, controller, entry=None, on_close=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.entry = entry
        self.on_close = on_close
        self.temp_photo_path = None
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()
        
        if self.entry:
            self._prefill_data()

    def _setup_ui(self):
        # HEADER BAR (Draft | Post + Avatar)
        header = ctk.CTkFrame(self, fg_color="transparent")
        header.pack(fill="x", padx=100, pady=(143, 40))
        
        status_label = ctk.CTkLabel(header, text="Draft", font=(FONT_OUTFIT, 40), text_color=COLOR_TEXT_SECONDARY)
        status_label.pack(side="left")

        post_group = ctk.CTkFrame(header, fg_color="transparent")
        post_group.pack(side="right")

        self.post_btn = ctk.CTkButton(
            post_group, text="Post", 
            font=(FONT_OUTFIT, 30), text_color="black",
            fg_color=COLOR_ACCENT_YELLOW, hover_color="#F1B900",
            corner_radius=100, height=52, width=150,
            command=self._show_post_confirmation
        )
        self.post_btn.pack(side="left", padx=16)

        # Avatar placeholder
        ctk.CTkFrame(post_group, width=60, height=60, corner_radius=30, fg_color="#D9D9D9").pack(side="left")

        # FORM CONTENT (offset 363px)
        self.scroll = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.scroll.pack(fill="both", expand=True, padx=(363, 100))

        # Title Entry (Massive)
        self.title_entry = ctk.CTkEntry(
            self.scroll, placeholder_text="Judul",
            font=(FONT_OUTFIT, 75, "normal"),
            text_color="black", placeholder_text_color="rgba(0,0,0,0.20)",
            fg_color="transparent", border_width=0,
            height=100
        )
        self.title_entry.pack(fill="x", pady=(0, 32))

        # Kategori
        self._create_section_label("Kategori")
        self.cat_frame = ctk.CTkFrame(self.scroll, fg_color="transparent")
        self.cat_frame.pack(fill="x", pady=(0, 32))
        
        self.cat_var = ctk.StringVar(value="Pilih Kategori")
        self.cat_menu = ctk.CTkOptionMenu(
            self.cat_frame, values=["Personal", "Kesehatan", "Pekerjaan", "Sosial"],
            variable=self.cat_var, 
            fg_color=COLOR_BG_WHITE, text_color=COLOR_TEXT_PRIMARY,
            button_color=COLOR_BG_WHITE, button_hover_color="#F0F0F0",
            font=(FONT_OUTFIT, 20), dropdown_font=(FONT_OUTFIT, 18),
            corner_radius=100, border_width=1, border_color="rgba(0,0,0,0.20)",
            height=57
        )
        self.cat_menu.pack(fill="x")

        # Penyebab
        self._create_section_label("Penyebab")
        self.trigger_entry = ctk.CTkEntry(
            self.scroll, placeholder_text="Tulis Penyebab",
            font=(FONT_OUTFIT, 20), text_color=COLOR_TEXT_PRIMARY,
            fg_color=COLOR_BG_WHITE, corner_radius=100,
            border_width=1, border_color="rgba(0,0,0,0.20)",
            height=57, padx=24
        )
        self.trigger_entry.pack(fill="x", pady=(0, 40))

        # Story / Description
        self._create_section_label("Tulis ceritamu hari ini!")
        self.desc_text = ctk.CTkTextbox(
            self.scroll, font=(FONT_OUTFIT, 25, "normal"),
            text_color="#434343", fg_color="transparent",
            height=300, border_width=0
        )
        self.desc_text.pack(fill="x", pady=(0, 40))

        # Target Hari Ini Card
        self.target_card = ctk.CTkFrame(self.scroll, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        self.target_card.pack(fill="x", pady=(0, 40))
        
        target_header = ctk.CTkFrame(self.target_card, fg_color="transparent")
        target_header.pack(fill="x", padx=28, pady=28)
        ctk.CTkFrame(target_header, width=70, height=70, corner_radius=35, fg_color="#D9D9D9").pack(side="left")
        
        target_txt = ctk.CTkFrame(target_header, fg_color="transparent")
        target_txt.pack(side="left", padx=16)
        ctk.CTkLabel(target_txt, text="Target Hari Ini", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(anchor="w")
        ctk.CTkLabel(target_txt, text="Peluk dirimu dengan kegiatan ini!", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY).pack(anchor="w")

        # Add Target Link
        add_btn = ctk.CTkButton(
            self.target_card, text="+ Tambah Target", 
            font=(FONT_OUTFIT, 20, "normal"), text_color="#575757",
            fg_color="transparent", hover_color="#F5F5F5",
            anchor="w", command=self._add_target_row
        )
        add_btn.pack(fill="x", padx=28, pady=(0, 28))

        # Photo Icon (Floating)
        self.photo_btn = ctk.CTkButton(
            self, text="📷", font=(FONT_OUTFIT, 30),
            width=65, height=65, corner_radius=32,
            fg_color=COLOR_BG_WHITE, text_color="black",
            border_width=1, border_color=COLOR_BORDER,
            command=self._pick_photo
        )
        self.photo_btn.place(x=271, y=631)

    def _create_section_label(self, text):
        lbl = ctk.CTkLabel(self.scroll, text=text, font=(FONT_OUTFIT, 30), text_color="black", anchor="w")
        lbl.pack(fill="x", pady=(0, 16))

    def _prefill_data(self):
        self.title_entry.insert(0, self.entry.getTitle())
        self.cat_var.set(self.entry.get_category())
        self.trigger_entry.insert(0, self.entry.getTrigger())
        self.desc_text.insert("1.0", self.entry.getDescription())

    def _add_target_row(self):
        # Implementation for adding target rows dynamically
        pass

    def _pick_photo(self):
        path = filedialog.askopenfilename(filetypes=[("Image files", "*.jpg *.jpeg *.png")])
        if path:
            self.temp_photo_path = path

    def _show_post_confirmation(self):
        confirm = messagebox.askyesno("Unggah Jurnal", "Apakah anda yakin untuk unggah jurnal ini?\nPastikan semua refleksi telah ditulis, ya!")
        if confirm:
            self._on_save()

    def _on_save(self):
        # Logic to save/update in DB
        now = datetime.now()
        entry_data = {
            "title": self.title_entry.get(),
            "category": self.cat_var.get(),
            "trigger": self.trigger_entry.get(),
            "description": self.desc_text.get("1.0", "end-1c"),
            "date": now.strftime("%Y-%m-%d"),
            "time": now.strftime("%H:%M"),
            "mood": "Neutral", # Default or selected
            "mood_score": 3
        }
        # Call controller...
        if self.on_close: self.on_close()
