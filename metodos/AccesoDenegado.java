package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class AccesoDenegado {

    public static void showAccesoDenegado(JFrame parent) {
        // Crear un JDialog personalizado
        JDialog dialog = new JDialog(parent, "Acceso Denegado", true);
        dialog.setUndecorated(true);
        dialog.setSize(350, 250);
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
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth()-3, getHeight()-3, 25, 25));
                
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
                int arc = 25; // Radio de curvatura
                int width = getWidth();
                int height = getHeight();

                // Relleno principal (rectángulo con bordes superiores redondeados)
                g2.fillRoundRect(0, 0, width, height + arc, arc, arc);
                g2.fillRect(0, arc, width, height - arc);

                g2.dispose();
            }
        };
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        headerPanel.setPreferredSize(new Dimension(350, 60));

        // Logo y título en el encabezado
        ImageIcon originalIcon = new ImageIcon("src/icons/Logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel titleLabel = new JLabel("FarmaCode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // CUERPO DEL MENSAJE
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Icono de tacha (X) azul
        JLabel denyIcon = new JLabel(new ImageIcon(createDenyIcon(60, new Color(0, 82, 155))));
        denyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mensaje de acceso denegado
        JLabel messageLabel = new JLabel("Acceso Denegado");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setForeground(new Color(0, 82, 155));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        bodyPanel.add(denyIcon);
        bodyPanel.add(messageLabel);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        // BOTÓN ACEPTAR
        JButton acceptButton = new JButton("Aceptar");
        acceptButton.setBackground(new Color(0, 82, 155));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setFocusPainted(false);
        acceptButton.setFont(new Font("Arial", Font.BOLD, 14));
        acceptButton.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        acceptButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        acceptButton.addActionListener(e -> dialog.dispose());

        // Panel para centrar el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(acceptButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    // Método para crear el icono de tacha (X)
    private static BufferedImage createDenyIcon(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar X
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(color);
        int margin = size / 4;
        g2.drawLine(margin, margin, size - margin, size - margin);
        g2.drawLine(size - margin, margin, margin, size - margin);

        g2.dispose();
        return image;
    }
}