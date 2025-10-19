package metodos;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Jesus Castillo
 */
public class JOptionPaneError extends JDialog {

    public JOptionPaneError(JFrame parent, String mensaje, String titulo) {
        super(parent, titulo, true);

        // Calcular el ancho óptimo basado en el texto PRIMERO
        JLabel lblMensajeTemporal = new JLabel();
        lblMensajeTemporal.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Procesar el mensaje para obtener las líneas
        String[] lineas = mensaje.split("\\n");
        int anchoMaximo = 0;
        int numeroLineas = lineas.length;

        // Medir cada línea para encontrar la más ancha
        FontMetrics fm = lblMensajeTemporal.getFontMetrics(lblMensajeTemporal.getFont());
        for (String linea : lineas) {
            String lineaSinHTML = linea.replaceAll("<[^>]*>", "").trim();
            if (!lineaSinHTML.isEmpty()) {
                int anchoLinea = fm.stringWidth(lineaSinHTML);
                anchoMaximo = Math.max(anchoMaximo, anchoLinea);
            }
        }

        // Calcular ancho óptimo considerando el contenido más ancho
        // Mínimo 350, máximo 600 para mensajes largos
        final int anchoOptimo = Math.max(350, Math.min(600, anchoMaximo + 80));

        // Calcular altura estimada basada en número de líneas
        int alturaEstimada = 150 + (numeroLineas * 20); // Base + líneas adicionales

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

                // Borde sutil rojo
                g2.setColor(new Color(220, 53, 69, 100));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        panel.setOpaque(false);

        // Encabezado rojo con gradiente
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente rojo
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0xDC3545),
                        0, getHeight(), new Color(0xB02A37)
                );
                g2.setPaint(gradient);

                // Solo esquinas superiores redondeadas
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 10, 20, 20);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(anchoOptimo, 55));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        // Icono de error
        JLabel lblIcono = new JLabel("⚠️");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        header.add(lblIcono);
        header.add(lblTitulo);

        // Cuerpo del mensaje
        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Usar JTextPane para mejor manejo de texto centrado
        JTextPane txtMensaje = new JTextPane();
        txtMensaje.setText(mensaje.replaceAll("<[^>]*>", "")); // Remover etiquetas HTML
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMensaje.setForeground(new Color(0x333333));
        txtMensaje.setOpaque(false);
        txtMensaje.setEditable(false);
        txtMensaje.setFocusable(false);

        // Alinear el texto al centro horizontal
        StyledDocument doc = txtMensaje.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Panel contenedor para centrar horizontal y vertical
        JPanel panelTexto = new JPanel(new GridBagLayout());
        panelTexto.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        panelTexto.add(txtMensaje, gbc);
        cuerpo.add(Box.createVerticalGlue()); // Espacio flexible arriba
        cuerpo.add(panelTexto);
        cuerpo.add(Box.createVerticalGlue()); // Espacio flexible abajo

        // Botón de aceptar personalizado
        JButton btnAceptar = new JButton("Aceptar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(0xB02A37));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0xC82333));
                } else {
                    g2.setColor(new Color(0xDC3545));
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

        btnAceptar.setPreferredSize(new Dimension(100, 35));
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setContentAreaFilled(false);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAceptar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAceptar.addActionListener(e -> dispose());

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        footer.add(btnAceptar);

        panel.add(header, BorderLayout.NORTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        // Configuración de la ventana
        setUndecorated(true);
        setContentPane(panel);

        // Tamaño basado en el contenido calculado
        setSize(anchoOptimo, Math.max(200, alturaEstimada));

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

        // Efecto de vibración sutil para llamar la atención
        Timer shakeTimer = new Timer(50, null);
        Point originalLocation = getLocation();
        final int[] shakeCount = {0};
        shakeTimer.addActionListener(e -> {
            if (shakeCount[0] < 6) {
                int offset = (shakeCount[0] % 2 == 0) ? 3 : -3;
                setLocation(originalLocation.x + offset, originalLocation.y);
                shakeCount[0]++;
            } else {
                setLocation(originalLocation);
                shakeTimer.stop();
            }
        });

        // Iniciar vibración después del fade-in
        Timer delayTimer = new Timer(300, e -> {
            shakeTimer.start();
            ((Timer) e.getSource()).stop();
        });
        delayTimer.start();
    }

    // Método estático para uso fácil
    public static void showError(JFrame parent, String mensaje, String titulo) {
        SwingUtilities.invokeLater(() -> {
            JOptionPaneError dialog = new JOptionPaneError(parent, mensaje, titulo);
            dialog.setVisible(true);
        });
    }

    // Método sobrecargado con título por defecto
    public static void showError(JFrame parent, String mensaje) {
        showError(parent, mensaje, "Error");
    }
}
