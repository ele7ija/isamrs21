package isamrs.tim21.klinika.exceptions;

public class BusinessLogicException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BusinessLogicException(String message, Throwable cause) {
    super(message, cause);
  }
  public BusinessLogicException(String message){
    super(message);
  }
  public BusinessLogicException(Throwable cause){
    super(cause);
  }
}