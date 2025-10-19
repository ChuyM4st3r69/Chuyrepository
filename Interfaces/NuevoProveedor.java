package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.sql.CallableStatement;
import java.sql.Types;
import static farmacia.Farmacia.ConectarBD;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;
import metodos.estiloComboBox;

public class NuevoProveedor extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;

    public NuevoProveedor() {
        initComponents();
        cargarColonias();
        estiloComboBox.aplicarEstiloComboBox(CboxColonias);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Calle y Número del proveedor...", TxtCalle);
        TextPrompt placeHolder3 = new TextPrompt("Correo del proveedor...", TxtCorreo);
        TextPrompt placeHolder4 = new TextPrompt("Nombre del proveedor...", TxtNomProv);
        TextPrompt placeHolder5 = new TextPrompt("RFC del proveedor...", TxtRFC);
        TextPrompt placeHolder6 = new TextPrompt("Telefono del proveedor...", TxtTel);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);
        TxtCorreo.setBorder(redondearTxt);
        TxtCalle.setBorder(redondearTxt);
        TxtRFC.setBorder(redondearTxt);
        TxtTel.setBorder(redondearTxt);
        TxtNomProv.setBorder(redondearTxt);

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

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
        BotonTransparente.hacerBotonTransparente(BtnPRequisicion);

        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlNuevoProv, 15, Color.WHITE);
        RoundPanel.aplicarBordesRedondeados(PnlRequisicionBtn, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        BtnPRequisicion.setBorder(new paintBorder(5, Color.getHSBColor(0.f, 0f, 0f)));
        BtnPRequisicion.setFocusPainted(false);
        
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
        jPanel1 = new javax.swing.JPanel();
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
        LblTitulo1 = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        PnlNuevoProv = new javax.swing.JPanel();
        LblCalle = new javax.swing.JLabel();
        LblEmpleado = new javax.swing.JLabel();
        LblCorreo = new javax.swing.JLabel();
        LblRFC = new javax.swing.JLabel();
        TxtTel = new javax.swing.JTextField();
        TxtCalle = new javax.swing.JTextField();
        TxtNomProv = new javax.swing.JTextField();
        TxtCorreo = new javax.swing.JTextField();
        LblDireccion = new javax.swing.JLabel();
        TxtRFC = new javax.swing.JTextField();
        LblTel = new javax.swing.JLabel();
        PnlRequisicionBtn = new javax.swing.JPanel();
        BtnPRequisicion = new javax.swing.JButton();
        CboxColonias = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 119, 182));

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(LblFarmacia))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnVentas)
                            .addComponent(BtnInventario)
                            .addComponent(BtnFacturas)
                            .addComponent(BtnDevoluciones)
                            .addComponent(BtnInicio)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
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
                .addGap(18, 18, 18)
                .addComponent(BtnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Proveedores");

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Dar de alta a nuevos proveedores.");
        LblTitulo.setOpaque(true);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 329, Short.MAX_VALUE)
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

        PnlNuevoProv.setBackground(new java.awt.Color(255, 255, 255));

        LblCalle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCalle.setText("Calle y num:");

        LblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblEmpleado.setText("Nombre Proveedor:");

        LblCorreo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreo.setText("Correo Electrónico:");

        LblRFC.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFC.setText("RFC:");
        LblRFC.setToolTipText("");

        TxtCalle.setToolTipText("");
        TxtCalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCalleActionPerformed(evt);
            }
        });

        TxtNomProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNomProvActionPerformed(evt);
            }
        });

        LblDireccion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblDireccion.setText("Colonia:");

        LblTel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTel.setText("Teléfono:");

        PnlRequisicionBtn.setBackground(new java.awt.Color(217, 217, 217));

        BtnPRequisicion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnPRequisicion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnPRequisicion.setText("Guardar Proveedor");
        BtnPRequisicion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnPRequisicionMouseClicked(evt);
            }
        });
        BtnPRequisicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPRequisicionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlRequisicionBtnLayout = new javax.swing.GroupLayout(PnlRequisicionBtn);
        PnlRequisicionBtn.setLayout(PnlRequisicionBtnLayout);
        PnlRequisicionBtnLayout.setHorizontalGroup(
            PnlRequisicionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnPRequisicion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlRequisicionBtnLayout.setVerticalGroup(
            PnlRequisicionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRequisicionBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnPRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout PnlNuevoProvLayout = new javax.swing.GroupLayout(PnlNuevoProv);
        PnlNuevoProv.setLayout(PnlNuevoProvLayout);
        PnlNuevoProvLayout.setHorizontalGroup(
            PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                        .addComponent(LblTel)
                        .addGap(18, 18, 18)
                        .addComponent(TxtTel))
                    .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                        .addComponent(LblCalle)
                        .addGap(18, 18, 18)
                        .addComponent(TxtCalle))
                    .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                        .addComponent(LblRFC)
                        .addGap(18, 18, 18)
                        .addComponent(TxtRFC))
                    .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                        .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblCorreo)
                            .addComponent(LblDireccion)
                            .addComponent(LblEmpleado))
                        .addGap(18, 18, 18)
                        .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtNomProv)
                            .addComponent(TxtCorreo)
                            .addComponent(CboxColonias, 0, 258, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(PnlRequisicionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        PnlNuevoProvLayout.setVerticalGroup(
            PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblCalle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtNomProv, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblEmpleado))
                .addGap(8, 8, 8)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblCorreo)
                    .addComponent(TxtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblDireccion)
                    .addComponent(CboxColonias, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PnlNuevoProvLayout.createSequentialGroup()
                        .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblRFC)
                            .addComponent(TxtRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblTel)
                            .addComponent(TxtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(PnlRequisicionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblTitulo)
                            .addComponent(LblTitulo1)
                            .addComponent(PnlNuevoProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo)
                .addGap(18, 18, 18)
                .addComponent(PnlNuevoProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 845, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void TxtCalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtCalleActionPerformed

    private void BtnPRequisicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPRequisicionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPRequisicionActionPerformed

    private void TxtNomProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNomProvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNomProvActionPerformed

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

    private void BtnPRequisicionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnPRequisicionMouseClicked
        altaProveedor();
    }//GEN-LAST:event_BtnPRequisicionMouseClicked

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

    private void altaProveedor() {
        String nombre = TxtNomProv.getText().trim();
        String calle = TxtCalle.getText().trim();
        String rfc = TxtRFC.getText().trim();
        String coloniaNombre = (String) CboxColonias.getSelectedItem();
        String correo = TxtCorreo.getText().trim();
        String telefono = TxtTel.getText().trim();

        // Validación básica
        if (nombre.isEmpty() || calle.isEmpty() || rfc.isEmpty()
                || coloniaNombre == null || correo.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar número telefónico (10 dígitos)
        if (!telefono.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this, "El número telefónico debe contener exactamente 10 dígitos.",
                    "Número inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar correo
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Correo electrónico inválido. Ejemplo: nombre@dominio.com",
                    "Correo inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar RFC (patrón simplificado de 13 caracteres alfanuméricos)
        if (!rfc.matches("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$")) {
            JOptionPane.showMessageDialog(this, "RFC inválido. Debe tener 13 caracteres y seguir el formato oficial.",
                    "RFC inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer coloniaId = coloniasMap.get(coloniaNombre);
        if (coloniaId == null) {
            JOptionPane.showMessageDialog(this, "Colonia no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        int idProveedor = -1;

        try {
            conn = ConectarBD();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar duplicados
            String sqlVerificar = "SELECT 'NOMBRE' FROM proveedores WHERE nombre = ? "
                    + "UNION "
                    + "SELECT 'RFC' FROM proveedores WHERE rfc = ? "
                    + "UNION "
                    + "SELECT 'CORREO' FROM correos_proveedores WHERE correo = ? "
                    + "UNION "
                    + "SELECT 'numero' FROM numeros_proveedores WHERE numero = ?";

            try (PreparedStatement psCheck = conn.prepareStatement(sqlVerificar)) {
                psCheck.setString(1, nombre);
                psCheck.setString(2, rfc);
                psCheck.setString(3, correo);
                psCheck.setString(4, telefono);

                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    String campoRepetido = rs.getString(1);
                    JOptionPane.showMessageDialog(this,
                            "Ya existe un proveedor con el mismo " + campoRepetido.toLowerCase(),
                            "Duplicado",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insertar proveedor
            conn.setAutoCommit(false);
            String sqlProveedor = "{CALL AltaProveedores(?, ?, ?, ?, ?)}";
            try (CallableStatement cstmtProveedor = conn.prepareCall(sqlProveedor)) {
                cstmtProveedor.setString(1, nombre);
                cstmtProveedor.setString(2, calle);
                cstmtProveedor.setString(3, rfc);
                cstmtProveedor.setInt(4, coloniaId);
                cstmtProveedor.registerOutParameter(5, Types.INTEGER);

                cstmtProveedor.execute();
                idProveedor = cstmtProveedor.getInt(5);
            }

            conn.commit();

        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "Error al registrar el proveedor: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        // Insertar correo y teléfono
        if (idProveedor != -1) {
            boolean correoOk = true, telefonoOk = true;

            try (CallableStatement cstmtCorreo = conn.prepareCall("{CALL AltaCorreosProveedores(?, ?)}")) {
                cstmtCorreo.setInt(1, idProveedor);
                cstmtCorreo.setString(2, correo);
                cstmtCorreo.execute();
            } catch (SQLException ex) {
                correoOk = false;
                JOptionPane.showMessageDialog(this, "Proveedor registrado, pero falló el correo: " + ex.getMessage(),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            try (CallableStatement cstmtTelefono = conn.prepareCall("{CALL AltaNumerosProveedores(?, ?)}")) {
                cstmtTelefono.setInt(1, idProveedor);
                cstmtTelefono.setString(2, telefono);
                cstmtTelefono.execute();
            } catch (SQLException ex) {
                telefonoOk = false;
                JOptionPane.showMessageDialog(this, "Proveedor registrado, pero falló el teléfono: " + ex.getMessage(),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

            if (correoOk && telefonoOk) {
                JOptionPane.showMessageDialog(this,
                        "Proveedor registrado exitosamente\nID: " + idProveedor,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Proveedor registrado con advertencias.\nID: " + idProveedor,
                        "Registro parcial",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    private void limpiarCampos() {
        TxtNomProv.setText("");
        TxtCalle.setText("");
        TxtCorreo.setText("");
        TxtRFC.setText("");
        TxtTel.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BtgActividad;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnPRequisicion;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CboxColonias;
    private javax.swing.JLabel LblCalle;
    private javax.swing.JLabel LblCorreo;
    private javax.swing.JLabel LblDireccion;
    private javax.swing.JLabel LblEmpleado;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblRFC;
    private javax.swing.JLabel LblTel;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlNuevoProv;
    private javax.swing.JPanel PnlRequisicionBtn;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtCalle;
    private javax.swing.JTextField TxtCorreo;
    private javax.swing.JTextField TxtNomProv;
    private javax.swing.JTextField TxtRFC;
    private javax.swing.JTextField TxtTel;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
