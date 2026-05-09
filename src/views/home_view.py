import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from datetime import datetime, timedelta

class HomeView(BaseView):
    """
    High-fidelity Landing Page (Beranda) from Figma/HTML.
    """
    def __init__(self, master, analytics_controller, entry_controller, **kwargs):
        super().__init__(master, **kwargs)
        self.analytics_controller = analytics_controller
        self.entry_controller = entry_controller
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        # MAIN SCROLLABLE CONTAINER
        self.scroll_root = ctk.CTkScrollableFrame(self, fg_color="transparent")
        self.scroll_root.pack(fill="both", expand=True)

        # Content Wrapper with 100px padding left
        self.wrapper = ctk.CTkFrame(self.scroll_root, fg_color="transparent")
        self.wrapper.pack(fill="both", expand=True, padx=100, pady=(115, 52))

        # Greeting: "Halo, Ara! Gimana perasaanmu hari ini? 🤔"
        greeting_label = ctk.CTkLabel(
            self.wrapper, text="Halo, Ara! Gimana perasaanmu hari ini? 🤔",
            font=(FONT_OUTFIT, 50, "normal"),
            text_color=COLOR_BROWN_ACCENT,
            anchor="w"
        )
        greeting_label.pack(fill="x", pady=(0, 48))

        # TOP GRID (3 Columns)
        top_grid = ctk.CTkFrame(self.wrapper, fg_color="transparent")
        top_grid.pack(fill="x", side="top")
        
        # Col 1: Streak + Chart
        col1 = ctk.CTkFrame(top_grid, fg_color="transparent")
        col1.pack(side="left", fill="both", expand=True, padx=(0, 20))
        self._setup_streak_widget(col1)
        self._setup_mood_chart_card(col1)

        # Col 2: Mini Calendar
        col2 = ctk.CTkFrame(top_grid, width=500, fg_color="transparent")
        col2.pack(side="left", fill="both", padx=20)
        self._setup_mini_calendar_card(col2)

        # Col 3: Mood Carousel
        col3 = ctk.CTkFrame(top_grid, fg_color="transparent")
        col3.pack(side="left", fill="both", expand=True, padx=(20, 0))
        self._setup_mood_carousel_card(col3)

        # BOTTOM ROW
        bottom_grid = ctk.CTkFrame(self.wrapper, fg_color="transparent")
        bottom_grid.pack(fill="x", pady=(40, 0))

        # Riwayat Jurnal (Left)
        col_history = ctk.CTkFrame(bottom_grid, fg_color="transparent")
        col_history.pack(side="left", fill="both", expand=True, padx=(0, 10))
        self._setup_journal_history_section(col_history)

        # Target Hari Ini (Right)
        col_target = ctk.CTkFrame(bottom_grid, width=500, fg_color="transparent")
        col_target.pack(side="right", fill="both", padx=(10, 0))
        self._setup_today_target_card(col_target)

    def _setup_streak_widget(self, parent):
        streak_card = ctk.CTkFrame(parent, height=137, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        streak_card.pack(fill="x", pady=(0, 10))
        
        inner = ctk.CTkFrame(streak_card, fg_color="transparent")
        inner.pack(padx=32, pady=32)

        # Streak Count
        num_frame = ctk.CTkFrame(inner, fg_color="transparent")
        num_frame.pack(side="left")
        
        streak_num = ctk.CTkLabel(num_frame, text="2", font=(FONT_OUTFIT, 50, "normal"), text_color=COLOR_TEXT_PRIMARY)
        streak_num.pack(side="left")
        
        # Fire Icon placeholder
        ctk.CTkFrame(num_frame, width=42, height=60, fg_color=COLOR_STREAK_ORANGE, corner_radius=5).pack(side="left", padx=10)
        
        ctk.CTkLabel(inner, text="day streak", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY).pack(side="left", padx=(0, 40))

        # Divider (Self-care label)
        ctk.CTkLabel(inner, text="Target Self-care", font=(FONT_OUTFIT, 25, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(side="left", padx=20)

        # Week dots
        dots_row = ctk.CTkFrame(inner, fg_color="transparent")
        dots_row.pack(side="left")
        for d in ["S", "M", "T", "W", "T", "F", "S"]:
            dot_col = ctk.CTkFrame(dots_row, fg_color="transparent")
            dot_col.pack(side="left", padx=12)
            
            is_done = d in ["S", "M", "T", "W", "S"] # Placeholder logic
            color = COLOR_ACCENT_GREEN if is_done else "transparent"
            border = 0 if is_done else 1.6
            
            ctk.CTkFrame(dot_col, width=24, height=24, corner_radius=12, fg_color=color, border_width=border, border_color=COLOR_ACCENT_GREEN).pack()
            ctk.CTkLabel(dot_col, text=d, font=(FONT_PLUS_JAKARTA, 16, "bold"), text_color=COLOR_TEXT_SECONDARY).pack()

    def _setup_mood_chart_card(self, parent):
        card = ctk.CTkFrame(parent, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        card.pack(fill="both", expand=True)
        
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(fill="x", padx=28, pady=28)
        
        ctk.CTkLabel(header, text="Grafik Suasana Hati", font=(FONT_OUTFIT, 25, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(side="left")
        
        date_pill = ctk.CTkFrame(header, fg_color=COLOR_ACCENT_PALE, corner_radius=10, border_width=1, border_color="#F1B900")
        date_pill.pack(side="right")
        ctk.CTkLabel(date_pill, text="20-26 April 2026", font=(FONT_OUTFIT, 15, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(padx=16, pady=8)

        # Matplotlib Area
        fig, ax = plt.subplots(figsize=(6, 2.5), dpi=100)
        fig.patch.set_facecolor('white')
        
        days = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']
        scores = [3, 4, 2, 5, 4, 3, 4]
        
        # Purple to Cream gradient logic here is tricky in matplotlib, simplified as color
        ax.fill_between(days, scores, color="#EF73FF", alpha=0.3)
        ax.plot(days, scores, color=COLOR_PRIMARY, linewidth=2)
        
        ax.set_ylim(0, 5)
        ax.spines['top'].set_visible(False)
        ax.spines['right'].set_visible(False)
        ax.spines['left'].set_color(COLOR_BORDER)
        ax.spines['bottom'].set_color(COLOR_BORDER)
        ax.tick_params(axis='x', colors=COLOR_TEXT_MUTED, labelsize=10)
        ax.tick_params(axis='y', left=False, labelleft=False)
        
        canvas = FigureCanvasTkAgg(fig, master=card)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=28, pady=(0, 28))

    def _setup_mini_calendar_card(self, parent):
        card = ctk.CTkFrame(parent, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        card.pack(fill="both", expand=True)
        card.pack_propagate(False)

        # Header: Arrows + Month
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(fill="x", padx=28, pady=28)
        
        ctk.CTkLabel(header, text="<", font=(FONT_OUTFIT, 25)).pack(side="left")
        ctk.CTkLabel(header, text="April", font=(FONT_OUTFIT, 25), text_color=COLOR_TEXT_PRIMARY).pack(side="left", expand=True)
        ctk.CTkLabel(header, text=">", font=(FONT_OUTFIT, 25)).pack(side="right")

        # Grid for Calendar
        grid = ctk.CTkFrame(card, fg_color="transparent")
        grid.pack(fill="both", expand=True, padx=20, pady=(0, 20))
        
        days = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"]
        for i, d in enumerate(days):
            ctk.CTkLabel(grid, text=d, font=(FONT_MONTSERRAT, 16, "normal"), text_color=COLOR_TEXT_MUTED).grid(row=0, column=i, pady=10)
            grid.grid_columnconfigure(i, weight=1)

        # Placeholder Days for April (Simplified)
        for i in range(1, 31):
            row = (i + 2) // 7 + 1
            col = (i + 2) % 7
            ctk.CTkLabel(grid, text=str(i), font=(FONT_MONTSERRAT, 20), text_color=COLOR_TEXT_SECONDARY).grid(row=row, column=col, pady=10)

    def _setup_mood_carousel_card(self, parent):
        card = ctk.CTkFrame(parent, fg_color="transparent")
        card.pack(fill="both", expand=True)
        
        ctk.CTkLabel(card, text="Moodmu Hari Ini", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY, anchor="w").pack(fill="x")
        ctk.CTkLabel(card, text="Pilih emosi yang paling mewakilimu saat ini!", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY, anchor="w").pack(fill="x", pady=(4, 32))
        
        carousel = ctk.CTkFrame(card, fg_color="transparent")
        carousel.pack(fill="x")
        
        ctk.CTkLabel(carousel, text="<", font=(FONT_OUTFIT, 30), text_color="#E6E6E6").pack(side="left")
        
        # Mood Avatar Placeholder (matches Figma circles/rings)
        avatar_container = ctk.CTkFrame(carousel, fg_color="transparent")
        avatar_container.pack(side="left", expand=True)
        
        ctk.CTkLabel(avatar_container, text="🤩", font=(FONT_OUTFIT, 80)).pack()
        ctk.CTkLabel(avatar_container, text="Excited", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY).pack(pady=8)
        
        ctk.CTkLabel(carousel, text=">", font=(FONT_OUTFIT, 30), text_color="#A5A5A5").pack(side="right")
        
        # Yellow CTA Button
        cta = ctk.CTkButton(
            card, text="Catat", 
            image=None, # Pencil icon would go here
            fg_color=COLOR_ACCENT_YELLOW, text_color=COLOR_BROWN_ACCENT,
            font=(FONT_OUTFIT, 20), height=52, corner_radius=10,
            command=lambda: self.master.master.switch_page("WriteJournal")
        )
        cta.pack(fill="x", pady=20)

    def _setup_journal_history_section(self, parent):
        ctk.CTkLabel(parent, text="Riwayat Jurnal", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY, anchor="w").pack(fill="x", pady=(0, 24))
        
        scroll = ctk.CTkScrollableFrame(parent, orientation="horizontal", height=320, fg_color="transparent")
        scroll.pack(fill="x")
        
        # Load real entries
        entries = self.entry_controller.get_all_entries()[:5]
        if not entries:
            ctk.CTkLabel(scroll, text="Mulai menulis jurnal yuk!", font=(FONT_OUTFIT, 20)).pack(pady=100)
            return

        for entry in entries:
            card = ctk.CTkFrame(scroll, width=480, height=280, fg_color=COLOR_ACCENT_PALE, corner_radius=20)
            card.pack(side="left", padx=10)
            card.pack_propagate(False)
            
            ctk.CTkLabel(card, text=entry.getDate(), font=(FONT_OUTFIT, 25), text_color=COLOR_TEXT_PRIMARY).pack(anchor="w", padx=28, pady=(28, 5))
            ctk.CTkLabel(card, text=entry.getTitle(), font=(FONT_OUTFIT, 22, "normal"), text_color=COLOR_TEXT_SECONDARY, wraplength=420, justify="left").pack(anchor="w", padx=28)
            ctk.CTkLabel(card, text=entry.getDescription()[:100] + "...", font=(FONT_OUTFIT, 20, "normal"), text_color="black", wraplength=420, justify="left").pack(anchor="w", padx=28, pady=(8, 28))

    def _setup_today_target_card(self, parent):
        card = ctk.CTkFrame(parent, height=302, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        card.pack(fill="both")
        card.pack_propagate(False)
        
        header = ctk.CTkFrame(card, fg_color="transparent")
        header.pack(fill="x", padx=28, pady=28)
        
        ctk.CTkFrame(header, width=70, height=70, corner_radius=35, fg_color="#D9D9D9").pack(side="left")
        
        txt = ctk.CTkFrame(header, fg_color="transparent")
        txt.pack(side="left", padx=16)
        ctk.CTkLabel(txt, text="Target Hari Ini", font=(FONT_OUTFIT, 30, "normal"), text_color=COLOR_TEXT_PRIMARY, anchor="w").pack(fill="x")
        ctk.CTkLabel(txt, text="Peluk dirimu dengan kegiatan ini!", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY, anchor="w").pack(fill="x")

        # Today's Targets
        today = datetime.now().strftime("%Y-%m-%d")
        targets = self.entry_controller.get_daily_targets(user_id=1, target_date=today)
        
        list_frame = ctk.CTkFrame(card, fg_color="transparent")
        list_frame.pack(fill="both", expand=True, padx=28)
        
        if not targets:
            ctk.CTkLabel(list_frame, text="Belum ada target harian.", font=(FONT_OUTFIT, 18, "normal"), text_color=COLOR_TEXT_MUTED).pack(pady=20)
        else:
            for t in targets[:3]:
                row = ctk.CTkFrame(list_frame, fg_color="transparent")
                row.pack(fill="x", pady=4)
                
                style = "line-through" if t['completed'] else "none"
                cb_var = ctk.BooleanVar(value=bool(t['completed']))
                cb = ctk.CTkCheckBox(
                    row, text=t['label'], variable=cb_var,
                    font=(FONT_OUTFIT, 20, "normal"),
                    fg_color=COLOR_ACCENT_GREEN, border_color=COLOR_ACCENT_GREEN,
                    command=lambda t_id=t['id'], v=cb_var: self.entry_controller.toggle_target_completion(1, t_id, today, v.get())
                )
                cb.pack(side="left")
            
            if len(targets) > 3:
                ctk.CTkLabel(list_frame, text=f"{len(targets)-3} lainnya", font=(FONT_OUTFIT, 18, "normal"), text_color="black").pack(anchor="w", pady=8)
