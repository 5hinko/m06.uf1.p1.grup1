package m06.uf1.audioplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import static m06.uf1.audioplayer.Vista.jTablaMusica;
import m06.uf1.audioplayer.controlador.Listas;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import m06.uf1.audioplayer.model.*;

public class Controlador {

    private Vista vista;
    private Audio audio;

    public Controlador() {
        vista = new Vista();
        audio = new Audio("audios/September.mp3");
        
        //afegirDades();
        afegirListenerBotons();
        //afegirListeners();
    }

    public void afegirListenerBotons() {
        vista.getPlay().addActionListener(new ControladorBotones());
        vista.getStop().addActionListener(new ControladorBotones());
        vista.getPausa().addActionListener(new ControladorBotones());
        vista.getContinuar().addActionListener(new ControladorBotones());
    }

    public void afegirDades() throws ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException {
       JComboBox boxAlbum =  vista.getjBoxAlbum();
       boxAlbum.removeAllItems();
       //List<String> listaAlbums = new ArrayList<>;
       
       Listas listas = new Listas();
       Document doc = listas.parseXML("carrega_dades.xml");
       listas.getCancionesALL(doc);
       listas.getListasALL(doc);
       
       //Temporal - no debraia ser asi
        for (ListaReproduccion lista_repro: listas.listaRepro) {
             boxAlbum.addItem(lista_repro.getNom());
        }        
        boxAlbum.addItem("Todas Las Canciones");
        //CargarCancionesPorLista(lista);
    }

    public void CargarCancionesPorLista(ListaReproduccion lista){
        
            int cantidadCanciones = lista.getLista_audios().size();
            
        
                jTablaMusica.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {"Musissca 1", null},
                    {"Musica 2", null},
                    {"Musica 3", null}
                },
                new String[]{
                    "Nombre Musica", "Por Ahora nada"
                }
        ));
    }
    
    class ControladorBotones implements ActionListener {

        //Dotem de funcionalitat als botons
        public void actionPerformed(ActionEvent esdeveniment) {
            //Declarem el gestor d'esdeveniments
            Object gestorEsdeveniments = esdeveniment.getSource();
            try {
                if (gestorEsdeveniments.equals(vista.getPlay())) { //Si hem pitjat el boto play
                    audio.getPlayer().play(); //reproduim l'àudio
                } else if (gestorEsdeveniments.equals(vista.getStop())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().stop(); //parem la reproducció de l'àudio
                } else if (gestorEsdeveniments.equals(vista.getPausa())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().pause(); //pausem la reproducció de l'àudio
                } else if (gestorEsdeveniments.equals(vista.getContinuar())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().resume(); //continuem la reproducció de l'àudio
                }
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }
}
