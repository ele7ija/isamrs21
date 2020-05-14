package isamrs.tim21.klinika.exceptions;

public class SendMailErrorResponse {
  private int status;
  private String message;

  public SendMailErrorResponse() {}


  public SendMailErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  
}