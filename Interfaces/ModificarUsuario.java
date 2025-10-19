package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
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
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.estiloComboBox;
import metodos.paintBorder;

/**
 *
 * @author Jesus Castillo
 */
public class ModificarUsuario extends javax.swing.JFrame {

    public ModificarUsuario() {

        initComponents();
        cargarPuestos();
        cargarColonias();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Apellidos del empleado...", TxtApeUser);
        TextPrompt placeHolder3 = new TextPrompt("Nombre del empleado...", TxtBuscarUser);
        TextPrompt placeHolder4 = new TextPrompt("Contraseña empleado...", TxtContraUser);
        TextPrompt placeHolder5 = new TextPrompt("Correo del empleado...", TxtCorreoUser);
        TextPrompt placeHolder6 = new TextPrompt("Numero de Seguro Social del empleado...", TxtNSS);
        TextPrompt placeHolder7 = new TextPrompt("Nombre del empleado...", TxtNomUser);
        TextPrompt placeHolder8 = new TextPrompt("RFC del empleado...", TxtRFC);
        TextPrompt placeHolder9 = new TextPrompt("Telefono del empleado...", TxtTelUer);
        TextPrompt placeHolder10 = new TextPrompt("Foto del empleado...", TxtURL);
        TextPrompt placeHolder11 = new TextPrompt("Usuario del empleado...", TxtUser);
        TextPrompt placeHolder12 = new TextPrompt("Calle y numero del empleado...", TxtCalleUser);

        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));

        TxtBuscar.setBorder(redondearTxt);
        TxtApeUser.setBorder(redondearTxt);
        TxtBuscarUser.setBorder(redondearTxt);
        TxtCalleUser.setBorder(redondearTxt);
        TxtContraUser.setBorder(redondearTxt);
        TxtCorreoUser.setBorder(redondearTxt);
        TxtNSS.setBorder(redondearTxt);
        TxtNomUser.setBorder(redondearTxt);
        TxtRFC.setBorder(redondearTxt);
        TxtTelUer.setBorder(redondearTxt);
        TxtUser.setBorder(redondearTxt);
        TxtURL.setBorder(redondearTxt);

        BotonTransparente.hacerBotonTransparente(BtnBuscarUser);
        BotonTransparente.hacerBotonTransparente(BtnModificarUser);

        BtnAggTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAggTel.setFocusPainted(false);

        BtnModTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnModTel.setFocusPainted(false);

        BtnEliTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnEliTel.setFocusPainted(false);

        BtnAggCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAggCorreo.setFocusPainted(false);

        BtnModCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnModCorreo.setFocusPainted(false);

        BtnEliCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));

        BtnEliCorreo.setFocusPainted(false);

        estiloComboBox.aplicarEstiloComboBox(CbdColonia);
        estiloComboBox.aplicarEstiloComboBox(CbdPuesto);

        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            BtnUsers.setText("Usuarios");
        } else {
            BtnUsers.setText("Ayuda");
        }

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");

        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

        RoundPanel.aplicarBordesRedondeados(PnlModificarBtn, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBuscar, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlInfo, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        
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

        BtgActividad = new javax.swing.ButtonGroup();
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
        LblNomUserB = new javax.swing.JLabel();
        LblRFCBB = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        RbnActivo = new javax.swing.JRadioButton();
        RbnInactivo = new javax.swing.JRadioButton();
        LblRFCB = new javax.swing.JLabel();
        LblNSSUserB = new javax.swing.JLabel();
        LblPuestoUserB = new javax.swing.JLabel();
        LblCorreoUser = new javax.swing.JLabel();
        LblRFCUser = new javax.swing.JLabel();
        TxtNomUser = new javax.swing.JTextField();
        TxtNSS = new javax.swing.JTextField();
        LblTelUser = new javax.swing.JLabel();
        LblNSSUser = new javax.swing.JLabel();
        TxtRFC = new javax.swing.JTextField();
        LblApeUser = new javax.swing.JLabel();
        LblNomUser = new javax.swing.JLabel();
        TxtTelUer = new javax.swing.JTextField();
        TxtApeUser = new javax.swing.JTextField();
        TxtCorreoUser = new javax.swing.JTextField();
        LblPuestoUser = new javax.swing.JLabel();
        CbdPuesto = new javax.swing.JComboBox<>();
        TxtUser = new javax.swing.JTextField();
        LblUser = new javax.swing.JLabel();
        TxtContraUser = new javax.swing.JTextField();
        LblPass = new javax.swing.JLabel();
        TxtBuscarUser = new javax.swing.JTextField();
        PnlModificarBtn = new javax.swing.JPanel();
        BtnModificarUser = new javax.swing.JButton();
        PnlBuscar = new javax.swing.JPanel();
        BtnBuscarUser = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        LstTelefonos = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        LstCorreos = new javax.swing.JList<>();
        LblTel = new javax.swing.JLabel();
        LblCorreo = new javax.swing.JLabel();
        BtnAggTel = new javax.swing.JButton();
        BtnEliTel = new javax.swing.JButton();
        BtnModTel = new javax.swing.JButton();
        BtnAggCorreo = new javax.swing.JButton();
        BtnEliCorreo = new javax.swing.JButton();
        BtnModCorreo = new javax.swing.JButton();
        CbdColonia = new javax.swing.JComboBox<>();
        TxtCalleUser = new javax.swing.JTextField();
        LblColoniaUser = new javax.swing.JLabel();
        LblCalleUser = new javax.swing.JLabel();
        TxtURL = new javax.swing.JTextField();
        LblURLUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        LblTitulo.setText("Editar usuarios ya existentes y asignarle o removerle sus permisos.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Modificar Usuario");

        PnlInfo.setBackground(new java.awt.Color(255, 255, 255));

        LblNomUserB.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblNomUserB.setForeground(new java.awt.Color(0, 0, 0));

        LblRFCBB.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFCBB.setForeground(new java.awt.Color(0, 0, 0));
        LblRFCBB.setText("RFC:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("NSS:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Puesto:");

        RbnActivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnActivo);
        RbnActivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnActivo.setForeground(new java.awt.Color(0, 0, 0));
        RbnActivo.setText("Activo");

        RbnInactivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnInactivo);
        RbnInactivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnInactivo.setForeground(new java.awt.Color(0, 0, 0));
        RbnInactivo.setText("Inactivo");
        RbnInactivo.setToolTipText("");

        LblRFCB.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        LblNSSUserB.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        LblPuestoUserB.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        javax.swing.GroupLayout PnlInfoLayout = new javax.swing.GroupLayout(PnlInfo);
        PnlInfo.setLayout(PnlInfoLayout);
        PnlInfoLayout.setHorizontalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInfoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblRFCBB)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(38, 38, 38)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblPuestoUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblNSSUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblRFCB, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(LblNomUserB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RbnActivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addComponent(RbnInactivo)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        PnlInfoLayout.setVerticalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInfoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(LblNomUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblRFCBB)
                    .addComponent(LblRFCB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(LblNSSUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(LblPuestoUserB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbnActivo)
                    .addComponent(RbnInactivo))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        LblCorreoUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreoUser.setForeground(new java.awt.Color(0, 0, 0));
        LblCorreoUser.setText("Correo Electrónico:");

        LblRFCUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFCUser.setForeground(new java.awt.Color(0, 0, 0));
        LblRFCUser.setText("RFC:");

        TxtNomUser.setBackground(new java.awt.Color(242, 242, 242));

        TxtNSS.setBackground(new java.awt.Color(242, 242, 242));

        LblTelUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTelUser.setForeground(new java.awt.Color(0, 0, 0));
        LblTelUser.setText("Teléfono:");

        LblNSSUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblNSSUser.setForeground(new java.awt.Color(0, 0, 0));
        LblNSSUser.setText("NSS:");

        TxtRFC.setBackground(new java.awt.Color(242, 242, 242));

        LblApeUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblApeUser.setForeground(new java.awt.Color(0, 0, 0));
        LblApeUser.setText("Apellidos:");

        LblNomUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblNomUser.setForeground(new java.awt.Color(0, 0, 0));
        LblNomUser.setText("Nombres:");

        TxtTelUer.setBackground(new java.awt.Color(242, 242, 242));

        TxtApeUser.setBackground(new java.awt.Color(242, 242, 242));

        TxtCorreoUser.setBackground(new java.awt.Color(242, 242, 242));

        LblPuestoUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblPuestoUser.setForeground(new java.awt.Color(0, 0, 0));
        LblPuestoUser.setText("Puesto:");

        CbdPuesto.setBackground(new java.awt.Color(242, 242, 242));
        CbdPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        TxtUser.setBackground(new java.awt.Color(242, 242, 242));

        LblUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblUser.setForeground(new java.awt.Color(0, 0, 0));
        LblUser.setText("Usuario:");

        TxtContraUser.setBackground(new java.awt.Color(242, 242, 242));

        LblPass.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblPass.setForeground(new java.awt.Color(0, 0, 0));
        LblPass.setText("Contraseña:");

        TxtBuscarUser.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarUserActionPerformed(evt);
            }
        });

        PnlModificarBtn.setBackground(new java.awt.Color(217, 217, 217));

        BtnModificarUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnModificarUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnModificarUser.setText("Modificar Empleado");
        BtnModificarUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModificarUserMouseClicked(evt);
            }
        });
        BtnModificarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnModificarUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlModificarBtnLayout = new javax.swing.GroupLayout(PnlModificarBtn);
        PnlModificarBtn.setLayout(PnlModificarBtnLayout);
        PnlModificarBtnLayout.setHorizontalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnModificarUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlModificarBtnLayout.setVerticalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlModificarBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnModificarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PnlBuscar.setBackground(new java.awt.Color(217, 217, 217));

        BtnBuscarUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnBuscarUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnBuscarUser.setText("Buscar Empleado");
        BtnBuscarUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnBuscarUserMouseClicked(evt);
            }
        });
        BtnBuscarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarUserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBuscarLayout = new javax.swing.GroupLayout(PnlBuscar);
        PnlBuscar.setLayout(PnlBuscarLayout);
        PnlBuscarLayout.setHorizontalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnBuscarUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlBuscarLayout.setVerticalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlBuscarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnBuscarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(LstTelefonos);

        jScrollPane2.setViewportView(LstCorreos);

        LblTel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTel.setForeground(new java.awt.Color(0, 0, 0));
        LblTel.setText("Teléfonos:");

        LblCorreo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreo.setForeground(new java.awt.Color(0, 0, 0));
        LblCorreo.setText("Correos:");

        BtnAggTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnAggTel.setText("Agregar");
        BtnAggTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAggTelMouseClicked(evt);
            }
        });

        BtnEliTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnEliTel.setText("Eliminar");
        BtnEliTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnEliTelMouseClicked(evt);
            }
        });

        BtnModTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnModTel.setText("Modificar");
        BtnModTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModTelMouseClicked(evt);
            }
        });

        BtnAggCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnAggCorreo.setText("Agregar");
        BtnAggCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAggCorreoMouseClicked(evt);
            }
        });

        BtnEliCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnEliCorreo.setText("Eliminar");
        BtnEliCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnEliCorreoMouseClicked(evt);
            }
        });

        BtnModCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnModCorreo.setText("Modificar");
        BtnModCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModCorreoMouseClicked(evt);
            }
        });

        CbdColonia.setBackground(new java.awt.Color(242, 242, 242));
        CbdColonia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        TxtCalleUser.setBackground(new java.awt.Color(242, 242, 242));

        LblColoniaUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblColoniaUser.setForeground(new java.awt.Color(0, 0, 0));
        LblColoniaUser.setText("Colonia:");

        LblCalleUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCalleUser.setForeground(new java.awt.Color(0, 0, 0));
        LblCalleUser.setText("Calle y núm:");

        TxtURL.setBackground(new java.awt.Color(242, 242, 242));

        LblURLUser.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblURLUser.setForeground(new java.awt.Color(0, 0, 0));
        LblURLUser.setText("URL Foto:");

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
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(PnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(89, 89, 89)
                                                .addComponent(LblNomUser)
                                                .addGap(0, 6, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(LblPuestoUser, javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(LblRFCUser, javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(LblApeUser, javax.swing.GroupLayout.Alignment.TRAILING))))
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LblTitulo)
                                            .addComponent(LblTitulo1)
                                            .addComponent(TxtBuscarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(26, 26, 26)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(CbdPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(75, 75, 75)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(LblNSSUser)
                                                    .addComponent(LblUser)
                                                    .addComponent(LblPass))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(LblCalleUser)
                                                    .addComponent(LblColoniaUser)
                                                    .addComponent(LblURLUser))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TxtNomUser, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(TxtApeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(BtnAggTel)
                                        .addGap(31, 31, 31)
                                        .addComponent(BtnModTel)
                                        .addGap(39, 39, 39)
                                        .addComponent(BtnEliTel))
                                    .addComponent(TxtTelUer, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(194, 194, 194))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblTel))
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(LblCorreo)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(9, 9, 9)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(LblTelUser)
                                            .addComponent(LblCorreoUser))
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(BtnAggCorreo)
                                                .addGap(32, 32, 32)
                                                .addComponent(BtnModCorreo)
                                                .addGap(36, 36, 36)
                                                .addComponent(BtnEliCorreo))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(TxtCorreoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(TxtContraUser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(CbdColonia, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TxtCalleUser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TxtUser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TxtNSS, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TxtURL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(PnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblTel)
                            .addComponent(LblCorreo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2))
                        .addGap(108, 108, 108))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblTitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TxtBuscarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtNomUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblNomUser)
                            .addComponent(LblNSSUser)
                            .addComponent(TxtNSS, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtApeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblApeUser)
                            .addComponent(LblUser)
                            .addComponent(TxtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblRFCUser)
                            .addComponent(LblPass)
                            .addComponent(TxtContraUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblPuestoUser)
                            .addComponent(CbdPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CbdColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblColoniaUser))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TxtTelUer, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblTelUser))
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BtnAggTel)
                                    .addComponent(BtnEliTel)
                                    .addComponent(BtnModTel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(LblCorreoUser)
                                    .addComponent(TxtCorreoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BtnAggCorreo)
                                    .addComponent(BtnEliCorreo)
                                    .addComponent(BtnModCorreo)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TxtCalleUser, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblCalleUser))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblURLUser))))
                        .addGap(0, 0, Short.MAX_VALUE))))
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

    private void TxtBuscarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarUserActionPerformed

    private void BtnModificarUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModificarUserMouseClicked
        try {
            modificarEmpleadoYUsuario();
        } catch (IOException ex) {
            System.getLogger(ModificarUsuario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_BtnModificarUserMouseClicked

    private void BtnModificarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnModificarUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnModificarUserActionPerformed

    private void BtnBuscarUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnBuscarUserMouseClicked
        buscarEmpleadoPorNombre();
    }//GEN-LAST:event_BtnBuscarUserMouseClicked

    private void BtnBuscarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBuscarUserActionPerformed

    private void BtnAggTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAggTelMouseClicked
        agregarTelefono();
    }//GEN-LAST:event_BtnAggTelMouseClicked

    private void BtnAggCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAggCorreoMouseClicked
        agregarCorreo();
    }//GEN-LAST:event_BtnAggCorreoMouseClicked

    private void BtnEliTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEliTelMouseClicked
        eliminarTelefono();
    }//GEN-LAST:event_BtnEliTelMouseClicked

    private void BtnEliCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEliCorreoMouseClicked
        eliminarCorreo();
    }//GEN-LAST:event_BtnEliCorreoMouseClicked

    private void BtnModTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModTelMouseClicked
        modificarTelefono();
    }//GEN-LAST:event_BtnModTelMouseClicked

    private void BtnModCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModCorreoMouseClicked
        modificarCorreo();
    }//GEN-LAST:event_BtnModCorreoMouseClicked

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

    private int idEmpleadoSeleccionado = -1;

    // Buscar empleado por nombre de usuario
    private void buscarEmpleadoPorNombre() {
        String nombreBuscado = TxtBuscarUser.getText().trim();

        if (nombreBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre para buscar.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Farmacia.ConectarBD();

            String sql = "SELECT e.idempleados, e.nombres, e.apellidos, e.rfc, e.nss, e.tipos_puestos_idtipos_puestos, "
                    + "e.calle_num, e.colonias_empleados_idcolonias_empleados, u.nombre, u.clave, u.activo "
                    + "FROM empleados e "
                    + "JOIN usuarios u ON e.idempleados = u.empleados_idempleados "
                    + "WHERE e.nombres = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreBuscado);
            rs = stmt.executeQuery();

            if (rs.next()) {

                idEmpleadoSeleccionado = rs.getInt("idempleados");
                cargarTelefonosEmpleado(idEmpleadoSeleccionado);
                cargarCorreosEmpleado(idEmpleadoSeleccionado);
                // Una vez seleccionado el empleado:
                try (PreparedStatement stmtFoto = conn.prepareStatement(
                        "SELECT ruta FROM fotos_perfil WHERE idfotos_perfil = ?")) {
                    stmtFoto.setInt(1, idEmpleadoSeleccionado);
                    try (ResultSet rsFoto = stmtFoto.executeQuery()) {
                        if (rsFoto.next()) {
                            TxtURL.setText(rsFoto.getString("ruta"));
                        } else {
                            TxtURL.setText("");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    TxtURL.setText("");
                }
                TxtNomUser.setText(rs.getString("nombres"));
                TxtApeUser.setText(rs.getString("apellidos"));
                TxtRFC.setText(rs.getString("rfc"));
                TxtNSS.setText(rs.getString("nss"));
                TxtUser.setText(rs.getString("nombre"));
                TxtContraUser.setText(rs.getString("clave"));
                TxtCalleUser.setText(rs.getString("calle_num")); // ← Calle

                LblRFCB.setText(rs.getString("rfc"));
                LblNSSUserB.setText(rs.getString("nss"));
                LblNomUserB.setText(rs.getString("nombres"));

                int idPuesto = rs.getInt("tipos_puestos_idtipos_puestos");
                CbdPuesto.setSelectedIndex(obtenerIndexPuesto(idPuesto));

                int idColonia = rs.getInt("colonias_empleados_idcolonias_empleados");
                CbdColonia.setSelectedIndex(obtenerIndexColonia(idColonia)); // ← Colonia

                try (PreparedStatement stmtPuesto = conn.prepareStatement(
                        "SELECT nombre FROM tipos_puestos WHERE idtipos_puestos = ?")) {
                    stmtPuesto.setInt(1, idPuesto);
                    ResultSet rsPuesto = stmtPuesto.executeQuery();
                    if (rsPuesto.next()) {
                        String nombrePuesto = rsPuesto.getString("nombre");
                        LblPuestoUserB.setText(nombrePuesto);
                    } else {
                        LblPuestoUserB.setText("No encontrado");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LblPuestoUserB.setText("Error");
                }

                boolean activo = rs.getBoolean("activo");
                RbnActivo.setSelected(activo);
                RbnInactivo.setSelected(!activo);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún empleado con ese nombre.");
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el empleado: " + e.getMessage());
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

    private int obtenerIndexPuesto(int idPuesto) {
        int index = 0;
        for (Integer id : mapaPuestos.keySet()) {
            if (id == idPuesto) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private int obtenerIndexColonia(int idColonia) {
        int index = 0;
        for (Integer id : mapaColonias.keySet()) {
            if (id == idColonia) {
                return index;
            }
            index++;
        }
        return -1;
    }

    // Modificar empleado y usuario
    private void modificarEmpleadoYUsuario() throws IOException {
        Connection conn = null;
        CallableStatement stmtEmpleado = null;
        CallableStatement stmtUsuario = null;

        try {
            conn = Farmacia.ConectarBD();

            // Obtener datos del formulario
            String nombres = TxtNomUser.getText().trim();
            String apellidos = TxtApeUser.getText().trim();
            String rfc = TxtRFC.getText().trim();
            String nss = TxtNSS.getText().trim();
            String clave = TxtContraUser.getText().trim();
            String nombreUsuario = TxtUser.getText().trim();
            String calle = TxtCalleUser.getText().trim();

            if (nombres.isEmpty() || clave.isEmpty() || nombreUsuario.isEmpty() || calle.isEmpty()) {
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

            int idPuesto = (int) mapaPuestos.keySet().toArray()[CbdPuesto.getSelectedIndex()];
            int idColonia = (int) mapaColonias.keySet().toArray()[CbdColonia.getSelectedIndex()];

            // Modificar Empleado
            stmtEmpleado = conn.prepareCall("{CALL ModificarEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}");
            stmtEmpleado.setInt(1, idEmpleadoSeleccionado);
            stmtEmpleado.setString(2, nombres);
            stmtEmpleado.setString(3, apellidos);
            stmtEmpleado.setString(4, rfc);
            stmtEmpleado.setString(5, nss);
            stmtEmpleado.setInt(6, idPuesto);
            stmtEmpleado.setInt(7, idColonia);
            stmtEmpleado.setString(8, calle);
            stmtEmpleado.execute();

            stmtUsuario = conn.prepareCall("{CALL ModificarUsuario(?, ?, ?, ?, ?)}");
            stmtUsuario.setInt(1, idEmpleadoSeleccionado);
            stmtUsuario.setString(2, clave);
            stmtUsuario.setString(3, nombreUsuario);
            stmtUsuario.setInt(4, idPuesto);
            stmtUsuario.setBoolean(5, RbnActivo.isSelected());
            stmtUsuario.execute();

            if (!TxtURL.getText().isEmpty()) {
                ImageHTTPHandler handler = new ImageHTTPHandler("http://localhost:8081");

                String serverPath = "src\\icons\\" + TxtURL.getText().trim();

                handler.upload(serverPath);
            }

            JOptionPaneMensaje dialog = new JOptionPaneMensaje(this, "Listo!", "Empleado y usuario modificados con éxito.");
            dialog.setVisible(true);
            limpiarCampos();

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.getMessage());
        } finally {
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

    private Map<Integer, String> mapaPuestos = new LinkedHashMap<>();

    private void cargarPuestos() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Farmacia.ConectarBD();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT idtipos_puestos, nombre FROM tipos_puestos");

            CbdPuesto.removeAllItems();
            mapaPuestos.clear();

            while (rs.next()) {
                int id = rs.getInt("idtipos_puestos");
                String nombre = rs.getString("nombre");
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

    private Map<Integer, String> mapaColonias = new LinkedHashMap<>();

    private void cargarColonias() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Farmacia.ConectarBD();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT idcolonias_empleados, nombre FROM colonias_empleados");

            CbdColonia.removeAllItems();
            mapaColonias.clear();

            while (rs.next()) {
                int id = rs.getInt("idcolonias_empleados");
                String nombre = rs.getString("nombre");
                if (id > 0 && nombre != null && !nombre.trim().isEmpty()) {
                    mapaColonias.put(id, nombre);
                    CbdColonia.addItem(nombre);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar colonias: " + e.getMessage());
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

    private DefaultListModel<String> modeloTelefonos = new DefaultListModel<>();
    private DefaultListModel<String> modeloCorreos = new DefaultListModel<>();

    private void cargarTelefonosEmpleado(int idEmpleado) {
        modeloTelefonos.clear();
        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement("SELECT numero FROM numeros_empleados WHERE empleados_idempleados = ?")) {
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modeloTelefonos.addElement(rs.getString("numero"));
            }
            LstTelefonos.setModel(modeloTelefonos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarCorreosEmpleado(int idEmpleado) {
        modeloCorreos.clear();
        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement("SELECT correo FROM correos_empleados WHERE empleados_idempleados = ?")) {
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modeloCorreos.addElement(rs.getString("correo"));
            }
            LstCorreos.setModel(modeloCorreos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarTelefono() {
        String nuevoTelefono = TxtTelUer.getText().trim();

        if (!nuevoTelefono.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "El número telefónico debe tener exactamente 10 dígitos numéricos.",
                    "Teléfono inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nuevoTelefono.isEmpty() || idEmpleadoSeleccionado == -1) {
            return;
        }

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO numeros_empleados (numero, empleados_idempleados) VALUES (?, ?)")) {
            stmt.setString(1, nuevoTelefono);
            stmt.setInt(2, idEmpleadoSeleccionado);
            stmt.executeUpdate();
            cargarTelefonosEmpleado(idEmpleadoSeleccionado);
            TxtTelUer.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarCorreo() {
        String nuevoCorreo = TxtCorreoUser.getText().trim();
        if (nuevoCorreo.isEmpty() || idEmpleadoSeleccionado == -1) {
            return;
        }

        // Validar correo con expresión regular
        if (!nuevoCorreo.matches("^[a-zA-Z0-9._%+-]+@farmacode\\.com$")) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un correo válido. Ejemplo: nombre@farmacode.com",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO correos_empleados (correo, empleados_idempleados) VALUES (?, ?)")) {
            stmt.setString(1, nuevoCorreo);
            stmt.setInt(2, idEmpleadoSeleccionado);
            stmt.executeUpdate();
            cargarCorreosEmpleado(idEmpleadoSeleccionado);
            TxtCorreoUser.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarTelefono() {
        String telefonoSeleccionado = LstTelefonos.getSelectedValue();
        if (telefonoSeleccionado == null) {
            return;
        }

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM numeros_empleados WHERE numero = ? AND empleados_idempleados = ?")) {
            stmt.setString(1, telefonoSeleccionado);
            stmt.setInt(2, idEmpleadoSeleccionado);
            stmt.executeUpdate();
            cargarTelefonosEmpleado(idEmpleadoSeleccionado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarCorreo() {
        String correoSeleccionado = LstCorreos.getSelectedValue();
        if (correoSeleccionado == null) {
            return;
        }

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM correos_empleados WHERE correo = ? AND empleados_idempleados = ?")) {
            stmt.setString(1, correoSeleccionado);
            stmt.setInt(2, idEmpleadoSeleccionado);
            stmt.executeUpdate();
            cargarCorreosEmpleado(idEmpleadoSeleccionado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modificarTelefono() {
        String telefonoViejo = LstTelefonos.getSelectedValue();
        String telefonoNuevo = TxtTelUer.getText().trim();
        if (telefonoViejo == null || telefonoNuevo.isEmpty()) {
            return;
        }

        eliminarTelefono();  // elimina el seleccionado
        TxtTelUer.setText(telefonoNuevo);  // vuelve a ponerlo como si fuera nuevo
        agregarTelefono();   // lo vuelve a insertar
    }

    private void modificarCorreo() {
        String correoViejo = LstCorreos.getSelectedValue();
        String correoNuevo = TxtCorreoUser.getText().trim();
        if (correoViejo == null || correoNuevo.isEmpty()) {
            return;
        }

        eliminarCorreo();
        TxtCorreoUser.setText(correoNuevo);
        agregarCorreo();
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
        TxtCalleUser.setText("");
        TxtCorreoUser.setText("");
        TxtTelUer.setText("");
        TxtContraUser.setText("");
        TxtUser.setText("");
        TxtURL.setText("");
        TxtBuscar.setText("");
        TxtBuscarUser.setText("");
        LblRFCBB.setText("");
        LblPuestoUserB.setText("");
        LblNSSUserB.setText("");

        // Radio buttons
        RbnActivo.setSelected(false);
        RbnInactivo.setSelected(false);

        // Listas
        ((DefaultListModel<String>) LstCorreos.getModel()).clear();
        ((DefaultListModel<String>) LstTelefonos.getModel()).clear();

        // Restablecer valores por defecto
        RbnActivo.setSelected(true); // Valor por defecto

        // Enfocar primer campo
        TxtNomUser.requestFocusInWindow();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BtgActividad;
    private javax.swing.JButton BtnAggCorreo;
    private javax.swing.JButton BtnAggTel;
    private javax.swing.JButton BtnBuscarUser;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnEliCorreo;
    private javax.swing.JButton BtnEliTel;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnModCorreo;
    private javax.swing.JButton BtnModTel;
    private javax.swing.JButton BtnModificarUser;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CbdColonia;
    private javax.swing.JComboBox<String> CbdPuesto;
    private javax.swing.JLabel LblApeUser;
    private javax.swing.JLabel LblCalleUser;
    private javax.swing.JLabel LblColoniaUser;
    private javax.swing.JLabel LblCorreo;
    private javax.swing.JLabel LblCorreoUser;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblNSSUser;
    private javax.swing.JLabel LblNSSUserB;
    private javax.swing.JLabel LblNomUser;
    private javax.swing.JLabel LblNomUserB;
    private javax.swing.JLabel LblPass;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblPuestoUser;
    private javax.swing.JLabel LblPuestoUserB;
    private javax.swing.JLabel LblRFCB;
    private javax.swing.JLabel LblRFCBB;
    private javax.swing.JLabel LblRFCUser;
    private javax.swing.JLabel LblTel;
    private javax.swing.JLabel LblTelUser;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblURLUser;
    private javax.swing.JLabel LblUser;
    private javax.swing.JList<String> LstCorreos;
    private javax.swing.JList<String> LstTelefonos;
    private javax.swing.JPanel PnlBtn;
    private javax.swing.JPanel PnlBuscar;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlInfo;
    private javax.swing.JPanel PnlModificarBtn;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JRadioButton RbnActivo;
    private javax.swing.JRadioButton RbnInactivo;
    private javax.swing.JTextField TxtApeUser;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarUser;
    private javax.swing.JTextField TxtCalleUser;
    private javax.swing.JTextField TxtContraUser;
    private javax.swing.JTextField TxtCorreoUser;
    private javax.swing.JTextField TxtNSS;
    private javax.swing.JTextField TxtNomUser;
    private javax.swing.JTextField TxtRFC;
    private javax.swing.JTextField TxtTelUer;
    private javax.swing.JTextField TxtURL;
    private javax.swing.JTextField TxtUser;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
