/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 * @author Cho_S
 */
public class BarraProgreso extends Thread {

    private Audio audioMusica;
    private JScrollBar barraProgreso;
    private JLabel textoProgreso;
    private int numBucleProgress = 0;
    private Thread threadSiguiente;
    private boolean start;
    private boolean pause;
    private boolean stop;

    public BarraProgreso(Object barra, Object progreso, Thread threadSiguiente) {
        this.barraProgreso = (JScrollBar) barra;
        this.textoProgreso = (JLabel) progreso;
        todosMismoVariable(false);
        this.threadSiguiente = threadSiguiente;
    }

    @Override
    public void run() {
        System.out.println("Hilo DJ");
        do {
            try {
                //Si no espera hace el bucle sin parar para ver la variable
                Thread.sleep(3);
            } catch (InterruptedException ex) {
                Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Start
            while (start) {
                int progresoNum = barraProgreso.getValue();
                int limitProgreso = barraProgreso.getMaximum();
                for (numBucleProgress = progresoNum; numBucleProgress <= limitProgreso && !stop; numBucleProgress++) {

                    //espera un seg
                    try {
                        java.lang.Thread.sleep(980);
                    } catch (Exception e) {
                        Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, e);
                    }

                    // Runs inside of the Swing UI thread
                    controlladorGUI(numBucleProgress);

                    //Pausa
                    if (pause) {
                        synchronized ((Object) barraProgreso) {
                            try {
                                barraProgreso.wait();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
                if (numBucleProgress >= limitProgreso) {
                    try {
                        itsStop();
                    } catch (BasicPlayerException ex) {
                        Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Hilo Terminado");
                    //threadSiguiente.start();
                    ExecutorService executor = Executors.newCachedThreadPool();
                    executor.submit(threadSiguiente);
                }
            }
            //reset
        } while (1 == 1);
    }

    private void controlladorGUI(int progresoNum) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textoProgreso.setText(convertDecimal(progresoNum / 60) + ":" + convertDecimal(progresoNum % 60));
                barraProgreso.setValue(progresoNum);
            }
        });
    }

    private String convertDecimal(int num) {
        return ((num < 10) ? "0" + num : "" + num);
    }

    public void todosMismoVariable(boolean active) {
        start = active;
        pause = active;
        stop = active;
    }

    public void itsPlay() throws BasicPlayerException {
        itsStop();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
        }
        todosMismoVariable(false);
        controlladorGUI(0);
        audioMusica.getPlayer().play();

        start = true;
    }

    public void itsStop() throws BasicPlayerException {
        start = false;
        stop = true;
        itsContinuar();

        audioMusica.getPlayer().stop();
        controlladorGUI(0);
    }

    public void itsPause() throws BasicPlayerException {
        pause = true;
        audioMusica.getPlayer().pause();
    }

    public void itsContinuar() throws BasicPlayerException {
        pause = false;
        synchronized ((Object) barraProgreso) {
            barraProgreso.notify();
        }
        audioMusica.getPlayer().resume();
    }

    public void posicionBarra(int num) {
        numBucleProgress = num;
        controlladorGUI(num);
    }

    public Audio getAudioMusica() {
        return audioMusica;
    }

    public void setAudioMusica(Audio audioMusica) {
        this.audioMusica = audioMusica;
    }

}
