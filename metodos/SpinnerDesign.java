package metodos;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SpinnerDesign {

    public static void aplicarEstiloSpinner(JSpinner spinner) {
        // Configuración básica
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setBackground(Color.WHITE);
        spinner.setForeground(new Color(50, 50, 50));
        spinner.setOpaque(true);
        
        // Obtener el editor del spinner
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setOpaque(false);
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        
        // Borde redondeado azul
        spinner.setBorder(new RoundBorder(new Color(0, 82, 155), 1.5f, 12));
        
        // Personalizar los botones
        Component arrowUp = spinner.getComponent(0);
        Component arrowDown = spinner.getComponent(1);
        
        if (arrowUp instanceof JButton && arrowDown instanceof JButton) {
            styleSpinnerButton((JButton) arrowUp, true);
            styleSpinnerButton((JButton) arrowDown, false);
        }
    }

    private static void styleSpinnerButton(JButton button, boolean isUpButton) {
        button.setBackground(new Color(0, 82, 155));
        button.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        // Icono de flecha personalizado
        button.setIcon(createSpinnerArrowIcon(isUpButton));
        button.setRolloverEnabled(true);
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 82, 155));
            }
        });
    }

    // Clase para el borde redondeado (igual que en ComboBox)
    private static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final float thickness;
        private final int radius;

        public RoundBorder(Color color, float thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(x + thickness/2, y + thickness/2, 
                    width - thickness, height - thickness, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets((int)thickness + 2, (int)thickness + 8, (int)thickness + 2, (int)thickness + 8);
        }
    }

    // Crear icono de flecha para spinner (arriba/abajo)
    private static Icon createSpinnerArrowIcon(boolean isUp) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                
                // Dibujar flecha hacia arriba o abajo
                int[] xPoints = {x, x + 8, x + 4};
                int[] yPoints;
                if (isUp) {
                    yPoints = new int[]{y + 6, y + 6, y};
                } else {
                    yPoints = new int[]{y, y, y + 6};
                }
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 12;
            }

            @Override
            public int getIconHeight() {
                return 8;
            }
        };
    }
}