package Interfaces;

import Entidades.ProveedorDAO;
import Entidades.Proveedor;
import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import static farmacia.Farmacia.ConectarBD;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.paintBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;
import metodos.estiloComboBox;

public class ModificarProv extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;

    public ModificarProv() {
        initComponents();
        cargarColonias();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Buscar proveedor...", TxtBuscarProv);
        TextPrompt placeHolder3 = new TextPrompt("Calee y Número del proveedor...", TxtCalle);
        TextPrompt placeHolder4 = new TextPrompt("Correo del proveedor...", TxtCorreo);
        TextPrompt placeHolder5 = new TextPrompt("Nombre del proveedor...", TxtNomProv);
        TextPrompt placeHolder6 = new TextPrompt("Teléfono del proveedor...", TxtTel);
        TextPrompt placeHolder7 = new TextPrompt("RFC del proveedor...", TxtRFC);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);
        TxtBuscarProv.setBorder(redondearTxt);
        TxtNomProv.setBorder(redondearTxt);
        TxtRFC.setBorder(redondearTxt);
        TxtCalle.setBorder(redondearTxt);
        TxtTel.setBorder(redondearTxt);
        TxtCorreo.setBorder(redondearTxt);

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

        BtnAggTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAggTel.setFocusPainted(false);

        BtnModTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnModTel.setFocusPainted(false);

        BtnEliTel.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnEliTel.setFocusPainted(false);

        BtnAgregarCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAgregarCorreo.setFocusPainted(false);

        BtnModificarCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnModificarCorreo.setFocusPainted(false);

        BtnEliminarCorreo.setBorder(new paintBorder(5, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnEliminarCorreo.setFocusPainted(false);

        estiloComboBox.aplicarEstiloComboBox(CboxColonias);
        
        // BtnTransparent
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnRequisiciones);
        BotonTransparente.hacerBotonTransparente(BtnProveedores);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnDevoluciones);
        BotonTransparente.hacerBotonTransparente(BtnFacturas);
        BotonTransparente.hacerBotonTransparente(BtnBuscar);
        
        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlFarmaLab, 15, Color.WHITE);

        RoundPanel.aplicarBordesRedondeados(PnlModificarBtn, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBuscar, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        BtnModificarP.setBorder(new paintBorder(5, Color.getHSBColor(0.f, 0f, 0f)));
        BtnModificarP.setFocusPainted(false);

        BtnBuscar.setBorder(new paintBorder(5, Color.getHSBColor(0.f, 0f, 0f)));
        BtnBuscar.setFocusPainted(false);

        BotonTransparente.hacerBotonTransparente(BtnModificarP);

        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            BtnUsers.setText("Usuarios");
        } else {
            BtnUsers.setText("Ayuda");
        }
        
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BtgActividad = new javax.swing.ButtonGroup();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlBtn = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        BtnInventario = new javax.swing.JButton();
        BtnVentas = new javax.swing.JButton();
        BtnRequisiciones = new javax.swing.JButton();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnProveedores = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnFacturas = new javax.swing.JButton();
        BtnDevoluciones = new javax.swing.JButton();
        BtnInicio = new javax.swing.JButton();
        PnlFarmaLab = new javax.swing.JPanel();
        LblNom = new javax.swing.JLabel();
        LblCorreoProv1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        LblRFC = new javax.swing.JLabel();
        LblRFCB = new javax.swing.JLabel();
        LblCorreoB = new javax.swing.JLabel();
        PnlRdn = new javax.swing.JPanel();
        RbnActivo = new javax.swing.JRadioButton();
        RbnInactivo = new javax.swing.JRadioButton();
        LblDirecB = new javax.swing.JLabel();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        TxtRFC = new javax.swing.JTextField();
        LblEmpleado = new javax.swing.JLabel();
        LblRFC1 = new javax.swing.JLabel();
        TxtNomProv = new javax.swing.JTextField();
        LblDireccion = new javax.swing.JLabel();
        TxtBuscarProv = new javax.swing.JTextField();
        PnlModificarBtn = new javax.swing.JPanel();
        BtnModificarP = new javax.swing.JButton();
        PnlBuscar = new javax.swing.JPanel();
        BtnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        LstTelefonos = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        LstCorreos = new javax.swing.JList<>();
        LblCorreos = new javax.swing.JLabel();
        LblTelefonos = new javax.swing.JLabel();
        TxtTel = new javax.swing.JTextField();
        TxtCorreo = new javax.swing.JTextField();
        LblTel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        BtnAggTel = new javax.swing.JButton();
        BtnModTel = new javax.swing.JButton();
        BtnEliTel = new javax.swing.JButton();
        BtnAgregarCorreo = new javax.swing.JButton();
        BtnModificarCorreo = new javax.swing.JButton();
        BtnEliminarCorreo = new javax.swing.JButton();
        CboxColonias = new javax.swing.JComboBox<>();
        LblCalle = new javax.swing.JLabel();
        TxtCalle = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1630, 870));

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Modificar información sobre proveedores.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Proveedores");

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

        LblFarmacia.setBackground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblFarmacia.setForeground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setText("FarmaCode");

        BtnProveedores.setBackground(new java.awt.Color(242, 242, 242));
        BtnProveedores.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnProveedores.setForeground(new java.awt.Color(0, 119, 182));
        BtnProveedores.setText("Proveedores");
        BtnProveedores.setBorder(null);
        BtnProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProveedoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(BtnProveedores)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        BtnUsers.setBackground(new java.awt.Color(0, 119, 182));
        BtnUsers.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnUsers.setForeground(new java.awt.Color(255, 255, 255));
        BtnUsers.setText("Usuarios");
        BtnUsers.setBorder(null);
        BtnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsersActionPerformed(evt);
            }
        });

        LblLogo.setBackground(new java.awt.Color(255, 255, 255));
        LblLogo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LblLogo.setForeground(new java.awt.Color(51, 51, 51));

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

        javax.swing.GroupLayout PnlBtnLayout = new javax.swing.GroupLayout(PnlBtn);
        PnlBtn.setLayout(PnlBtnLayout);
        PnlBtnLayout.setHorizontalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(LblFarmacia))
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnVentas)
                            .addComponent(BtnInventario)
                            .addComponent(BtnFacturas)
                            .addComponent(BtnDevoluciones)
                            .addComponent(BtnInicio)))
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers)
                            .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlBtnLayout.setVerticalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addGap(105, 105, 105)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        PnlFarmaLab.setBackground(new java.awt.Color(255, 255, 255));

        LblNom.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N

        LblCorreoProv1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreoProv1.setText("Dirección: ");

        jPanel2.setBackground(new java.awt.Color(217, 217, 217));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        LblRFC.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFC.setText("RFC: ");

        PnlRdn.setBackground(new java.awt.Color(255, 255, 255));

        RbnActivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnActivo);
        RbnActivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnActivo.setText("Activo");
        RbnActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbnActivoActionPerformed(evt);
            }
        });

        RbnInactivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnInactivo);
        RbnInactivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnInactivo.setText("Inactivo");
        RbnInactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbnInactivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlRdnLayout = new javax.swing.GroupLayout(PnlRdn);
        PnlRdn.setLayout(PnlRdnLayout);
        PnlRdnLayout.setHorizontalGroup(
            PnlRdnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlRdnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RbnActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RbnInactivo)
                .addContainerGap(148, Short.MAX_VALUE))
        );
        PnlRdnLayout.setVerticalGroup(
            PnlRdnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRdnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PnlRdnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbnInactivo)
                    .addComponent(RbnActivo))
                .addContainerGap())
        );

        javax.swing.GroupLayout PnlFarmaLabLayout = new javax.swing.GroupLayout(PnlFarmaLab);
        PnlFarmaLab.setLayout(PnlFarmaLabLayout);
        PnlFarmaLabLayout.setHorizontalGroup(
            PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblCorreoProv1)
                            .addComponent(LblRFC))
                        .addGap(18, 18, 18)
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblRFCB, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblDirecB, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(172, 172, 172)
                        .addComponent(LblCorreoB, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(LblNom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlRdn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlFarmaLabLayout.setVerticalGroup(
            PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(LblNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                                .addComponent(LblRFC)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFarmaLabLayout.createSequentialGroup()
                                .addComponent(LblRFCB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblDirecB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblCorreoProv1)))
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(LblCorreoB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(PnlRdn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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

        TxtRFC.setBackground(new java.awt.Color(242, 242, 242));

        LblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblEmpleado.setText("Nombre Proveedor:");

        LblRFC1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFC1.setText("RFC:");
        LblRFC1.setToolTipText("");

        TxtNomProv.setBackground(new java.awt.Color(242, 242, 242));
        TxtNomProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNomProvActionPerformed(evt);
            }
        });

        LblDireccion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblDireccion.setText("Colonia:");

        TxtBuscarProv.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarProvActionPerformed(evt);
            }
        });

        PnlModificarBtn.setBackground(new java.awt.Color(217, 217, 217));

        BtnModificarP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnModificarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnModificarP.setText("Modificar Proveedor");
        BtnModificarP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModificarPMouseClicked(evt);
            }
        });
        BtnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnModificarPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlModificarBtnLayout = new javax.swing.GroupLayout(PnlModificarBtn);
        PnlModificarBtn.setLayout(PnlModificarBtnLayout);
        PnlModificarBtnLayout.setHorizontalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnModificarP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlModificarBtnLayout.setVerticalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlModificarBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnModificarP, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PnlBuscar.setBackground(new java.awt.Color(217, 217, 217));

        BtnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnBuscar.setText("Buscar Proveedor");
        BtnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnBuscarMouseClicked(evt);
            }
        });
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBuscarLayout = new javax.swing.GroupLayout(PnlBuscar);
        PnlBuscar.setLayout(PnlBuscarLayout);
        PnlBuscarLayout.setHorizontalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlBuscarLayout.setVerticalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlBuscarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(LstTelefonos);

        jScrollPane2.setViewportView(LstCorreos);

        LblCorreos.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreos.setText("Correos:");

        LblTelefonos.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTelefonos.setText("Telefonos:");

        TxtTel.setBackground(new java.awt.Color(242, 242, 242));

        TxtCorreo.setBackground(new java.awt.Color(242, 242, 242));

        LblTel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTel.setText("Telefono:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Correo:");

        BtnAggTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnAggTel.setForeground(new java.awt.Color(0, 0, 0));
        BtnAggTel.setText("Agregar");
        BtnAggTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAggTelMouseClicked(evt);
            }
        });
        BtnAggTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAggTelActionPerformed(evt);
            }
        });

        BtnModTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnModTel.setForeground(new java.awt.Color(0, 0, 0));
        BtnModTel.setText("Modificar");
        BtnModTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModTelMouseClicked(evt);
            }
        });

        BtnEliTel.setBackground(new java.awt.Color(242, 242, 242));
        BtnEliTel.setForeground(new java.awt.Color(0, 0, 0));
        BtnEliTel.setText("Eliminar");
        BtnEliTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnEliTelMouseClicked(evt);
            }
        });

        BtnAgregarCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnAgregarCorreo.setForeground(new java.awt.Color(0, 0, 0));
        BtnAgregarCorreo.setText("Agregar");
        BtnAgregarCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAgregarCorreoMouseClicked(evt);
            }
        });

        BtnModificarCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnModificarCorreo.setForeground(new java.awt.Color(0, 0, 0));
        BtnModificarCorreo.setText("Modificar");
        BtnModificarCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModificarCorreoMouseClicked(evt);
            }
        });

        BtnEliminarCorreo.setBackground(new java.awt.Color(242, 242, 242));
        BtnEliminarCorreo.setForeground(new java.awt.Color(0, 0, 0));
        BtnEliminarCorreo.setText("Eliminar");
        BtnEliminarCorreo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnEliminarCorreoMouseClicked(evt);
            }
        });

        CboxColonias.setBackground(new java.awt.Color(242, 242, 242));

        LblCalle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCalle.setText("Calle y num:");

        TxtCalle.setEditable(false);
        TxtCalle.setBackground(new java.awt.Color(242, 242, 242));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PnlFarmaLab, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(LblCorreos))
                                        .addGap(72, 72, 72)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(LblTelefonos)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(TxtBuscarProv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(LblTitulo)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(197, 197, 197)
                                        .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(LblRFC1)
                                            .addComponent(LblTel)
                                            .addComponent(jLabel1))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(BtnAgregarCorreo)
                                                .addGap(79, 79, 79)
                                                .addComponent(BtnModificarCorreo)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(BtnEliminarCorreo))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(BtnAggTel)
                                                .addGap(80, 80, 80)
                                                .addComponent(BtnModTel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(BtnEliTel))
                                            .addComponent(TxtTel)
                                            .addComponent(TxtCorreo)
                                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LblCalle, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(LblDireccion, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(LblEmpleado))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(CboxColonias, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(TxtCalle)
                                            .addComponent(TxtNomProv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(LblTitulo1))
                        .addContainerGap(32, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LblTitulo1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtBuscarProv, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PnlFarmaLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblCorreos)
                            .addComponent(LblTelefonos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtNomProv, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblEmpleado))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblCalle))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblDireccion)
                            .addComponent(CboxColonias, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblRFC1)
                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblTel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnAggTel)
                            .addComponent(BtnModTel)
                            .addComponent(BtnEliTel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BtnAgregarCorreo)
                            .addComponent(BtnModificarCorreo)
                            .addComponent(BtnEliminarCorreo))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 848, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtBuscarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarProvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarProvActionPerformed

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

    private void BtnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnConfigActionPerformed

    }//GEN-LAST:event_BtnConfigActionPerformed

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

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void TxtNomProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNomProvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNomProvActionPerformed

    private void BtnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnModificarPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnModificarPActionPerformed

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBuscarActionPerformed

    private void RbnInactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbnInactivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RbnInactivoActionPerformed

    private void BtnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnBuscarMouseClicked
        BuscarProveedor();
    }//GEN-LAST:event_BtnBuscarMouseClicked

    private void BtnModificarPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModificarPMouseClicked
        ModificarProveedor();
    }//GEN-LAST:event_BtnModificarPMouseClicked

    private void RbnActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbnActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RbnActivoActionPerformed

    private void BtnAggTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAggTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAggTelActionPerformed

    private void BtnAggTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAggTelMouseClicked
        AgregarTelefono();
    }//GEN-LAST:event_BtnAggTelMouseClicked

    private void BtnEliTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEliTelMouseClicked
        EliminarTel();
    }//GEN-LAST:event_BtnEliTelMouseClicked

    private void BtnModTelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModTelMouseClicked
        ModificarTel();
    }//GEN-LAST:event_BtnModTelMouseClicked

    private void BtnAgregarCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAgregarCorreoMouseClicked
        AgregarCorreo();
    }//GEN-LAST:event_BtnAgregarCorreoMouseClicked

    private void BtnModificarCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModificarCorreoMouseClicked
        ModificarCorreo();
    }//GEN-LAST:event_BtnModificarCorreoMouseClicked

    private void BtnEliminarCorreoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEliminarCorreoMouseClicked
        EliminarCorreo();
    }//GEN-LAST:event_BtnEliminarCorreoMouseClicked

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

    Proveedor proveedorActual = null;

    private void BuscarProveedor() {
        // Obtener el nombre del campo de texto
        String nombre = TxtBuscarProv.getText().trim();

        // Validar que no esté vacío
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un nombre para buscar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscar el proveedor en la base de datos
        proveedorActual = ProveedorDAO.buscarPorNombre(nombre);

        if (proveedorActual != null) {
            // Mostrar datos en las etiquetas
            LblNom.setText(proveedorActual.getNombre());
            LblRFCB.setText(proveedorActual.getRfc());
            LblDirecB.setText(proveedorActual.getDireccion());

            // Mostrar número_calle directamente
            String numeroCalle = proveedorActual.getNumeroCalle();
            TxtCalle.setText(numeroCalle);

            // Configurar radio buttons de estado
            RbnActivo.setSelected(proveedorActual.getActivo() == 1);
            RbnInactivo.setSelected(proveedorActual.getActivo() == 0);

            // Llenar campos editables
            TxtNomProv.setText(proveedorActual.getNombre());
            TxtRFC.setText(proveedorActual.getRfc());

            // Configurar la colonia
            String coloniaProveedor = obtenerColoniaPorDireccion(proveedorActual.getDireccion());
            if (coloniaProveedor != null) {
                CboxColonias.setSelectedItem(coloniaProveedor);
            } else {
                CboxColonias.setSelectedIndex(0);
            }

            // Mostrar teléfonos y correos
            LstTelefonos.setListData(proveedorActual.getTelefonos().toArray(new String[0]));
            LstCorreos.setListData(proveedorActual.getCorreos().toArray(new String[0]));

        } else {
            JOptionPane.showMessageDialog(this,
                    "Proveedor no encontrado",
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        }
    }

    // Método auxiliar para obtener el nombre de la colonia
    private String obtenerColoniaPorDireccion(String direccion) {
        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement ps = conn.prepareStatement(
                "SELECT c.nombre FROM colonias_proveedores c "
                + "JOIN proveedores p ON p.colonias_proveedores_idcolonias_proveedores = c.idcolonias_proveedores "
                + "WHERE p.idproveedores = ?")) {

            ps.setInt(1, proveedorActual.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Método para limpiar los campos cuando no se encuentra el proveedor
    private void limpiarCampos() {
        LblNom.setText("");
        LblRFCB.setText("");
        LblDirecB.setText("");
        RbnActivo.setSelected(false);
        RbnInactivo.setSelected(false);
        TxtNomProv.setText("");
        TxtRFC.setText("");
        CboxColonias.setSelectedIndex(0);
        LstTelefonos.setListData(new String[0]);
        LstCorreos.setListData(new String[0]);
    }

    private void ModificarProveedor() {
        if (proveedorActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe buscar y seleccionar un proveedor antes de modificar.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = TxtNomProv.getText().trim();
        String rfc = TxtRFC.getText().trim();
        String numeroCalle = TxtCalle.getText().trim();
        String coloniaSeleccionada = (String) CboxColonias.getSelectedItem();

        // Validación de campos vacíos
        if (nombre.isEmpty() || rfc.isEmpty() || numeroCalle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nombre, RFC y dirección (calle y número) no pueden estar vacíos.",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación de RFC (formato típico de 13 caracteres: 4 letras, 6 números, 3 alfanuméricos)
        if (!rfc.matches("^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$")) {
            JOptionPane.showMessageDialog(this,
                    "RFC inválido. Debe tener 13 caracteres con el formato correcto.",
                    "RFC incorrecto",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación de colonia
        Integer idColonia = coloniasMap.get(coloniaSeleccionada);
        if (idColonia == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una colonia válida.",
                    "Colonia incorrecta",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Actualizar datos
        proveedorActual.setNombre(nombre);
        proveedorActual.setRfc(rfc);
        proveedorActual.setNumeroCalle(numeroCalle);  // dirección
        proveedorActual.setColoniaId(idColonia);
        proveedorActual.setActivo(RbnActivo.isSelected() ? 1 : 0);

        try {
            boolean actualizado = ProveedorDAO.actualizarProveedor(proveedorActual);

            if (!actualizado) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el proveedor.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (proveedorActual.getActivo() == 0) {
                boolean baja = ProveedorDAO.darDeBajaProductos(proveedorActual.getId());
                if (!baja) {
                    JOptionPane.showMessageDialog(this,
                            "Proveedor desactivado, pero no se pudo dar de baja sus productos.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Proveedor actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            actualizarVistaProveedor();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar proveedor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarVistaProveedor() {
        // Actualizar las etiquetas de visualización
        LblNom.setText(proveedorActual.getNombre());
        LblRFCB.setText(proveedorActual.getRfc());
        LblDirecB.setText(proveedorActual.getDireccion());
        RbnActivo.setSelected(proveedorActual.getActivo() == 1);
        RbnInactivo.setSelected(proveedorActual.getActivo() == 0);
    }
    
    // Metodo para obtener telefonos
    public static ArrayList<String> obtenerTelefonos(int idProveedor) {
        ArrayList<String> telefonos = new ArrayList<>();
        Connection conn = Farmacia.ConectarBD();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT numero FROM numeros_proveedores WHERE proveedores_idproveedores = ?")) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                telefonos.add(rs.getString("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return telefonos;
    }

    // Igual para correos
    public static ArrayList<String> obtenerCorreos(int idProveedor) {
        ArrayList<String> correos = new ArrayList<>();
        Connection conn = Farmacia.ConectarBD();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT correo FROM correos_proveedores WHERE proveedores_idproveedores = ?")) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                correos.add(rs.getString("correo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return correos;
    }

    private void AgregarTelefono() {
        String nuevoTelefono = TxtTel.getText().trim();

        // Validar número de teléfono (10 dígitos numéricos)
        if (!nuevoTelefono.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "El número telefónico debe tener exactamente 10 dígitos numéricos.",
                    "Teléfono inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = farmacia.Farmacia.ConectarBD();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO numeros_proveedores (numero, proveedores_idproveedores) VALUES (?, ?)");
            ps.setString(1, nuevoTelefono);
            ps.setInt(2, proveedorActual.getId());
            ps.executeUpdate();
            conn.close();
            cargarDatosTel(); // refrescar lista
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        }
    }

    private void EliminarTel() {
        int index = LstTelefonos.getSelectedIndex();
        if (index != -1) {
            String seleccionado = LstTelefonos.getSelectedValue();
            try {
                Connection conn = farmacia.Farmacia.ConectarBD();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM numeros_proveedores WHERE numero = ?");
                ps.setString(1, seleccionado);
                ps.executeUpdate();
                conn.close();
                cargarDatosTel();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void ModificarTel() {
        int index = LstTelefonos.getSelectedIndex();
        String nuevoTelefono = TxtTel.getText().trim();

        // Validar número de teléfono (10 dígitos)
        if (!nuevoTelefono.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "El número telefónico debe tener exactamente 10 dígitos numéricos.",
                    "Teléfono inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (index != -1 && !nuevoTelefono.isEmpty()) {
            String valorAnterior = LstTelefonos.getSelectedValue();
            try {
                Connection conn = farmacia.Farmacia.ConectarBD();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE numeros_proveedores SET numero = ? WHERE numero = ?");
                ps.setString(1, nuevoTelefono);
                ps.setString(2, valorAnterior);
                ps.executeUpdate();
                conn.close();
                cargarDatosTel();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
            }
        }
    }

    private void cargarDatosTel() {
        // Verificar si el modelo actual es un DefaultListModel
        DefaultListModel<String> modelo;
        if (LstTelefonos.getModel() instanceof DefaultListModel) {
            modelo = (DefaultListModel<String>) LstTelefonos.getModel();
        } else {
            modelo = new DefaultListModel<>();
            LstTelefonos.setModel(modelo);
        }

        modelo.clear();

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement ps = conn.prepareStatement("SELECT numero FROM numeros_proveedores WHERE proveedores_idproveedores = ?")) {

            ps.setInt(1, proveedorActual.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addElement(rs.getString("numero"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void AgregarCorreo() {
        String nuevoCorreo = TxtCorreo.getText().trim();

        // Validar correo con expresión regular
        if (!nuevoCorreo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un correo válido. Ejemplo: nombre@dominio.com",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = farmacia.Farmacia.ConectarBD();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO correos_proveedores (correo, proveedores_idproveedores) VALUES (?, ?)");
            ps.setString(1, nuevoCorreo);
            ps.setInt(2, proveedorActual.getId());
            ps.executeUpdate();
            conn.close();
            cargarDatosCorreo(); // refrescar lista
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + e.getMessage());
        }
    }

    private void EliminarCorreo() {
        int index = LstCorreos.getSelectedIndex();
        if (index != -1) {
            String seleccionado = LstCorreos.getSelectedValue();
            try {
                Connection conn = farmacia.Farmacia.ConectarBD();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM correos_proveedores WHERE correo = ?");
                ps.setString(1, seleccionado);
                ps.executeUpdate();
                conn.close();
                cargarDatosCorreo();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void ModificarCorreo() {
        int index = LstCorreos.getSelectedIndex();
        String nuevoCorreo = TxtCorreo.getText().trim();

        // Validar correo
        if (!nuevoCorreo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un correo válido. Ejemplo: nombre@dominio.com",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (index != -1 && !nuevoCorreo.isEmpty()) {
            String valorAnterior = LstCorreos.getSelectedValue();
            try {
                Connection conn = farmacia.Farmacia.ConectarBD();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE correos_proveedores SET correo = ? WHERE correo = ?");
                ps.setString(1, nuevoCorreo);
                ps.setString(2, valorAnterior);
                ps.executeUpdate();
                conn.close();
                cargarDatosCorreo();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
            }
        }
    }

    private void cargarDatosCorreo() {
        // Verificar si el modelo actual es un DefaultListModel
        DefaultListModel<String> modelo;
        if (LstCorreos.getModel() instanceof DefaultListModel) {
            modelo = (DefaultListModel<String>) LstCorreos.getModel();
        } else {
            modelo = new DefaultListModel<>();
            LstCorreos.setModel(modelo);
        }

        modelo.clear();

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement ps = conn.prepareStatement("SELECT correo FROM correos_proveedores WHERE proveedores_idproveedores = ?")) {

            ps.setInt(1, proveedorActual.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addElement(rs.getString("correo"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private Map<String, Integer> coloniasMap = new HashMap<>();

    private void cargarColonias() {
        Connection conn = ConectarBD();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT idcolonias_proveedores, nombre FROM colonias_proveedores";
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idcolonias_proveedores");
                String nombre = rs.getString("nombre");
                coloniasMap.put(nombre, id);
                CboxColonias.addItem(nombre);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las colonias: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BtgActividad;
    private javax.swing.JButton BtnAggTel;
    private javax.swing.JButton BtnAgregarCorreo;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnEliTel;
    private javax.swing.JButton BtnEliminarCorreo;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnModTel;
    private javax.swing.JButton BtnModificarCorreo;
    private javax.swing.JButton BtnModificarP;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CboxColonias;
    private javax.swing.JLabel LblCalle;
    private javax.swing.JLabel LblCorreoB;
    private javax.swing.JLabel LblCorreoProv1;
    private javax.swing.JLabel LblCorreos;
    private javax.swing.JLabel LblDirecB;
    private javax.swing.JLabel LblDireccion;
    private javax.swing.JLabel LblEmpleado;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblNom;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblRFC;
    private javax.swing.JLabel LblRFC1;
    private javax.swing.JLabel LblRFCB;
    private javax.swing.JLabel LblTel;
    private javax.swing.JLabel LblTelefonos;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JList<String> LstCorreos;
    private javax.swing.JList<String> LstTelefonos;
    private javax.swing.JPanel PnlBtn;
    private javax.swing.JPanel PnlBuscar;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlFarmaLab;
    private javax.swing.JPanel PnlModificarBtn;
    private javax.swing.JPanel PnlRdn;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JRadioButton RbnActivo;
    private javax.swing.JRadioButton RbnInactivo;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarProv;
    private javax.swing.JTextField TxtCalle;
    private javax.swing.JTextField TxtCorreo;
    private javax.swing.JTextField TxtNomProv;
    private javax.swing.JTextField TxtRFC;
    private javax.swing.JTextField TxtTel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
