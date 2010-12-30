package me.pacho.FormalFootball.base;

import java.util.Random;

import me.pacho.FormalFootball.Exceptions.LoggerException;
import me.pacho.FormalFootball.Exceptions.LogicException;
import me.pacho.FormalFootball.Exceptions.NonExistentTokenException;
import me.pacho.FormalFootball.util.Logger;
import me.pacho.FormalFootball.util.Pair;

public class FormalFootball {
	public final static int PARTS=2;
	public final static int TIME=25;
	
	//Probabilidad de anotar si la distancia al arco contrario es 1,2,3,4
	public final static float[] PTIRAR={0.15f,0.10f,0.05f,0.02f};
	//Probabilidad de mantener en control el balon al moverlo  si la distancia al arco propio es 1,2,3,4
	public final static float[] PMOVER={1f,0.7f,0.5f,0.3f};
	
	private Team team0;
	private Team team1;
	
	private State S;
	private Logger logger;
	//To Print in console
	private boolean verbose=false;
	public FormalFootball(int t0,int t1){
		team0=new Team(t0,false);
		team1=new Team(t1,true);
		logger=new Logger(t0+"_"+t1);
		try {
			play();
		} catch (LoggerException e) {
			e.printStackTrace();
		}
		//print(team0);
		//print(team1);
	}




	private void play() throws LoggerException {
		S=new State(team0.getOffensive_positions(),team0.getId(),team1.getDefensive_positions(),team1.getId(),team0.getId(),team0.getOwnArc(),new Pair(0,0),0,0);

		for(int i=0;i<PARTS;i++){
			//Inicializaci—n de cada tiempo
			if(i%2==0){
				S.setBall_owner(team0.getId());
				S.setBall_position(team0.getOwnArc());
				S.setTeam0_positions(team0.getOffensive_positions());
				S.setTeam1_positions(team1.getDefensive_positions());				
			}
			else{
				S.setBall_owner(team1.getId());
				S.setBall_position(team1.getOwnArc());
				S.setTeam0_positions(team0.getDefensive_positions());
				S.setTeam1_positions(team1.getOffensive_positions());
			}

			S.setTime(i);
			S.setTurns(0);
			//Comienzo del tiempo
			logger.addState(S.toString());
			logger.addAction(team0.getId()+",,"+Logger.COMIENZO);
			logger.addAction(team1.getId()+",,"+Logger.COMIENZO);
			logger.closeTurn();
			for(int j=0;j<TIME;j++){
				applyStrategy(defensor(),defensor().getActionsFromKey(S.getBall_position(), false));
				applyStrategy(attacker(),attacker().getActionsFromKey(S.getBall_position(), true));
				S.setTurns(j);
			}
			
			print("FIN DE TIEMPO!");
		}
		//fin del partido
		if(S.getScore().a>S.getScore().b){
			logger.addAction(team0.getId()+",,"+Logger.GANAR);
			logger.addAction(team1.getId()+",,"+Logger.PERDER);
		}
		else if(S.getScore().a<S.getScore().b){
			logger.addAction(team0.getId()+",,"+Logger.PERDER);
			logger.addAction(team1.getId()+",,"+Logger.GANAR);
		}
		else{
			logger.addAction(team0.getId()+",,"+Logger.PERDER);
			logger.addAction(team1.getId()+",,"+Logger.PERDER);
		}
		logger.closeTurn();
		
		//Save the score to the for the csv file.
		int winner=-1;
		if(S.getScore().a>S.getScore().b)winner=team0.getId();
		if(S.getScore().b>S.getScore().a)winner=team1.getId();
		logger.setScore("\n"+team0.getId()+","+team1.getId()+","+S.getScore()+","+winner);
		logger.saveLog();
	}



	private void applyStrategy(Team team,String[] actions) throws LoggerException {
		//Revisar que durante el turno el bal—n siga estando en poder del mismo equipo
		int attacker=attacker().getId();
		for(int i=0;i<actions.length && attacker==attacker().getId();i++){
			State S1=new State(S);
			String actual=actions[i];
			String[] parameters=actual.split(",");
			print("Team "+team.getId()+", Action: "+actual);
			try{
				//Check the team of the strategy is the ball owner for certain actions
				if(team.getId()==S1.getBall_owner()){
					//tirar
					if(actual.charAt(0)=='t'){
						S1=tirar(team,S1);
					}
					//pasar
					if(actual.charAt(0)=='p'){
						S1=pasar(S1,parameters[1].charAt(0));
					}
					//andarConBalon
					if(actual.charAt(0)=='n'){
						S1=andarConBalon(S1,parameters[1].charAt(0));
					}
					
				}
				//correrSinBalon
				if(actual.charAt(0)=='o'){
					S1=correrSinBalon(S1,team,parameters[1].charAt(0),parameters[2].charAt(0));
				}
				if(S1.testInvariant());S=S1;//Si no se cumple la invarinte no hace la asignaci—n
				
			}
			catch(NonExistentTokenException e){
				print("NonExistentTokenException: "+e.getMessage());
				//e.printStackTrace();
			}
			catch(LogicException e){
				print("LogicException: "+e.getMessage());
				//e.printStackTrace();
				}
			finally{
				print(S.nicePrint());
			}
		}
		if(actions.length==0){
			print("No strategy: Team "+team.getId()+" doesn't have actions for the ball position"+S.getBall_position());
			logger.addAction(team.getId()+","+selectRandomPlayer()+","+Logger.REPOSO);
		}


		logger.addState(S.toString());
		logger.closeTurn();
		
	}


	private char selectRandomPlayer(){
		Random ran=new Random();
		try {
			return playerFromindex(ran.nextInt(5));
		} catch (NonExistentTokenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 'a';
	}

	private State correrSinBalon(State s, Team team, char jugador, char direction) throws NonExistentTokenException, LogicException {
		State s1=new State(s);
		int player_index=indexFromPlayer(jugador);		
		Pair direction_pair=pairFromDirection(direction);
		Pair player_new_position=getPositionsFromId(team.getId(), s1)[player_index].add(direction_pair);
		
		//Si no hay nadie de su equipo puede ir
		if(!isThereSomePlayerOfThisTeamHere(s1, team, player_new_position)){
			s1=changePlayerPosition(s1, player_index, team.getId(), direction_pair);
			logger.addAction(team.getId()+","+jugador+","+Logger.CORRER);
			//Si no tengo el bal—n y en la casilla a la que llego est‡ testMyLuck.
			if(team.getId()==defensor().getId() && s1.getBall_position().equals(player_new_position)){
				int distance=Math.abs(team.getOpponentArc().b-s1.getBall_position().b);
				if(!testMyLuck(PMOVER[distance-1])) {
					s1=changeBallOwner(s1);
					//Si gana el balon
					logger.addAction(team.getId()+","+jugador+","+Logger.RECIBIR);
				}
				
			}
		}
		else{
			throw new LogicException("El jugador "+jugador+" iba a ocupar una posici—n ya ocupada por uno de su equipo: "+player_new_position);
		
		}
		return s1;
	}

	

	private State andarConBalon(State s, char direction) throws NonExistentTokenException {
		State s1=new State(s);
		Pair direction_pair=pairFromDirection(direction);
		Pair new_ball_position=s1.getBall_position().add(direction_pair);
		int ball_owner=indexOfPlayerWhoHasTheBall(s1);
		
		//Si hay alguien pasa
		if(isThereSomePlayerOfThisTeamHere(s1,attacker(),new_ball_position)){
			s1=pasar(s1,direction);
		}
		//Si no, se mueve y mueve el balon
		else{
			s1=changePlayerPosition(s1, ball_owner, attacker().getId(), direction_pair);
			s1.setBall_position(new_ball_position);
			int distance=Math.abs(defensor().getOwnArc().b-s1.getBall_position().b);
			logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.ANDAR);
			//En caso de perder el balon
			if(testMyLuck(PMOVER[distance-1])){
				s1=changeBallOwner(s1);
				logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.RECIBIR);
			}
		}
		return s1;
	}

	public State changePlayerPosition(State s, int player_index, int team, Pair direction_pair){
		State s1=new State(s);
		Pair[] team_positions;
		if(team==team0.getId()){
			team_positions=s1.getTeam0_positions();
			team_positions[player_index]=team_positions[player_index].add(direction_pair);
			s1.setTeam0_positions(team_positions);
		}
		else{
			team_positions=s1.getTeam1_positions();
			team_positions[player_index]=team_positions[player_index].add(direction_pair);
			s1.setTeam1_positions(team_positions);
		}
		return s1;
	}


	private boolean isThereSomePlayerOfThisTeamHere(State s1,Team team, Pair position) {
		Pair[] team_positions;
		if(team.getId()==team0.getId()){
			team_positions=s1.getTeam0_positions();
		}
		else{
			team_positions=s1.getTeam1_positions();
		}
		for(int i=0;i<team_positions.length;i++){
			if(team_positions[i].equals(position))return true;
		}
		return false;
	}




	private State pasar(State s, char direction) throws NonExistentTokenException {
		State s1=new State(s);
		Pair new_ball_position=s1.getBall_position().add(pairFromDirection(direction));
		if(isThereSomePlayerOfThisTeamHere(s1,attacker(), new_ball_position)){
			logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.PASAR);
			s1.setBall_position(new_ball_position);

			//si hay alguien del equipo contrario cubriendo
			if(isThereSomePlayerOfThisTeamHere(s1, defensor(), new_ball_position)){
				int distance=Math.abs(attacker().getOwnArc().b-s1.getBall_position().b);
				if(testMyLuck(PMOVER[distance-1])) {
					s1=changeBallOwner(s1);
					//Despues del cambio Ahora es el atacante
					logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.RECIBIR);
				}
				else{
					//Sigue atacando
					logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.RECIBIR);					
				}
			}
			else{
				//Sigue atacando
				logger.addAction(attacker().getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.RECIBIR);					
			}
		}
		//Si no hay nadie tira al arco
		else{
			s1=tirar(attacker(),s1);
		}
		return s1;
	}




	private State tirar(Team t,State s) throws NonExistentTokenException {
		State s1=new State(s);
		//b in pairs =y in coordinates
		int distance=Math.abs(t.getOpponentArc().b-s1.getBall_position().b);
		logger.addAction(t.getId()+","+playerFromindex(indexOfPlayerWhoHasTheBall(s1))+","+Logger.TIRAR);
		s1.setBall_position(t.getOpponentArc());
		if(testMyLuck(PTIRAR[distance-1])){
			s1=gol(s1);
			logger.addAction(defensor().getId()+",a,"+Logger.GOL);
			logger.addAction(t.getId()+",a,"+Logger.CELEBRACION_GOL);
		}
		else{
			logger.addAction(defensor().getId()+",a,"+Logger.TAPAR);			
		}
		s1=changeBallOwner(s1);
		return s1;
	}


	private State changeBallOwner(State s) {
		print("El bal—n ha cambiado de equipo!");
		State s1=new State(s);
		if(s1.getBall_owner()==team0.getId()){
			s1.setBall_owner(team1.getId());
		}
		else{
			s1.setBall_owner(team0.getId());
		}
		return s1;
	}




	private State gol(State s) {
		State s1=new State(s);
		Pair score=s1.getScore();
		if(s1.getBall_owner()==team0.getId())s1.setScore(new Pair(score.a+1,score.b));
		else s1.setScore(new Pair(score.a,score.b+1));
		return s1;
	}
	
	//return the team positions from it's id
	public Pair[] getPositionsFromId(int id, State s1){
		Pair[] team_positions=null;
		if(id==team0.getId()){
			team_positions=s1.getTeam0_positions();
		}
		else{
			team_positions=s1.getTeam1_positions();
		}
		return team_positions;
	}

	public int indexOfPlayerWhoHasTheBall(State s1){
		int who=-1;
		Pair ball=s1.getBall_position();
		Pair[] positions=getPositionsFromId(attacker().getId(), s1);
		for(int i=0;i<positions.length;i++){
			if(positions[i].equals(ball))who=i;
		}
		print("IOPWHTB: "+s1.getBall_position()+"/"+who);
		return who;
	}


	/**
	 * Method to tell if certain action under a probability of p is successful or not.
	 * @param p the probability of the event.
	 * @return true if the event had success, false if not.
	 */
	private boolean testMyLuck(float p){
		Random random=new Random();
		float value=random.nextFloat();
		if(value<=p){
			print("LUCK: YEY!: "+value+"<="+p);
			return true;
		}
		print("LUCK: NOPE!: "+value+">"+p);
		return false;
	}

	private Team defensor(){
		if(S.getBall_owner()==team0.getId())return team1;
		else return team0;
	}
	private Team attacker(){
		if(S.getBall_owner()==team0.getId())return team0;
		else return team1;
	}

	
	private int indexFromPlayer(char player) throws NonExistentTokenException{
		int index=-1;
		if(player=='a'){
			index=0;
		}
		else if(player=='b'){
			index=1;
		}
		else if(player=='c'){
			index=2;
		}
		else if(player=='d'){
			index=3;
		}
		else if(player=='e'){
			index=4;
		}
		if(index==-1) throw new NonExistentTokenException("Tried to get index for player "+player); 
		return index;
	}
	
	private char playerFromindex(int index) throws NonExistentTokenException{
		char player='z';
		if(index==0){
			player='a';
		}
		else if(index==1){
			player='b';
		}
		else if(index==2){
			player='c';
		}
		else if(index==3){
			player='d';
		}
		else if(index==4){
			player='e';
		}
		if(player=='z') throw new NonExistentTokenException("Tried to get player for index "+index); 
		return player;
	}
	
	private Pair pairFromDirection(char direction) throws NonExistentTokenException{
		Pair pair=null;
		//front
		if(direction=='f'){
			pair=new Pair(0,1);
		}
		//back
		else if(direction=='v'){
			pair=new Pair(0,-1);
		}
		//right
		else if(direction=='r'){
			pair=new Pair(1,0);
		}
		//left
		else if(direction=='l'){
			pair=new Pair(-1,0);
		}
		if(pair==null) throw new NonExistentTokenException("Tried to get pair for direction "+direction); 
		return pair;
	}
	
	private void print(String s){
		if(verbose)System.out.println(s);
	}


	
	

	/**
	 * @param args team0 team1
	 */
	public static void main(String[] args) {
		for(int i=0;i<6;i++){
			for(int j=0;j<6;j++){
				//for(int t=0;t<10;t++){
					new FormalFootball(i, j);
					//System.out.println(i+","+j+","+t);
				//}
			}
		}
	}
}
