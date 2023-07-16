package co.bharat.sudarshansaur.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.bharat.sudarshansaur.dto.ResponseData;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<String>> handleException(Exception ex) {
    	ResponseData<String> responseData = ResponseData.<String>builder()
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).data(null).build();
		return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




