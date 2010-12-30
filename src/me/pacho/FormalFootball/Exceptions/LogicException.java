package me.pacho.FormalFootball.Exceptions;

public class LogicException extends Exception {
	String message;
	public LogicException(String message){
		super();
		this.message=message;
	}

	public String getMessage(){
		return message;
	}
}
