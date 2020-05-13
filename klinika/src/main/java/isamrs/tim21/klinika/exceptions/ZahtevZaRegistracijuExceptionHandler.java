package isamrs.tim21.klinika.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ZahtevZaRegistracijuExceptionHandler {
  
  @ExceptionHandler
  public ResponseEntity<SendMailErrorResponse> handleException(SendMailException e){
    //pravim SendMailErrorResponse i setujem mu message
    SendMailErrorResponse error = new SendMailErrorResponse();
    error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage(e.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}