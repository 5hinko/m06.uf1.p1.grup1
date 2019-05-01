package m06.uf1.audioplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;

public class Vista {

    private JFrame finestra;
    private JPanel jPrincipal;
    private JPanel columnaLista;
    private JPanel columnaMusica;
    private JPanel menuControl;

    private JComboBox jBoxAlbum;
    private JScrollPane jScroll;
    private JTable jTablaMusica;

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

        jPrincipal = new JPanel(new GridLayout(0,3));
        jPrincipal.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        columnaLista = new JPanel();
        columnaLista.setLayout(new BoxLayout(columnaLista, BoxLayout.PAGE_AXIS));

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

        textoLlista = new JLabel("Nombre Llista - ");
        textoTitulo = new JLabel("Titulo Canción");
        imagenLabel = new JLabel("imagen");
        imagenLabel.setSize(300, 300);
        textoAutor = new JLabel("Autor :");
        textoDescr = new JLabel("Descr :");
        
        jBarraProgreso = new JScrollBar();
        jBarraProgreso.setOrientation(JScrollBar.HORIZONTAL);
        jBarraProgreso.setMaximum(0);
        jBarraProgreso.setMaximum(100);
        jBarraProgreso.setPreferredSize(new Dimension(250,15));
        textoDuracion = new JLabel("0:00/3:00");

        //Titulo 
        menuControl = new JPanel( new FlowLayout());
        menuControl.add(textoLlista);
        menuControl.add(textoTitulo);
        
        columnaMusica.add(menuControl,BorderLayout.NORTH);

        //Centro
        /*menuControl = new JPanel();
        menuControl.setLayout(new BorderLayout());
        menuControl.add(imagenLabel, BorderLayout.NORTH);
        
        menuControl.add(textoAutor, BorderLayout.CENTER);
        menuControl.add(textoDescr, BorderLayout.CENTER);
        
        menuControl.add(jBarraProgreso, BorderLayout.SOUTH);
        menuControl.add(textoDuracion, BorderLayout.SOUTH);
        
        columnaMusica.add(menuControl,BorderLayout.CENTER);*/
        
        menuControl = new JPanel();
        menuControl.setLayout(new BoxLayout(menuControl, BoxLayout.PAGE_AXIS));
        menuControl.add(imagenLabel);
        
        menuControl.add(textoAutor);
        menuControl.add(textoDescr);
        
        menuControl.add(jBarraProgreso);
        menuControl.add(textoDuracion);
        
        columnaMusica.add(menuControl,BorderLayout.CENTER);

        //Abajo Control

        menuControl = new JPanel();
        menuControl.setLayout(new GridLayout(0,4));
        play = new JButton("Play");
        stop = new JButton("Stop");
        pausa = new JButton("Pause");
        continuar = new JButton("Continue");
        menuControl.add(play);
        menuControl.add(pausa);
        menuControl.add(continuar);
        menuControl.add(stop);

        columnaMusica.add(menuControl, BorderLayout.SOUTH);

        jPrincipal.add(columnaLista);
        jPrincipal.add(columnaMusica);
        finestra.add(jPrincipal);
        
        //finestra.setSize(400, 300);
        //finestra.setResizable(false);
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
}
