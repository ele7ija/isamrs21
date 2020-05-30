package isamrs.tim21.klinika.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import isamrs.tim21.klinika.dto.CustomResponse;

@ControllerAdvice
public class BusinessLogicExceptionHandler {
  
  @ExceptionHandler(value = BusinessLogicException.class)
  public ResponseEntity<CustomResponse<Object>> handleException(BusinessLogicException e){
    return new ResponseEntity<CustomResponse<Object>>(
      new CustomResponse<Object>(null, false, e.getMessage()),
      HttpStatus.OK);
  }
}