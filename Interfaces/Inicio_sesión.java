package Interfaces;

import farmacia.Farmacia;
import java.awt.Color;
import java.awt.Cursor;
import metodos.TextPrompt;
import metodos.paintBorder;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import metodos.RoundTxt;
import metodos.RoundPanel;
import metodos.BotonTransparente;
import metodos.RoundPanelAbajo;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import metodos.RoundPassword;
import java.sql.SQLException;
import java.sql.CallableStatement;
import javax.swing.JFrame;
import java.sql.Types;
import metodos.JOptionPaneBienvenida;
import metodos.JOptionPaneError;

/**
 *
 * @author Jesus Castillo
 */
public class Inicio_sesión extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;
    public static String contra;
    private boolean passwordVisible = false;

    public Inicio_sesión() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Username", TxtUser);
        TextPrompt placeHolder2 = new TextPrompt("Password", JPassword);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.WHITE);
        TxtUser.setBorder(redondearTxt);

        JPassword.setBorder(redondearTxt);

        BotonTransparente.hacerBotonTransparente(BtnOlvidarC);

        // Panel Redondo
        RoundPanel.aplicarBordesRedondeados(PnlFondoBlanco, 40, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBtn, 20, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        // Panel Redondo Abajo
        RoundPanelAbajo.aplicarBordesInferioresRedondeados(PnlContainer, 40, Color.getHSBColor(0f, 0f, 0.85f));

        // ImgAjustable
        this.pintarImagen(LblLogo, "src/icons/Logo.png");
        initPasswordToggle(LblOjo,JPassword);

        // Boton Redondo
        BtnIniciar.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnIniciar.setFocusPainted(false);

        // Boton Transparente
        BotonTransparente.hacerBotonTransparente(BtnIniciar);

        // Campos Obligatorios
        BtnIniciar.setEnabled(false);

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
                boolean camposLlenos = !TxtUser.getText().trim().isEmpty() && !JPassword.getText().trim().isEmpty();
                boolean numeros = TxtUser.getText().matches("^[a-zA-Z]+$");
                boolean validar = (camposLlenos && numeros);
                BtnIniciar.setEnabled(validar);
            }
        };

        TxtUser.getDocument().addDocumentListener(listener);
        JPassword.getDocument().addDocumentListener(listener);

        // JPassword Redondo
        JPassword.setBorder(new RoundPassword(30, Color.WHITE));

        // Simula que se presionó el botón
        JPassword.addActionListener((ActionEvent e) -> {
            BtnIniciar.doClick();
        });
    }

    private void initPasswordToggle(JLabel toggleLabel, JPasswordField passwordField) {
        // Configurar cursor como mano para indicar que es clickeable
        toggleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Agregar tooltip explicativo
        toggleLabel.setToolTipText("Mostrar/Ocultar contraseña");

        // Agregar listener de clic
        toggleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                togglePasswordVisibility(toggleLabel, passwordField);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Efecto hover - cambiar opacidad o tamaño ligeramente
                toggleLabel.setOpaque(true);
                toggleLabel.setBackground(new Color(0, 0, 0, 20));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Quitar efecto hover
                toggleLabel.setOpaque(false);
            }
        });

        // Establecer estado inicial (oculto)
        this.pintarImagen(toggleLabel, "src/icons/visible.png");
        passwordField.setEchoChar('•'); // Carácter de puntos
    }

    private void togglePasswordVisibility(JLabel toggleLabel, JPasswordField passwordField) {
        if (passwordVisible) {
            // Ocultar contraseña
            passwordField.setEchoChar('•'); // Volver a mostrar puntos
            this.pintarImagen(toggleLabel, "src/icons/visible.png"); // Icono de ojo abierto
            toggleLabel.setToolTipText("Mostrar contraseña");
            passwordVisible = false;
        } else {
            // Mostrar contraseña
            passwordField.setEchoChar((char) 0); // Quitar puntos, mostrar texto plano
            this.pintarImagen(toggleLabel, "src/icons/invisible.png"); // Icono de ojo cerrado
            toggleLabel.setToolTipText("Ocultar contraseña");
            passwordVisible = true;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlFondo = new javax.swing.JPanel();
        PnlFondoBlanco = new javax.swing.JPanel();
        LblLogo = new javax.swing.JLabel();
        LblFarmaCode = new javax.swing.JLabel();
        PnlContainer = new javax.swing.JPanel();
        LblUser = new javax.swing.JLabel();
        TxtUser = new javax.swing.JTextField();
        LblPass = new javax.swing.JLabel();
        PnlBtn = new javax.swing.JPanel();
        BtnIniciar = new javax.swing.JButton();
        JPassword = new javax.swing.JPasswordField();
        BtnOlvidarC = new javax.swing.JButton();
        LblOjo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PnlFondo.setBackground(new java.awt.Color(170, 170, 170));

        PnlFondoBlanco.setBackground(new java.awt.Color(255, 255, 255));

        LblFarmaCode.setBackground(new java.awt.Color(0, 119, 182));
        LblFarmaCode.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        LblFarmaCode.setForeground(new java.awt.Color(0, 119, 182));
        LblFarmaCode.setText("FarmaCode");

        PnlContainer.setBackground(new java.awt.Color(217, 217, 217));

        LblUser.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        LblUser.setForeground(new java.awt.Color(0, 119, 182));
        LblUser.setText("Usuario:");

        TxtUser.setBackground(new java.awt.Color(217, 217, 217));
        TxtUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        TxtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtUserActionPerformed(evt);
            }
        });

        LblPass.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        LblPass.setForeground(new java.awt.Color(0, 119, 182));
        LblPass.setText("Contraseña:");

        PnlBtn.setBackground(new java.awt.Color(0, 119, 182));

        BtnIniciar.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        BtnIniciar.setForeground(new java.awt.Color(255, 255, 255));
        BtnIniciar.setText("Iniciar Sesión");
        BtnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnIniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBtnLayout = new javax.swing.GroupLayout(PnlBtn);
        PnlBtn.setLayout(PnlBtnLayout);
        PnlBtnLayout.setHorizontalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        PnlBtnLayout.setVerticalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );

        JPassword.setBackground(new java.awt.Color(217, 217, 217));
        JPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JPasswordKeyPressed(evt);
            }
        });

        BtnOlvidarC.setBackground(new java.awt.Color(255, 255, 255));
        BtnOlvidarC.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        BtnOlvidarC.setForeground(new java.awt.Color(0, 119, 182));
        BtnOlvidarC.setText("¿Olvidaste tu contraseña?");
        BtnOlvidarC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnOlvidarCMouseClicked(evt);
            }
        });

        LblOjo.setForeground(new java.awt.Color(0, 0, 0));
        LblOjo.setText("Ojito");

        javax.swing.GroupLayout PnlContainerLayout = new javax.swing.GroupLayout(PnlContainer);
        PnlContainer.setLayout(PnlContainerLayout);
        PnlContainerLayout.setHorizontalGroup(
            PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlContainerLayout.createSequentialGroup()
                .addContainerGap(120, Short.MAX_VALUE)
                .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlContainerLayout.createSequentialGroup()
                        .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(306, 306, 306))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlContainerLayout.createSequentialGroup()
                        .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BtnOlvidarC)
                            .addGroup(PnlContainerLayout.createSequentialGroup()
                                .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblUser)
                                    .addComponent(LblPass))
                                .addGap(84, 84, 84)
                                .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JPassword)
                                    .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblOjo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98))))
        );
        PnlContainerLayout.setVerticalGroup(
            PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlContainerLayout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblUser))
                .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlContainerLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(PnlContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblPass)
                            .addComponent(JPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PnlContainerLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(LblOjo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnOlvidarC)
                .addGap(80, 80, 80)
                .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PnlFondoBlancoLayout = new javax.swing.GroupLayout(PnlFondoBlanco);
        PnlFondoBlanco.setLayout(PnlFondoBlancoLayout);
        PnlFondoBlancoLayout.setHorizontalGroup(
            PnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoBlancoLayout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(LblFarmaCode)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlFondoBlancoLayout.setVerticalGroup(
            PnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFondoBlancoLayout.createSequentialGroup()
                .addGroup(PnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFondoBlancoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(PnlFondoBlancoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(LblFarmaCode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(PnlContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );

        javax.swing.GroupLayout PnlFondoLayout = new javax.swing.GroupLayout(PnlFondo);
        PnlFondo.setLayout(PnlFondoLayout);
        PnlFondoLayout.setHorizontalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFondoLayout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addComponent(PnlFondoBlanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224))
        );
        PnlFondoLayout.setVerticalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(PnlFondoBlanco, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PnlFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnIniciarActionPerformed

        validarUsuario(TxtUser, JPassword);

    }//GEN-LAST:event_BtnIniciarActionPerformed

    private void JPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JPasswordKeyPressed

    }//GEN-LAST:event_JPasswordKeyPressed

    private void TxtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtUserActionPerformed

    }//GEN-LAST:event_TxtUserActionPerformed

    private void BtnOlvidarCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnOlvidarCMouseClicked
        ConsultarCorreo CC = new ConsultarCorreo();

        this.setVisible(false);

        CC.setVisible(true);
        CC.setLocationRelativeTo(null);
        CC.setTitle("Ingresar Correo");
    }//GEN-LAST:event_BtnOlvidarCMouseClicked

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

    public void validarUsuario(JTextField usuario, JPasswordField password) {
        try {
            // Obtener y limpiar credenciales
            String nombreUsuario = usuario.getText().trim();
            String pass = String.valueOf(password.getPassword()).trim();

            // Validar campos no vacíos
            if (nombreUsuario.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Usuario y contraseña son requeridos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar credenciales en BD usando procedimiento almacenado
            try (Connection conn = Farmacia.IniciarBD(); CallableStatement cstmt = conn.prepareCall("{? = call VerificarUsuario(?, ?)}")) {

                cstmt.registerOutParameter(1, Types.TINYINT);
                cstmt.setString(2, nombreUsuario);
                cstmt.setString(3, pass);

                cstmt.execute();

                int resultado = cstmt.getInt(1);

                if (resultado == 1) {
                    // Verificación adicional de contraseña
                    String claveBD = obtenerClaveBD(nombreUsuario); // Método para obtener la clave del usuario
                    if (!claveBD.equals(pass)) {
                        JOptionPane.showMessageDialog(null,
                                "Contraseña incorrecta",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        password.setText("");
                        return;
                    }

                    // Obtener datos del empleado asociado al usuario
                    int tipoUsuario = obtenerTipoUsuario(nombreUsuario); // Método para obtener el tipo de usuario
                    int idEmpleado = obtenerIdEmpleado(nombreUsuario); // Método para obtener el ID del empleado

                    // Obtener nombre completo del empleado
                    String nombreCompleto = obtenerNombreEmpleado(idEmpleado);

                    if (nombreCompleto == null) {
                        JOptionPane.showMessageDialog(null,
                                "No se encontró información del empleado",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Registrar acceso en la base de datos
                    registrarAcceso(idEmpleado);

                    // Determinar el nombre del puesto basado en tipo_usuario
                    String nombrePuesto;
                    switch (tipoUsuario) {
                        case 1:
                            nombrePuesto = "Administrador";
                            break;
                        case 2:
                            nombrePuesto = "Cajero";
                            break;
                        case 3:
                            nombrePuesto = "Almacenista";
                            break;
                        default:
                            nombrePuesto = "Desconocido";
                            break;
                    }

                    // Establecer datos de sesión
                    SesionUsuario.idEmpleado = idEmpleado;
                    SesionUsuario.puesto = nombrePuesto;
                    SesionUsuario.nombreCompleto = nombreCompleto;

                    JOptionPaneBienvenida dialog = new JOptionPaneBienvenida(this, nombreCompleto, nombrePuesto);
                    dialog.setVisible(true);
                    
                    Produccion prod = new Produccion();

                    // Redirigir según tipo de usuario
                    switch (tipoUsuario) {
                        case 1 -> { // Administrador
                            Farmacia.ConectarBDLike("administrador", "admin1234");
                            abrirVentana(prod, "Administrador");
                        }
                        case 2 -> { // Cajero
                            Farmacia.ConectarBDLike("cajero", "caja1234");
                            abrirVentana(prod, "Cajero");
                        }
                        case 3 -> { // Almacenista
                            Farmacia.ConectarBDLike("almacenista", "almacen1234");
                            abrirVentana(prod, "Almacenista");

                        }
                        default -> {
                            JOptionPane.showMessageDialog(null,
                                    "Puesto no reconocido: " + tipoUsuario,
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    this.setVisible(false);
                } else {
                    JOptionPaneError.showError(this, "Usuario o contraseña incorrectos", "Error de autenticación");
                    usuario.setText("");
                    password.setText("");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarAcceso(int idEmpleado) throws SQLException {
        // Consulta SQL parametrizada para insertar el acceso
        String sql = "insert INTO accesos (tipo, hora_entrada, hora_salida, empleados_idempleados) VALUES (1, NOW(), NULL, ?)";

        try (Connection conn = Farmacia.IniciarBD(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecer parámetro del ID del empleado
            pstmt.setInt(1, idEmpleado);

            // Ejecutar inserción
            pstmt.executeUpdate();
        }
    }

    private void abrirVentana(JFrame ventana, String puesto) {
        // Configuración básica de la ventana
        ventana.setVisible(true);   // Hacer visible la ventana
        ventana.setLocationRelativeTo(null);    // Localizar en el centro la ventana

        // Personalizar título de la ventana
        ventana.setTitle("Sistema - " + puesto + " - " + SesionUsuario.nombreCompleto);
    }

    private String obtenerNombreEmpleado(int idEmpleado) throws SQLException {
        // Consulta SQL para obtener nombre
        String sql = "SELECT nombres "
                + "FROM empleados WHERE idempleados = ?";
        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Establecer parámetro del ID de empleado
            ps.setInt(1, idEmpleado);

            // Ejecutar consulta y procesar resultados
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("nombres") : null;
        }
    }

    public class SesionUsuario {

        public static int idEmpleado;
        public static String nombreCompleto;
        public static String puesto;

        public static void cerrarSesion() {
            idEmpleado = 0;
            nombreCompleto = null;
            puesto = null;
        }
    }

    private String obtenerClaveBD(String nombreUsuario) throws SQLException {
        // Consulta SQL para obtener la clave
        try (Connection conn = Farmacia.IniciarBD(); PreparedStatement pstmt = conn.prepareStatement("SELECT clave FROM usuarios WHERE nombre = ?")) {

            // Establecer parámetro del nombre de usuario
            pstmt.setString(1, nombreUsuario);

            // Ejecutar consulta y procesar resultados
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("clave");
                }
            }
        }
        return null;
    }

    private int obtenerTipoUsuario(String nombreUsuario) throws SQLException {
        // Consulta SQL para obtener el tipo de usuario
        try (Connection conn = Farmacia.IniciarBD(); PreparedStatement pstmt = conn.prepareStatement("SELECT tipo_usuario FROM usuarios WHERE nombre = ?")) {

            // Establecer parámetro del nombre de usuario
            pstmt.setString(1, nombreUsuario);

            // Ejecutar consulta y procesar resultados
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tipo_usuario");
                }
            }
        }
        return -1; // Valor por defecto si no se encuentra
    }

    private int obtenerIdEmpleado(String nombreUsuario) throws SQLException {
        // Consulta SQL para obtener la relación usuario-empleado
        try (Connection conn = Farmacia.IniciarBD(); PreparedStatement pstmt = conn.prepareStatement("SELECT empleados_idempleados FROM usuarios WHERE nombre = ?")) {

            // Establecer parámetro y ejecutar consulta
            pstmt.setString(1, nombreUsuario);

            // Ejecutar consulta y procesar resultados
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("empleados_idempleados");
                }
            }
        }
        return -1; // Valor por defecto si no se encuentra
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnIniciar;
    private javax.swing.JButton BtnOlvidarC;
    private javax.swing.JPasswordField JPassword;
    private javax.swing.JLabel LblFarmaCode;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblOjo;
    private javax.swing.JLabel LblPass;
    private javax.swing.JLabel LblUser;
    private javax.swing.JPanel PnlBtn;
    private javax.swing.JPanel PnlContainer;
    private javax.swing.JPanel PnlFondo;
    private javax.swing.JPanel PnlFondoBlanco;
    private javax.swing.JTextField TxtUser;
    // End of variables declaration//GEN-END:variables
}
