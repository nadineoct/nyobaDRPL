import customtkinter as ctk
from ..utils.constants import *

class CalendarDayDetailDialog(ctk.CTkToplevel):
    """
    High-fidelity Modal for managing daily targets.
    """
    def __init__(self, master, controller, date_str, **kwargs):
        super().__init__(master, **kwargs)
        self.controller = controller
        self.date_str = date_str
        
        self.title("")
        self.geometry("688x500")
        self.overrideredirect(True) # Borderless like Figma
        self.configure(fg_color=COLOR_BG_WHITE)
        self.attributes("-topmost", True)
        
        # Center the dialog
        self.update_idletasks()
        x = (self.winfo_screenwidth() // 2) - (688 // 2)
        y = (self.winfo_screenheight() // 2) - (500 // 2)
        self.geometry(f"+{x}+{y}")

        self._setup_ui()

    def _setup_ui(self):
        # Main Container with 55px padding
        container = ctk.CTkFrame(self, fg_color="transparent")
        container.pack(fill="both", expand=True, padx=55, pady=55)

        # Header Row
        header = ctk.CTkFrame(container, fg_color="transparent")
        header.pack(fill="x", pady=(0, 24))
        
        ctk.CTkLabel(header, text="Target Self-Care", font=(FONT_OUTFIT, 30), text_color=COLOR_TEXT_PRIMARY).pack(side="left")
        
        close_btn = ctk.CTkButton(header, text="✕", width=39, height=39, fg_color="transparent", text_color="black", font=(FONT_OUTFIT, 25), command=self.destroy)
        close_btn.pack(side="right")
        
        ctk.CTkLabel(header, text=self.date_str, font=(FONT_OUTFIT, 29), text_color=COLOR_TEXT_MUTED).pack(side="right", padx=16)

        # List Area
        self.scroll = ctk.CTkScrollableFrame(container, fg_color="transparent", height=250)
        self.scroll.pack(fill="both", expand=True)

        # Mock Targets (Real logic would fetch from controller)
        targets = self.controller.get_daily_targets(1, self.date_str)
        if not targets:
            # Show empty state with "Add" link
            self._add_empty_state()
        else:
            for t in targets:
                self._add_target_row(t)

        # Footer Actions
        footer = ctk.CTkFrame(container, fg_color="transparent")
        footer.pack(fill="x", pady=(24, 0))
        
        save_btn = ctk.CTkButton(
            footer, text="Simpan", 
            font=(FONT_OUTFIT, 25), text_color=COLOR_BROWN_ACCENT,
            fg_color=COLOR_ACCENT_YELLOW, height=52, corner_radius=12,
            command=self.destroy
        )
        save_btn.pack(fill="x")

    def _add_target_row(self, target):
        row = ctk.CTkFrame(self.scroll, fg_color="transparent")
        row.pack(fill="x", pady=8)
        
        cb_var = ctk.BooleanVar(value=bool(target['completed']))
        cb = ctk.CTkCheckBox(
            row, text=target['label'], variable=cb_var,
            font=(FONT_OUTFIT, 20, "normal"), text_color="black",
            fg_color=COLOR_ACCENT_GREEN, border_color=COLOR_ACCENT_GREEN
        )
        cb.pack(side="left")
        
        # Trash icon for delete
        del_btn = ctk.CTkButton(row, text="🗑", width=30, fg_color="transparent", text_color=COLOR_RED_ALERT, command=lambda: self._on_delete_target(target['id']))
        del_btn.pack(side="right")

    def _add_empty_state(self):
        ctk.CTkLabel(self.scroll, text="Kamu belum ada target apapun, nih!", font=(FONT_OUTFIT, 20, "normal"), text_color=COLOR_TEXT_SECONDARY).pack(pady=20)
        ctk.CTkButton(self.scroll, text="+ Tambah Target", font=(FONT_OUTFIT, 20, "normal"), text_color="#575757", fg_color="transparent").pack()

    def _on_delete_target(self, target_id):
        # Implementation for confirmation and deletion
        pass
