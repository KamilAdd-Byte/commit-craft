package pl.commit.craft.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponseCommiting errorResponseCommiting = new ErrorResponseCommiting("INVALID_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(errorResponseCommiting, HttpStatus.BAD_REQUEST);
    }
}

