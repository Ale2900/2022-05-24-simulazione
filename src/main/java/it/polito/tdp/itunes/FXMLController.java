/**
\ * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Arco;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Genre genere=this.cmbGenere.getValue(); //l'input del grafo lo prendo dalla combobox 
    	if(genere==null){ //CONTROLLO DA FARE IN CASO IL BOTTONE CREA GRAFO VENISSE PREMUTO SENZA SELEZIONARE IL PARAMETRO
    		this.txtResult.appendText(" Per favore selezionare un genere \n");
    		return;
    	}
    	model.creaGrafo(genere);
    	
    	this.txtResult.appendText("Grafo creato con "+ model.nVertici()+" vertici e "+model.nArchi()+" archi \n ");

    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	Genre genere=this.cmbGenere.getValue();
    	//ovviamente devo prendere di nuovo il parametro dalla combobox, metodi diversi non si ricordano del parametro selezionato   
    	List<Arco> massimi=this.model.getMassimi(genere);
    	for(Arco a: massimi) {
    		this.txtResult.appendText(a+"\n"); //infatti l'oggetto Arco sa già come stamparsi, gliel'ho detto nel toString()
    	}
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	//per inserire nella tendina tutti i generi musicali del DB devo prendere dal model 
    	this.cmbGenere.getItems().addAll(this.model.getGeneri()); //altrimenti potevo fare una lista fuori e passargli quella
    	
    	
    }

}
