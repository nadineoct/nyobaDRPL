import customtkinter as ctk
from ..utils.constants import *

class NavBar(ctk.CTkFrame):
    """
    High-fidelity Navigation Bar mapped from Figma/HTML.
    """
    def __init__(self, master, current_page="Beranda", on_navigate=None, **kwargs):
        bg_color = COLOR_PRIMARY_DARK if current_page == "Beranda" else COLOR_PRIMARY
        super().__init__(master, height=115, corner_radius=0, fg_color=bg_color, **kwargs)
        self.on_navigate = on_navigate
        self.current_page = current_page
        self._setup_ui()

    def _setup_ui(self):
        # Container for content with 100px horizontal padding
        container = ctk.CTkFrame(self, fg_color="transparent")
        container.pack(fill="both", expand=True, padx=100)

        # LEFT SIDE: Logo + Divider + Search (if applicable)
        left_group = ctk.CTkFrame(container, fg_color="transparent")
        left_group.pack(side="left", fill="y")

        logo_label = ctk.CTkLabel(
            left_group, text="JuK", 
            font=(FONT_OUTFIT, 50, "bold"), 
            text_color=COLOR_BG_WHITE
        )
        logo_label.pack(side="left")
        
        divider = ctk.CTkFrame(left_group, width=7, height=35, fg_color=COLOR_PURPLE_LIGHT, corner_radius=0)
        divider.pack(side="left", padx=(32, 20))

        if self.current_page in ["Jurnal", "Search", "EntryDetail"]:
            search_frame = ctk.CTkFrame(
                left_group, width=581, height=64, 
                fg_color=COLOR_BG_WHITE, corner_radius=32
            )
            search_frame.pack(side="left", padx=10)
            search_frame.pack_propagate(False)

            # Search Icon Placeholder (using Unicode)
            ctk.CTkLabel(search_frame, text="🔍", font=(FONT_OUTFIT, 22), text_color="#A5A5A5").pack(side="left", padx=(24, 10))
            
            self.search_entry = ctk.CTkEntry(
                search_frame, placeholder_text="Cari Jurnal",
                font=(FONT_OUTFIT, 22, "normal"),
                text_color=COLOR_TEXT_PRIMARY,
                fg_color="transparent", border_width=0
            )
            self.search_entry.pack(side="left", fill="both", expand=True)
            self.search_entry.bind("<Return>", self._on_search_submit)

        # RIGHT SIDE: Nav Links + CTA Button
        right_group = ctk.CTkFrame(container, fg_color="transparent")
        right_group.pack(side="right", fill="y")

        pages = [("Beranda", "Beranda"), ("Jurnal", "Jurnal"), ("Kalendar", "Kalendar")]
        for label, page_name in pages:
            is_active = self.current_page == page_name
            weight = "bold" if is_active else "normal"
            color = "#FDF3FF" if is_active else "#F2F6FC"
            
            link = ctk.CTkButton(
                right_group, text=label,
                font=(FONT_OUTFIT, 25, weight),
                text_color=color,
                fg_color="transparent",
                hover_color=COLOR_PRIMARY_DARK,
                width=100,
                command=lambda p=page_name: self.on_navigate(p) if self.on_navigate else None
            )
            link.pack(side="left", padx=32)

        # Dynamic CTA Button
        cta_label = "Tulis Jurnal"
        if self.current_page == "Kalendar":
            cta_label = "Tambah Self-Care"
            
        cta_btn = ctk.CTkButton(
            right_group, text=cta_label,
            font=(FONT_OUTFIT, 25),
            text_color=COLOR_PRIMARY,
            fg_color=COLOR_BG_WHITE,
            hover_color="#EEEEEE",
            corner_radius=10,
            height=60,
            width=200,
            command=self._on_cta_click
        )
        cta_btn.pack(side="left", padx=(32, 0))

    def _on_search_submit(self, event):
        if self.on_navigate:
            keyword = self.search_entry.get().strip()
            self.on_navigate("Search", keyword=keyword)

    def _on_cta_click(self):
        if self.on_navigate:
            if self.current_page == "Kalendar":
                # Special handle for self-care could go here
                self.on_navigate("WriteJournal") 
            else:
                self.on_navigate("WriteJournal")
