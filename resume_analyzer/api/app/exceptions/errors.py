class application_exception(Exception):
    """Base class for handling custom exceptions"""
    status_code = 500

    def __init__(self, message=None, status_code=None):
        super().__init__(message)
        self.message = str(message) or "An application error occurred."
        if status_code:
            self.status_code = status_code

    def to_dict(self):
        return {
            "error": self.message,
            "status": self.status_code,
        }


class path_not_found(application_exception):
    """Exception raised when the path is not found."""
    status_code = 404
    def __init__(self, message="Path not found."):
        super().__init__(message, self.status_code)


class file_not_found(application_exception):
    """Exception raised when the file is not found."""
    status_code = 404
    def __init__(self, message="File not found."):
        super().__init__(message, self.status_code)


class model_not_found(application_exception):
    """Exception raised when the model is not found."""
    status_code = 404
    def __init__(self, message="Model not found."):
        super().__init__(message, self.status_code)

