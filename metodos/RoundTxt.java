package metodos;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Color;
import javax.swing.border.Border;

public class RoundTxt implements Border {

    private int radius;
    private Color color; // Color del borde

    public RoundTxt(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Define el espacio que el borde ocupa alrededor del componente.
        // Asegura que el texto no se superponga con las esquinas redondeadas.
        return new Insets(radius / 2 + 1, radius / 2 + 1, radius / 2 + 1, radius / 2 + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // El borde no es opaco, permitiendo la transparencia de las esquinas
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibuja el borde redondeado
        g2.setColor(color); // Usa el color del borde
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

        g2.dispose();
    }
}