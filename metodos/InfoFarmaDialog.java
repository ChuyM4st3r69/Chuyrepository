package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jesus Castillo
 */
public class InfoFarmaDialog {

    public static void showDialog(JFrame parent) {
        // Crear un JDialog personalizado
        JDialog dialog = new JDialog(parent, "Información de FarmaCode", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 300); // Un poco más ancho para la información
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Panel principal con bordes redondeados y borde azul
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar fondo blanco con bordes redondeados
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

                // Dibujar borde azul
                g2.setColor(new Color(0, 82, 155));
                g2.setStroke(new BasicStroke(3f)); // Grosor del borde
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, 25, 25));

                g2.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // ENCABEZADO AZUL (solo bordes superiores redondeados)
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 82, 155));

                // Dibujamos un rectángulo con solo las esquinas superiores redondeadas
                int arc = 25;
                int width = getWidth();
                int height = getHeight();

                g2.fillRoundRect(0, 0, width, height + arc, arc, arc);
                g2.fillRect(0, arc, width, height - arc);

                g2.dispose();
            }
        };
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setPreferredSize(new Dimension(400, 60));

        // Logo y título en el encabezado
        ImageIcon originalIcon = new ImageIcon("src/icons/Logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel titleLabel = new JLabel("FarmaCode - Contacto");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CUERPO CON LA INFORMACIÓN
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Icono de información
        JLabel infoIcon = new JLabel(new ImageIcon(createInfoIcon(60, new Color(0, 82, 155))));
        infoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Información de contacto
        JLabel contactTitle = new JLabel("Contácte a un administrador:");
        contactTitle.setFont(new Font("Arial", Font.BOLD, 16));
        contactTitle.setForeground(new Color(0, 82, 155));
        contactTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contactTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JPanel phonePanel = createContactPanel("Teléfono:", "669 932 8266");
        JPanel emailPanel = createContactPanel("Correo:", "soporteFarmacode@gmail.com");

        bodyPanel.add(infoIcon);
        bodyPanel.add(contactTitle);
        bodyPanel.add(phonePanel);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bodyPanel.add(emailPanel);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        // BOTÓN ACEPTAR (mejorado)
        JButton acceptButton = new JButton("Aceptar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo azul con bordes redondeados
                g2.setColor(new Color(0, 82, 155));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Texto blanco centrado
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Borde más oscuro para el botón
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 60, 120));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        acceptButton.setContentAreaFilled(false);
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setFont(new Font("Arial", Font.BOLD, 14));
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        acceptButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(acceptButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private static JPanel createContactPanel(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(100, 100, 100));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 14));
        val.setForeground(new Color(70, 70, 70));

        panel.add(lbl);
        panel.add(val);

        return panel;
    }

    private static BufferedImage createInfoIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar círculo de fondo
        g2.setColor(color);
        g2.fillOval(2, 2, size - 4, size - 4);

        // Dibujar "i" de información
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(size / 2, size / 4, size / 2, size / 4 * 3); // Línea vertical
        g2.fillOval(size / 2 - 2, size / 4 * 3 + 5, 4, 4); // Punto inferior

        g2.dispose();
        return image;
    }
}
