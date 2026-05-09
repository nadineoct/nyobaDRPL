import customtkinter as ctk
from .base_view import BaseView
from ..utils.constants import *

class RegistrationFormView(BaseView):
    """
    Onboarding form for first-time users. Updated for high-fidelity style.
    """
    def __init__(self, master, controller, on_success=None, **kwargs):
        super().__init__(master, controller, **kwargs)
        self.on_success = on_success
        self.temp_avatar_path = None
        self.configure(fg_color=COLOR_BG_WHITE)
        self._setup_ui()

    def _setup_ui(self):
        self.grid_columnconfigure(0, weight=1)
        self.grid_rowconfigure(0, weight=1)
        self.grid_rowconfigure(2, weight=1)
        
        form_frame = ctk.CTkFrame(self, fg_color="transparent")
        form_frame.grid(row=1, column=0)
        
        title = ctk.CTkLabel(
            form_frame, text="Selamat Datang di JuK", 
            font=(FONT_OUTFIT, SIZE_LG, "bold"),
            text_color=COLOR_PRIMARY
        )
        title.pack(pady=(0, 10))
        
        subtitle = ctk.CTkLabel(
            form_frame, text="Kenalan dulu yuk! Siapa namamu?", 
            font=(FONT_OUTFIT, SIZE_SM),
            text_color=COLOR_TEXT_SECONDARY
        )
        subtitle.pack(pady=(0, 30))

        # Avatar Section
        avatar_frame = ctk.CTkFrame(form_frame, fg_color="transparent")
        avatar_frame.pack(pady=(0, 30))
        
        self.avatar_canvas = ctk.CTkFrame(avatar_frame, width=120, height=120, corner_radius=60, fg_color="#D9D9D9")
        self.avatar_canvas.pack(side="left")
        self.avatar_canvas.pack_propagate(False)
        
        ctk.CTkLabel(self.avatar_canvas, text="👤", font=(FONT_OUTFIT, 60)).place(relx=0.5, rely=0.5, anchor="center")
        
        ctk.CTkButton(
            avatar_frame, text="Pilih Avatar", width=150,
            fg_color=COLOR_BORDER, text_color=COLOR_TEXT_PRIMARY,
            font=(FONT_OUTFIT, SIZE_XS),
            command=self._pick_avatar
        ).pack(side="left", padx=20)
        
        self.name_entry = ctk.CTkEntry(
            form_frame, placeholder_text="Masukkan namamu...",
            width=500, height=60, font=(FONT_OUTFIT, SIZE_SM),
            corner_radius=10, border_color=COLOR_BORDER
        )
        self.name_entry.pack(pady=(0, 40))
        
        save_btn = ctk.CTkButton(
            form_frame, text="Mulai Menulis",
            font=(FONT_OUTFIT, SIZE_MD),
            fg_color=COLOR_PRIMARY, hover_color=COLOR_PRIMARY_DARK,
            corner_radius=10, height=60, width=250,
            command=self._on_save
        )
        save_btn.pack()

    def _pick_avatar(self):
        from tkinter import filedialog
        path = filedialog.askopenfilename(filetypes=[("Image files", "*.jpg *.jpeg *.png")])
        if path:
            self.temp_avatar_path = path
    
    def _on_save(self):
        name = self.name_entry.get().strip()
        if self.controller.submit_entry({"name": name, "avatar": self.temp_avatar_path}):
            if self.on_success:
                self.on_success()
        else:
            self.display_message("Nama tidak boleh kosong!")

    def display_message(self, msg: str):
        err_label = ctk.CTkLabel(self, text=msg, text_color=COLOR_RED_ALERT, font=(FONT_OUTFIT, SIZE_XS))
        err_label.grid(row=2, column=0, pady=10)
        self.after(3000, err_label.destroy)
