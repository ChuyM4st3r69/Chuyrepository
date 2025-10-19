package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Jesus Castillo
 */
public class JOptionPaneBienvenida extends JDialog {

    public JOptionPaneBienvenida(JFrame parent, String nombreCompleto, String nombrePuesto) {
        super(parent, "Inicio de sesión", true);

        // Panel principal con bordes redondeados
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo con sombra sutil
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

                // Fondo principal blanco
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);

                // Borde sutil
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setOpaque(false);

        // Encabezado azul con gradiente
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente azul más oscuro
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0x0056B3),
                        0, getHeight(), new Color(0x003D82)
                );
                g2.setPaint(gradient);

                // Solo esquinas superiores redondeadas
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 20, 20);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(400, 55));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        // Logo mejorado
        JLabel lblLogo = new JLabel();
        try {
            // Intenta cargar el logo desde diferentes ubicaciones
            ImageIcon logoIcon = null;
            String[] posiblesPaths = {
                "src/icons/Logo.png",
                "icons/Logo.png",
                "resources/Logo.png",
                "Logo.png"
            };

            for (String path : posiblesPaths) {
                File logoFile = new File(path);
                if (logoFile.exists()) {
                    BufferedImage originalImg = ImageIO.read(logoFile);
                    Image scaledImg = originalImg.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                    logoIcon = new ImageIcon(scaledImg);
                    break;
                }
            }

            if (logoIcon != null) {
                lblLogo.setIcon(logoIcon);
            } else {
                // Si no se encuentra el logo, crear un placeholder
                lblLogo.setText("🏥");
                lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                lblLogo.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            // Fallback: usar emoji como placeholder
            lblLogo.setText("🏥");
            lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            lblLogo.setForeground(Color.WHITE);
        }

        JLabel lblTitulo = new JLabel("FarmaCode");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        header.add(lblLogo);
        header.add(lblTitulo);

        // Cuerpo del mensaje con mejor formato
        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblSaludo = new JLabel("¡Bienvenid@!");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSaludo.setForeground(new Color(0x333333));
        lblSaludo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSaludo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNombre = new JLabel("<html><center>" + nombreCompleto + "</center></html>");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(new Color(0x007BFF));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblPuesto = new JLabel("<html><center>Puesto: " + nombrePuesto + "</center></html>");
        lblPuesto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPuesto.setForeground(new Color(0x666666));
        lblPuesto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPuesto.setHorizontalAlignment(SwingConstants.CENTER);

        cuerpo.add(lblSaludo);
        cuerpo.add(Box.createVerticalStrut(10));
        cuerpo.add(lblNombre);
        cuerpo.add(Box.createVerticalStrut(8));
        cuerpo.add(lblPuesto);

        // Botón mejorado
        JButton btnCerrar = new JButton("Aceptar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(0x0056B3));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0x0069D9));
                } else {
                    g2.setColor(new Color(0x007BFF));
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Texto del botón
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };

        btnCerrar.setPreferredSize(new Dimension(100, 35));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        footer.add(btnCerrar);

        panel.add(header, BorderLayout.NORTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        // Configuración de la ventana
        setUndecorated(true);
        setContentPane(panel);
        setSize(400, 240);

        // Hacer la ventana con forma redondeada
        SwingUtilities.invokeLater(() -> {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
        });

        setLocationRelativeTo(parent);

        // Efecto de aparición suave
        setOpacity(0.0f);
        Timer fadeInTimer = new Timer(10, null);
        fadeInTimer.addActionListener(e -> {
            float opacity = getOpacity();
            if (opacity < 1.0f) {
                setOpacity(Math.min(opacity + 0.05f, 1.0f));
            } else {
                fadeInTimer.stop();
            }
        });
        fadeInTimer.start();
    }
}
