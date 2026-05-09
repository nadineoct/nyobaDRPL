import customtkinter as ctk
from ..utils.constants import *

class MoodAvatar(ctk.CTkCanvas):
    """
    Custom Mood Avatar drawing rings and faces to match Figma specs.
    """
    def __init__(self, master, mood_label="Neutral", size=100, bg="white", **kwargs):
        super().__init__(master, width=size, height=size, bg=bg, highlightthickness=0, **kwargs)
        self.mood_label = mood_label
        self.size = size
        self.draw()

    def draw(self):
        self.delete("all")
        s = self.size
        center = s / 2
        
        # Base Ring and Face logic based on mood
        if self.mood_label == "Excited":
            # Outer Ring
            self.create_oval(s*0.1, s*0.1, s*0.9, s*0.9, outline="#FFCE2B", width=s*0.08)
            # Eyes (Arcs)
            self.create_arc(s*0.25, s*0.35, s*0.45, s*0.5, start=0, extent=180, style="arc", width=4)
            self.create_arc(s*0.55, s*0.35, s*0.75, s*0.5, start=0, extent=180, style="arc", width=4)
            # Mouth (Big Smile)
            self.create_arc(s*0.3, s*0.5, s*0.7, s*0.8, start=180, extent=180, fill="black")
        
        elif self.mood_label == "Happy":
            self.create_oval(s*0.1, s*0.1, s*0.9, s*0.9, outline=COLOR_ACCENT_GREEN, width=s*0.08)
            # Eyes (Dots)
            self.create_oval(s*0.3, s*0.35, s*0.4, s*0.45, fill="black")
            self.create_oval(s*0.6, s*0.35, s*0.7, s*0.45, fill="black")
            # Mouth (Curve)
            self.create_arc(s*0.35, s*0.5, s*0.65, s*0.75, start=180, extent=180, style="arc", width=4)

        elif self.mood_label == "Sad":
            self.create_oval(s*0.1, s*0.1, s*0.9, s*0.9, outline="#00A3FE", width=s*0.08)
            # Eyes (Dots)
            self.create_oval(s*0.3, s*0.35, s*0.4, s*0.45, fill="black")
            self.create_oval(s*0.6, s*0.35, s*0.7, s*0.45, fill="black")
            # Mouth (Frown)
            self.create_arc(s*0.35, s*0.6, s*0.65, s*0.8, start=0, extent=180, style="arc", width=4)

        elif self.mood_label == "Angry":
            self.create_oval(s*0.1, s*0.1, s*0.9, s*0.9, outline=COLOR_RED_ALERT, width=s*0.08)
            # Eyes (Angry slant)
            self.create_line(s*0.25, s*0.3, s*0.45, s*0.45, width=4)
            self.create_line(s*0.75, s*0.3, s*0.55, s*0.45, width=4)
            # Mouth (Straight)
            self.create_line(s*0.35, s*0.7, s*0.65, s*0.7, width=4)

        else: # Neutral
            self.create_oval(s*0.1, s*0.1, s*0.9, s*0.9, outline="#D6D6D6", width=s*0.08)
            # Eyes (Dots)
            self.create_oval(s*0.3, s*0.35, s*0.4, s*0.45, fill="black")
            self.create_oval(s*0.6, s*0.35, s*0.7, s*0.45, fill="black")
            # Mouth (Straight)
            self.create_line(s*0.35, s*0.65, s*0.65, s*0.65, width=4)
