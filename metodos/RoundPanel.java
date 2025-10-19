package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundPanel {
    public static void aplicarBordesRedondeados(JPanel panel, int radio, Color colorBorde) {
        panel.setOpaque(false);

        panel.setUI(new javax.swing.plaf.PanelUI() {
            @Override
            public void update(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Crear la forma redondeada
                RoundRectangle2D shape = new RoundRectangle2D.Float(0, 0, c.getWidth()-1, c.getHeight()-1, radio, radio);
                
                // Rellenar el fondo
                g2.setColor(c.getBackground());
                g2.fill(shape);
                
                // Dibujar el borde
                g2.setColor(colorBorde);
                g2.draw(shape);
                
                g2.dispose();
                super.update(g, c);
            }
        });
    }
}
