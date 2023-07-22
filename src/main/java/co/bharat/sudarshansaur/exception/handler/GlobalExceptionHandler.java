package co.bharat.sudarshansaur.exception.handler;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.bharat.sudarshansaur.dto.ResponseData;

@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class,SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ResponseData<String>> handleConstraintViolationException(RuntimeException ex) {
    	ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.CONFLICT.value()).message(ex.getLocalizedMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseData<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
    	ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
    	ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




