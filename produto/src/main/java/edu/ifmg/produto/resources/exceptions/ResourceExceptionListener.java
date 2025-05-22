package edu.ifmg.produto.resources.exceptions;

import edu.ifmg.produto.services.exceptions.DatabaseException;
import edu.ifmg.produto.services.exceptions.ResourceNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

//essa classe abaixo fica sempre escutando os erros, e se o erro for do tipo ResourceNotFound ele vai executar o metodo resourceNorFound
@ControllerAdvice //rever com alguem depois, não consegui prestar atenção na aula :(
public class ResourceExceptionListener { //posso trocar listener por handler e essa classe conforme mais erros surgirem e eu precisar tratá-los

    @ExceptionHandler(ResourceNotFound.class) //quando uma excessão do tipo Resource not found acontecer o java vai acionar o metodo abaixo
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

    @ExceptionHandler(DatabaseException.class) //quando uma excessão do tipo DtabaseException acontecer o java vai acionar o metodo abaixo
    public ResponseEntity<StandartError> databaseException(DatabaseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandartError error = new StandartError();
        error.setStatus(status.value());
        error.setMessage(ex.getMessage());
        error.setError("Database exception");
        error.setTimestamp(Instant.now());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    //é importante saber o nome da exceção para tratá-la
    @ExceptionHandler(MethodArgumentNotValidException.class) //quando uma excessão do tipo DtabaseException acontecer o java vai acionar o metodo abaixo
    public ResponseEntity<ValidationError> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError error = new ValidationError();
        error.setStatus(status.value());
        error.setMessage(ex.getMessage());
        error.setError("Validation exception");
        error.setTimestamp(Instant.now());
        error.setPath(request.getRequestURI());

        for (FieldError f : ex.getBindingResult().getFieldErrors() ) {
            error.addFieldMessage(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(error);
    }
}