package me.pacho.FormalFootball.Exceptions;

public class LoggerException extends Exception {
	String message;
	public LoggerException(String message){
		super();
		this.message=message;
	}

	public String getMessage(){
		return message;
	}
}
