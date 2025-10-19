package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Path2D;

public class RoundIzqPanel {
    public static void aplicarBordesRedondeadosIzquierda(JPanel panel, int radio) {
        panel.setOpaque(false);

        panel.setUI(new javax.swing.plaf.PanelUI() {
            @Override
            public void update(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Crear un path que solo redondee las esquinas izquierdas
                Path2D path = new Path2D.Float();
                int width = c.getWidth();
                int height = c.getHeight();
                
                // Esquina superior izquierda
                path.moveTo(radio, 0);
                path.lineTo(width, 0);
                
                // Esquina superior derecha (sin redondear)
                path.lineTo(width, height);
                
                // Esquina inferior izquierda
                path.lineTo(radio, height);
                path.quadTo(0, height, 0, height - radio);
                
                // Esquina inferior izquierda (continuación)
                path.lineTo(0, radio);
                path.quadTo(0, 0, radio, 0);
                
                g2.setColor(c.getBackground());
                g2.fill(path);
                g2.dispose();

                super.update(g, c);
            }
        });
    }
}