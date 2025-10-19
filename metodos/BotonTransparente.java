package metodos;

import javax.swing.*;
import java.awt.*;

public class BotonTransparente {
    public static void hacerBotonTransparente(JButton boton) {
        // Hacer el fondo del botón transparente
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        
        // Opcional: Personalizar el aspecto cuando el ratón pasa por encima
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void update(Graphics g, JComponent c) {
                if (c.isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                       RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fondo completamente transparente
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                    g2.dispose();
                }
                super.update(g, c);
            }
        });
    }
}
