package me.pacho.FormalFootball.base;
import me.pacho.FormalFootball.Exceptions.LogicException;
import me.pacho.FormalFootball.util.Pair;

public class State {

	
	/*	e0: positions of the plays of the first team
		e1: positions of the plays of the second team
		ball_owner: the team in which control is the ball
		ball_position: x,y where the ball is
		match_score: score of the match
		match_timing: part, plays	*/	
	private Pair[] team1_positions=new Pair[5];
	private Pair[] team0_positions=new Pair[5];
	private int team1_id;
	private int team0_id;
	private int ball_owner;
	private Pair ball_position;
	private Pair score;
	private int time;
	private int turns;
	
	
	public State(Pair[] team0_positions,int team0_id, Pair[] team1_positions,int team1_id,
			int ball_owner, Pair ball_position, Pair score, int time, int turns) {
		super();
		this.team0_id=team0_id;
		this.team1_id=team1_id;
		this.team1_positions = team1_positions;
		this.team0_positions = team0_positions;
		this.ball_owner = ball_owner;
		this.ball_position = ball_position;
		this.score = score;
		this.turns = turns;
		this.time = time;
	}
	
	public State(State s){
		super();
		//DEEP COPY
		//Objects
			for(int i=0;i<team0_positions.length;i++){
				this.team0_positions[i]=new Pair(s.getTeam0_positions()[i]);
			}
			for(int i=0;i<team1_positions.length;i++){
				this.team1_positions[i]=new Pair(s.getTeam1_positions()[i]);
			}
		 
			
		this.ball_position = new Pair(s.getBall_position());
		this.score = new Pair(s.getScore());

		//Primitives
			this.ball_owner = s.getBall_owner();
			
			this.turns = s.getTurns();
			this.time = s.getTime();
			
			this.team0_id=s.getTeam0_id();
			this.team1_id=s.getTeam1_id();
	}
	
	private int getTeam0_id() {
		return team0_id;
	}
	
	private int getTeam1_id() {
		return team1_id;
	}

	public boolean testInvariant() throws LogicException{
		//System.out.println("INVARIANTE");
		
		//Revisar que los arqueros estŽn en su sitio
		if(team0_positions[0].a!=1 && team0_positions[0].b!=0){
			throw new LogicException("El arquero del equipo "+team0_id+" se encuentra en una posici—n invalida: "+team0_positions[0]);
		}
		if(team1_positions[0].a!=1 && team1_positions[0].b!=0){
			throw new LogicException("El arquero del equipo "+team1_id+" se encuentra en una posici—n invalida: "+team1_positions[0]);
		}
		//Revisar que todos los jugadores estandar esten dentro de los limites de su campo
		for(int i=1;i<team0_positions.length;i++){
			if(team0_positions[i].a<0 || team0_positions[i].a>2 || team0_positions[i].b<1 || team0_positions[i].b>3) {
				throw new LogicException("El jugador del equipo "+team0_id+" con indice "+i+" se encuentra por fuera del espacio permitido, en la posici—n "+team0_positions[i]);
			}
		}
		for(int i=1;i<team1_positions.length;i++){
			if(team1_positions[i].a<0 || team1_positions[i].a>2 || team1_positions[i].b<1 || team1_positions[i].b>3) {
				throw new LogicException("El jugador del equipo "+team1_id+" con indice "+i+" se encuentra por fuera del espacio permitido, en la posici—n "+team1_positions[i]);
			}
		}

		//Revisar que no haya 2 o m‡s jugadores del mismo equipo en el mismo lugar
		for(int i=0;i<team0_positions.length;i++){
			for(int j=i+1;j<team0_positions.length;j++){
				if(team0_positions[i].equals(team0_positions[j])) {
					throw new LogicException("Los jugadores del equipo "+team0_id+" con indices "+i+","+j+" se encuentran en la misma posici—n del campo: "+team0_positions[i]);
				}
			}
		}
		
		for(int i=0;i<team1_positions.length;i++){
			for(int j=i+1;j<team1_positions.length;j++){
				if(team1_positions[i].equals(team1_positions[j])) {
					throw new LogicException("Los jugadores del equipo "+team1_id+" con indices "+i+","+j+" se encuentran en la misma posici—n del campo: "+team1_positions[i]);
				}
			}
		}
		//Revisar que el balon estŽ en el campo y no estŽ s—lo
		if(ball_position.a<0||ball_position.a>2||ball_position.b<0||ball_position.b>4){
			throw new LogicException("El bal—n se encuentra por fuera de los limites del campo: "+ball_position);
		}
		boolean sbHasTheBall=false;
		Pair[] attacker_positions;
		if(ball_owner==team0_id){
			attacker_positions=team0_positions;
		}
		else{
			attacker_positions=team1_positions;
		}
		
		for(int i=0;i<attacker_positions.length;i++){
			if(attacker_positions[i].equals(ball_position)) {
				//System.out.println("WHO: "+i);
				sbHasTheBall=true;
			}
		}
		if(!sbHasTheBall)throw new LogicException("Ninguno de los jugadores atacantes tiene el balon: "+this);
		
		return true;
	}


	//GETERS
	public Pair[] getTeam1_positions() {
		return team1_positions;
	}

	public Pair[] getTeam0_positions() {
		return team0_positions;
	}

	public int getBall_owner() {
		return ball_owner;
	}

	public Pair getBall_position() {
		return ball_position;
	}

	public Pair getScore() {
		return score;
	}

	public int getTurns() {
		return turns;
	}

	public int getTime() {
		return time;
	}
	//SETTERS
	public void setTeam1_positions(Pair[] team1_positions) {
		this.team1_positions = team1_positions;
	}

	public void setTeam0_positions(Pair[] team0_positions) {
		this.team0_positions = team0_positions;
	}

	public void setBall_owner(int ball_owner) {
		this.ball_owner = ball_owner;
	}

	public void setBall_position(Pair ball_position) {
		this.ball_position = ball_position;
	}

	public void setScore(Pair score) {
		this.score = score;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	//toString
	public String toString(){
		String state="";
		//Positions of the first team
		for(int i=0;i<5;i++){
			state+=team0_positions[i]+",";
		}
		//Positions of the second team
		for(int i=0;i<5;i++){
			state+=team1_positions[i]+",";
		}
		//Ball info
		state+=ball_position+",";
		//state+=ball_owner+",";
		//Score
		state+=score+",";
		//Timing
		state+=time+","+turns;
		return state; 
	}

	public String nicePrint() {
		String state="STATE:\n\t"+team0_id+": ";
		//Positions of the first team
		for(int i=0;i<5;i++){
			state+="("+team0_positions[i]+"),";
		}
		//Positions of the second team
		state+="\n\t"+team1_id+": ";
		for(int i=0;i<5;i++){
			state+="("+team1_positions[i]+"),";
		}
		//Ball info
		state+="\n\t ball pos: "+ball_position;
		state+="\n\t ball owner: "+ball_owner;
		//Score
		state+="\n\t score:"+score;
		//Timing
		state+="\n\t time/turns:"+time+","+turns;
		return state; 
	}
	

	
}
