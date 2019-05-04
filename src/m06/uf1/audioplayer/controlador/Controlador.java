package m06.uf1.audioplayer.controlador;

import java.awt.Point;
import m06.uf1.audioplayer.model.Audio;
import m06.uf1.audioplayer.vista.Vista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import m06.uf1.audioplayer.model.ModelTaula;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import m06.uf1.audioplayer.model.*;

public class Controlador {

    private final String LISTAR_TODAS = "Totes les cançons ";
    private ArrayList<ArrayList> listaCanciones;
    private Vista vista;
    private static Audio audio;
    private String listaSeleccionadaAReporucir = null;

    private JComboBox vistaCombBoxAlbum;
    public static JTable vistaTablaListado;
    private JScrollBar vistaBarraProgreso;

    private static BarraProgreso hiloControladorBarraProgreso;

    Listas listas = new Listas();

    public Controlador() {
        try {
            vista = new Vista();
            audio = null;

            instanciaVariables();

            afegirDades();
            afegirListeners();
            afegirListenerBotons();
            //Seleciona el 1r pero no carga, los demas si
            //vistaCombBoxAlbum.setSelectedIndex(1);
            vistaCombBoxAlbum.setSelectedIndex(0);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void instanciaVariables() {
        vistaCombBoxAlbum = vista.getjBoxAlbum();
        vistaBarraProgreso = vista.getjBarraProgreso();
        vistaTablaListado = vista.getjTablaMusica();

        listaSeleccionadaAReporucir = LISTAR_TODAS;
        hiloControladorBarraProgreso = new BarraProgreso(vistaBarraProgreso, vista.getTextoTiempo(), new CancionTerminda());
    }

    public void afegirListenerBotons() {
        vista.getPlay().addActionListener(new ControladorBotones());
        vista.getStop().addActionListener(new ControladorBotones());
        vista.getPausa().addActionListener(new ControladorBotones());
        vista.getContinuar().addActionListener(new ControladorBotones());
        vista.getAnterior().addActionListener(new ControladorBotones());
        vista.getSiguiente().addActionListener(new ControladorBotones());
    }

    public void afegirDades() throws ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException {

        vistaCombBoxAlbum.removeAllItems();

        Document doc = listas.parseXML("carrega_dades.xml");
        listas.getCancionesALL(doc);
        listas.getListasALL(doc);

        //Temporal - no debraia ser asi
        listaCanciones = new ArrayList<>();
        vistaCombBoxAlbum.addItem(LISTAR_TODAS);
        for (ListaReproduccion lista_repro : listas.listaRepro) {
            vistaCombBoxAlbum.addItem(lista_repro.getNom());
            introducirDatosLista(lista_repro);
        }
        insertarDatosTablaMusica(listaCanciones);
        //CargarCancionesPorLista(lista);

        //barra progreso
        vistaBarraProgreso.setValue(0);

        //Hilo de hacer la barra de progreso
        hiloControladorBarraProgreso.start();
    }

    private void afegirListeners() {
        vistaCombBoxAlbum.addItemListener((ItemEvent e) -> {
            //Vaciar la lista
            listaCanciones = new ArrayList<>();
            String nombreLista = e.getItem().toString();

            listaSeleccionadaAReporucir = nombreLista;
            vista.getTextoAlbumTitulo().setText(vistaCombBoxAlbum.getSelectedItem().toString());
            if (nombreLista.equals(LISTAR_TODAS)) {
                for (ListaReproduccion args : listas.listaRepro) {
                    introducirDatosLista(args);
                }
            } else {
                ListaReproduccion listaSeleccionada = new ListaReproduccion();
                for (ListaReproduccion args : listas.listaRepro) {
                    if (args.getNom().equals(nombreLista)) {
                        introducirDatosLista(args);
                    }
                }
            }
            insertarDatosTablaMusica(listaCanciones);
        });

        vistaTablaListado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int rowSelected = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() <= 2 && table.getSelectedRow() != -1) {


                    selecionarCancion(rowSelected);

                    if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        try {
                            // your valueChanged overridden method

                            audio.getPlayer().play(); //reproduim l'àudio
                            hiloControladorBarraProgreso.itsPlay();
                        } catch (BasicPlayerException ex) {
                            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }
        );

    }

    private void selecionarCancion(int rowSelected) {
        String nombre = (String) vistaTablaListado.getValueAt(rowSelected, 0).toString();
        listas.listaAudios.stream().filter((cancion) -> (cancion.getNom().equals(nombre))).map((cancion) -> {
            vista.getTextoTitulo().setText(cancion.getNom());
            return cancion;
        }).map((cancion) -> {
            vista.getTextoAutor().setText(cancion.getAutor());
            return cancion;
        }).forEachOrdered((cancion) -> {
            vista.getTextoMaxDuracion().setText(convertTiempoStr(cancion.getDurada()));
            vistaBarraProgreso.setMaximum(cancion.getDurada());
            try {
                if (audio != null) {
                    audio.getPlayer().stop();
                    hiloControladorBarraProgreso.itsStop();
                }
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Controlador.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            audio = new Audio(cancion.getRuta());

        });
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
        //vistaTablaListado.getSelectionModel().clearSelection();
        vistaTablaListado.clearSelection();
        vistaTablaListado.setModel(new ModelTaula(listaCanciones));
        RenderizadorCeldas renderizador = new RenderizadorCeldas();
        for (int i = 0; i < vistaTablaListado.getColumnCount(); i++) {
            vistaTablaListado.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
        TableColumnModel tcm = vistaTablaListado.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));
        vistaTablaListado.changeSelection(0, 0, true, false);

        try {
            if (audio != null) {
                audio.getPlayer().stop();
                hiloControladorBarraProgreso.itsStop();
                audio = null;
            }
        } catch (BasicPlayerException ex) {
            Logger.getLogger(Controlador.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //Poner información
        selecionarCancion(vistaTablaListado.getSelectedRow());
    }

    private void introducirDatosLista(ListaReproduccion args) {

        //Poner cancion en array para lista
        ArrayList<String> firstString;
        System.out.println("Has llegado a: " + args.getNom());
        //Algo feo pero funciona
        vista.getTextoDescr().setEditable(true);
        vista.getTextoDescr().setText(
                (listaSeleccionadaAReporucir.equals(LISTAR_TODAS)
                ? "Llistat amb totes les cançons" : args.getDescripcio()));
        vista.getTextoDescr().setEditable(false);
        vista.getImagenLabel().setIcon(new ImageIcon(
                (listaSeleccionadaAReporucir.equals(LISTAR_TODAS)
                ? "covers\\ShinyBG.png" : args.getRutaImatge())));
        for (String cancion : args.getLista_audios()) {
            firstString = new ArrayList<>();
            for (AudioMP3 audio : listas.listaAudios) {
                if (cancion.equals(audio.getNom())) {
                    firstString.add(audio.getNom());
                    //String duracion = audio.getDurada() +"";
                    firstString.add(convertTiempoStr(audio.getDurada()));
                    firstString.add(audio.getRuta());
                    listaCanciones.add(firstString);
                    System.out.println(audio.getAutor() + " nom " + audio.getNom());
                }
            }
        }
    }

    private class CancionTerminda extends Thread {

        @Override
        public void run() {
            int filaMusica = vistaTablaListado.getSelectedRow();
  
            if (filaMusica < vistaTablaListado.getRowCount()) {//!cancionProgrmar.isEmpty()) {

                selecionarCancion(filaMusica + 1);//vistaTablaListado.getValueAt(filaMusica + 1, 2).toString());
                vistaTablaListado.changeSelection(filaMusica + 1, 0, true, false);
                try {
                    audio.getPlayer().play(); //reproduim l'àudio
                    hiloControladorBarraProgreso.itsPlay();
                } catch (BasicPlayerException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
                    selecionarCancion(vistaTablaListado.getSelectedRow());
                    audio.getPlayer().play(); //reproduim l'àudio
                    hiloControladorBarraProgreso.itsPlay();
                } else if (gestorEsdeveniments.equals(vista.getStop())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().stop(); //parem la reproducció de l'àudio
                    hiloControladorBarraProgreso.itsStop();
                } else if (gestorEsdeveniments.equals(vista.getPausa())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().pause(); //pausem la reproducció de l'àudio
                    hiloControladorBarraProgreso.itsPause();
                } else if (gestorEsdeveniments.equals(vista.getContinuar())) {
                    //Si hem pitjat el boto stop
                    audio.getPlayer().resume(); //continuem la reproducció de l'àudio
                    hiloControladorBarraProgreso.itsContinuar();
                } else if (gestorEsdeveniments.equals(vista.getAnterior())) {
                    int actual = vistaTablaListado.getSelectedRow();
                    if (actual != 0) {
                        getAnteriorCancion();
                    } else {
                        System.out.println("Se ha llegado al inicio");
                    }
                } else if (gestorEsdeveniments.equals(vista.getSiguiente())) {

                    int actual = vistaTablaListado.getSelectedRow();
                    if (actual != vistaTablaListado.getRowCount() - 1) {
                        new CancionTerminda().start();
                    } else {
                        System.out.println("Se ha llegado al maximo");
                    }

                }
            } catch (BasicPlayerException e) {
                vistaTablaListado.changeSelection(0, 0, false, true);
                vista.getAnterior().setEnabled(false);
            }

        }
    }

    public void getAnteriorCancion() {
        int actual = vistaTablaListado.getSelectedRow();
        vistaTablaListado.changeSelection(actual - 1, 0, false, true);
        selecionarCancion(actual - 1);
        try {
            audio.getPlayer().play(); //reproduim l'àudio
            hiloControladorBarraProgreso.itsPlay();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
