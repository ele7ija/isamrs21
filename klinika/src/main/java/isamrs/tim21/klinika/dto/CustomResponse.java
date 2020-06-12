package isamrs.tim21.klinika.dto;

public class CustomResponse<T> {
	private T result;
	private boolean success;
	private String message;
	
	public CustomResponse(){}

	public CustomResponse(T result, boolean success, String message){
		this.result = result;
		this.success = success;
		this.message = message;
	}
	
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
