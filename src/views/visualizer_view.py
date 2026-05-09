import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

class VisualizerView(BaseView):
    """
    Detailed visualization of emotional trends and self-care statistics.
    """
    def __init__(self, master, controller, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        # Header
        header = ctk.CTkFrame(self, fg_color="transparent")
        header.pack(fill="x", padx=100, pady=(50, 40))
        
        ctk.CTkLabel(header, text="Analisis Tren Mood", font=(FONT_OUTFIT, 30, "bold"), text_color=COLOR_PRIMARY).pack(side="left")
        
        # Period Filter
        self.period_var = ctk.StringVar(value="7 Hari Terakhir")
        self.period_menu = ctk.CTkOptionMenu(
            header, values=["7 Hari Terakhir", "30 Hari Terakhir", "Bulan Ini", "Tahun Ini"],
            variable=self.period_var, fg_color=COLOR_ACCENT_YELLOW, text_color=COLOR_BROWN_ACCENT,
            command=lambda _: self.update_display()
        )
        self.period_menu.pack(side="right")

        # Stats Cards
        stats_frame = ctk.CTkFrame(self, fg_color="transparent")
        stats_frame.pack(fill="x", padx=100, pady=(0, 40))
        
        self.avg_mood_card = self._create_stat_card(stats_frame, "Rata-rata Mood", "Neutral")
        self.success_rate_card = self._create_stat_card(stats_frame, "Keberhasilan Self-care", "0%")
        
        # Chart Area
        self.chart_container = ctk.CTkFrame(self, fg_color=COLOR_BG_WHITE, corner_radius=20, border_width=1, border_color=COLOR_BORDER)
        self.chart_container.pack(fill="both", expand=True, padx=100, pady=(0, 50))
        
        self.update_display()

    def _create_stat_card(self, parent, label, value):
        card = ctk.CTkFrame(parent, fg_color=COLOR_ACCENT_PALE, corner_radius=15, height=120)
        card.pack(side="left", fill="x", expand=True, padx=10)
        card.pack_propagate(False)
        
        ctk.CTkLabel(card, text=label, font=(FONT_OUTFIT, 18), text_color=COLOR_BROWN_ACCENT).pack(pady=(20, 5))
        val_label = ctk.CTkLabel(card, text=value, font=(FONT_OUTFIT, 30, "bold"), text_color=COLOR_BROWN_ACCENT)
        val_label.pack()
        return val_label

    def update_display(self):
        # Determine date range based on filter
        end_date = datetime.now().date()
        period = self.period_var.get()
        if period == "7 Hari Terakhir":
            start_date = end_date - timedelta(days=6)
        elif period == "30 Hari Terakhir":
            start_date = end_date - timedelta(days=29)
        elif period == "Bulan Ini":
            start_date = end_date.replace(day=1)
        else: # Tahun Ini
            start_date = end_date.replace(month=1, day=1)
            
        self.controller.filter_by_date(start_date.strftime("%Y-%m-%d"), end_date.strftime("%Y-%m-%d"))
        dates, scores = self.controller.get_visualization()
        
        # Update Stats Cards
        avg_score = self.controller.calculate_average_mood()
        mood_labels = ["Angry", "Sad", "Neutral", "Happy", "Excited"]
        avg_mood_text = mood_labels[round(avg_score)-1] if avg_score > 0 else "N/A"
        self.avg_mood_card.configure(text=avg_mood_text)
        
        success_rate = self.controller.calculate_self_care_rate()
        self.success_rate_card.configure(text=f"{int(success_rate)}%")
        
        # Clear existing chart
        for child in self.chart_container.winfo_children():
            child.destroy()
            
        if not dates:
            ctk.CTkLabel(self.chart_container, text="Tidak ada data untuk periode ini.", font=(FONT_OUTFIT, 20)).place(relx=0.5, rely=0.5, anchor="center")
            return

        fig, ax = plt.subplots(figsize=(10, 5), dpi=100)
        fig.patch.set_facecolor('white')
        
        # Convert string dates to abbreviated format for axis
        display_dates = [datetime.strptime(d, "%Y-%m-%d").strftime("%d %b") for d in dates]
        
        ax.plot(display_dates, scores, marker='o', color=COLOR_PRIMARY, linewidth=3, markersize=8)
        ax.fill_between(display_dates, scores, color=COLOR_PRIMARY, alpha=0.1)
        
        ax.set_ylim(0, 6)
        ax.set_yticks([1, 2, 3, 4, 5])
        ax.set_yticklabels(['Angry', 'Sad', 'Neutral', 'Happy', 'Excited'])
        
        ax.spines['top'].set_visible(False)
        ax.spines['right'].set_visible(False)
        plt.xticks(rotation=45)
        
        canvas = FigureCanvasTkAgg(fig, master=self.chart_container)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=30, pady=30)

    def display_visualization(self):
        self.update_display()

    def show_notification(self):
        pass
