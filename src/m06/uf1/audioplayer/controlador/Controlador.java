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
import javax.xml.parsers.ParserConfigurationException;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import m06.uf1.audioplayer.model.ModelTaula;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import m06.uf1.audioplayer.model.*;

public class Controlador {

    private final String LISTAR_TODAS = "Todas las listas";
    private ArrayList<ArrayList> listaCanciones;
    private Vista vista;
    private Audio audio;
    private String listaSeleccionadaAReporucir = null;

    private JComboBox vistaCombBoxAlbum;
    public JTable vistaTablaListado;
    private JScrollBar vistaBarraProgreso;

    private static BarraProgreso hiloControladorBarraProgreso;

    Listas listas = new Listas();

    public Controlador() {
        try {
            vista = new Vista();
            audio = null;

            instanciaVariables();

            afegirDades();
            afegirListenerBotons();
            afegirListeners();

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

        hiloControladorBarraProgreso = new BarraProgreso(vistaBarraProgreso, vista.getTextoTiempo());
    }

    public void afegirListenerBotons() {
        vista.getPlay().addActionListener(new ControladorBotones());
        vista.getStop().addActionListener(new ControladorBotones());
        vista.getPausa().addActionListener(new ControladorBotones());
        vista.getContinuar().addActionListener(new ControladorBotones());
    }

    public void afegirDades() throws ParserConfigurationException, SAXException, IOException, FileNotFoundException, ParseException {
        /**
         * *
         * No hace falta insertar los datos porque al pasa por El ComboBox al
         * crear la 1r vez ya lo hace
         */
        //Intro Tabla y vaciar la lista
        listaCanciones = new ArrayList<>();

        insertarDatosTablaMusica(listaCanciones);

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

        //Hilo itento de hacer la barra de progreso
        hiloControladorBarraProgreso.start();
    }

    private void afegirListeners() {
        vistaCombBoxAlbum.addItemListener((ItemEvent e) -> {
            //Vaciar la lista
            listaCanciones = new ArrayList<>();
            String nombreLista = e.getItem().toString();

            vista.getTextoAlbumTitulo().setText(vistaCombBoxAlbum.getSelectedItem().toString());
            if (nombreLista.equals(LISTAR_TODAS)) {
                for (ListaReproduccion args : listas.listaRepro) {
                    introducirDatosLista(args);
                }
            } else {

                listaSeleccionadaAReporucir = nombreLista;
                ListaReproduccion listaSeleccionada = new ListaReproduccion();
                for (ListaReproduccion args : listas.listaRepro) {
                    if (args.getNom().equals(nombreLista)) {
                        introducirDatosLista(args);
                    }
                }
            }
            insertarDatosTablaMusica(listaCanciones);
        });

        //https://stackoverflow.com/questions/14852719/double-click-listener-on-jtable-in-java
        /*vistaTablaListado.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (vistaTablaListado.getRowCount() > 0) {
                    String nombre = (String)vistaTablaListado.getValueAt(vistaTablaListado.getSelectedRow(), 0).toString();
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
                System.out.println("2 veces" + e.getFirstIndex() + " " + vistaTablaListado.getSelectedRow());
            }
        });*/
        vistaTablaListado.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int rowSelected = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    // your valueChanged overridden method 

                    String nombre = (String) vistaTablaListado.getValueAt(rowSelected, 0).toString();
                    selecionarCancion(nombre);
                }
                System.out.println("1 veces" + " " + vistaTablaListado.getSelectedRow());
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

    private void selecionarCancion(String nombre) {
        listas.listaAudios.stream().filter((cancion) -> (cancion.getNom().equals(nombre))).map((cancion) -> {
            vista.getTextoTitulo().setText(cancion.getNom());
            return cancion;
        }).map((cancion) -> {
            vista.getTextoAutor().setText(cancion.getAutor());
            return cancion;
        }).forEachOrdered((cancion) -> {
            vista.getTextoMaxDuracion().setText(convertTiempoStr(cancion.getDurada()));
            vistaBarraProgreso.setMaximum(cancion.getDurada());
            /*try {

                audio.getPlayer().stop();
                hiloControladorBarraProgreso.itsStop();
            } catch (BasicPlayerException ex) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }*/
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
        vistaTablaListado.changeSelection(0, 0, true, false);

        //Poner información
            //String nombre = listaCanciones.get(0).get(0).toString();
            //selecionarCancion(nombre);
    }

    private void introducirDatosLista(ListaReproduccion args) {

        //Poner cancion en array para lista
        ArrayList<String> firstString;
        System.out.println("Has llegado a: " + args.getNom());
        //Algo feo pero funciona
        vista.getTextoDescr().setEditable(true);
        vista.getTextoDescr().setText(args.getDescripcio());
        vista.getTextoDescr().setEditable(false);
        vista.getImagenLabel().setIcon(new ImageIcon(args.getRutaImatge()));
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

    public static void cancionTerminada() {

    }

    class ControladorBotones implements ActionListener {

        //Dotem de funcionalitat als botons
        @Override
        public void actionPerformed(ActionEvent esdeveniment) {
            //Declarem el gestor d'esdeveniments
            Object gestorEsdeveniments = esdeveniment.getSource();
            ArrayList<String> cancionesDentro = new ArrayList<>();

            //Si quieres poder seleccionar una cancion y reproducirla, haz que sea un map.
            ArrayList<Audio> archivosAudio = new ArrayList<>();
            int i = 0;

            if (listaSeleccionadaAReporucir != null) {
                for (ListaReproduccion listaSeleccionada : listas.listaRepro) {
                    if (listaSeleccionada.getNom().equals(listaSeleccionadaAReporucir)) {
                        for (String direccion : listaSeleccionada.getLista_audios()) {
                            archivosAudio.add(new Audio("audios\\" + direccion + ".mp3"));
                        }
                    }
                }
            }

            //foreach para ir pillando los audios y meterlos en un array
            // ArrayList<AudioMP3> arrayAudios = new
            if (audio == null) {
                String nombre = vistaTablaListado.getValueAt(vistaTablaListado.getSelectedRow(), 0).toString();
                System.out.println("Nombre : " + nombre);
                for (AudioMP3 rutaMP3 : listas.listaAudios) {
                    if (rutaMP3.getNom().equals(nombre)) {
                        audio = new Audio(rutaMP3.getRuta());
                        i = 0;
                    }
                }

            }
            String duracionS = (String) vistaTablaListado.getValueAt(i, 1);
            //TODO ESTO CUANDO TERMINE EL AUDIO
            //tienes un contador i, si ese contador, no es mayor al lenght del array "archivosAudio"
            //Un array con todas las direcciones de los audios de la lista.
            //haces audio = new Audio(archivosaudios.get(i)); -- o algo asi, eso cojera el siguiente archivo.
            //y selecicionas el siguiente elemento de la columna tmb vistaTal.changeSelection(i,0,fadlse,true);
            //Si es igual o mayo(igual) haces lo mismo pero selecionando el elemento 0.

            //audio = archivosAudio.get(1);
            try {
                if (gestorEsdeveniments.equals(vista.getPlay())) { //Si hem pitjat el boto play
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
                }
            } catch (BasicPlayerException e) {
                vistaTablaListado.changeSelection(0, 0, false, true);
            }

        }
    }

}
