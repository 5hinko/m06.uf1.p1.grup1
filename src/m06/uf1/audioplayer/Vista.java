package m06.uf1.audioplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;

public class Vista {

    private JFrame finestra;
    private JPanel jPrincipal;
    private JPanel columnaLista;
    private JPanel columnaMusica;
    private JPanel menuControl;
    private JPanel menuControl2;

    private JComboBox jBoxAlbum;
    private JScrollPane jScroll;
    public static JTable jTablaMusica;

    private JLabel imagenLabel;
    private JLabel textoLlista;
    private JLabel textoDescr;
    private JLabel textoTitulo;
    private JLabel textoAutor;
    private JScrollBar jBarraProgreso;
    private JLabel textoDuracion;

    private JButton play;
    private JButton stop;
    private JButton pausa;
    private JButton continuar;

    public Vista() {

        finestra = new JFrame("Reproductor Àudio");

        jPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        
        jPrincipal.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        columnaLista = new JPanel();
        columnaLista.setLayout(new BoxLayout(columnaLista, BoxLayout.PAGE_AXIS));
        columnaLista.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        jBoxAlbum = new JComboBox();
        jTablaMusica = new JTable();
        
        jBoxAlbum.addItem("Album 1");
        jBoxAlbum.addItem("Album 2");
        jBoxAlbum.addItem("Todo");
        jTablaMusica.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {"Musica 1", null},
                    {"Musica 2", null},
                    {"Musica 3", null}
                },
                new String[]{
                    "Nombre Musica", "Por Ahora nada"
                }
        ));
        jTablaMusica.setPreferredScrollableViewportSize(new Dimension(
                jTablaMusica.getPreferredSize().width,
                jTablaMusica.getRowHeight() * 20));
        jScroll = new JScrollPane(jTablaMusica);
        
        columnaLista.add(jBoxAlbum);
        columnaLista.add(jScroll);

        //Columna Musica
        columnaMusica = new JPanel();
        columnaMusica.setLayout(new BorderLayout());
        columnaMusica.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        textoLlista = new JLabel("Nombre Llista");
        textoTitulo = new JLabel("Titulo Canción");
        imagenLabel = new JLabel("Imagen 300X200?");
        int width= 300, height = 200;
        imagenLabel.setMinimumSize(new Dimension(width, height));
        imagenLabel.setMaximumSize(new Dimension(width, height));
        imagenLabel.setPreferredSize(new Dimension(width, height));
        imagenLabel.setBackground(Color.red);
        textoAutor = new JLabel("Yo mismo");
        textoDescr = new JLabel("Pues es muy bonito");
        
        jBarraProgreso = new JScrollBar();
        jBarraProgreso.setOrientation(JScrollBar.HORIZONTAL);
        jBarraProgreso.setMaximum(0);
        jBarraProgreso.setMaximum(100);
        jBarraProgreso.setPreferredSize(new Dimension(250,15));
        textoDuracion = new JLabel("0:00/3:00");

        //Titulo 
        menuControl = new JPanel( new FlowLayout());
        menuControl.add(textoLlista);
        menuControl.add(new JLabel(" - "));
        menuControl.add(textoTitulo);
        
        columnaMusica.add(menuControl,BorderLayout.NORTH);

        //Centro
        menuControl = new JPanel();
        menuControl.setLayout(new BorderLayout());
        imagenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuControl.add(imagenLabel, BorderLayout.NORTH);
        
        menuControl2 = new JPanel();
        menuControl2.setLayout( new BoxLayout(menuControl2, BoxLayout.PAGE_AXIS));
        JPanel menuControlExtension = new JPanel(new FlowLayout());
        menuControlExtension.add(new JLabel("Autor: "));
        menuControlExtension.add(textoAutor);
        menuControl2.add(menuControlExtension);
        menuControlExtension = new JPanel(new FlowLayout());
        menuControlExtension.add(new JLabel("Descripción: "));
        menuControlExtension.add(textoDescr);
        menuControl2.add(menuControlExtension);
        menuControl.add(menuControl2, BorderLayout.CENTER);
        
        menuControl2 = new JPanel( new BorderLayout());
        menuControl2.setBorder(BorderFactory.createEmptyBorder(10,5,0,5));
        menuControl2.add(jBarraProgreso,BorderLayout.CENTER);
        menuControl2.add(textoDuracion,BorderLayout.EAST);
        menuControl.add(menuControl2, BorderLayout.SOUTH);
        /*
        
        menuControl.setLayout(new BoxLayout(menuControl, BoxLayout.PAGE_AXIS));
        menuControl.add(imagenLabel);
        
        menuControl.add(textoAutor);
        menuControl.add(textoDescr);
        
        menuControl.add(jBarraProgreso);
        menuControl.add(textoDuracion);*/
        
        columnaMusica.add(menuControl,BorderLayout.CENTER);

        //Abajo Control

        menuControl = new JPanel();
        menuControl.setLayout(new GridLayout(0,4));
        menuControl.setBorder(BorderFactory.createEmptyBorder(20,5,0,5));
        play = new JButton("Play");
        stop = new JButton("Stop");
        pausa = new JButton("Pause");
        continuar = new JButton("Continue");
        menuControl.add(play);
        menuControl.add(pausa);
        menuControl.add(continuar);
        menuControl.add(stop);

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
        
        //finestra.setSize(400, 300);
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

    public JLabel getTextoLlista() {
        return textoLlista;
    }

    public JLabel getTextoDescr() {
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

    public JLabel getTextoDuracion() {
        return textoDuracion;
    }
}
