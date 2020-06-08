package isamrs.tim21.klinika.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.hibernate.PessimisticLockException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import isamrs.tim21.klinika.dto.CustomResponse;

@ControllerAdvice
public class PessimisticLockExceptionHandler {
  @ExceptionHandler(value = PessimisticLockException.class)
  public ResponseEntity<CustomResponse<Object>> handleException(PessimisticLockException e){
    //pravim SendMailErrorResponse i setujem mu message
    return new ResponseEntity<CustomResponse<Object>>(
      new CustomResponse<Object>(null, false, "Greška. Pokušajte ponovo."),
      HttpStatus.OK);
  }
}