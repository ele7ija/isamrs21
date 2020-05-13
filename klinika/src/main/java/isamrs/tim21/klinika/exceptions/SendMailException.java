package isamrs.tim21.klinika.exceptions;

public class SendMailException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SendMailException(String message, Throwable cause) {
    super(message, cause);
  }
  public SendMailException(String message){
    super(message);
  }
  public SendMailException(Throwable cause){
    super(cause);
  }
}