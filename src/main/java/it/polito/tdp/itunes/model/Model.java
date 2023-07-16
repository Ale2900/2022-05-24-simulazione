package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Track,DefaultWeightedEdge > grafo;
	//conviene creare una mappa che dal trackID mi restituisce la canzone
	private Map<Integer, Track> idMap; //è quella che mi serve per le query
	
	public Model() {
		this.dao=new ItunesDAO();
		idMap=new HashMap<>(); 
		//qui nel costruttore del model infatti inizializzo un nuovo DAO e la mappa che viene riempita grazie al metodo getAllTracks che avevo modificato
		
		this.dao.getAllTracks(idMap); 
		//ATTENZIONE: quel metodo che avevo fatto nel DAO che prende tutte le traccie el inserisci nella mappa viene richiamato adesso
		//ATTENZIONE: fai sempre cosi
	    //ATTENZIONE: quando devi fare le mappe inizializzale sempre nel costruttore del model tanto sono standard
		
	
	}
	
	//metodo che mi prende la lista di tutti i generi, lo uso per popolare la tendina
	public List<Genre> getGeneri(){
		return dao.getAllGenres();
	}
	
	public void creaGrafo(Genre genre) {
		this.grafo=new SimpleWeightedGraph<Track, DefaultWeightedEdge>(DefaultWeightedEdge.class); 
		//lo inizializzo qui cosi che ogni volta che si preme il pulsante crea Grafo se ne crea uno nuovo
		//cosi non devo preoccuparmi di creare un metodo che mi "pulisca" il grado vecchio e ne crei uno nuovo
		
		
		//vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(genre, this.idMap));  
	
		//archi
		//nel metodo che mi da gli archi mi faccio dare anche il peso dell'arco e lo inserisco come nuova colonna
		
		List<Arco> archi=this.dao.getArchi(genre, this.idMap);
		for(Arco a: archi) {
			Graphs.addEdge(this.grafo, a.getT1(), a.getT2(), a.getPeso());  // all'addEdge servono gli oggetti perciò è un bene che nella classe aroc ci siano gli oggetti Track
			
		}
	}
	
	//questo metodo mi serve per stampare a schermo il numero di vertici
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	//questo metodo mi serve per stampare a schermo il numero di archi
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	//ALGORIMTMO PER ARCHI DI GRADO MASSIMO
	
	public List<Arco> getMassimi(Genre genere){ //ovviamente serve il parametro perche devo richiamare la query degli archi
		List<Arco> archi=new ArrayList<Arco>(this.dao.getArchi(genere, idMap)); // creo una nuova lista con tutti gli archi del grafo corrente
		List<Arco> result=new ArrayList<Arco>();                                //creo una lista dove mettere gli archi con grado massimo
		Collections.sort(archi);                 //ordino gia gli archi in ordine decrescente, ho creato a posta il compareTO()
	
		
		//scorro a questo punto tutti gli archi del grafo e controllo quali hanno grafo massimo
		//se ci sono archi di peso massimo avranno sicuramente peso uguale al primo elemento della lista ordinata decrescentemente
		//questo perchè quando faccio il sort della lista di archi loro si ordinano in ordine decrescente di peso
		for(Arco a: archi) {
			if(a.getPeso()==archi.get(0).getPeso()) 
				result.add(a);
			else 
				break;
		} 
		
		return result;
	
	}
	
}
