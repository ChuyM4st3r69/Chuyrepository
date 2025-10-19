package Interfaces;

import Interfaces.Inicio_sesión.SesionUsuario;
import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import static farmacia.Farmacia.mostrarPuesto;
import java.awt.Color;
import java.awt.HeadlessException;
import metodos.TextPrompt;
import metodos.paintBorder;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import metodos.RoundTxt;
import metodos.RoundPanel;
import metodos.RoundIzqPanel;
import metodos.BotonTransparente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;
import metodos.AccesoDenegado;
//import metodos.GraficaVentasDiarias;
import metodos.ImageHTTPHandler;
import metodos.InfoFarmaDialog;
import metodos.MetricasVentas;
import static metodos.MetricasVentas.formatearEntero;
import static metodos.MetricasVentas.formatearMoneda;
import static metodos.MetricasVentas.formatearVariacionColores;
import static metodos.MetricasVentas.obtenerMetricas;
import metodos.SesionTimer;

/**
 *
 * @author Jesus Castillo
 */
public class Produccion extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;
    public static Produccion instancia;

    public Produccion() {

        initComponents();

        mostrarHoraInicio();

        actualizarGraficaLabel();

        iniciarActualizacionAutomatica();

        //cargarFotoUsuario();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        instancia = this;

        SesionTimer.iniciarTemporizador(LblTimeActivo);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

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

        LblNomAdmin.setText(SesionUsuario.nombreCompleto);
        LblPuesto.setText(SesionUsuario.puesto);
        LblPuestoAdmin.setText(SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);

        // BtnRedondo
        BtnAccInv.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccInv.setBackground(Color.WHITE);
        BtnAccInv.setFocusPainted(false);

        BtnAccVen.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccVen.setBackground(Color.WHITE);
        BtnAccVen.setFocusPainted(false);

        BtnAccFac.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccFac.setBackground(Color.WHITE);
        BtnAccFac.setFocusPainted(false);

        BtnAccProv.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccProv.setBackground(Color.WHITE);
        BtnAccProv.setFocusPainted(false);

        BtnAccReq.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccReq.setBackground(Color.WHITE);
        BtnAccReq.setFocusPainted(false);

        BtnAccRep.setBorder(new paintBorder(4, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnAccRep.setBackground(Color.WHITE);
        BtnAccRep.setFocusPainted(false);

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");
        this.pintarImagen(this.LblInvIcon, "src/icons/InvIcon.png");
        this.pintarImagen(LblVentaIcon, "src/icons/VentaIcon.png");
        this.pintarImagen(LblFacIcon, "src/icons/facturaIcon.png");
        this.pintarImagen(LblProvIcon, "src/icons/ProvIcon.png");
        this.pintarImagen(LblRequisicion, "src/icons/RequisicionIcon.png");
        this.pintarImagen(LblReportIcon, "src/icons/ReportIcon.png");

        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlInventario, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlClientes, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlVentas, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlRecetas, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlReportes, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlProveedores, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlInfo, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlEstadistic, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnCerrarSesion);
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnRequisiciones);
        BotonTransparente.hacerBotonTransparente(BtnProveedores);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnDevoluciones);
        BotonTransparente.hacerBotonTransparente(BtnFacturas);

        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            BtnUsers.setText("Usuarios");
        } else {
            BtnUsers.setText("Ayuda");
        }

        cargarFotoUsuario();
    }

    /**
     * Carga la foto del usuario desde la base de datos
     */
    private void cargarFotoUsuario() {

        int id = Inicio_sesión.SesionUsuario.idEmpleado;
        try {
            String foto = "SELECT ruta FROM fotos_perfil WHERE idfotos_perfil = ? LIMIT 1";

            try (
                    Connection conn = Farmacia.ConectarBD(); PreparedStatement puesto1 = conn.prepareStatement(foto);) {
                puesto1.setInt(1, id);

                try (ResultSet rs = puesto1.executeQuery()) {
                    if (rs.next()) {
                        ImageHTTPHandler handler = new ImageHTTPHandler("http://localhost:8081");

                        String serverPath = rs.getString("ruta");

                        // Recupera la imagen en bytes
                        byte[] imageBytes = handler.obtain(serverPath);

                        // Convierte a BufferedImage
                        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                        // Obtiene el tamaño del JLabel
                        int width = LblFoto.getWidth();
                        int height = LblFoto.getHeight();

                        // Escala la imagen
                        Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                        // Asigna la imagen escalada al JLabel
                        LblFoto.setIcon(new ImageIcon(scaledImage));

                    } else {
                        JOptionPane.showMessageDialog(null, "Algo esta mal");
                    }
                }
            }
        } catch (HeadlessException | IOException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }

    }

    public void actualizarLabels() {
        MetricasVentas.ResultadoMetricas metricas = obtenerMetricas();

        // Ventas
        LblCantVend.setText(formatearMoneda(metricas.ventasHoy));
        LblPerVend.setText(formatearVariacionColores(metricas.ventasHoy, metricas.ventasAyer));

        // Productos
        LblNumProd.setText(formatearEntero(metricas.productosHoy));
        LblPerVent.setText(formatearVariacionColores(metricas.productosHoy, metricas.productosAyer));

        // Transacciones
        LblNumTrans.setText(formatearEntero(metricas.transaccionesHoy));
        LblPerTrans.setText(formatearVariacionColores(metricas.transaccionesHoy, metricas.transaccionesAyer));

        // Ticket promedio
        LblNumCli.setText(formatearMoneda(metricas.ticketPromedioHoy));
        LblPerCli.setText(formatearVariacionColores(metricas.ticketPromedioHoy, metricas.ticketPromedioAyer));
    }

    private void iniciarActualizacionAutomatica() {
        // Actualizar inmediatamente
        actualizarLabels();

        // Configurar actualización cada 5 minutos (300,000 ms)
        Timer timer = new Timer(300000, e -> actualizarLabels());
        timer.start();
    }

    private void actualizarGraficaLabel() {
        //GraficaVentasDiarias.actualizarGraficaEnLabel(LblGrafica);
    }

    public void minimizarVentana() {
        this.setState(JFrame.ICONIFIED);
    }

    public void restaurarVentana() {
        this.setState(JFrame.NORMAL);
        this.toFront();
    }

    public void mostrarHoraInicio() {
        // Formato de 12 horas con AM/PM (ejemplo: 03:45 PM)
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", new Locale("es", "ES"));
        String horaActual = sdf.format(new Date());
        LblTime.setText(horaActual);
    }

    public void validarPuesto(JTextField usuario, JPasswordField password) {
        try {
            String puesto = "SELECT u.tipo_usuario AS Puesto FROM empleados e JOIN usuarios u ON e.idempleados = u.empleados_idempleados WHERE u.clave = ? LIMIT 1";

            try (
                    Connection conn = Farmacia.ConectarBD(); PreparedStatement puesto1 = conn.prepareStatement(puesto);) {
                String contra = String.valueOf(password.getPassword());
                puesto1.setString(1, contra);

                try (ResultSet rs = puesto1.executeQuery()) {
                    if (rs.next()) {
                        mostrarPuesto(LblPuesto, contra);
                        this.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Algo esta mal");
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlInventario = new javax.swing.JPanel();
        BtnAccInv = new javax.swing.JButton();
        LblInvIcon = new javax.swing.JLabel();
        LblInv = new javax.swing.JLabel();
        LblActuInv = new javax.swing.JLabel();
        LblFechaActu = new javax.swing.JLabel();
        LblInfoInv = new javax.swing.JLabel();
        LblInfoInv2 = new javax.swing.JLabel();
        LblInfoInv3 = new javax.swing.JLabel();
        LblInfoInv4 = new javax.swing.JLabel();
        PnlVentas = new javax.swing.JPanel();
        BtnAccVen = new javax.swing.JButton();
        LblVentaIcon = new javax.swing.JLabel();
        LblVenta = new javax.swing.JLabel();
        LblUltVent = new javax.swing.JLabel();
        LblTimeVent = new javax.swing.JLabel();
        LblInfoVenta = new javax.swing.JLabel();
        LblInfoVenta2 = new javax.swing.JLabel();
        LblInfoVenta3 = new javax.swing.JLabel();
        LblInfoVenta4 = new javax.swing.JLabel();
        PnlClientes = new javax.swing.JPanel();
        LblFacIcon = new javax.swing.JLabel();
        BtnAccFac = new javax.swing.JButton();
        LblFacturas = new javax.swing.JLabel();
        LblRegister = new javax.swing.JLabel();
        LblInfoCli = new javax.swing.JLabel();
        LblInfoCli2 = new javax.swing.JLabel();
        LblInfoCli3 = new javax.swing.JLabel();
        PnlRecetas = new javax.swing.JPanel();
        BtnAccReq = new javax.swing.JButton();
        LblRequisicion = new javax.swing.JLabel();
        LblRequisiciones = new javax.swing.JLabel();
        LblInfoRecetas = new javax.swing.JLabel();
        LblInfoRecetas2 = new javax.swing.JLabel();
        LblInfoRecetas3 = new javax.swing.JLabel();
        LblInfoRecetas4 = new javax.swing.JLabel();
        LblUltReporte1 = new javax.swing.JLabel();
        LblFechaReporte1 = new javax.swing.JLabel();
        PnlReportes = new javax.swing.JPanel();
        LblReportIcon = new javax.swing.JLabel();
        BtnAccRep = new javax.swing.JButton();
        LblDev = new javax.swing.JLabel();
        LblUltReporte = new javax.swing.JLabel();
        LblFechaReporte = new javax.swing.JLabel();
        LblInfReport = new javax.swing.JLabel();
        LblInfReport2 = new javax.swing.JLabel();
        LblInfReport3 = new javax.swing.JLabel();
        LblInfReport4 = new javax.swing.JLabel();
        PnlProveedores = new javax.swing.JPanel();
        LblProvIcon = new javax.swing.JLabel();
        BtnAccProv = new javax.swing.JButton();
        LblProv = new javax.swing.JLabel();
        LblPedidos = new javax.swing.JLabel();
        LblInfProv = new javax.swing.JLabel();
        LblInfProv2 = new javax.swing.JLabel();
        LblInfProv3 = new javax.swing.JLabel();
        LblInfProv4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        BtnInventario = new javax.swing.JButton();
        BtnVentas = new javax.swing.JButton();
        BtnRequisiciones = new javax.swing.JButton();
        BtnProveedores = new javax.swing.JButton();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnInicio = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnFacturas = new javax.swing.JButton();
        BtnDevoluciones = new javax.swing.JButton();
        PnlInfo = new javax.swing.JPanel();
        LblSesion = new javax.swing.JLabel();
        LblNomAdmin = new javax.swing.JLabel();
        LblPuestoAdmin = new javax.swing.JLabel();
        LblInicioSe = new javax.swing.JLabel();
        LblTiempoAct = new javax.swing.JLabel();
        LblTime = new javax.swing.JLabel();
        LblTimeActivo = new javax.swing.JLabel();
        BtnCerrarSesion = new javax.swing.JButton();
        LblFoto = new javax.swing.JLabel();
        PnlEstadistic = new javax.swing.JPanel();
        LblEstadist = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        LblVentTot = new javax.swing.JLabel();
        LblCantVend = new javax.swing.JLabel();
        LblPerVent = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        LblTrans = new javax.swing.JLabel();
        LblNumTrans = new javax.swing.JLabel();
        LblPerTrans = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        LblProdVend = new javax.swing.JLabel();
        LblNumProd = new javax.swing.JLabel();
        LblPerVend = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        LblNewCli = new javax.swing.JLabel();
        LblNumCli = new javax.swing.JLabel();
        LblPerCli = new javax.swing.JLabel();
        LblGrafica = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(0, 119, 182));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jLabel18.setForeground(new java.awt.Color(82, 82, 82));
        jLabel18.setText("Productos vendidos");

        jLabel19.setForeground(new java.awt.Color(0, 48, 73));
        jLabel19.setText("432");

        jLabel20.setForeground(new java.awt.Color(22, 163, 74));
        jLabel20.setText("8% vs. ayer");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

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
        LblTitulo.setText("Bienvenido (a) al panel de control. Acceda a todas las herramientas necesarias para la gestión eficiente de su farmacia.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Sistema De Gestión de Farmacias");

        PnlInventario.setBackground(new java.awt.Color(255, 255, 255));

        BtnAccInv.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccInv.setText("Acceder");
        BtnAccInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccInvActionPerformed(evt);
            }
        });

        LblInvIcon.setText("INVE");

        LblInv.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblInv.setForeground(new java.awt.Color(0, 48, 73));
        LblInv.setText("Inventario");

        LblActuInv.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblActuInv.setText("Última actualización:");

        LblFechaActu.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblFechaActu.setText("Hoy, 10:25.");

        LblInfoInv.setText("Gestione el stock de medicamentos");

        LblInfoInv2.setText("y productos, configure alertas de ");

        LblInfoInv3.setText("existencias, bajas y realice pedidos");

        LblInfoInv4.setText("a proveedores.");

        javax.swing.GroupLayout PnlInventarioLayout = new javax.swing.GroupLayout(PnlInventario);
        PnlInventario.setLayout(PnlInventarioLayout);
        PnlInventarioLayout.setHorizontalGroup(
            PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInventarioLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInventarioLayout.createSequentialGroup()
                        .addGroup(PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblFechaActu)
                            .addComponent(LblActuInv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAccInv))
                    .addGroup(PnlInventarioLayout.createSequentialGroup()
                        .addGroup(PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfoInv4)
                            .addComponent(LblInfoInv3)
                            .addGroup(PnlInventarioLayout.createSequentialGroup()
                                .addComponent(LblInvIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblInv))
                            .addComponent(LblInfoInv2)
                            .addComponent(LblInfoInv))
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlInventarioLayout.setVerticalGroup(
            PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblInvIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblInv))
                .addGap(18, 18, 18)
                .addComponent(LblInfoInv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoInv2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoInv3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoInv4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInventarioLayout.createSequentialGroup()
                        .addComponent(BtnAccInv)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInventarioLayout.createSequentialGroup()
                        .addComponent(LblActuInv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblFechaActu)
                        .addGap(5, 5, 5))))
        );

        PnlVentas.setBackground(new java.awt.Color(255, 255, 255));

        BtnAccVen.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccVen.setText("Acceder");
        BtnAccVen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccVenActionPerformed(evt);
            }
        });

        LblVentaIcon.setText("VENTA");

        LblVenta.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblVenta.setForeground(new java.awt.Color(0, 48, 73));
        LblVenta.setText("Ventas");

        LblUltVent.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblUltVent.setText("Última venta:");

        LblTimeVent.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblTimeVent.setText("Hace 5 minutos.");

        LblInfoVenta.setText("Registre ventas, emita facturas,");

        LblInfoVenta2.setText("gestione devoluciones y");

        LblInfoVenta3.setText("consulte el historial de");

        LblInfoVenta4.setText("transacciones por cliente.");

        javax.swing.GroupLayout PnlVentasLayout = new javax.swing.GroupLayout(PnlVentas);
        PnlVentas.setLayout(PnlVentasLayout);
        PnlVentasLayout.setHorizontalGroup(
            PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlVentasLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlVentasLayout.createSequentialGroup()
                        .addGroup(PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblUltVent)
                            .addComponent(LblTimeVent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAccVen))
                    .addGroup(PnlVentasLayout.createSequentialGroup()
                        .addGroup(PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfoVenta4)
                            .addComponent(LblInfoVenta3)
                            .addComponent(LblInfoVenta2)
                            .addComponent(LblInfoVenta)
                            .addGroup(PnlVentasLayout.createSequentialGroup()
                                .addComponent(LblVentaIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblVenta)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlVentasLayout.setVerticalGroup(
            PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblVentaIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblVenta))
                .addGap(18, 18, 18)
                .addComponent(LblInfoVenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoVenta2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoVenta3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoVenta4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAccVen, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlVentasLayout.createSequentialGroup()
                        .addComponent(LblUltVent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblTimeVent)))
                .addContainerGap())
        );

        PnlClientes.setBackground(new java.awt.Color(255, 255, 255));

        LblFacIcon.setText("FACT");

        BtnAccFac.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccFac.setText("Acceder");
        BtnAccFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccFacActionPerformed(evt);
            }
        });

        LblFacturas.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblFacturas.setForeground(new java.awt.Color(0, 48, 73));
        LblFacturas.setText("Facturas");

        LblRegister.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblRegister.setText("05 facturas pendientes.");

        LblInfoCli.setText("Genera facturas electronicas");

        LblInfoCli2.setText("con la información requerida");

        LblInfoCli3.setText("de la venta.");

        javax.swing.GroupLayout PnlClientesLayout = new javax.swing.GroupLayout(PnlClientes);
        PnlClientes.setLayout(PnlClientesLayout);
        PnlClientesLayout.setHorizontalGroup(
            PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlClientesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlClientesLayout.createSequentialGroup()
                        .addComponent(LblRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAccFac))
                    .addGroup(PnlClientesLayout.createSequentialGroup()
                        .addGroup(PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfoCli3)
                            .addComponent(LblInfoCli2)
                            .addComponent(LblInfoCli)
                            .addGroup(PnlClientesLayout.createSequentialGroup()
                                .addComponent(LblFacIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblFacturas)))
                        .addGap(0, 77, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlClientesLayout.setVerticalGroup(
            PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblFacIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblFacturas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblInfoCli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoCli2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoCli3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAccFac, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LblRegister, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        PnlRecetas.setBackground(new java.awt.Color(255, 255, 255));

        BtnAccReq.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccReq.setText("Acceder");
        BtnAccReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccReqActionPerformed(evt);
            }
        });

        LblRequisicion.setText("REQUI");

        LblRequisiciones.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRequisiciones.setForeground(new java.awt.Color(0, 48, 73));
        LblRequisiciones.setText("Requisiciones");

        LblInfoRecetas.setText("Solicitar medicamentos que se");

        LblInfoRecetas2.setText("necesitan restablecer, ya sea");

        LblInfoRecetas3.setText("por baja existencia, próxima");

        LblInfoRecetas4.setText("caduciadad o alta demanda.");

        LblUltReporte1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblUltReporte1.setText("Última requisición:");

        LblFechaReporte1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblFechaReporte1.setText("Ayer, 18:30.");

        javax.swing.GroupLayout PnlRecetasLayout = new javax.swing.GroupLayout(PnlRecetas);
        PnlRecetas.setLayout(PnlRecetasLayout);
        PnlRecetasLayout.setHorizontalGroup(
            PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlRecetasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRecetasLayout.createSequentialGroup()
                        .addGroup(PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblFechaReporte1)
                            .addComponent(LblUltReporte1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAccReq))
                    .addGroup(PnlRecetasLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfoRecetas)
                            .addGroup(PnlRecetasLayout.createSequentialGroup()
                                .addComponent(LblRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblRequisiciones))
                            .addComponent(LblInfoRecetas2)
                            .addComponent(LblInfoRecetas3)
                            .addComponent(LblInfoRecetas4))
                        .addGap(0, 40, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlRecetasLayout.setVerticalGroup(
            PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRecetasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblRequisiciones))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LblInfoRecetas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoRecetas2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoRecetas3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfoRecetas4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(PnlRecetasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAccReq, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRecetasLayout.createSequentialGroup()
                        .addComponent(LblUltReporte1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblFechaReporte1)))
                .addContainerGap())
        );

        PnlReportes.setBackground(new java.awt.Color(255, 255, 255));

        LblReportIcon.setText("DEV");

        BtnAccRep.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccRep.setText("Acceder");
        BtnAccRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccRepActionPerformed(evt);
            }
        });

        LblDev.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblDev.setForeground(new java.awt.Color(0, 48, 73));
        LblDev.setText("Devoluciones");

        LblUltReporte.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblUltReporte.setText("Última devolución:");

        LblFechaReporte.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblFechaReporte.setText("Hace 5 días.");

        LblInfReport.setText("Genere informe de ventas,");

        LblInfReport2.setText("inventario, rentabilidad y");

        LblInfReport3.setText("comportamiento de clientes");

        LblInfReport4.setText("para toma de decisiones.");

        javax.swing.GroupLayout PnlReportesLayout = new javax.swing.GroupLayout(PnlReportes);
        PnlReportes.setLayout(PnlReportesLayout);
        PnlReportesLayout.setHorizontalGroup(
            PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlReportesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlReportesLayout.createSequentialGroup()
                        .addGroup(PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblFechaReporte)
                            .addComponent(LblUltReporte))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAccRep))
                    .addGroup(PnlReportesLayout.createSequentialGroup()
                        .addGroup(PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfReport4)
                            .addComponent(LblInfReport3)
                            .addComponent(LblInfReport2)
                            .addComponent(LblInfReport)
                            .addGroup(PnlReportesLayout.createSequentialGroup()
                                .addComponent(LblReportIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblDev)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlReportesLayout.setVerticalGroup(
            PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblReportIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblDev))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfReport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfReport2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfReport3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfReport4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(PnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAccRep, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlReportesLayout.createSequentialGroup()
                        .addComponent(LblUltReporte)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblFechaReporte)))
                .addContainerGap())
        );

        PnlProveedores.setBackground(new java.awt.Color(255, 255, 255));

        LblProvIcon.setText("PROV");

        BtnAccProv.setForeground(new java.awt.Color(0, 119, 182));
        BtnAccProv.setText("Acceder");
        BtnAccProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAccProvActionPerformed(evt);
            }
        });

        LblProv.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblProv.setForeground(new java.awt.Color(0, 48, 73));
        LblProv.setText("Proveedores");

        LblPedidos.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        LblPedidos.setText("03 pedidos en tránsito.");

        LblInfProv.setText("Administrar relaciones con");

        LblInfProv2.setText("proveedores, realice pedidos y");

        LblInfProv3.setText("controle entregas de productos");

        LblInfProv4.setText("farmacéutico.");

        javax.swing.GroupLayout PnlProveedoresLayout = new javax.swing.GroupLayout(PnlProveedores);
        PnlProveedores.setLayout(PnlProveedoresLayout);
        PnlProveedoresLayout.setHorizontalGroup(
            PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlProveedoresLayout.createSequentialGroup()
                        .addComponent(LblPedidos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(BtnAccProv))
                    .addGroup(PnlProveedoresLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblInfProv)
                            .addGroup(PnlProveedoresLayout.createSequentialGroup()
                                .addComponent(LblProvIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(LblProv))
                            .addComponent(LblInfProv2)
                            .addComponent(LblInfProv3)
                            .addComponent(LblInfProv4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PnlProveedoresLayout.setVerticalGroup(
            PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblProvIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblProv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfProv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfProv2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfProv3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblInfProv4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PnlProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnAccProv)
                    .addComponent(LblPedidos))
                .addContainerGap())
        );

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

        LblFarmacia.setBackground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblFarmacia.setForeground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setText("FarmaCode");

        BtnInicio.setBackground(new java.awt.Color(242, 242, 242));
        BtnInicio.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnInicio.setForeground(new java.awt.Color(0, 119, 182));
        BtnInicio.setText("Inicio");
        BtnInicio.setBorder(null);
        BtnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInicioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSombraLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnProveedores)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnVentas)
                            .addComponent(BtnInventario)
                            .addComponent(BtnFacturas)
                            .addComponent(BtnDevoluciones)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(LblFarmacia))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addGap(81, 81, 81)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

        PnlInfo.setBackground(new java.awt.Color(255, 255, 255));

        LblSesion.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        LblSesion.setForeground(new java.awt.Color(9, 56, 80));
        LblSesion.setText("Información de sesión");

        LblNomAdmin.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        LblNomAdmin.setForeground(new java.awt.Color(9, 56, 80));
        LblNomAdmin.setText("Ángel Barrón");

        LblPuestoAdmin.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        LblPuestoAdmin.setText("Administrador");

        LblInicioSe.setForeground(new java.awt.Color(126, 127, 129));
        LblInicioSe.setText("Inicio de sesión:");

        LblTiempoAct.setForeground(new java.awt.Color(126, 127, 129));
        LblTiempoAct.setText("Tiempo activo:");

        LblTime.setText("Hoy, 10:33");

        BtnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        BtnCerrarSesion.setText("Cerrar Sesión");
        BtnCerrarSesion.setBorder(null);
        BtnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCerrarSesionActionPerformed(evt);
            }
        });

        LblFoto.setText("FOTO");

        javax.swing.GroupLayout PnlInfoLayout = new javax.swing.GroupLayout(PnlInfo);
        PnlInfo.setLayout(PnlInfoLayout);
        PnlInfoLayout.setHorizontalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInfoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addComponent(LblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblPuestoAdmin)
                            .addComponent(LblNomAdmin))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlInfoLayout.createSequentialGroup()
                                .addComponent(LblSesion)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInfoLayout.createSequentialGroup()
                                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblInicioSe)
                                    .addComponent(LblTiempoAct))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LblTimeActivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(20, 20, 20))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlInfoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnCerrarSesion)
                .addContainerGap())
        );
        PnlInfoLayout.setVerticalGroup(
            PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlInfoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(LblSesion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlInfoLayout.createSequentialGroup()
                        .addComponent(LblNomAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LblPuestoAdmin))
                    .addComponent(LblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblInicioSe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LblTiempoAct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblTimeActivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnCerrarSesion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnlEstadistic.setBackground(new java.awt.Color(255, 255, 255));

        LblEstadist.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        LblEstadist.setForeground(new java.awt.Color(9, 56, 80));
        LblEstadist.setText("Estadísticas del día");

        jPanel2.setBackground(new java.awt.Color(0, 119, 182));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        LblVentTot.setForeground(new java.awt.Color(82, 82, 82));
        LblVentTot.setText("Ventas totales");

        LblCantVend.setForeground(new java.awt.Color(0, 48, 73));
        LblCantVend.setText("$24,856.50");

        LblPerVent.setForeground(new java.awt.Color(0, 0, 0));
        LblPerVent.setText("12% vs. ayer");

        jPanel3.setBackground(new java.awt.Color(0, 119, 182));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        LblTrans.setForeground(new java.awt.Color(82, 82, 82));
        LblTrans.setText("Transacciones");

        LblNumTrans.setForeground(new java.awt.Color(0, 48, 73));
        LblNumTrans.setText("180");

        LblPerTrans.setForeground(new java.awt.Color(0, 0, 0));
        LblPerTrans.setText("5% vs. ayer");

        jPanel4.setBackground(new java.awt.Color(0, 119, 182));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        LblProdVend.setForeground(new java.awt.Color(82, 82, 82));
        LblProdVend.setText("Productos vendidos");

        LblNumProd.setForeground(new java.awt.Color(0, 48, 73));
        LblNumProd.setText("432");

        LblPerVend.setForeground(new java.awt.Color(0, 0, 0));
        LblPerVend.setText("8% vs. ayer");

        jPanel6.setBackground(new java.awt.Color(0, 119, 182));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        LblNewCli.setForeground(new java.awt.Color(82, 82, 82));
        LblNewCli.setText("Nuevos Clientes");

        LblNumCli.setForeground(new java.awt.Color(0, 48, 73));
        LblNumCli.setText("14");

        LblPerCli.setForeground(new java.awt.Color(0, 0, 0));
        LblPerCli.setText("3% vs. ayer");

        javax.swing.GroupLayout PnlEstadisticLayout = new javax.swing.GroupLayout(PnlEstadistic);
        PnlEstadistic.setLayout(PnlEstadisticLayout);
        PnlEstadisticLayout.setHorizontalGroup(
            PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlEstadisticLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblVentTot)
                            .addComponent(LblCantVend)
                            .addComponent(LblPerVent)))
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblProdVend)
                            .addComponent(LblNumProd)
                            .addComponent(LblPerVend)))
                    .addComponent(LblEstadist))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblTrans)
                            .addComponent(LblNumTrans)
                            .addComponent(LblPerTrans)))
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblNewCli)
                            .addComponent(LblNumCli)
                            .addComponent(LblPerCli))))
                .addGap(28, 28, 28))
            .addComponent(LblGrafica, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlEstadisticLayout.setVerticalGroup(
            PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlEstadisticLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblEstadist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(LblVentTot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblCantVend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblPerVent))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(LblTrans)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblNumTrans)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblPerTrans))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlEstadisticLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(LblProdVend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblNumProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblPerVend))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlEstadisticLayout.createSequentialGroup()
                        .addComponent(LblNewCli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblNumCli)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblPerCli))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblGrafica, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PnlEstadistic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PnlInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PnlVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PnlProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PnlReportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PnlRecetas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(47, 47, 47))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblTitulo)
                                    .addComponent(LblTitulo1))
                                .addGap(0, 127, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlEstadistic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PnlInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PnlVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PnlRecetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PnlProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PnlReportes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 844, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInventarioActionPerformed
        Inventario inv = new Inventario();

        this.setState(JFrame.ICONIFIED);

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
        try {
            Farmacia.registrarSalida(idEmpleado);

            Inicio_sesión inicio = new Inicio_sesión();

            this.dispose();

            inicio.setVisible(true);
            inicio.setLocationRelativeTo(null);
            inicio.setTitle("Inicio de sesión");

        } catch (SQLException ex) {
            System.getLogger(ModificarProducto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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

    private void BtnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCerrarSesionActionPerformed
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
    }//GEN-LAST:event_BtnCerrarSesionActionPerformed

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
        Produccion prod = new Produccion();

        this.setState(JFrame.ICONIFIED);

        prod.setVisible(true);
        prod.setLocationRelativeTo(null);
        prod.setTitle("Producción");
    }//GEN-LAST:event_BtnInicioActionPerformed

    private void BtnAccInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccInvActionPerformed
        Inventario inv = new Inventario();

        this.setState(JFrame.ICONIFIED);

        inv.setVisible(true);
        inv.setLocationRelativeTo(null);
        inv.setTitle("Inventario");
    }//GEN-LAST:event_BtnAccInvActionPerformed

    private void BtnAccVenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccVenActionPerformed
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
    }//GEN-LAST:event_BtnAccVenActionPerformed

    private void BtnAccRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccRepActionPerformed
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
    }//GEN-LAST:event_BtnAccRepActionPerformed

    private void BtnAccReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccReqActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Requisiciones req = new Requisiciones();

            this.setState(JFrame.ICONIFIED);

            req.setVisible(true);
            req.setLocationRelativeTo(null);
            req.setTitle("Requisiciones");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }
    }//GEN-LAST:event_BtnAccReqActionPerformed

    private void BtnAccFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccFacActionPerformed
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
    }//GEN-LAST:event_BtnAccFacActionPerformed

    private void BtnAccProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAccProvActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Proveedores prov = new Proveedores();

            this.setState(JFrame.ICONIFIED);

            prov.setVisible(true);
            prov.setLocationRelativeTo(null);
            prov.setTitle("Proveedores");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }
    }//GEN-LAST:event_BtnAccProvActionPerformed

    private void BtnConfigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnConfigMouseClicked
        try {
            Farmacia.registrarSalida(idEmpleado);

            Inicio_sesión inicio = new Inicio_sesión();

            this.dispose();

            inicio.setVisible(true);
            inicio.setLocationRelativeTo(null);
            inicio.setTitle("Inicio de sesión");

        } catch (SQLException ex) {
            System.getLogger(ModificarProducto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAccFac;
    private javax.swing.JButton BtnAccInv;
    private javax.swing.JButton BtnAccProv;
    private javax.swing.JButton BtnAccRep;
    private javax.swing.JButton BtnAccReq;
    private javax.swing.JButton BtnAccVen;
    private javax.swing.JButton BtnCerrarSesion;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblActuInv;
    private javax.swing.JLabel LblCantVend;
    private javax.swing.JLabel LblDev;
    private javax.swing.JLabel LblEstadist;
    private javax.swing.JLabel LblFacIcon;
    private javax.swing.JLabel LblFacturas;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblFechaActu;
    private javax.swing.JLabel LblFechaReporte;
    private javax.swing.JLabel LblFechaReporte1;
    public javax.swing.JLabel LblFoto;
    private javax.swing.JLabel LblGrafica;
    private javax.swing.JLabel LblInfProv;
    private javax.swing.JLabel LblInfProv2;
    private javax.swing.JLabel LblInfProv3;
    private javax.swing.JLabel LblInfProv4;
    private javax.swing.JLabel LblInfReport;
    private javax.swing.JLabel LblInfReport2;
    private javax.swing.JLabel LblInfReport3;
    private javax.swing.JLabel LblInfReport4;
    private javax.swing.JLabel LblInfoCli;
    private javax.swing.JLabel LblInfoCli2;
    private javax.swing.JLabel LblInfoCli3;
    private javax.swing.JLabel LblInfoInv;
    private javax.swing.JLabel LblInfoInv2;
    private javax.swing.JLabel LblInfoInv3;
    private javax.swing.JLabel LblInfoInv4;
    private javax.swing.JLabel LblInfoRecetas;
    private javax.swing.JLabel LblInfoRecetas2;
    private javax.swing.JLabel LblInfoRecetas3;
    private javax.swing.JLabel LblInfoRecetas4;
    private javax.swing.JLabel LblInfoVenta;
    private javax.swing.JLabel LblInfoVenta2;
    private javax.swing.JLabel LblInfoVenta3;
    private javax.swing.JLabel LblInfoVenta4;
    private javax.swing.JLabel LblInicioSe;
    private javax.swing.JLabel LblInv;
    private javax.swing.JLabel LblInvIcon;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblNewCli;
    private javax.swing.JLabel LblNomAdmin;
    private javax.swing.JLabel LblNumCli;
    private javax.swing.JLabel LblNumProd;
    private javax.swing.JLabel LblNumTrans;
    private javax.swing.JLabel LblPedidos;
    private javax.swing.JLabel LblPerCli;
    private javax.swing.JLabel LblPerTrans;
    private javax.swing.JLabel LblPerVend;
    private javax.swing.JLabel LblPerVent;
    private javax.swing.JLabel LblProdVend;
    private javax.swing.JLabel LblProv;
    private javax.swing.JLabel LblProvIcon;
    public static javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblPuestoAdmin;
    private javax.swing.JLabel LblRegister;
    private javax.swing.JLabel LblReportIcon;
    private javax.swing.JLabel LblRequisicion;
    private javax.swing.JLabel LblRequisiciones;
    private javax.swing.JLabel LblSesion;
    private javax.swing.JLabel LblTiempoAct;
    public javax.swing.JLabel LblTime;
    private javax.swing.JLabel LblTimeActivo;
    private javax.swing.JLabel LblTimeVent;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblTrans;
    private javax.swing.JLabel LblUltReporte;
    private javax.swing.JLabel LblUltReporte1;
    private javax.swing.JLabel LblUltVent;
    private javax.swing.JLabel LblVentTot;
    private javax.swing.JLabel LblVenta;
    private javax.swing.JLabel LblVentaIcon;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlClientes;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlEstadistic;
    private javax.swing.JPanel PnlInfo;
    private javax.swing.JPanel PnlInventario;
    private javax.swing.JPanel PnlProveedores;
    private javax.swing.JPanel PnlRecetas;
    private javax.swing.JPanel PnlReportes;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JPanel PnlVentas;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables
}
