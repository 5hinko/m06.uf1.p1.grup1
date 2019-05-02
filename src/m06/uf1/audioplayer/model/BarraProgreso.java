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
import sun.misc.VM;

/**
 *
 * @author Cho_S
 */
public class BarraProgreso extends Thread {

    private JScrollBar barraProgreso;
    private JLabel textoProgreso;
    private int numProgress = 0;
    private boolean start;
    private boolean pause;
    private boolean stop;

    public BarraProgreso(Object barra, Object progreso) {
        this.barraProgreso = (JScrollBar) barra;
        this.textoProgreso = (JLabel) progreso;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Hilo Escucha");
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

                    // Runs inside of the Swing UI thread
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            System.out.println("Hilo Start");
                            textoProgreso.setText((progresoNum / 60) + ":" + (progresoNum % 60));
                            barraProgreso.setValue(numProgress);
                        }
                    });

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
            todosMismoVariable(false);
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
        itsContinuar();
        stop = true;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                barraProgreso.setValue(0);
            }
        });
    }

    public void itsPause() {
        pause = true;
    }

    public void itsContinuar() {
        pause = false;
        notify();
    }

    public void posicionBarra(float num) {
        numProgress = (int) num;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                barraProgreso.setValue(numProgress);
            }
        });
    }

}
