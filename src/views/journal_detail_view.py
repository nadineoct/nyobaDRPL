import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
from .mood_avatar import MoodAvatar
from tkinter import messagebox

class JournalDetailView(BaseView):
    """
    High-fidelity Journal Detail View from Figma/HTML.
    """
    def __init__(self, master, controller, entry=None, on_back=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.entry = entry
        self.on_back = on_back
        self.configure(fg_color=COLOR_BG_WHITE)
        
        if self.entry:
            self._setup_ui()

    def _setup_ui(self):
        # MAIN SCROLLABLE CONTAINER
        self.scroll = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.scroll.pack(fill="both", expand=True, padx=(363, 100), pady=50)

        # 1. Main Header Image
        img_box = ctk.CTkFrame(self.scroll, height=371, fg_color="#D6D6D6", corner_radius=20)
        img_box.pack(fill="x", pady=(0, 24))

        # 2. Title & Mood Row
        header_row = ctk.CTkFrame(self.scroll, fg_color="transparent")
        header_row.pack(fill="x", pady=(0, 80))

        # Left Side: Title + Meta + Category
        left_info = ctk.CTkFrame(header_row, fg_color="transparent")
        left_info.pack(side="left", fill="both", expand=True)

        title_lbl = ctk.CTkLabel(
            left_info, text=self.entry.getTitle(),
            font=(FONT_OUTFIT, 75, "normal"), text_color="black",
            anchor="w", justify="left"
        )
        title_lbl.pack(fill="x")

        # Meta Row (Avatar + Name + Date)
        meta_row = ctk.CTkFrame(left_info, fg_color="transparent")
        meta_row.pack(fill="x", pady=24)
        
        ctk.CTkFrame(meta_row, width=60, height=60, corner_radius=30, fg_color="#D9D9D9").pack(side="left")
        
        meta_txt = ctk.CTkFrame(meta_row, fg_color="transparent")
        meta_txt.pack(side="left", padx=24)
        ctk.CTkLabel(meta_txt, text="Arara", font=(FONT_OUTFIT, 30, "normal"), text_color="#434343").pack(side="left")
        ctk.CTkLabel(meta_txt, text=f"{self.entry.getDate()} {self.entry.getTime()}", font=(FONT_OUTFIT, 25, "normal"), text_color="#767676").pack(side="left", padx=24)

        # Categories
        cat_row = ctk.CTkFrame(left_info, fg_color="transparent")
        cat_row.pack(fill="x")
        cat_pill = ctk.CTkFrame(cat_row, fg_color=COLOR_ACCENT_PALE, corner_radius=100, border_width=1, border_color="#A66502")
        cat_pill.pack(side="left")
        ctk.CTkLabel(cat_pill, text=self.entry.get_category(), font=(FONT_OUTFIT, 30, "normal"), text_color="black").pack(padx=32, pady=8)

        # Right Side: Mood Indicator
        mood_group = ctk.CTkFrame(header_row, fg_color="transparent")
        mood_group.pack(side="right", padx=(40, 0))
        
        avatar = MoodAvatar(mood_group, mood_label=self.entry.getMood(), size=100, bg="white")
        avatar.pack()
        ctk.CTkLabel(mood_group, text=self.entry.getMood(), font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(pady=8)

        # 3. Content Sections
        # Penyebab
        cause_row = ctk.CTkFrame(self.scroll, fg_color="transparent")
        cause_row.pack(fill="x", pady=(0, 40))
        ctk.CTkLabel(cause_row, text="Penyebab:", font=(FONT_OUTFIT, 30), text_color="#434343").pack(side="left")
        ctk.CTkLabel(cause_row, text=self.entry.getTrigger(), font=(FONT_OUTFIT, 25, "normal"), text_color="#767676").pack(side="left", padx=16)

        # Description
        desc_lbl = ctk.CTkLabel(
            self.scroll, text=self.entry.getDescription(),
            font=(FONT_OUTFIT, 30), text_color="#434343",
            anchor="w", justify="left", wraplength=1100
        )
        desc_lbl.pack(fill="x", pady=(0, 40))

        # Secondary Image
        ctk.CTkFrame(self.scroll, height=478, fg_color="#D6D6D6", corner_radius=20).pack(fill="x", pady=(0, 40))

        # 4. Target Card
        self._setup_target_card(self.scroll)

        # 5. Three-dot Actions
        actions_btn = ctk.CTkButton(
            self, text="⋮", font=(FONT_OUTFIT, 30),
            width=65, height=65, corner_radius=32,
            fg_color=COLOR_BG_WHITE, text_color="black",
            border_width=1, border_color=COLOR_BORDER,
            command=self._show_actions
        )
        actions_btn.place(x=271, y=738)

    def _setup_target_card(self, parent):
        card = ctk.CTkFrame(parent, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        card.pack(fill="x", pady=(0, 80))
        
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(fill="x", padx=28, pady=28)
        ctk.CTkFrame(header, width=70, height=70, corner_radius=35, fg_color="#D9D9D9").pack(side="left")
        
        txt = ctk.CTkFrame(header, fg_color="transparent")
        txt.pack(side="left", padx=16)
        ctk.CTkLabel(txt, text="Target Hari Ini", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(anchor="w")
        ctk.CTkLabel(txt, text="Peluk dirimu dengan kegiatan ini!", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY).pack(anchor="w")

    def _show_actions(self):
        # Create a small floating menu
        menu = ctk.CTkToplevel(self)
        menu.geometry("200x150+720+738")
        menu.overrideredirect(True)
        menu.configure(fg_color=COLOR_BG_WHITE)
        menu.attributes("-topmost", True)
        
        ctk.CTkButton(
            menu, text="Edit Jurnal", font=(FONT_OUTFIT, 20, "normal"),
            fg_color="transparent", text_color=COLOR_TEXT_PRIMARY,
            anchor="w", command=lambda: [menu.destroy(), self._on_edit()]
        ).pack(fill="x", padx=20, pady=(20, 10))
        
        ctk.CTkButton(
            menu, text="Hapus Jurnal", font=(FONT_OUTFIT, 20, "normal"),
            fg_color="transparent", text_color=COLOR_TEXT_PRIMARY,
            anchor="w", command=lambda: [menu.destroy(), self._on_delete()]
        ).pack(fill="x", padx=20, pady=(10, 20))
        
        menu.bind("<FocusOut>", lambda e: menu.destroy())

    def _on_edit(self):
        self.master.master.switch_page("WriteJournal", entry=self.entry)

    def _on_delete(self):
        confirm = messagebox.askyesno("Hapus Jurnal", "Apakah anda yakin untuk hapus jurnal ini?\nAksi ini tidak dapat dipulihkan")
        if confirm:
            if self.controller.delete_entry(self.entry.get_id()):
                if self.on_back: self.on_back()

    def set_entry(self, entry):
        self.entry = entry
        for child in self.winfo_children():
            child.destroy()
        self._setup_ui()
