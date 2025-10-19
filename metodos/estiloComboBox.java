package metodos;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class estiloComboBox {

    public static void aplicarEstiloComboBox(JComboBox<?> comboBox) {
        // Configuración básica
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(50, 50, 50));
        comboBox.setFocusable(false);

        // Borde redondeado azul
        comboBox.setBorder(new RoundBorder(new Color(0, 82, 155), 1.5f, 12));
        comboBox.setEditor(new DefaultEditor(comboBox));

        // Personalizar el renderer de los ítems
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (isSelected) {
                    setBackground(new Color(220, 240, 255));
                    setForeground(new Color(0, 82, 155));
                } else {
                    setBackground(index % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                
                return this;
            }
        });

        // Personalizar el botón desplegable
        Component arrowButton = comboBox.getComponent(0);
        if (arrowButton instanceof JButton) {
            JButton button = (JButton) arrowButton;
            button.setBackground(new Color(0, 82, 155));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            
            // Icono personalizado (flecha azul)
            button.setIcon(createArrowIcon());
        }
    }

    // Clase para el borde redondeado
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

    // Editor personalizado para el combobox
    private static class DefaultEditor extends JTextField implements ComboBoxEditor {
        public DefaultEditor(JComboBox<?> combo) {
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            setFont(combo.getFont());
            setBackground(Color.WHITE);
        }

        @Override
        public Component getEditorComponent() {
            return this;
        }

        @Override
        public void setItem(Object anObject) {
            setText(anObject != null ? anObject.toString() : "");
        }

        @Override
        public Object getItem() {
            return getText();
        }
    }

    // Crear icono de flecha personalizado
    private static Icon createArrowIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 82, 155));
                
                // Dibujar flecha hacia abajo
                int[] xPoints = {x, x + 10, x + 5};
                int[] yPoints = {y, y, y + 6};
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