import customtkinter as ctk

class BaseView(ctk.CTkFrame):
    """
    Base class for all views in JuKi.
    """
    def __init__(self, master, controller=None, **kwargs):
        super().__init__(master, **kwargs)
        self.controller = controller

    def show(self):
        self.tkraise()

    def update_display(self):
        """Override in subclasses if needed."""
        pass
