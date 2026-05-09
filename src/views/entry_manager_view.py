import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *

class EntryManagerView(BaseView):
    """
    Provides administrative actions (Edit/Delete) for a journal entry.
    """
    def __init__(self, master, controller, on_edit=None, on_delete=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.on_edit = on_edit
        self.on_delete = on_delete
        self.configure(fg_color="transparent")
        self._setup_ui()

    def _setup_ui(self):
        # Action Buttons Frame
        btn_frame = ctk.CTkFrame(self, fg_color=COLOR_BG_WHITE, corner_radius=10, border_width=1, border_color=COLOR_BORDER)
        btn_frame.pack(padx=20, pady=20)

        edit_btn = ctk.CTkButton(
            btn_frame, text="Edit Jurnal",
            fg_color="transparent", text_color=COLOR_TEXT_PRIMARY,
            hover_color="#F5F5F5", font=(FONT_OUTFIT, 18),
            command=self.on_edit
        )
        edit_btn.pack(fill="x", padx=10, pady=(10, 5))

        delete_btn = ctk.CTkButton(
            btn_frame, text="Hapus Jurnal",
            fg_color="transparent", text_color="#FF4D4D",
            hover_color="#FFF0F0", font=(FONT_OUTFIT, 18),
            command=self._confirm_delete
        )
        delete_btn.pack(fill="x", padx=10, pady=(5, 10))

    def _confirm_delete(self):
        # In a real app, show a popup dialog
        if self.on_delete:
            self.on_delete()

    def display_entry_options(self, entry):
        # Logic to show options for a specific entry
        pass
