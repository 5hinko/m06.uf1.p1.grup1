package m06.uf1.audioplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javazoom.jlgui.basicplayer.BasicPlayerException;

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

    private void afegirDades() {
       JComboBox boxAlbum =  vista.getjBoxAlbum();
       boxAlbum.removeAllItems();
       //List<String> listaAlbums = new ArrayList<>;
       
       boxAlbum.addItem("Todo");
       boxAlbum.addItem("asddas");
        
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
