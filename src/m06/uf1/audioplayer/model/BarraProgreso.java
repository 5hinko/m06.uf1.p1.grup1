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
    private boolean continuar;
    private boolean stop;

    public BarraProgreso(Object barra) {
        this.barraProgreso = (JScrollBar) barra;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
/*
        do {
            //Start
            while () {

                for (i = 0; i <= 100; i++) {

                    // Runs inside of the Swing UI thread
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            progressBar.setValue(i);
                        }
                    });

                    try {
                        java.lang.Thread.sleep(980);
                    } catch (Exception e) {
                    }
                }

                //Pausa
                if (pause) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BarraProgreso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Stop
                exit;
            }
            //reset
        } while (1 == 1);
*/
        System.out.println("No Me dejes morir!");
    }

    public void saludos() {
        System.out.println("hi");
    }

    public void itsPlay() {

    }

    public void itsStop() {
        barraProgreso.setValue(0);
    }

    public void itsPause() {
        pause = true;
    }

    public void itsContinuar() {
        notify();
    }

}
