package Interfaces;

import metodos.CorreoUtil;
import java.awt.Color;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import metodos.BotonTransparente;
import metodos.RoundPanel;
import metodos.RoundPanelAbajo;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.paintBorder;
import java.sql.Connection;
import java.util.Random;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import metodos.JOptionPaneError;
import metodos.JOptionPaneMensaje;

/**
 *
 * @author Jesus Castillo
 */
public class ConsultarCorreo extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;

    public ConsultarCorreo() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        TextPrompt placeHolder = new TextPrompt("Correo Electronico", TxtCorreoUser);
        TextPrompt placeHolder2 = new TextPrompt("Correo Electronico", PassField);

        RoundTxt redondearTxt = new RoundTxt(30, Color.WHITE);
        TxtCorreoUser.setBorder(redondearTxt);

        PassField.setBorder(redondearTxt);

        BtnSiguiente.setEnabled(false);

        // Panel Redondo
        RoundPanel.aplicarBordesRedondeados(PnlFondoBlanco3, 40, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBtnSiguiente, 20, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlRegresar, 20, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        // Panel Redondo Abajo
        RoundPanelAbajo.aplicarBordesInferioresRedondeados(PnlContainer3, 40, Color.getHSBColor(0f, 0f, 0.85f));

        BtnSiguiente.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnSiguiente.setFocusPainted(false);

        BtnRegresar.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnRegresar.setFocusPainted(false);

        // Boton Transparente
        BotonTransparente.hacerBotonTransparente(BtnSiguiente);
        BotonTransparente.hacerBotonTransparente(BtnRegresar);

        // ImgAjustable
        this.pintarImagen(LblLogo3, "src/icons/Logo.png");

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarEstado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarEstado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarEstado();
            }

            private void validarEstado() {
                String correo = TxtCorreoUser.getText().trim();
                String pass = PassField.getText().trim();

                boolean camposLlenos = !correo.isEmpty() && !pass.isEmpty();
                BtnSiguiente.setEnabled(camposLlenos);
            }
        };

        // Agregar el listener a ambos campos
        TxtCorreoUser.getDocument().addDocumentListener(listener);
        PassField.getDocument().addDocumentListener(listener);
    }

    private void pintarImagen(JLabel lbl, String ruta) {
        this.imagen = new ImageIcon(ruta);
        this.icono = new ImageIcon(
                this.imagen.getImage().getScaledInstance(
                        lbl.getWidth(),
                        lbl.getHeight(),
                        Image.SCALE_DEFAULT
                )
        );
        lbl.setIcon(this.icono);
        this.repaint();

    }

    private void VerificarCorreos() {
        // Obtener y limpiar datos del formulario
        String correo = TxtCorreoUser.getText().trim();
        String pass = PassField.getText().trim();

        // Validar campos iguales
        if (!pass.equals(correo)) {
            JOptionPaneError.showError(this, "Los correos no son iguales");
            return;
        }

        // Verificar si el correo existe en la base de datos
        if (!correoExisteEnBD(correo)) {
            JOptionPaneError.showError(this, "El correo no está registrado.");
            return;
        }

        // Generar código de verificación aleatorio de 4 dígitos
        String codigo = String.format("%04d", new Random().nextInt(10000));

        // Enviar código al correo
        boolean enviado = CorreoUtil.enviarCodigoPorCorreo(correo, codigo);

        if (enviado) {
            // Solicitar código al usuario
            //String codigoIngresado = JOptionPane.showInputDialog(this, "Se ha enviado un código a tu correo.\nIngresa el código de verificación:");

            VerificacionCodigo dialogo = new VerificacionCodigo(this); // "this" sería tu JFrame padre
            dialogo.setVisible(true); // Se mostrará modalmente

            String codigoIngresado = "";

            if (dialogo.isCodigoValido()) {
                codigoIngresado = dialogo.getCodigoIngresado();
            }

            // Verificar código ingresado
            if (codigoIngresado != null && codigoIngresado.equals(codigo)) {
                // Código correcto - proceder
                JOptionPaneMensaje dialog = new JOptionPaneMensaje(this, "Código verificado", "Cambia tu contraseña ahora");
                dialog.setVisible(true);

                this.setVisible(false); // Ocultar ventana actual

                // Crear el segundo JFrame y pasarle el correo
                NuevaContraseña frameActualizar = new NuevaContraseña(correo);
                frameActualizar.setVisible(true);
                frameActualizar.setLocationRelativeTo(null);
                frameActualizar.setTitle("Nueva contraseña");
                this.dispose(); // Opcional: cerrar el frame actual

            } else {
                // Código incorrecto
                JOptionPaneError.showError(this, "Código incorrecto.");
                this.dispose();

                Inicio_sesión inicio = new Inicio_sesión();

                inicio.setVisible(true);
                inicio.setLocationRelativeTo(null);
                inicio.setTitle("Inicio de sesión");
            }
        } else {
            // Error al enviar correo
            JOptionPaneError.showError(this, "Error al enviar correo.");

            Inicio_sesión inicio = new Inicio_sesión();

            inicio.setVisible(true);
            inicio.setLocationRelativeTo(null);
            inicio.setTitle("Inicio de sesión");
        }
    }

    private boolean correoExisteEnBD(String correo) {
        try (Connection conn = farmacia.Farmacia.ConectarBD()) {

            // Consulta para verificar existencia
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM correos_empleados WHERE correo = ?");
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Retorna true si encuentra coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PnlFondoBlanco3 = new javax.swing.JPanel();
        LblLogo3 = new javax.swing.JLabel();
        LblFarmaCode3 = new javax.swing.JLabel();
        PnlContainer3 = new javax.swing.JPanel();
        LblUser3 = new javax.swing.JLabel();
        TxtCorreoUser = new javax.swing.JTextField();
        PnlBtnSiguiente = new javax.swing.JPanel();
        BtnSiguiente = new javax.swing.JButton();
        LblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PnlRegresar = new javax.swing.JPanel();
        BtnRegresar = new javax.swing.JButton();
        PassField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(170, 170, 170));

        PnlFondoBlanco3.setBackground(new java.awt.Color(255, 255, 255));

        LblFarmaCode3.setBackground(new java.awt.Color(0, 119, 182));
        LblFarmaCode3.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        LblFarmaCode3.setForeground(new java.awt.Color(0, 119, 182));
        LblFarmaCode3.setText("FarmaCode");

        PnlContainer3.setBackground(new java.awt.Color(217, 217, 217));

        LblUser3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblUser3.setForeground(new java.awt.Color(0, 119, 182));
        LblUser3.setText("Correo electrónico");

        TxtCorreoUser.setBackground(new java.awt.Color(217, 217, 217));
        TxtCorreoUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        TxtCorreoUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCorreoActionPerformed(evt);
            }
        });

        PnlBtnSiguiente.setBackground(new java.awt.Color(0, 119, 182));

        BtnSiguiente.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        BtnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        BtnSiguiente.setText("Recibir codigo");
        BtnSiguiente.setToolTipText("");
        BtnSiguiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnSiguienteMouseClicked(evt);
            }
        });
        BtnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnIniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBtnSiguienteLayout = new javax.swing.GroupLayout(PnlBtnSiguiente);
        PnlBtnSiguiente.setLayout(PnlBtnSiguienteLayout);
        PnlBtnSiguienteLayout.setHorizontalGroup(
            PnlBtnSiguienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnSiguiente, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        );
        PnlBtnSiguienteLayout.setVerticalGroup(
            PnlBtnSiguienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnSiguiente, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(0, 119, 182));
        LblTitulo.setText("Recuperar Contraseña");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 119, 182));
        jLabel1.setText("Ingrese su correo electrónico para recibir un código de verificación");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 119, 182));
        jLabel2.setText("Confirme su correo electrónico");

        PnlRegresar.setBackground(new java.awt.Color(0, 119, 182));
        PnlRegresar.setForeground(new java.awt.Color(0, 119, 182));

        BtnRegresar.setBackground(new java.awt.Color(0, 119, 182));
        BtnRegresar.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        BtnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        BtnRegresar.setText("Regresar");
        BtnRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnRegresarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PnlRegresarLayout = new javax.swing.GroupLayout(PnlRegresar);
        PnlRegresar.setLayout(PnlRegresarLayout);
        PnlRegresarLayout.setHorizontalGroup(
            PnlRegresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlRegresarLayout.setVerticalGroup(
            PnlRegresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        PassField.setBackground(new java.awt.Color(217, 217, 217));
        PassField.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout PnlContainer3Layout = new javax.swing.GroupLayout(PnlContainer3);
        PnlContainer3.setLayout(PnlContainer3Layout);
        PnlContainer3Layout.setHorizontalGroup(
            PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlContainer3Layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addGroup(PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(PnlContainer3Layout.createSequentialGroup()
                        .addComponent(PnlRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlBtnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblTitulo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtCorreoUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addComponent(LblUser3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PassField))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlContainer3Layout.setVerticalGroup(
            PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlContainer3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(LblUser3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TxtCorreoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PassField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PnlBtnSiguiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PnlRegresar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout PnlFondoBlanco3Layout = new javax.swing.GroupLayout(PnlFondoBlanco3);
        PnlFondoBlanco3.setLayout(PnlFondoBlanco3Layout);
        PnlFondoBlanco3Layout.setHorizontalGroup(
            PnlFondoBlanco3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoBlanco3Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(LblLogo3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(LblFarmaCode3)
                .addContainerGap(207, Short.MAX_VALUE))
            .addComponent(PnlContainer3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlFondoBlanco3Layout.setVerticalGroup(
            PnlFondoBlanco3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoBlanco3Layout.createSequentialGroup()
                .addGroup(PnlFondoBlanco3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFondoBlanco3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LblLogo3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(PnlFondoBlanco3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(LblFarmaCode3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(PnlContainer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(290, 290, 290)
                .addComponent(PnlFondoBlanco3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(315, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(PnlFondoBlanco3, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnIniciarActionPerformed

    }//GEN-LAST:event_BtnIniciarActionPerformed

    private void TxtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCorreoActionPerformed

    }//GEN-LAST:event_TxtCorreoActionPerformed

    private void BtnSiguienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnSiguienteMouseClicked
        VerificarCorreos();


    }//GEN-LAST:event_BtnSiguienteMouseClicked

    private void BtnRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRegresarMouseClicked
        this.dispose();

        Inicio_sesión inicio = new Inicio_sesión();

        inicio.setVisible(true);
        inicio.setLocationRelativeTo(null);
        inicio.setTitle("Inicio de sesión");
    }//GEN-LAST:event_BtnRegresarMouseClicked

    public class VerificacionCodigo extends JDialog {

        private JTextField campoCodigo;
        private JButton botonVerificar;
        private JButton botonCancelar;
        private boolean codigoValido = false;

        public VerificacionCodigo(JFrame parent) {
            super(parent, "Verificación de Código", true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

            // Encabezado con gradiente azul
            JPanel header = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Gradiente azul oscuro
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
            header.setPreferredSize(new Dimension(450, 60));
            header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

            // Logo mejorado
            JLabel lblLogo = new JLabel();
            try {
                String[] posiblesPaths = {
                    "src/icons/Logo.png",
                    "icons/Logo.png",
                    "resources/Logo.png",
                    "Logo.png"
                };

                ImageIcon logoIcon = null;
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
                    lblLogo.setText("🔐");
                    lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                    lblLogo.setForeground(Color.WHITE);
                }
            } catch (Exception e) {
                lblLogo.setText("🔐");
                lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                lblLogo.setForeground(Color.WHITE);
            }

            JLabel lblTitulo = new JLabel("FarmaCode");
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

            header.add(lblLogo);
            header.add(lblTitulo);

            // Contenido principal
            JPanel contenido = new JPanel();
            contenido.setOpaque(false);
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            contenido.setBorder(BorderFactory.createEmptyBorder(25, 40, 30, 40));

            // Título principal
            JLabel etiquetaVerificar = new JLabel("Verificar Código");
            etiquetaVerificar.setFont(new Font("Segoe UI", Font.BOLD, 22));
            etiquetaVerificar.setForeground(new Color(0x333333));
            etiquetaVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Mensaje explicativo
            JLabel mensajeCorreo = new JLabel("<html><center>Hemos enviado un código de verificación<br/>a tu correo electrónico</center></html>");
            mensajeCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            mensajeCorreo.setForeground(new Color(0x666666));
            mensajeCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
            mensajeCorreo.setHorizontalAlignment(SwingConstants.CENTER);

            // Icono de email
            JLabel iconoEmail = new JLabel("📧");
            iconoEmail.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            iconoEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Etiqueta del campo
            JLabel etiquetaCodigo = new JLabel("Código de Verificación");
            etiquetaCodigo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            etiquetaCodigo.setForeground(new Color(0x333333));
            etiquetaCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Campo de texto estilizado
            campoCodigo = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Fondo
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                    // Borde
                    if (hasFocus()) {
                        g2.setColor(new Color(0x0056B3));
                        g2.setStroke(new BasicStroke(2));
                    } else {
                        g2.setColor(new Color(0xCCCCCC));
                        g2.setStroke(new BasicStroke(1));
                    }
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                    super.paintComponent(g);
                }
            };

            campoCodigo.setOpaque(false);
            campoCodigo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            campoCodigo.setHorizontalAlignment(JTextField.CENTER);
            campoCodigo.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
            campoCodigo.setMaximumSize(new Dimension(200, 45));
            campoCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Panel de botones
            JPanel panelBotones = new JPanel();
            panelBotones.setOpaque(false);
            panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
            panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Botón Verificar
            botonVerificar = new JButton("Verificar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isPressed()) {
                        g2.setColor(new Color(0x003D82));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(0x004494));
                    } else {
                        g2.setColor(new Color(0x0056B3));
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

            botonVerificar.setPreferredSize(new Dimension(110, 40));
            botonVerificar.setFocusPainted(false);
            botonVerificar.setBorderPainted(false);
            botonVerificar.setContentAreaFilled(false);
            botonVerificar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            botonVerificar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Botón Cancelar
            botonCancelar = new JButton("Cancelar") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isPressed()) {
                        g2.setColor(new Color(0x5A6268));
                    } else if (getModel().isRollover()) {
                        g2.setColor(new Color(0x6C757D));
                    } else {
                        g2.setColor(new Color(0x868E96));
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

            botonCancelar.setPreferredSize(new Dimension(110, 40));
            botonCancelar.setFocusPainted(false);
            botonCancelar.setBorderPainted(false);
            botonCancelar.setContentAreaFilled(false);
            botonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            botonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            panelBotones.add(botonVerificar);
            panelBotones.add(botonCancelar);

            // Agregar componentes al contenido
            contenido.add(etiquetaVerificar);
            contenido.add(Box.createRigidArea(new Dimension(0, 8)));
            contenido.add(mensajeCorreo);
            contenido.add(Box.createRigidArea(new Dimension(0, 20)));
            contenido.add(iconoEmail);
            contenido.add(Box.createRigidArea(new Dimension(0, 20)));
            contenido.add(etiquetaCodigo);
            contenido.add(Box.createRigidArea(new Dimension(0, 8)));
            contenido.add(campoCodigo);
            contenido.add(Box.createRigidArea(new Dimension(0, 20)));
            contenido.add(panelBotones);
            contenido.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio adicional

            panel.add(header, BorderLayout.NORTH);
            panel.add(contenido, BorderLayout.CENTER);

            // Configuración de la ventana
            setUndecorated(true);
            setContentPane(panel);
            setSize(450, 420);

            // Hacer la ventana con forma redondeada
            SwingUtilities.invokeLater(() -> {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            });

            setLocationRelativeTo(parent);

            // Event listeners
            botonVerificar.addActionListener(e -> {
                String codigo = campoCodigo.getText().trim();
                if (codigo.isEmpty()) {
                    JOptionPaneError.showError((JFrame)getParent(),"Por favor, ingresa el código de verificación");
                    return;
                }
                codigoValido = true;
                dispose();
            });

            botonCancelar.addActionListener(e -> {
                codigoValido = false;
                dispose();
            });

            // Enter key para verificar
            campoCodigo.addActionListener(e -> botonVerificar.doClick());

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

            // Focus automático en el campo
            SwingUtilities.invokeLater(() -> campoCodigo.requestFocus());
        }

        public String getCodigoIngresado() {
            return campoCodigo.getText().trim();
        }

        public boolean isCodigoValido() {
            return codigoValido;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnRegresar;
    private javax.swing.JButton BtnSiguiente;
    private javax.swing.JLabel LblFarmaCode3;
    private javax.swing.JLabel LblLogo3;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblUser3;
    private javax.swing.JTextField PassField;
    private javax.swing.JPanel PnlBtnSiguiente;
    private javax.swing.JPanel PnlContainer3;
    private javax.swing.JPanel PnlFondoBlanco3;
    private javax.swing.JPanel PnlRegresar;
    private javax.swing.JTextField TxtCorreoUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
