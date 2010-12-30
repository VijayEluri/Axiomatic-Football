package me.pacho.FormalFootball.util;

public class Pair {
	//A class to model a Pair in the form a,b
	public int a;
	public int b;
	public Pair(int a, int b) {
		this.a = a;
		this.b = b;
	}		
	public Pair(String string) {
		String[] parts=string.split(",");
		a=Integer.parseInt(parts[0]);
		b=Integer.parseInt(parts[1]);
	}
	public Pair(Pair p) {
		a=p.a;
		b=p.b;
	}
	public String toString(){
		return a+","+b;
	}
	public boolean equals(Pair other){
		if(a==other.a && b==other.b)return true;
		return false;
	}
	public Pair add(Pair other){
		return new Pair(a+other.a,b+other.b);
	}

}
