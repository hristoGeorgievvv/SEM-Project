package nl.tudelft.sem.sem54.fridge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(createBody(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductExistsException.class)
    public ResponseEntity<Object> handleProductExistsException(
            ProductExistsException ex, WebRequest request) {
        return new ResponseEntity<>(createBody(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(createBody(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotUndoException.class)
    public ResponseEntity<Object> handleCannotUndoException(
            CannotUndoException ex, WebRequest request) {
        return new ResponseEntity<>(createBody(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncompleteRequestException.class)
    public ResponseEntity<Object> handleIncompleteRequestException(
            CannotUndoException ex, WebRequest request) {
        return new ResponseEntity<>(createBody(ex), HttpStatus.BAD_REQUEST);

    }

    private Map<String, Object> createBody(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return body;
    }
}
