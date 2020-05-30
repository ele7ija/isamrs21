package isamrs.tim21.klinika.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import isamrs.tim21.klinika.dto.CustomResponse;

@ControllerAdvice
public class VersionMismatchHanlder {
  @ExceptionHandler(value = ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<CustomResponse<Object>> handleException(ObjectOptimisticLockingFailureException e){
    //pravim SendMailErrorResponse i setujem mu message
    return new ResponseEntity<CustomResponse<Object>>(
      new CustomResponse<Object>(null, false, "Greška. Verzija entiteta je zastarela."),
      HttpStatus.OK);
  }
}