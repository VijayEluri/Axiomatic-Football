package me.pacho.FormalFootball.Exceptions;

public class NonExistentTokenException extends Exception {
	String message;
	public NonExistentTokenException(String message){
		super();
		this.message=message;
	}

	public String getMessage(){
		return message;
	}
}
