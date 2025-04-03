package edu.ifmg.produto.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionListener {

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFound ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        StandartError error = new StandartError();
        error.setStatus(status.value());
        error.setMessage(ex.getMessage());
        error.setError("Resource not found");
        error.setTimestamp(Instant.now());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);

    }

}