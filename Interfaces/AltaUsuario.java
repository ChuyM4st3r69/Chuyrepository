package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;
import metodos.RoundIzqPanel;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.BotonTransparente;
import metodos.ImageHTTPHandler;
import metodos.InfoFarmaDialog;
import metodos.JOptionPaneError;
import metodos.JOptionPaneMensaje;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.estiloComboBox;
import metodos.paintBorder;

/**
 *
 * @author Jesus Castillo
 */
public class AltaUsuario extends javax.swing.JFrame {

    public AltaUsuario() {
        initComponents();
        cargarPuestos();
        cargarColonias();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Apellidos del empleado...", TxtApeUser);
        TextPrompt placeHolder3 = new TextPrompt("Calle del empleado...", TxtCalle);
        TextPrompt placeHolder4 = new TextPrompt("Contraseña empleado...", TxtContraUser);
        TextPrompt placeHolder5 = new TextPrompt("Correo del empleado...", TxtCorreoUser);
        TextPrompt placeHolder6 = new TextPrompt("Numero de Seguro Social del empleado...", TxtNSS);
        TextPrompt placeHolder7 = new TextPrompt("Nombre del empleado...", TxtNomUser);
        TextPrompt placeHolder8 = new TextPrompt("RFC del empleado...", TxtRFC);
        TextPrompt placeHolder9 = new TextPrompt("Telefono del empleado...", TxtTelUer);
        TextPrompt placeHolder10 = new TextPrompt("Foto del empleado...", TxtURL);
        TextPrompt placeHolder11 = new TextPrompt("Usuario del empleado...", TxtUser);

        estiloComboBox.aplicarEstiloComboBox(CbdColonia);
        estiloComboBox.aplicarEstiloComboBox(CbdPuesto);

        // TxtRedondo
        TxtBuscar.setBorder(redondearTxt);
        TxtApeUser.setBorder(redondearTxt);
        TxtCalle.setBorder(redondearTxt);
        TxtContraUser.setBorder(redondearTxt);
        TxtCorreoUser.setBorder(redondearTxt);
        TxtNSS.setBorder(redondearTxt);
        TxtNomUser.setBorder(redondearTxt);
        TxtTelUer.setBorder(redondearTxt);
        TxtRFC.setBorder(redondearTxt);
        TxtTelUer.setBorder(redondearTxt);
        TxtUser.setBorder(redondearTxt);
        TxtURL.setBorder(redondearTxt);

        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

        BotonTransparente.hacerBotonTransparente(BtnAltaUser);

        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlAltaUser, 15, Color.WHITE);
        RoundPanel.aplicarBordesRedondeados(PnlInfo, 15, Color.WHITE);
        RoundPanel.aplicarBordesRedondeados(PnlAltaUser, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        BtnAltaUser.setBorder(new paintBorder(5, Color.getHSBColor(0.f, 0f, 0f)));
        BtnAltaUser.setFocusPainted(false);

        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            BtnUsers.setText("Usuarios");
        } else {
            BtnUsers.setText("Ayuda");
        }

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");
        
        TxtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    java.awt.Window ventana = SwingUtilities.getWindowAncestor(TxtBuscar);
                    if (ventana instanceof JFrame) {
                        metodos.BuscadorGeneral.buscar(TxtBuscar, (JFrame) ventana);
                    }
                }
            }
        });
    }

    private void pintarImagen(JLabel label, String ruta) {
        try {
            ImageIcon icon = new ImageIcon(ruta);
            Icon icono = new ImageIcon(
                    icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT)
            );
            label.setIcon(icono);
            label.repaint();
        } catch (Exception ex) {
            System.out.println("Error al cargar imagen: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlBtn = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnUsers = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnDevoluciones = new javax.swing.JButton();
        BtnInicio = new javax.swing.JButton();
        BtnInventario = new javax.swing.JButton();
        BtnVentas = new javax.swing.JButton();
        BtnRequisiciones = new javax.swing.JButton();
        BtnProveedores = new javax.swing.JButton();
        BtnFacturas = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlInfo = new javax.swing.JPanel();
        LblNomUser = new javax.swing.JLabel();
        TxtApeUser = new javax.swing.JTextField();
        LblApeUser = new javax.swing.JLabel();
        TxtNomUser = new javax.swing.JTextField();
        LblRFCUser = new javax.swing.JLabel();
        TxtRFC = new javax.swing.JTextField();
        LblNSSUser = new javax.swing.JLabel();
        TxtNSS = new javax.swing.JTextField();
        LblTelUser = new javax.swing.JLabel();
        TxtTelUer = new javax.swing.JTextField();
        LblCorreoUser = new javax.swing.JLabel();
        TxtCorreoUser = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        CbdPuesto = new javax.swing.JComboBox<>();
        LblUser = new javax.swing.JLabel();
        TxtUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        TxtContraUser = new javax.swing.JTextField();
        TxtCalle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        CbdColonia = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TxtURL = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        PnlAltaUser = new javax.swing.JPanel();
        BtnAltaUser = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PnlBtn.setBackground(new java.awt.Color(0, 119, 182));

        PnlDecoracion.setBackground(new java.awt.Color(217, 217, 217));

        javax.swing.GroupLayout PnlDecoracionLayout = new javax.swing.GroupLayout(PnlDecoracion);
        PnlDecoracion.setLayout(PnlDecoracionLayout);
        PnlDecoracionLayout.setHorizontalGroup(
            PnlDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 248, Short.MAX_VALUE)
        );
        PnlDecoracionLayout.setVerticalGroup(
            PnlDecoracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        LblFarmacia.setBackground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblFarmacia.setForeground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setText("FarmaCode");

        BtnUsers.setBackground(new java.awt.Color(242, 242, 242));
        BtnUsers.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnUsers.setForeground(new java.awt.Color(0, 119, 182));
        BtnUsers.setText("Usuarios");
        BtnUsers.setBorder(null);
        BtnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(BtnUsers)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BtnConfig.setBackground(new java.awt.Color(0, 119, 182));
        BtnConfig.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnConfig.setForeground(new java.awt.Color(255, 255, 255));
        BtnConfig.setText("Cerrar sesión");
        BtnConfig.setBorder(null);
        BtnConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnConfigMouseClicked(evt);
            }
        });
        BtnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnConfigActionPerformed(evt);
            }
        });

        LblLogo.setBackground(new java.awt.Color(255, 255, 255));
        LblLogo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LblLogo.setForeground(new java.awt.Color(51, 51, 51));

        BtnDevoluciones.setBackground(new java.awt.Color(0, 119, 182));
        BtnDevoluciones.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnDevoluciones.setForeground(new java.awt.Color(255, 255, 255));
        BtnDevoluciones.setText("Devoluciones");
        BtnDevoluciones.setBorder(null);
        BtnDevoluciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDevolucionesActionPerformed(evt);
            }
        });

        BtnInicio.setBackground(new java.awt.Color(0, 119, 182));
        BtnInicio.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnInicio.setForeground(new java.awt.Color(255, 255, 255));
        BtnInicio.setText("Inicio");
        BtnInicio.setBorder(null);
        BtnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInicioActionPerformed(evt);
            }
        });

        BtnInventario.setBackground(new java.awt.Color(0, 119, 182));
        BtnInventario.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnInventario.setForeground(new java.awt.Color(255, 255, 255));
        BtnInventario.setText("Inventario");
        BtnInventario.setBorder(null);
        BtnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInventarioActionPerformed(evt);
            }
        });

        BtnVentas.setBackground(new java.awt.Color(0, 119, 182));
        BtnVentas.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnVentas.setForeground(new java.awt.Color(255, 255, 255));
        BtnVentas.setText("Ventas");
        BtnVentas.setBorder(null);
        BtnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnVentasActionPerformed(evt);
            }
        });

        BtnRequisiciones.setBackground(new java.awt.Color(0, 119, 182));
        BtnRequisiciones.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnRequisiciones.setForeground(new java.awt.Color(255, 255, 255));
        BtnRequisiciones.setText("Requisiciones");
        BtnRequisiciones.setBorder(null);
        BtnRequisiciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRequisicionesActionPerformed(evt);
            }
        });

        BtnProveedores.setBackground(new java.awt.Color(0, 119, 182));
        BtnProveedores.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnProveedores.setForeground(new java.awt.Color(255, 255, 255));
        BtnProveedores.setText("Proveedores");
        BtnProveedores.setBorder(null);
        BtnProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProveedoresActionPerformed(evt);
            }
        });

        BtnFacturas.setBackground(new java.awt.Color(0, 119, 182));
        BtnFacturas.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnFacturas.setForeground(new java.awt.Color(255, 255, 255));
        BtnFacturas.setText("Facturas");
        BtnFacturas.setBorder(null);
        BtnFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnFacturasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBtnLayout = new javax.swing.GroupLayout(PnlBtn);
        PnlBtn.setLayout(PnlBtnLayout);
        PnlBtnLayout.setHorizontalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnDevoluciones)
                            .addComponent(LblFarmacia)
                            .addComponent(BtnInventario)
                            .addComponent(BtnVentas)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnFacturas)
                            .addComponent(BtnInicio)))
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlBtnLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(BtnConfig)
                    .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlBtnLayout.setVerticalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(BtnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        PnlCabecera.setBackground(new java.awt.Color(255, 255, 255));

        TxtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarActionPerformed(evt);
            }
        });

        LblPuesto.setBackground(new java.awt.Color(0, 119, 182));
        LblPuesto.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblPuesto.setForeground(new java.awt.Color(0, 119, 182));
        LblPuesto.setText("Administrador");

        javax.swing.GroupLayout PnlCabeceraLayout = new javax.swing.GroupLayout(PnlCabecera);
        PnlCabecera.setLayout(PnlCabeceraLayout);
        PnlCabeceraLayout.setHorizontalGroup(
            PnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCabeceraLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(TxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 332, Short.MAX_VALUE)
                .addComponent(LblPuesto)
                .addGap(82, 82, 82))
        );
        PnlCabeceraLayout.setVerticalGroup(
            PnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCabeceraLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(PnlCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblPuesto))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Crea nuevos usuarios y asigna sus permisos.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Nuevo Usuario");

        PnlInfo.setBackground(new java.awt.Color(255, 255, 255));

        LblNomUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblNomUser.setForeground(new java.awt.Color(0, 0, 0));
        LblNomUser.setText("Nombres:");

        LblApeUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblApeUser.setForeground(new java.awt.Color(0, 0, 0));
        LblApeUser.setText("Apellidos:");

        LblRFCUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFCUser.setForeground(new java.awt.Color(0, 0, 0));
        LblRFCUser.setText("RFC:");

        LblNSSUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblNSSUser.setForeground(new java.awt.Color(0, 0, 0));
        LblNSSUser.setText("NSS:");

        LblTelUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTelUser.setForeground(new java.awt.Color(0, 0, 0));
        LblTelUser.setText("Teléfono:");

        LblCorreoUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreoUser.setForeground(new java.awt.Color(0, 0, 0));
        LblCorreoUser.setText("Correo Electrónico:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Puesto:");

        CbdPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        LblUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblUser.setForeground(new java.awt.Color(0, 0, 0));
        LblUser.setText("Usuario:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Contraseña:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Calle y num:");

        CbdColonia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Colonia:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("URL Foto:");

        javax.swing.GroupLayout PnlInfoLayout = new javax.swing.GroupLayout(PnlInfo);
        PnlInfo.setLayout(PnlInfoLayout);
        PnlInfoLayout.setHorizontalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInfoLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblRFCUser)
                    .addComponent(LblNSSUser)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addComponent(LblNomUser)
                        .addGap(18, 18, 18)
                        .addComponent(TxtNomUser, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LblTelUser))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addComponent(LblApeUser)
                        .addGap(18, 18, 18)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtApeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtNSS, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(LblCorreoUser))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addComponent(TxtCorreoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(LblUser))
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addComponent(TxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1))
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addComponent(TxtTelUer, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)))
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(CbdPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TxtContraUser, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CbdColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addComponent(TxtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        PnlInfoLayout.setVerticalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInfoLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtNomUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblNomUser)
                            .addComponent(LblTelUser)))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtTelUer, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(CbdPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblApeUser)
                            .addComponent(TxtApeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblUser)
                            .addComponent(LblCorreoUser)
                            .addComponent(TxtCorreoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblRFCUser)
                            .addComponent(jLabel4)))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(TxtContraUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TxtNSS, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LblNSSUser)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3))
                    .addComponent(CbdColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        PnlAltaUser.setBackground(new java.awt.Color(204, 204, 204));

        BtnAltaUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/AltaUser.png"))); // NOI18N
        BtnAltaUser.setText("Alta Usuario");
        BtnAltaUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAltaUserMouseClicked(evt);
            }
        });
        BtnAltaUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAltaUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlAltaUserLayout = new javax.swing.GroupLayout(PnlAltaUser);
        PnlAltaUser.setLayout(PnlAltaUserLayout);
        PnlAltaUserLayout.setHorizontalGroup(
            PnlAltaUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
            .addGroup(PnlAltaUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(BtnAltaUser, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
        );
        PnlAltaUserLayout.setVerticalGroup(
            PnlAltaUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
            .addGroup(PnlAltaUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(BtnAltaUser, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblTitulo)
                                    .addComponent(LblTitulo1))
                                .addGap(535, 535, 535)
                                .addComponent(PnlAltaUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LblTitulo1)
                        .addGap(18, 18, 18)
                        .addComponent(LblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(PnlAltaUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(59, 59, 59)
                .addComponent(PnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PnlBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsersActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Usuarios user = new Usuarios();

            this.setState(JFrame.ICONIFIED);

            user.setVisible(true);
            user.setLocationRelativeTo(null);
            user.setTitle("Usuarios");
        } else {
            InfoFarmaDialog.showDialog(this);
        }
    }//GEN-LAST:event_BtnUsersActionPerformed

    private void BtnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnConfigActionPerformed

    }//GEN-LAST:event_BtnConfigActionPerformed

    private void BtnDevolucionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDevolucionesActionPerformed
        if (null != Inicio_sesión.SesionUsuario.puesto)
            switch (Inicio_sesión.SesionUsuario.puesto) {
                case "Administrador" -> {
                    Devoluciones dev = new Devoluciones();
                    this.setState(JFrame.ICONIFIED);
                    dev.setVisible(true);
                    dev.setLocationRelativeTo(null);
                    dev.setTitle("Devoluciones");
                }
                case "Almacenista" -> {
                    DevolucionProveedor devPro = new DevolucionProveedor();
                    this.setState(JFrame.ICONIFIED);
                    devPro.setVisible(true);
                    devPro.setLocationRelativeTo(null);
                    devPro.setTitle("Devoluciones");
                }
                case "Cajero" -> {
                    DevolucionClientes devCli = new DevolucionClientes();
                    this.setState(JFrame.ICONIFIED);
                    devCli.setVisible(true);
                    devCli.setLocationRelativeTo(null);
                    devCli.setTitle("Devoluciones");
                }
                default -> {
                    AccesoDenegado.showAccesoDenegado(this);
                }
            }
    }//GEN-LAST:event_BtnDevolucionesActionPerformed

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInventarioActionPerformed
        Inventario inv = new Inventario();

        this.setVisible(false);

        inv.setVisible(true);
        inv.setLocationRelativeTo(null);
        inv.setTitle("Inventario");
    }//GEN-LAST:event_BtnInventarioActionPerformed

    private void BtnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnVentasActionPerformed
        if (null == Inicio_sesión.SesionUsuario.puesto) {
            NuevasVentas newVenta = new NuevasVentas();

            this.setState(JFrame.ICONIFIED);

            newVenta.setVisible(true);
            newVenta.setLocationRelativeTo(null);
            newVenta.setTitle("Nueva Venta");
        } else
            switch (Inicio_sesión.SesionUsuario.puesto) {
                case "Administrador" -> {
                    Ventas ventas = new Ventas();
                    this.setState(JFrame.ICONIFIED);
                    ventas.setVisible(true);
                    ventas.setLocationRelativeTo(null);
                    ventas.setTitle("Ventas");
                }
                case "Almacenista" ->
                    AccesoDenegado.showAccesoDenegado(this);
                default -> {
                    NuevasVentas newVenta = new NuevasVentas();
                    this.setState(JFrame.ICONIFIED);
                    newVenta.setVisible(true);
                    newVenta.setLocationRelativeTo(null);
                    newVenta.setTitle("Nueva Venta");
                }
            }
    }//GEN-LAST:event_BtnVentasActionPerformed

    private void BtnRequisicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRequisicionesActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Requisiciones req = new Requisiciones();

            this.setState(JFrame.ICONIFIED);

            req.setVisible(true);
            req.setLocationRelativeTo(null);
            req.setTitle("Requisiciones");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }
    }//GEN-LAST:event_BtnRequisicionesActionPerformed

    private void BtnProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnProveedoresActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Proveedores prov = new Proveedores();

            this.setState(JFrame.ICONIFIED);

            prov.setVisible(true);
            prov.setLocationRelativeTo(null);
            prov.setTitle("Proveedores");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }
    }//GEN-LAST:event_BtnProveedoresActionPerformed

    private void BtnFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnFacturasActionPerformed
        if (null == Inicio_sesión.SesionUsuario.puesto) {
            NuevaFactura newFac = new NuevaFactura();

            this.setState(JFrame.ICONIFIED);

            newFac.setVisible(true);
            newFac.setLocationRelativeTo(null);
            newFac.setTitle("Nueva Factura");
        } else
            switch (Inicio_sesión.SesionUsuario.puesto) {
                case "Administrador" -> {
                    Facturas fac = new Facturas();
                    this.setState(JFrame.ICONIFIED);
                    fac.setVisible(true);
                    fac.setLocationRelativeTo(null);
                    fac.setTitle("Facturas");
                }
                case "Almacenista" ->
                    AccesoDenegado.showAccesoDenegado(this);
                default -> {
                    NuevaFactura newFac = new NuevaFactura();
                    this.setState(JFrame.ICONIFIED);
                    newFac.setVisible(true);
                    newFac.setLocationRelativeTo(null);
                    newFac.setTitle("Nueva Factura");
                }
            }
    }//GEN-LAST:event_BtnFacturasActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnAltaUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAltaUserMouseClicked
        registrarEmpleadoYUsuario();
    }//GEN-LAST:event_BtnAltaUserMouseClicked

    private void BtnConfigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnConfigMouseClicked
        try {
            Farmacia.registrarSalida(idEmpleado);

            // Cerrar todas las ventanas de la aplicación
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window != null && window.isDisplayable()) {
                    window.dispose();
                }
            }

            Inicio_sesión inicio = new Inicio_sesión();
            inicio.setVisible(true);
            inicio.setLocationRelativeTo(null);
            inicio.setTitle("Inicio de sesión");
        } catch (SQLException ex) {
            System.out.println("Error");
        }
    }//GEN-LAST:event_BtnConfigMouseClicked

    private void BtnAltaUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAltaUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAltaUserActionPerformed

    private void registrarEmpleadoYUsuario() {
        Connection conn = null;
        CallableStatement stmtEmpleado = null;
        CallableStatement stmtUsuario = null;
        CallableStatement stmtContacto = null;

        try {
            // Establecer conexión
            conn = Farmacia.ConectarBD();

            // Obtener valores de los campos
            String nombres = TxtNomUser.getText().trim();
            String apellidos = TxtApeUser.getText().trim();
            String rfc = TxtRFC.getText().trim().toUpperCase();
            String nss = TxtNSS.getText().trim();
            String calle = TxtCalle.getText().trim();
            String correo = TxtCorreoUser.getText().trim();
            String telefono = TxtTelUer.getText().trim();
            String clave = TxtContraUser.getText();
            String nombreUsuario = TxtUser.getText().trim();

            // Obtener IDs de selecciones
            int indexPuesto = CbdPuesto.getSelectedIndex();
            int idPuesto = (int) mapaPuestos.keySet().toArray()[indexPuesto];

            int indexColonia = CbdColonia.getSelectedIndex();
            int idColonia = (int) mapaColonias.keySet().toArray()[indexColonia];

            int tipoUsuario = idPuesto; // o lo que corresponda

            // Validar campos obligatorios
            if (nombres.isEmpty() || apellidos.isEmpty() || rfc.isEmpty() || nss.isEmpty()
                    || calle.isEmpty() || clave.isEmpty()
                    || nombreUsuario.isEmpty()) {
                JOptionPaneError.showError(this, "Faltan campos obligatorios.");
                return;
            }

            // Validaciones individuales
            boolean valido = true;
            StringBuilder errores = new StringBuilder();

            // 1. Nombres (solo letras, espacios y acentos)
            if (!nombres.matches("^[\\p{L} .'-]{2,50}$")) {
                errores.append("- Nombre inválido. Solo letras (2-50 caracteres)\n");
                valido = false;
            }

            // 2. Apellidos
            if (!apellidos.matches("^[\\p{L} .'-]{2,50}$")) {
                errores.append("- Apellido inválido. Solo letras (2-50 caracteres)\n");
                valido = false;
            }

            // 3. RFC
            if (!rfc.matches("^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$")) {
                errores.append("- RFC inválido. Formato: XXXX000000XXX\n");
                valido = false;
            }

            // 4. NSS (11 dígitos)
            String nssLimpio = nss.replaceAll("[^0-9]", "");
            if (nssLimpio.length() != 11 || !nssLimpio.matches("^\\d{11}$")) {
                errores.append("- NSS debe tener exactamente 11 dígitos\n");
                valido = false;
            }

            // 5. Calle
            if (calle.length() < 5) {
                errores.append("- Calle muy corta (mínimo 5 caracteres)\n");
                valido = false;
            }

            // 6. Correo electrónico
            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                errores.append("- Correo electrónico inválido\n");
                valido = false;
            }

            // 7. Teléfono (10 dígitos)
            String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
            if (!telefonoLimpio.matches("^\\d{10}$")) {
                errores.append("- Teléfono debe tener 10 dígitos\n");
                valido = false;
            }

            // 8. Contraseña (8+ caracteres, 1 mayúscula, 1 número, 1 especial)
            if (!clave.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                errores.append("- La contraseña debe tener:\n  • Mínimo 8 caracteres\n  • 1 mayúscula\n  • 1 número\n  • 1 caracter especial (@$!%*?&)\n");
                valido = false;
            }

            // 9. Nombre de usuario (4-20 caracteres alfanuméricos)
            if (!nombreUsuario.matches("^[a-zA-Z0-9]{4,20}$")) {
                errores.append("- Nombre de usuario: 4-20 caracteres alfanuméricos\n");
                valido = false;
            }

            if (!valido) {
                JOptionPaneError.showError(this, "Errores encontrados:\n" + errores.toString());
                return;
            }

            // Validar correo con expresión regular
            if (!correo.matches("^[a-zA-Z0-9._%+-]+@farmacode\\.com$")) {
                JOptionPane.showMessageDialog(this,
                        "Ingresa un correo válido. Ejemplo: nombre@farmacode.com",
                        "Correo inválido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Registrar empleado (procedimiento almacenado)
            stmtEmpleado = conn.prepareCall("{CALL AltaEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}");
            stmtEmpleado.setString(1, nombres);
            stmtEmpleado.setString(2, apellidos);
            stmtEmpleado.setString(3, rfc);
            stmtEmpleado.setString(4, nss);
            stmtEmpleado.setString(5, calle);
            stmtEmpleado.setInt(6, idPuesto);
            stmtEmpleado.setInt(7, idColonia);
            stmtEmpleado.registerOutParameter(8, java.sql.Types.INTEGER);

            stmtEmpleado.execute();
            int idEmpleado = stmtEmpleado.getInt(8);

            ImageHTTPHandler handler = new ImageHTTPHandler("http://localhost:8081");

            String imagePath = "src\\icons\\" + TxtURL.getText();
            try {
                if (TxtURL.getText().equals("")) {
                    imagePath = "src\\icons\\fotoPerfil.jpg";

                    handler.upload(imagePath);

                } else {
                    handler.upload(imagePath);
                }
            } catch (IOException ex) {
                System.getLogger(AltaUsuario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

            // AltaUsuario
            stmtUsuario = conn.prepareCall("{CALL AltaUsuario(?, ?, ?, ?)}");
            stmtUsuario.setInt(1, tipoUsuario);
            stmtUsuario.setString(2, clave);
            stmtUsuario.setInt(3, idEmpleado);
            stmtUsuario.setString(4, nombreUsuario);
            stmtUsuario.execute();

            // AltaContactoEmpleado
            stmtContacto = conn.prepareCall("{CALL AltaContactoEmpleado(?, ?, ?)}");
            stmtContacto.setString(1, telefono);
            stmtContacto.setString(2, correo);
            stmtContacto.setInt(3, idEmpleado);
            stmtContacto.execute();
            
            JOptionPaneMensaje dialog = new JOptionPaneMensaje(this, "Listo!", "Empleado, usuario y contacto registrados con éxito.");
            dialog.setVisible(true);

            limpiarCampos();

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (stmtContacto != null) {
                    stmtContacto.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (stmtUsuario != null) {
                    stmtUsuario.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (stmtEmpleado != null) {
                    stmtEmpleado.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    private Map<Integer, String> mapaPuestos = new LinkedHashMap<>();       // Mapa para almacenar puestos (ID -> Nombre)
    private Map<Integer, String> mapaColonias = new LinkedHashMap<>();      // Mapa para almacenar colonias (ID -> Nombre)

    private void cargarPuestos() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establecer conexión con la base de datos
            conn = Farmacia.ConectarBD();
            stmt = conn.createStatement();

            // Preparar consulta SQL
            rs = stmt.executeQuery("SELECT idtipos_puestos, nombre FROM tipos_puestos");

            // Limpiar datos existentes
            CbdPuesto.removeAllItems();
            mapaPuestos.clear();

            // Procesar resultados
            while (rs.next()) {
                int id = rs.getInt("idtipos_puestos");
                String nombre = rs.getString("nombre");

                // Almacenar en mapa y modelo del combo
                mapaPuestos.put(id, nombre);
                CbdPuesto.addItem(nombre);
            }

        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }
    
    /**
     * Limpia todos los campos
     */
    public void limpiarCampos() {

        // Campos de texto
        TxtNomUser.setText("");
        TxtApeUser.setText("");
        TxtRFC.setText("");
        TxtNSS.setText("");
        TxtCalle.setText("");
        TxtCorreoUser.setText("");
        TxtTelUer.setText("");
        TxtContraUser.setText("");
        TxtUser.setText("");
        TxtURL.setText("");
        TxtBuscar.setText("");

        // Enfocar primer campo
        TxtNomUser.requestFocusInWindow();
    }

    private void cargarColonias() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Farmacia.ConectarBD();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT idcolonias_empleados, nombre FROM colonias_empleados");

            // Limpiar estructuras existentes
            CbdColonia.removeAllItems();
            mapaColonias.clear();

            // Procesar cada registro
            while (rs.next()) {
                int id = rs.getInt("idcolonias_empleados");
                String nombre = rs.getString("nombre");

                // Almacenar en mapa
                mapaColonias.put(id, nombre);
                CbdColonia.addItem(nombre);
            }

        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAltaUser;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CbdColonia;
    private javax.swing.JComboBox<String> CbdPuesto;
    private javax.swing.JLabel LblApeUser;
    private javax.swing.JLabel LblCorreoUser;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblNSSUser;
    private javax.swing.JLabel LblNomUser;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblRFCUser;
    private javax.swing.JLabel LblTelUser;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblUser;
    private javax.swing.JPanel PnlAltaUser;
    private javax.swing.JPanel PnlBtn;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlInfo;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtApeUser;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtCalle;
    private javax.swing.JTextField TxtContraUser;
    private javax.swing.JTextField TxtCorreoUser;
    private javax.swing.JTextField TxtNSS;
    private javax.swing.JTextField TxtNomUser;
    private javax.swing.JTextField TxtRFC;
    private javax.swing.JTextField TxtTelUer;
    private javax.swing.JTextField TxtURL;
    private javax.swing.JTextField TxtUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
