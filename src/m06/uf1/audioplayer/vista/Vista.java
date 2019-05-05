package m06.uf1.audioplayer.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import m06.uf1.audioplayer.model.ModelTaula;
import m06.uf1.audioplayer.controlador.RenderizadorCeldas;

public class Vista {

    private JFrame finestra;
    private JPanel jPrincipal;
    private JPanel columnaLista;
    private JPanel columnaMusica;
    private JPanel menuControl;
    private JPanel menuControl2;

    private JComboBox jBoxAlbum;
    private JScrollPane jScroll;
    private JTable jTablaMusica;

    private JLabel imagenLabel;
    private JLabel textoAlbumTitulo;
    private JTextArea textoDescr;
    private JLabel textoTitulo;
    private JLabel textoAutor;
    private JScrollBar jBarraProgreso;
    private JLabel textoTiempo;
    private JLabel textoMaxDuracion;

    private JButton play;
    private JButton stop;
    private JButton pausa;
    private JButton continuar;
    private JButton anterior;
    private JButton siguiente;

    public Vista() {

        finestra = new JFrame("Reproductor Àudi");

        jPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();

        jPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        columnaLista = new JPanel();
        columnaLista.setLayout(new BoxLayout(columnaLista, BoxLayout.PAGE_AXIS));
        columnaLista.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jBoxAlbum = new JComboBox();
        jTablaMusica = new JTable();

        jBoxAlbum.addItem("Todo");
        jBoxAlbum.addItem("Album 1");
        jBoxAlbum.addItem("Album 2");

        ArrayList<ArrayList> listaCanciones = new ArrayList<>();
        ArrayList<String> firstString = new ArrayList<>();
        firstString.add("Musica 1");
        firstString.add("03:30");
        firstString.add("03:30");
        listaCanciones.add(firstString);
        firstString = new ArrayList<>();
        firstString.add("Musica 2        ");
        firstString.add(null);
        firstString.add(null);
        listaCanciones.add(firstString);
        firstString = new ArrayList<>();
        firstString.add("Musica 3");
        firstString.add("03:30");
        firstString.add("03:30");
        listaCanciones.add(firstString);

        jTablaMusica.setModel(new ModelTaula(listaCanciones));
        TableColumnModel tcm = jTablaMusica.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));

        //Selecionar solo uno 
        jTablaMusica.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Dimesiones de la tabla general
        jTablaMusica.setPreferredScrollableViewportSize(new Dimension(
                (jTablaMusica.getPreferredSize().width * 3) / 2,
                jTablaMusica.getRowHeight() * 22));

        //Colores y select
        RenderizadorCeldas renderizador = new RenderizadorCeldas();
        for (int i = 0; i < jTablaMusica.getColumnCount(); i++) {
            jTablaMusica.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
        jScroll = new JScrollPane(jTablaMusica);

        columnaLista.add(jBoxAlbum);
        columnaLista.add(jScroll);

        //Columna Musica Layout
        columnaMusica = new JPanel();
        columnaMusica.setLayout(new BorderLayout());
        columnaMusica.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        textoAlbumTitulo = new JLabel("Totes les cançons");
        textoTitulo = new JLabel("Títul Cançó");
        imagenLabel = new JLabel();
        int cubWidth = 200;
        imagenLabel.setMinimumSize(new Dimension(cubWidth, cubWidth));
        imagenLabel.setMaximumSize(new Dimension(cubWidth, cubWidth));
        imagenLabel.setPreferredSize(new Dimension(cubWidth, cubWidth));
        imagenLabel.setBackground(Color.red);
        imagenLabel.setBorder(BorderFactory.createTitledBorder(""));
        imagenLabel.setIcon(new ImageIcon("covers\\ShinyBG.png"));
        textoAutor = new JLabel("Yo mismo");
        textoDescr = new JTextArea("Pues es muy bonito", 2, 33);
        textoDescr.setEditable(false);
        textoDescr.setBorder(BorderFactory.createTitledBorder("Descripció"));

        jBarraProgreso = new JScrollBar();
        jBarraProgreso.setOrientation(JScrollBar.HORIZONTAL);
        jBarraProgreso.setMaximum(0);
        jBarraProgreso.setMaximum(100);
        jBarraProgreso.setPreferredSize(new Dimension(250, 15));
        textoTiempo = new JLabel("0:00");
        textoMaxDuracion = new JLabel("03:00");

        //Titulo LAyout
        menuControl = new JPanel(new FlowLayout());
        menuControl.add(textoAlbumTitulo);
        menuControl.add(new JLabel(" - "));
        menuControl.add(textoTitulo);

        columnaMusica.add(menuControl, BorderLayout.NORTH);

        //Centro LAyout
        menuControl = new JPanel();
        menuControl.setLayout(new BorderLayout());
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuControl.add(imagenLabel, BorderLayout.NORTH);

        menuControl2 = new JPanel();
        menuControl2.setLayout(new BoxLayout(menuControl2, BoxLayout.PAGE_AXIS));
        JPanel panelExpress = new JPanel(new FlowLayout());
        panelExpress.add(new JLabel("Autor: "));
        panelExpress.add(textoAutor);
        menuControl2.add(panelExpress);
        menuControl2.add(textoDescr);
        menuControl.add(menuControl2, BorderLayout.CENTER);

        menuControl2 = new JPanel(new BorderLayout());
        menuControl2.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        menuControl2.add(jBarraProgreso, BorderLayout.CENTER);
        panelExpress = new JPanel(new FlowLayout());
        panelExpress.add(textoTiempo);
        panelExpress.add(new JLabel("/"));
        panelExpress.add(textoMaxDuracion);
        menuControl2.add(panelExpress, BorderLayout.EAST);
        menuControl.add(menuControl2, BorderLayout.SOUTH);

        columnaMusica.add(menuControl, BorderLayout.CENTER);

        //Abajo Control Layout
        menuControl = new JPanel();
        menuControl.setLayout(new GridLayout(0, 6));
        menuControl.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        Image img;
        Image newimg;
        try {
            cubWidth = 20;
            img = ImageIO.read(new File("icono\\boton-de-reproduccion.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            play = new JButton(new ImageIcon(newimg));

            cubWidth = 17;
            img = ImageIO.read(new File("icono\\detener.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            stop = new JButton(new ImageIcon(newimg));

            img = ImageIO.read(new File("icono\\pausa.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            pausa = new JButton(new ImageIcon(newimg));

            img = ImageIO.read(new File("icono\\boton-de-reproduccion.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            continuar = new JButton(new ImageIcon(newimg));
            continuar = new JButton("Continue");

            img = ImageIO.read(new File("icono\\espalda.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            anterior = new JButton(new ImageIcon(newimg));

            img = ImageIO.read(new File("icono\\proximo.png"));
            newimg = img.getScaledInstance(cubWidth, cubWidth, java.awt.Image.SCALE_SMOOTH);
            siguiente = new JButton(new ImageIcon(newimg));
        } catch (IOException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        play = new JButton("Play");
        stop = new JButton("Stop");
        pausa = new JButton("Pause");
        continuar = new JButton("Continue");
        anterior = new JButton("Anterior");
        siguiente = new JButton("Siguiente");*/
        menuControl.add(anterior);
        menuControl.add(play);
        menuControl.add(pausa);
        menuControl.add(continuar);
        menuControl.add(stop);
        menuControl.add(siguiente);
        //anterior.setEnabled(false);

        columnaMusica.add(menuControl, BorderLayout.SOUTH);

        constraint.fill = GridBagConstraints.HORIZONTAL;
        //constraint.ipady = 40;
        constraint.gridx = 0;
        constraint.gridy = 0;
        jPrincipal.add(columnaLista, constraint);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridwidth = 3;
        constraint.gridx = 1;
        constraint.gridy = 0;
        jPrincipal.add(columnaMusica, constraint);

        finestra.add(jPrincipal);

        //finestra.setSize(700, 450);
        finestra.setResizable(false);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLocationRelativeTo(null);

        finestra.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        finestra.setLocation(dim.width / 2 - finestra.getSize().width / 2, dim.height / 2 - finestra.getSize().height / 2);

        finestra.setVisible(true);
    }

    public JFrame getFinestra() {
        return finestra;
    }

    public void setFinestra(JFrame finestra) {
        this.finestra = finestra;
    }

    public JPanel getPanell() {
        return jPrincipal;
    }

    public void setPanell(JPanel panell) {
        this.jPrincipal = panell;
    }

    public JButton getPlay() {
        return play;
    }

    public void setPlay(JButton play) {
        this.play = play;
    }

    public JButton getStop() {
        return stop;
    }

    public void setStop(JButton stop) {
        this.stop = stop;
    }

    public JButton getPausa() {
        return pausa;
    }

    public void setPausa(JButton pausa) {
        this.pausa = pausa;
    }

    public JButton getContinuar() {
        return continuar;
    }

    public void setContinuar(JButton continuar) {
        this.continuar = continuar;
    }

    public JComboBox getjBoxAlbum() {
        return jBoxAlbum;
    }

    public JTable getjTablaMusica() {
        return jTablaMusica;
    }

    public JLabel getImagenLabel() {
        return imagenLabel;
    }

    public JLabel getTextoAlbumTitulo() {
        return textoAlbumTitulo;
    }

    public JTextArea getTextoDescr() {
        return textoDescr;
    }

    public JLabel getTextoTitulo() {
        return textoTitulo;
    }

    public JLabel getTextoAutor() {
        return textoAutor;
    }

    public JScrollBar getjBarraProgreso() {
        return jBarraProgreso;
    }

    public JLabel getTextoTiempo() {
        return textoTiempo;
    }

    public JLabel getTextoMaxDuracion() {
        return textoMaxDuracion;
    }

    public JButton getAnterior() {
        return anterior;
    }

    public JButton getSiguiente() {
        return siguiente;
    }

}
