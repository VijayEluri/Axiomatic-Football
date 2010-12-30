package me.pacho.FormalFootball.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import me.pacho.FormalFootball.Exceptions.LoggerException;

public class Logger {
	
	//ACTION NAMES
	public static final String TIRAR="TIRAR";
	public static final String RECIBIR="RECIBIR";
	public static final String PASAR="PASAR";
	public static final String ANDAR="ANDAR";
	public static final String CORRER="CORRER";
	public static final String REPOSO="REPOSO";
	
	public static final String GOL="GOL";
	public static final String TAPAR="TAPAR";
	public static final String CELEBRACION_GOL="CELEBRACION_GOL";
	
	public static final String COMIENZO="COMIENZO";
	public static final String GANAR="GANAR";
	public static final String PERDER="PERDER";

	public static final int TIME_NORMAL_ACTION=3;
	public static final int TIME_COMIENZO=10;
	public static final int TIME_CELEBRACION_GOL=10;
	public static final int TIME_END=0;
	public static final int TIME_GOL=10;
	
	
	public static final String DIR_VIDEOS="/Users/francisco/Desktop/";
	private String playlist;
	private String animation;
	private String html;
	private ArrayList<String> action_buffer;
	private String state_buffer;
	private String prefix;
	private String score;
	private int delay=0;
	public Logger(String prefix){
		this.prefix=prefix;
		playlist="";
		animation="";
		html="";
		action_buffer=new ArrayList<String>();
		state_buffer="";
	}
	
	public void addVideo(String video){
		playlist+=DIR_VIDEOS+video+"\n";
	}
	public void addState(String state) throws LoggerException{
		if(state_buffer!=""){
			throw new LoggerException("El buffer de estados esta ocupado.");
		}
		animation+=state;
	}
	public void closeTurn(){
		animation+=(state_buffer+","+delay+"\n");
		delay=0;
		//Limpiar Buffers
		state_buffer="";
	}
	//team id,player,action
	public void addAction(String action){
		String[] parts=action.split(",");
		String key=parts[2];
		String team=parts[0];
		String player=parts[1];
		if(key.equals(COMIENZO)){
			delay+=TIME_COMIENZO;
			addVideo(team+"/comienzo.avi");
		}
		
		else if(key.equals(CORRER)){
			//delay+=TIME_NORMAL_ACTION;
			//addVideo(team+"/"+player+"_correrSinBalon.avi");
		}
		else if(key.equals(ANDAR)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/"+player+"_andarConBalon.avi");
		}
		else if(key.equals(PASAR)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/"+player+"_pase.avi");
		}
		else if(key.equals(REPOSO)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/"+player+"_reposo.avi");
		}
		else if(key.equals(TIRAR)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/"+player+"_tirar.avi");
		}
		else if(key.equals(RECIBIR)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/"+player+"_recibe.avi");
		}
		else if(key.equals(GOL)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/gol.avi");
		}
		else if(key.equals(CELEBRACION_GOL)){
			delay+=TIME_CELEBRACION_GOL;
			addVideo(team+"/celebracion_gol.avi");
		}
		else if(key.equals(TAPAR)){
			delay+=TIME_NORMAL_ACTION;
			addVideo(team+"/tapa.avi");
		}
		else if(key.equals(GANAR)){
			delay+=TIME_END;
			addVideo(team+"/celebracion_partido.avi");
		}
		else if(key.equals(PERDER)){
			delay+=TIME_END;
			addVideo(team+"/derrota.avi");
		}
		else{
		     System.err.println("Video no contemplado: " + action);

		}
	}
	

	
	public void setScore(String sc){
		score=sc;
	}
	
	public void saveLog(){
		saveToFile(animation,"./out/"+prefix+"_animation.txt",false);
		saveToFile(playlist,"./out/"+prefix+"_playlist.m3u",false);
		saveToFile(animation,"./out/"+prefix+"_animation.txt",false);
		saveToFile(score,"./out/scores.csv",true);
	}
	
	public void saveToFile(String info,String path,boolean append){
		try{
			// Create file 
			FileWriter fstream = new FileWriter(path,append);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(info);
			//Close the output stream
			out.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }
	}
}
