package m06.uf1.audioplayer.controlador;

import m06.uf1.audioplayer.model.Audio;
import m06.uf1.audioplayer.vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import m06.uf1.audioplayer.model.ModelTaula;
import m06.uf1.audioplayer.controlador.Listas;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import m06.uf1.audioplayer.model.*;

public class Controlador {

    private final String LISTAR_TODAS = "Todas las listas";
    private ArrayList<ArrayList> listaCanciones;
    private Vista vista;
    private Audio audio;

    private JComboBox vistaCombBoxAlbum;
    private JTable vistaTablaListado;
    private JScrollBar vistaBarraProgreso;

    private static BarraProgreso hiloControladorBarraProgreso;

    Listas listas = new Listas();

    public Controlador() {
        try {
            vista = new Vista();
            audio = new Audio("audios/September.mp3");

            instanciaVariables();

            afegirDades();
            afegirListenerBotons();
            afegirListeners();
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void instanciaVariables() {
        vistaCombBoxAlbum = vista.getjBoxAlbum();
        vistaBarraProgreso = vista.getjBarraProgreso();
        vistaTablaListado = vista.getjTablaMusica();

        hiloControladorBarraProgreso = new BarraProgreso(vistaBarraProgreso, vista.getTextoTiempo());
    }

    public void afegirListenerBotons() {
        vista.getPlay().addActionListener(new ControladorBotones());
        vista.getStop().addActionListener(new ControladorBotones());
        vista.getPausa().addActionListener(new ControladorBotones());
        vista.getContinuar().addActionListener(new ControladorBotones());
    }

    public void afegirDades() throws ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException {
        vistaCombBoxAlbum.removeAllItems();
        //List<String> listaAlbums = new ArrayList<>;

        Document doc = listas.parseXML("carrega_dades.xml");
        listas.getCancionesALL(doc);
        listas.getListasALL(doc);

        //Temporal - no debraia ser asi
        vistaCombBoxAlbum.addItem(LISTAR_TODAS);
        vistaCombBoxAlbum.setSelectedIndex(0);
        for (ListaReproduccion lista_repro : listas.listaRepro) {
            vistaCombBoxAlbum.addItem(lista_repro.getNom());
        }
        //CargarCancionesPorLista(lista);

        //barra progreso
        vistaBarraProgreso.setValue(0);

        /**
         * *
         * No hace falta insertar los datos porque al pasa por El ComboBox al
         * crear la 1r vez ya lo hace
         */
        //Intro Tabla y vaciar la lista
        listaCanciones = new ArrayList<>();

        insertarDatosTablaMusica(listaCanciones);

        vistaTablaListado.changeSelection(0, 0, false, false);
        if (vistaTablaListado.getRowCount() > 0) {
        }
        System.out.println("Hola");
        //Hilo itento de hacer la barra de progreso
        hiloControladorBarraProgreso.start();

        try{
            hiloControladorBarraProgreso.itsPlay();
            Thread.sleep(5000);
            hiloControladorBarraProgreso.itsPause();
            Thread.sleep(5000);
            hiloControladorBarraProgreso.itsContinuar();
            Thread.sleep(5000);
            hiloControladorBarraProgreso.itsStop();

        }catch(Exception ex){

        }
    }

    private void afegirListeners() {
        vistaCombBoxAlbum.addItemListener((ItemEvent e) -> {
            //Vaciar la lista
            listaCanciones = new ArrayList<>();
            if (e.getItem().toString().equals(LISTAR_TODAS)) {
                System.out.println("Ha llegado a todos");

            } else {

                ArrayList<String> firstString = new ArrayList<>();

                String nombreLista = e.getItem().toString();

                ListaReproduccion listaSeleccionada = new ListaReproduccion();
                for (ListaReproduccion args : listas.listaRepro) {
                    if (args.getNom().equals(nombreLista)) {
                        System.out.println("Has llegado a: " + args.getNom());
                        for (String cancion : args.getLista_audios()) {
                            firstString = new ArrayList<>();
                            for (AudioMP3 audio : listas.listaAudios) {
                                if (cancion.equals(audio.getNom())) {
                                    firstString.add(audio.getNom());
                                    //String duracion = audio.getDurada() +"";
                                    firstString.add(convertTiempoStr(audio.getDurada()));
                                    listaCanciones.add(firstString);
                                    System.out.println(audio.getAutor() + " nom " + audio.getNom());
                                }
                            }
                        }

                    }
                }

                vistaTablaListado.setModel(new ModelTaula(listaCanciones));
                RenderizadorCeldas renderizador = new RenderizadorCeldas();
                for (int i = 0; i < vistaTablaListado.getColumnCount(); i++) {
                    vistaTablaListado.getColumnModel().getColumn(i).setCellRenderer(renderizador);
                }

            }

            insertarDatosTablaMusica(listaCanciones);
        });

        vistaTablaListado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (vistaTablaListado.getRowCount() > 0) {
                    String nombre = vistaTablaListado.getValueAt(vistaTablaListado.getSelectedRow(), 0).toString();
                    listas.listaAudios.stream().filter((cancion) -> (cancion.getNom().equals(nombre))).map((cancion) -> {
                        vista.getTextoTitulo().setText(cancion.getNom());
                        return cancion;
                    }).map((cancion) -> {
                        vista.getTextoAutor().setText(cancion.getAutor());
                        return cancion;
                    }).forEachOrdered((cancion) -> {
                        vista.getTextoMaxDuracion().setText(convertTiempoStr(cancion.getDurada()));
                        vista.getjBarraProgreso().setMaximum(cancion.getDurada());

                    });
                } else {
                    //Possiblemente vació
                }
            }
        });
        /*
        vistaBarraProgreso.addAdjustmentListener((AdjustmentEvent e) -> {

            try {
                audio.getPlayer().seek(0);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            vistaBarraProgreso.setValue(e.getValue());
        });
         */
    }

    private String convertTiempoStr(int num) {
        int min = num / 60;
        int seg = num % 60;

        return (convertDecimal(min) + ":" + convertDecimal(seg));
    }

    private String convertDecimal(int num) {
        return ((num < 10) ? "0" + num : "" + num);
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
        @Override
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
