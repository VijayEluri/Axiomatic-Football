package me.pacho.FormalFootball.base;

import java.util.ArrayList;

import me.pacho.FormalFootball.util.Pair;

public class Actions {
	private Pair key;
	private String[] actions;
	
	public Actions(Pair key, String[] actions) {
		super();
		this.key = key;
		this.actions = actions;
	}
	public Pair getKey() {
		return key;
	}
	public void setKey(Pair key) {
		this.key = key;
	}
	public String[] getActions() {
		return actions;
	}
	public void setActions(String[] actions) {
		this.actions = actions;
	}
	
	public String toString(){
		String result=key+"=";
		
		for(int i=0;i<actions.length;i++){
			result+=actions[i]+";";
		}
		return result;
	}
}
