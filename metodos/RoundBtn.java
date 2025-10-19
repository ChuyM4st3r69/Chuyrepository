package metodos;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundBtn extends AbstractBorder {
    private final int radius;
    private final Color borderColor;

    public RoundBtn(int radius, Color borderColor) {
        this.radius = radius;
        this.borderColor = borderColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar borde redondeado
        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(radius / 2, radius / 2, radius / 2, radius / 2);
        return insets;
    }

    // ✅ Método para aplicar el estilo a un botón ya creado
    public static void configurarBoton(JButton boton, int radio, Color colorBorde, Color colorFondo, Color colorTexto) {
        boton.setBorder(new paintBorder(radio, colorBorde));
        boton.setOpaque(true); // Deja que Swing pinte el fondo
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(true); // Importante para que se pinte el fondo correctamente
    }
}
