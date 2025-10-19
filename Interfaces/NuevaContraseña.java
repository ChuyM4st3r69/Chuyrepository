package Interfaces;

import farmacia.Farmacia;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import metodos.BotonTransparente;
import metodos.JOptionPaneError;
import metodos.JOptionPaneMensaje;
import metodos.RoundPanel;
import metodos.RoundPanelAbajo;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.paintBorder;

/**
 *
 * @author Jesus Castillo
 */
public class NuevaContraseña extends javax.swing.JFrame {

    private String correoUsuario;
    private ImageIcon imagen;
    private Icon icono;

    public NuevaContraseña(String correo) {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        TextPrompt placeHolder = new TextPrompt("Nueva Contraseña", TxtContraUser);
        TextPrompt placeHolder2 = new TextPrompt("Repite la Contraseña", PassFieldContra);

        RoundTxt redondearTxt = new RoundTxt(30, Color.WHITE);
        TxtContraUser.setBorder(redondearTxt);

        PassFieldContra.setBorder(redondearTxt);

        this.correoUsuario = correo;

        this.pintarImagen(LblLogo3, "src/icons/Logo.png");

        // Panel Redondo
        RoundPanel.aplicarBordesRedondeados(PnlGuardar, 40, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlFondoBlanco3, 40, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlContainer3, 40, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        
        // Panel Redondo Abajo
        RoundPanelAbajo.aplicarBordesInferioresRedondeados(PnlContainer3, 40, Color.getHSBColor(0f, 0f, 0.85f));
        
        BtnGuardar.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnGuardar.setFocusPainted(false);
        
        // Boton Transparente
        BotonTransparente.hacerBotonTransparente(BtnGuardar);
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
        TxtContraUser = new javax.swing.JTextField();
        PnlGuardar = new javax.swing.JPanel();
        BtnGuardar = new javax.swing.JButton();
        LblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PassFieldContra = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(170, 170, 170));

        PnlFondoBlanco3.setBackground(new java.awt.Color(255, 255, 255));

        LblFarmaCode3.setBackground(new java.awt.Color(0, 119, 182));
        LblFarmaCode3.setFont(new java.awt.Font("Segoe UI", 1, 60)); // NOI18N
        LblFarmaCode3.setForeground(new java.awt.Color(0, 119, 182));
        LblFarmaCode3.setText("FarmaCode");

        PnlContainer3.setBackground(new java.awt.Color(217, 217, 217));

        LblUser3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblUser3.setForeground(new java.awt.Color(0, 119, 182));
        LblUser3.setText("Nueva Contraseña");

        TxtContraUser.setBackground(new java.awt.Color(217, 217, 217));
        TxtContraUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        TxtContraUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtContraUserTxtCorreoActionPerformed(evt);
            }
        });

        PnlGuardar.setBackground(new java.awt.Color(0, 119, 182));

        BtnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        BtnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        BtnGuardar.setText("Guardar");
        BtnGuardar.setToolTipText("");
        BtnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnGuardarMouseClicked(evt);
            }
        });
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarBtnIniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlGuardarLayout = new javax.swing.GroupLayout(PnlGuardar);
        PnlGuardar.setLayout(PnlGuardarLayout);
        PnlGuardarLayout.setHorizontalGroup(
            PnlGuardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        PnlGuardarLayout.setVerticalGroup(
            PnlGuardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(0, 119, 182));
        LblTitulo.setText("Nueva Contraseña");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 119, 182));
        jLabel1.setText("Ingrese su nueva contraseña para completar el proceso");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 119, 182));
        jLabel2.setText("Confirmar Contraseña");

        PassFieldContra.setBackground(new java.awt.Color(217, 217, 217));

        javax.swing.GroupLayout PnlContainer3Layout = new javax.swing.GroupLayout(PnlContainer3);
        PnlContainer3.setLayout(PnlContainer3Layout);
        PnlContainer3Layout.setHorizontalGroup(
            PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlContainer3Layout.createSequentialGroup()
                .addContainerGap(275, Short.MAX_VALUE)
                .addComponent(PnlGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220))
            .addGroup(PnlContainer3Layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addGroup(PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlContainer3Layout.createSequentialGroup()
                        .addGroup(PnlContainer3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(LblTitulo)
                            .addComponent(TxtContraUser, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                            .addComponent(LblUser3)
                            .addComponent(PassFieldContra))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PnlContainer3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addComponent(TxtContraUser, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(PassFieldContra, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(PnlGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
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
                .addContainerGap(88, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnGuardarMouseClicked
        validarContrasena();

    }//GEN-LAST:event_BtnGuardarMouseClicked

    private void BtnGuardarBtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarBtnIniciarActionPerformed

    }//GEN-LAST:event_BtnGuardarBtnIniciarActionPerformed

    private void TxtContraUserTxtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtContraUserTxtCorreoActionPerformed

    }//GEN-LAST:event_TxtContraUserTxtCorreoActionPerformed

    private void validarContrasena() {
        String usuario = TxtContraUser.getText();
        String contrasena = new String(PassFieldContra.getPassword());

        // 1. Verificar si los textos son iguales
        if (!usuario.equals(contrasena)) {
            JOptionPane.showMessageDialog(null, "Error: Las contraseñas no son iguales");
            return;
        }

        // 2. Validar requisitos de la contraseña
        StringBuilder errores = new StringBuilder();

        // Longitud mínima de 8 caracteres
        if (contrasena.length() < 8) {
            errores.append("- Mínimo 8 caracteres\n");
        }

        // Al menos una mayúscula
        if (!Pattern.compile("[A-Z]").matcher(contrasena).find()) {
            errores.append("- Al menos una letra mayúscula\n");
        }

        // Al menos un número
        if (!Pattern.compile("[0-9]").matcher(contrasena).find()) {
            errores.append("- Al menos un número\n");
        }

        // Al menos un caracter especial
        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(contrasena).find()) {
            errores.append("- Al menos un caracter especial\n");
        }

        // Mostrar resultados
        if (errores.length() > 0) {
            JOptionPaneError.showError(this, "La contraseña no cumple con los siguientes requisitos:\n" + errores.toString());
        } else {
            actualizarContrasena(usuario, contrasena);
            JOptionPane.showMessageDialog(this,
                    "Contraseña actualizada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            JOptionPaneMensaje msj = new JOptionPaneMensaje(this, "Contraseña actualizada exitosamente", "Éxito");
            msj.setVisible(true);
            this.dispose();

            Inicio_sesión inicio = new Inicio_sesión();
            inicio.setVisible(true);
            inicio.setLocationRelativeTo(null);
            inicio.setTitle("Inicio sesión");
            
        }
    }

    private boolean actualizarContrasena(String correo, String nuevaContrasena) {
        String Contrasena = nuevaContrasena;

        try (Connection conn = Farmacia.ConectarBD(); CallableStatement stmt = conn.prepareCall("{call ActualizarContrasena(?, ?)}")) {

            stmt.setString(1, correoUsuario);
            stmt.setString(2, Contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("filas_afectadas") > 0;
            }
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JLabel LblFarmaCode3;
    private javax.swing.JLabel LblLogo3;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblUser3;
    private javax.swing.JPasswordField PassFieldContra;
    private javax.swing.JPanel PnlContainer3;
    private javax.swing.JPanel PnlFondoBlanco3;
    private javax.swing.JPanel PnlGuardar;
    private javax.swing.JTextField TxtContraUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
