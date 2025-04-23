class path_not_found(Exception):
    """Exception raised when the path is not found."""
    def __init__(self, message="Path not found."):
        self.message = message
        super().__init__(self.message)

class file_not_found(Exception):
    """Exception raised when the file is not found."""
    def __init__(self, message="File not found."):
        self.message = message
        super().__init__(self.message)
    
class model_not_found(Exception):
    """Exception raised when the model is not found."""
    def __init__(self, message="Model not found."):
        self.message = message
        super().__init__(self.message)