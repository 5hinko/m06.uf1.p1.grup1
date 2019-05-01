package m06.uf1.audioplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import m06.uf1.audioplayer.model.BarraProgreso;

public class Controlador {

    private final String LISTAR_TODAS = "Todas las listas";
    private ArrayList<ArrayList> listaCanciones;
    private Vista vista;
    private Audio audio;

    private JComboBox vistaCombBoxAlbum;
    private JTable vistaTablaListado;
    private JScrollBar vistaBarraProgreso;

    private static BarraProgreso hiloControladorBarraProgreso ;
    
    public Controlador() {
        vista = new Vista();
        audio = new Audio("audios/acdc - hells bells.mp3");

        instanciaVariables();
        afegirDades();

        afegirListenerBotons();
        afegirListeners();
    }

    private void instanciaVariables() {
        vistaCombBoxAlbum = vista.getjBoxAlbum();
        vistaBarraProgreso = vista.getjBarraProgreso();
        vistaTablaListado = vista.getjTablaMusica();
        
        hiloControladorBarraProgreso = new BarraProgreso(vistaBarraProgreso);
    }

    public void afegirListenerBotons() {
        vista.getPlay().addActionListener(new ControladorBotones());
        vista.getStop().addActionListener(new ControladorBotones());
        vista.getPausa().addActionListener(new ControladorBotones());
        vista.getContinuar().addActionListener(new ControladorBotones());
    }

    private void afegirDades() {

        vistaCombBoxAlbum.removeAllItems();
        //List<String> listaAlbums = new ArrayList<>;

        vistaCombBoxAlbum.addItem(LISTAR_TODAS);
        vistaCombBoxAlbum.setSelectedIndex(0);

        //barra progreso
        vistaBarraProgreso.setValue(0);

        //Intro Tabla y vaciar la lista
        listaCanciones = new ArrayList<>();

        insertarDatosTablaMusica(listaCanciones);

        vistaTablaListado.changeSelection(0, 0, false, false);
        
        hiloControladorBarraProgreso.start();
        
    }

    private void afegirListeners() {
        vistaCombBoxAlbum.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                //Vaciar la lista
                listaCanciones = new ArrayList<>();
                if (e.getItem().toString().equals(LISTAR_TODAS)) {
                    //Todas las listas
                } else {
                    //Mirar cual quiere
                }

                insertarDatosTablaMusica(listaCanciones);
            }
        });

        vistaTablaListado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                vistaTablaListado.getValueAt(vistaTablaListado.getSelectedRow(), 0).toString();
            }
        });

        vista.getjBarraProgreso().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                e.getValue();
            }
        });

    }

    private void insertarDatosTablaMusica(ArrayList<ArrayList> listaCanciones) {
        //Bonito Tabla
        vistaTablaListado.setModel(new ModelTaula(listaCanciones));
        RenderizadorCeldas renderizador = new RenderizadorCeldas();
        for (int i = 0; i < vistaTablaListado.getColumnCount(); i++) {
            vistaTablaListado.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
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
