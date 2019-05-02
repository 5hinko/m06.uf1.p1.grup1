/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.audioplayer.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollBar;
import sun.misc.VM;

/**
 *
 * @author Cho_S
 */
public class BarraProgreso extends Thread {

    private JScrollBar barraProgreso;
    private int numProgress = 0;
    private boolean start;
    private boolean pause;
    private boolean stop;

    public BarraProgreso(Object barra) {
        this.barraProgreso = (JScrollBar) barra;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.

        do {
            //Start
            while (start) {

                int progresoNum = barraProgreso.getValue();
                int limitProgreso = barraProgreso.getMaximum();
                for (numProgress = progresoNum; numProgress <= limitProgreso || !stop; numProgress++) {

                    //espera un seg
                    try {
                        java.lang.Thread.sleep(980);
                    } catch (Exception e) {
                    }

                    synchronized (barraProgreso) {
                        barraProgreso.setValue(numProgress);
                    }
                    // Runs inside of the Swing UI thread
                    /*
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            barraProgreso.setValue(i);
                        }
                    });
                     */

                    //Pausa
                    if (pause) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            //reset
        } while (1 == 1);
        //System.out.println("No Me dejes morir!");
    }

    public void todosMismoVariable(boolean active) {
        start = active;
        pause = active;
        stop = active;
    }

    public void saludos() {
        System.out.println("hi");
    }

    public void itsPlay() {
        barraProgreso.setValue(0);
        todosMismoVariable(false);
        start = true;
    }

    public void itsStop() {
        start = false;
        stop = true;
        try {
            wait(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
        }
        todosMismoVariable(false);
        barraProgreso.setValue(0);
    }

    public void itsPause() {
        pause = true;
    }

    public void itsContinuar() {
        notify();
        pause = false;
    }

    public void posicionBarra(float num) {
        numProgress = (int) num;
    }

}
