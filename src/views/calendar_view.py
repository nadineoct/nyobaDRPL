import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
from .mood_avatar import MoodAvatar
from .calendar_day_detail_dialog import CalendarDayDetailDialog
from datetime import datetime
import calendar

class CalendarView(BaseView):
    """
    High-fidelity Calendar View from Figma/HTML.
    """
    def __init__(self, master, controller, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        # MAIN HORIZONTAL LAYOUT
        # 1. Sidebar (294px)
        self.sidebar = ctk.CTkFrame(self, width=294, fg_color=COLOR_PURPLE_LIGHT, corner_radius=0)
        self.sidebar.pack(side="left", fill="y")
        self.sidebar.pack_propagate(False)
        self._setup_sidebar_content()

        # 2. Main Area (1626px)
        self.main_content = ctk.CTkFrame(self, fg_color="transparent")
        self.main_content.pack(side="left", fill="both", expand=True)

        # Header: Month Navigation
        self.cal_header = ctk.CTkFrame(self.main_content, height=120, fg_color=COLOR_BG_WHITE, border_width=1, border_color=COLOR_BORDER, corner_radius=0)
        self.cal_header.pack(fill="x")
        
        header_inner = ctk.CTkFrame(self.cal_header, fg_color="transparent")
        header_inner.place(relx=0.5, rely=0.5, anchor="center")
        
        ctk.CTkLabel(header_inner, text="<", font=(FONT_OUTFIT, 32), text_color=COLOR_TEXT_PRIMARY).pack(side="left", padx=50)
        ctk.CTkLabel(header_inner, text="April", font=(FONT_OUTFIT, 35), text_color=COLOR_TEXT_PRIMARY).pack(side="left", padx=50)
        ctk.CTkLabel(header_inner, text=">", font=(FONT_OUTFIT, 32), text_color=COLOR_TEXT_PRIMARY).pack(side="left", padx=50)

        # Calendar Grid
        self.grid_container = ctk.CTkFrame(self.main_content, fg_color="transparent")
        self.grid_container.pack(fill="both", expand=True)
        
        self.update_display()

    def _setup_sidebar_content(self):
        content = ctk.CTkFrame(self.sidebar, fg_color="transparent")
        content.pack(fill="both", expand=True, padx=32, pady=32)

        # Streak Card
        streak_card = ctk.CTkFrame(content, height=180, fg_color=COLOR_BG_WHITE, corner_radius=20)
        streak_card.pack(fill="x")
        streak_card.pack_propagate(False)
        
        s_num = ctk.CTkLabel(streak_card, text="2", font=(FONT_OUTFIT, 50, "normal"), text_color="black")
        s_num.pack(pady=(30, 0))
        ctk.CTkLabel(streak_card, text="day streak", font=(FONT_OUTFIT, 20, "normal"), text_color="black").pack()

        # Targets
        ctk.CTkLabel(content, text="Target Self-care", font=(FONT_OUTFIT, 25), text_color=COLOR_TEXT_PRIMARY).pack(pady=(40, 24))
        
        for i in range(3):
            row = ctk.CTkFrame(content, fg_color="transparent")
            row.pack(fill="x", pady=8)
            ctk.CTkCheckBox(
                row, text=f"Target {i+1}", font=(FONT_OUTFIT, 20),
                fg_color=COLOR_ACCENT_GREEN, border_color=COLOR_ACCENT_GREEN,
                text_color_disabled=COLOR_TEXT_MUTED
            ).pack(side="left")

        # User at bottom
        user_group = ctk.CTkFrame(content, fg_color="transparent")
        user_group.pack(side="bottom", fill="x", pady=32)
        ctk.CTkFrame(user_group, width=70, height=70, corner_radius=35, fg_color="#D9D9D9").pack(side="left")
        ctk.CTkLabel(user_group, text="Arara", font=(FONT_OUTFIT, 25), text_color="black").pack(side="left", padx=16)

    def update_display(self):
        for child in self.grid_container.winfo_children():
            child.destroy()

        days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
        for i, d in enumerate(days):
            ctk.CTkLabel(self.grid_container, text=d, font=(FONT_OUTFIT, 15, "normal"), text_color=COLOR_TEXT_DISABLED).grid(row=0, column=i, sticky="nsew", pady=16)
            self.grid_container.grid_columnconfigure(i, weight=1)

        # Mock logic for April 2026
        for i in range(30):
            day_num = i + 1
            row = (i + 3) // 7 + 1
            col = (i + 3) % 7
            
            cell = ctk.CTkFrame(self.grid_container, fg_color=COLOR_BG_WHITE, border_width=1, border_color=COLOR_BORDER, corner_radius=0)
            cell.grid(row=row, column=col, sticky="nsew")
            self.grid_container.grid_rowconfigure(row, weight=1)

            # Day Number
            num_lbl = ctk.CTkLabel(cell, text=str(day_num), font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_PRIMARY)
            num_lbl.pack(pady=10)

            # If entry exists...
            if day_num == 27:
                # Target Pills
                pill = ctk.CTkFrame(cell, fg_color=COLOR_ACCENT_PALE, corner_radius=10)
                pill.pack(fill="x", padx=10, pady=2)
                ctk.CTkLabel(pill, text="Target 1", font=(FONT_OUTFIT, 15), text_color=COLOR_TEXT_SECONDARY).pack(padx=8, pady=4)
                
                # Mood Avatar
                av = MoodAvatar(cell, mood_label="Excited", size=40, bg="white")
                av.pack(pady=5)
            
            def on_click(e, d=day_num):
                CalendarDayDetailDialog(self.master, self.controller, f"2026-04-{d:02d}")

            cell.bind("<Button-1>", on_click)
            num_lbl.bind("<Button-1>", on_click)
