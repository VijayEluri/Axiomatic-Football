package me.pacho.FormalFootball.base;

import me.pacho.FormalFootball.util.Pair;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class Team {

	private int id;
	private boolean rotated;
	private Pair[] defensive_positions=new Pair[5];
	private Pair[] offensive_positions=new Pair[5];
	
	private ArrayList<Actions> defensive_strategy=new ArrayList<Actions>();
	private ArrayList<Actions> offensive_strategy= new ArrayList<Actions>();
	
	public Team(int id,boolean rotated) {
		this.id=id;
		this.rotated=rotated;
		String ext=".ff";
		if(rotated)ext+=".fr";
		readPositionsFromFile(defensive_positions,"./strategies/"+id+"/pdefensiva.txt"+ext);
		readPositionsFromFile(offensive_positions,"./strategies/"+id+"/pofensiva.txt"+ext);
		
		readStrategyFromFile(defensive_strategy, "./strategies/"+id+"/defensiva.txt"+ext);
		readStrategyFromFile(offensive_strategy, "./strategies/"+id+"/ofensiva.txt"+ext);
	}

	private void readPositionsFromFile(Pair[] positions,String path) {
		try{
		    // Open the file
		    FileInputStream fstream = new FileInputStream(path);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String line;
		    //Read File Line By Line
		    while ((line = br.readLine()) != null)   {
		    	if(!line.equals("")){
			    	String[] parts=line.split("=");
	
			    			    	
			    	if(parts[1].equals("a;")){
			    		positions[0]=new Pair(parts[0]);
			    	}
			    	else if(parts[1].equals("b;")){
			    		positions[1]=new Pair(parts[0]);
			    	}
			    	else if(parts[1].equals("c;")){
			    		positions[2]=new Pair(parts[0]);
			    	}
			    	else if(parts[1].equals("d;")){
			    		positions[3]=new Pair(parts[0]);
			    	}
			    	else if(parts[1].equals("e;")){
			    		positions[4]=new Pair(parts[0]);
			    	}
		    	}
		    }
		    //Close the input stream
		    in.close();
		    }catch (IOException e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		
	}
	
	
	private void readStrategyFromFile(ArrayList<Actions> strategy,String path) {
		try{
		    // Open the file
		    FileInputStream fstream = new FileInputStream(path);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String line;
		    //Read File Line By Line
		    while ((line = br.readLine()) != null)   {
		    	if(!line.equals("")){
			    	String[] parts=line.split("=");
			    	Pair key=new Pair(parts[0]);
			    	String[] actions=parts[1].split(";");
			    	strategy.add(new Actions(key, actions));
		    	}
		    }
		    //Close the input stream
		    in.close();
		    }catch (IOException e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		
	}
	public String toString(){

		String team="Team: "+id+"\n";
		//Offensive Positions
		team+="Offensive positions: ";
		for(int i=0;i<5;i++){
			team+=offensive_positions[i]+",";
		}
		//Defensive Positions
		team+="\nDefensive positions: ";
		for(int i=0;i<5;i++){
			team+=defensive_positions[i]+",";
		}
		//Offensive Strategy
		team+="\nOffensive Strategy: \n";
		System.out.println(offensive_strategy.size());
		for(int i=0;i<offensive_strategy.size();i++){
			team+="\t"+offensive_strategy.get(i)+"\n";
		}
		//Defensive Strategy
		team+="\nDefensive Strategy: \n";
		for(int i=0;i<defensive_strategy.size();i++){
			team+="\t"+defensive_strategy.get(i)+"\n";
		}
		return team; 
	}
	
	public String[] getActionsFromKey(Pair key, boolean attacking){
		if(attacking){
			return getActionsFromKeyAndArray(key, offensive_strategy);
		}
		else{
			return getActionsFromKeyAndArray(key, defensive_strategy);
		}
	}
	
	public String[] getActionsFromKeyAndArray(Pair key, ArrayList<Actions> array){
		for(int i=0;i<array.size();i++){
			Actions actual=array.get(i);
			if(key.equals(actual.getKey())) return actual.getActions();
		}
		//ninguna accion
		return new String[0];
	}
	
	public int getId() {
		return id;
	}

	public Pair[] getDefensive_positions() {
		return defensive_positions;
	}

	public Pair[] getOffensive_positions() {
		return offensive_positions;
	}

	public ArrayList<Actions> getDefensive_strategy() {
		return defensive_strategy;
	}

	public ArrayList<Actions> getOffensive_strategy() {
		return offensive_strategy;
	}
	
	public Pair getOpponentArc(){
		if(rotated) return new Pair(1,0);
		else return new Pair(1,4);
	}
	public Pair getOwnArc(){
		if(!rotated) return new Pair(1,0);
		else return new Pair(1,4);
	}
}
