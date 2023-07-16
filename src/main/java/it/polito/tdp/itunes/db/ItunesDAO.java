package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Arco;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	
	//ATTENZIONE, QUESTO METODO è STATO MODIFICATO, CI HO MESSO UNA MAPPA PER FAR SI CHE 
	//IL METODO MI RESTITUISCA UNA MAPPA ID-CANZONE
	//ho modificato il metodo perchè la query per popolare la tendina mi restituiva gli id, allora gli passo la mappa per ottenere le canzoni
	public void getAllTracks(Map<Integer, Track>  idMap){
		final String sql = "SELECT * FROM Track";
	
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				if(!idMap.containsKey(res.getInt("TrackId"))) { 
				//se l'idMap non contiente l'id della traccia corrente allora ne creo una nuova e la aggiugo alla mappa  
				Track t=new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice"));
				//appena la prende dal db la aggiungo alla mappa
				idMap.put(t.getTrackId(), t);
				
				
				//ATTENZIONE: qui la query serve a prendere dal database tutti gli oggetti Track e popolare la mappa corrispondente
				//            in modo tale che nelle altre query, quando ottengo come risultato l'id di una Track posso chiedere alla mappa stessa
				//            l'oggetto corrispondente a quell'id
				
				//COME FACCIO A SAPERE QUANDO MI SERVE LA MAPPA?
				//In questo caso per esempio il testo mi dice che i vertici del grafo sono degli oggetti della classe Album, quindi
				//se mi rendo conto che è più facile fare una query dove ottengo solo i codici degli album fare una mappa è la cosa piu conveniente 
				
				}
			}
			conn.close();
			//vedi che il metodo è void, non ci sono valori di ritorno
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT * FROM Genre";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	//I vertici sono tutte le canzoni di quel genere selezionato che passo come input
	public List <Track> getVertici(Genre genre, Map<Integer, Track >idMap){
		String sql="SELECT TrackId "
				+ "FROM track "
				+ "WHERE GenreID=?";  
		//ATTENZIONE: il metodo mi restituisce una lista degli id delle canzoni (vertici del grafo)
		//quindi quando prendo il risultato della query chiedo alla mappa di restituirmi l'oggetto corrispondente a quell'id
		//infatti il risultato del metodo è una lista di track, ma se la query mi da gli id e io voglio gli oggetti devo per forza passare dalla mappa
		List<Track> result=new ArrayList<>();
	
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, genre.getGenreId()); //se gli passo l'oggetto genere e la query prevede l'id del genere lo posso chiedere all'oggetto
			
			ResultSet res=st.executeQuery();
			
			while(res.next()) {
				result.add(idMap.get(res.getInt("TrackId")));
				//visto che con la query ottengo gli id delle canzoni gli passo la mappa e mi prendo direttamente gli oggetti
				//finche il risultato della query non è vuuoto

				//ATTENZIONE: qui invece (a differenza del metodo precendente) sto usanado la mappa che ho creato prima, in quanto
				//            la query per ottente i vertici mi restituisce gli id delle canzoni e recupero gli oggetti tramite la mappa
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
			
	}
	
	
	//METODO PER ARCHI E PESO: dato quello che voglio farmi restituire dalla query conviene creare una classe Arco
	//dove gli oggetti contengono tutti gli attributi che mi servono per definire un arco, peso compreso
	//La classe Arco qui contiene Traccia1, Traccia2, peso
	//posso permettermi di mettere nella classe Arco gli oggetti Track proprio perche ho fatto la mappa
	public List <Arco> getArchi(Genre genere, Map<Integer, Track> idMap){
		String sql="SELECT t1.TrackId AS track1, t2.TrackId AS track2, ABS(t1.milliseconds-t2.milliseconds) AS differenza " //ABS mi restituisce il valore assoluto dell'operazione tra parentesi
				+ "FROM track t1, track t2 "                                                                                //faccio due copie della stessa tabella per poter confrontare ogni coppia
				+ "WHERE t1.trackId>t2.trackId  "                                                                           //condizione per cui la stessa coppia viene analizzata una volta sola
				+ "AND t1.MediaTypeId=t2.MediaTypeId  "                                                                     //condizione per cui deve essere presente un arco
				+ "AND t1.GenreId=t2.GenreId AND t1.GenreId=?";                                                             //ultima condizione, che corrisponde a quella dei vertici e poi parametro che si prende dal grafo 
		//nota che la query mi restituisce gia il peso dell'arco
		List<Arco> result=new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, genere.getGenreId()); //anche qui, se come parametro passo l'oggetto alloea l'id lo chiedo all'oggetto
			
			ResultSet res=st.executeQuery();
			
			while(res.next()) {
				result.add(new Arco(idMap.get(res.getInt("track1")), idMap.get(res.getInt("track2")), res.getInt("differenza")));
			
				//ATTENZIONE: nella classe Arco ho messo direttamente gli oggetti come vertici, ma siccome la query mi restituisce gli id chiedo alla mappa di restituirmi gli oggetti
				//per questo non dimenticare di passare la mappa al metodo, oltre ai parametri, se il metodo restituisce gli id
				//In particolare: l'arco chiede gli oggetti canzoni che sono vertici del grafo. ma con la query ottengo gli identificativi quindi passo dalla mappa
			}
			
			conn.close();
			st.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}

	
	
}
