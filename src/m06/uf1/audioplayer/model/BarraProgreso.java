/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import m06.uf1.audioplayer.controlador.Controlador;

/**
 *
 * @author Cho_S
 */
public class BarraProgreso extends Thread {

    private JScrollBar barraProgreso;
    private JLabel textoProgreso;
    private int numBucleProgress = 0;
    private boolean start;
    private boolean pause;
    private boolean stop;

    public BarraProgreso(Object barra, Object progreso) {
        this.barraProgreso = (JScrollBar) barra;
        this.textoProgreso = (JLabel) progreso;
        todosMismoVariable(false);
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Hilo DJ");
        do {
            try {
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
                    itsStop();
                    System.out.println("Hilo Terminado");
                    Controlador.cancionTerminada();
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

    public void itsPlay() {
        itsStop();
        try {
            Thread.sleep(3);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
        }
        todosMismoVariable(false);
        start = true;
    }

    public void itsStop() {
        start = false;
        stop = true;
        itsContinuar();

        numBucleProgress = 0;
        controlladorGUI(0);
    }

    public void itsPause() {
        pause = true;
    }

    public void itsContinuar() {
        pause = false;
        synchronized ((Object) barraProgreso) {
            barraProgreso.notify();
        }
    }

    public void posicionBarra(int num) {
        numBucleProgress = num;
        controlladorGUI(num);
    }

}
