package metodos;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DateChooserDesign {

    public static void aplicarEstiloDateChooser(JDateChooser dateChooser) {
        // Configuración básica
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Personalizar el editor de fecha
        JTextField dateEditor = (JTextField) dateChooser.getDateEditor();
        dateEditor.setBackground(Color.WHITE);
        dateEditor.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        dateEditor.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Borde redondeado azul para el componente completo
        dateChooser.setBorder(new RoundBorder(new Color(0, 82, 155), 1.5f, 12));
        
        // Personalizar el botón del calendario
        JButton calendarButton = dateChooser.getCalendarButton();
        calendarButton.setBackground(new Color(0, 82, 155));
        calendarButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        calendarButton.setContentAreaFilled(false);
        calendarButton.setIcon(createCalendarIcon());
        calendarButton.setRolloverEnabled(true);
        
        // Efecto hover
        calendarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                calendarButton.setBackground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                calendarButton.setBackground(new Color(0, 82, 155));
            }
        });
        
        // Personalizar el popup del calendario
        JCalendar calendar = dateChooser.getJCalendar();
        calendar.setBackground(Color.WHITE);
        calendar.setDecorationBackgroundColor(new Color(0, 82, 155));
        calendar.setDecorationBackgroundVisible(true);
        calendar.setWeekOfYearVisible(false);
        
        // Personalizar los días
        calendar.setSundayForeground(new Color(200, 0, 0)); // Rojo para domingos
        calendar.setWeekdayForeground(Color.BLACK);
        
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

    // Icono de calendario personalizado
    private static Icon createCalendarIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar icono de calendario estilizado
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(x + 2, y + 4, 12, 10, 2, 2); // Cuerpo
                g2.setColor(new Color(0, 60, 120));
                g2.fillRoundRect(x + 2, y, 12, 5, 2, 2);      // Encabezado
                
                // Líneas para días
                g2.setColor(new Color(0, 82, 155));
                g2.drawLine(x + 5, y + 7, x + 7, y + 7);
                g2.drawLine(x + 9, y + 7, x + 11, y + 7);
                g2.drawLine(x + 5, y + 10, x + 7, y + 10);
                g2.drawLine(x + 9, y + 10, x + 11, y + 10);
                
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    // Método para crear un JDateChooser con estilo ya aplicado
    public static JDateChooser crearDateChooserConEstilo() {
        JDateChooser dateChooser = new JDateChooser();
        aplicarEstiloDateChooser(dateChooser);
        dateChooser.setDateFormatString("dd/MM/yyyy");
        return dateChooser;
    }
}