package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class RoundPanelAbajo {
    public static void aplicarBordesInferioresRedondeados(JPanel panel, int radio, Color colorBorde) {
        panel.setOpaque(false);

        panel.setUI(new javax.swing.plaf.PanelUI() {
            @Override
            public void update(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();
                
                // Crear un path con solo las esquinas inferiores redondeadas
                Path2D path = new Path2D.Float();
                
                // Comenzar desde la esquina superior izquierda
                path.moveTo(0, 0);
                
                // Línea recta a la esquina superior derecha
                path.lineTo(width, 0);
                
                // Línea recta al inicio de la curva inferior derecha
                path.lineTo(width, height - radio);
                
                // Curva esquina inferior derecha
                path.quadTo(width, height, width - radio, height);
                
                // Línea recta al inicio de la curva inferior izquierda
                path.lineTo(radio, height);
                
                // Curva esquina inferior izquierda
                path.quadTo(0, height, 0, height - radio);
                
                // Cerrar el path volviendo al punto inicial
                path.lineTo(0, 0);
                
                // Rellenar el fondo
                g2.setColor(c.getBackground());
                g2.fill(path);
                
                // Dibujar el borde
                g2.setColor(colorBorde);
                g2.draw(path);
                
                g2.dispose();
                super.update(g, c);
            }
        });
    }
}