package metodos;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jesus Castillo
 */
public class SesionTimer {
    private static long inicio = -1;
    private static boolean corriendo = false;

    public static void iniciarTemporizador(JLabel label) {

        inicio = System.currentTimeMillis();
        corriendo = true;

        Thread hilo = new Thread(() -> {
            while (corriendo) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

                long ahora = System.currentTimeMillis();
                long totalSegundos = (ahora - inicio) / 1000;
                long horas = totalSegundos / 3600;
                long minutos = (totalSegundos % 3600) / 60;
                long segundos = totalSegundos % 60;

                String tiempo = String.format("%02d:%02d:%02d", horas, minutos, segundos);
                SwingUtilities.invokeLater(() -> label.setText(tiempo));
            }
        });

        hilo.setDaemon(true);
        hilo.start();
    }

    public static void detenerTemporizador() {
        corriendo = false;
    }
}

