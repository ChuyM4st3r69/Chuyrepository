package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import metodos.AccesoDenegado;
import metodos.BotonTransparente;
import metodos.InfoFarmaDialog;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;

/**
 *
 * @author Jesus Castillo
 */
public class Facturas extends javax.swing.JFrame {

    private ImageIcon imagen;
    private Icon icono;
    private JPanel panelFacturasContenedor;
    private JScrollPane scrollPaneFacturas;
    private DefaultTableModel modeloFacturas;
    private JPanel panelPrincipalFacturas;

    public Facturas() {
        initComponents();

        inicializarSistemaFacturas();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Buscar facturas...", TxtBuscarFactura);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);

        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");

        // PnlRedondo
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlNuevaFac, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnRequisiciones);
        BotonTransparente.hacerBotonTransparente(BtnProveedores);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnDevoluciones);
        BotonTransparente.hacerBotonTransparente(BtnFacturas);
        BotonTransparente.hacerBotonTransparente(BtnNuevaFac);

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

    private void inicializarSistemaFacturas() {
        // Crear el panel principal
        panelPrincipalFacturas = new JPanel(new BorderLayout());
        panelPrincipalFacturas.setBackground(Color.WHITE);
        panelPrincipalFacturas.setPreferredSize(new Dimension(1164, 453));
        panelPrincipalFacturas.setMaximumSize(new Dimension(1164, 453));

        // Crear panel de título
        JPanel panelTitulo = crearPanelTituloCompacto("Gestión de Facturas Realizadas");

        // Crear panel de búsqueda
        JPanel panelBusqueda = crearPanelBusquedaCompacto();

        // Crear área de contenido horizontal
        crearAreaContenidoHorizontal();

        // Ensamblar todo
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setPreferredSize(new Dimension(1164, 100));
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        panelPrincipalFacturas.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipalFacturas.add(scrollPaneFacturas, BorderLayout.CENTER);

        // Reemplazar el contenido del panel existente
        PnlFacturas.removeAll();
        PnlFacturas.setLayout(new BorderLayout());
        PnlFacturas.add(panelPrincipalFacturas, BorderLayout.CENTER);
        PnlFacturas.revalidate();
        PnlFacturas.repaint();

        // Cargar datos iniciales
        cargarFacturasDesdeBD();
    }

    private void actualizarDimensionesContenedor() {
        int numTarjetas = panelFacturasContenedor.getComponentCount();
        int anchoNecesario = (numTarjetas * 230) + (numTarjetas * 10) + 20; // 230px por tarjeta + espacios

        panelFacturasContenedor.setPreferredSize(new Dimension(anchoNecesario, 300));
        panelFacturasContenedor.revalidate();
        panelFacturasContenedor.repaint();
        scrollPaneFacturas.revalidate();
        scrollPaneFacturas.repaint();
    }

    private JPanel crearPanelErrorHorizontal(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 245, 245));
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel lblError = new JLabel("<html><center>❌<br><br>Error al cargar<br>facturas<br><small>"
                + mensaje + "</small></center></html>");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(new Color(200, 50, 50));
        lblError.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblError, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBusquedaCompacto() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        panel.setPreferredSize(new Dimension(1164, 50));

        // Campo de búsqueda horizontal
        JPanel panelCampo = new JPanel(new BorderLayout(10, 0));
        panelCampo.setOpaque(false);

        // Configurar campo de búsqueda
        if (TxtBuscarFactura == null) {
            TxtBuscarFactura = new javax.swing.JTextField();
        }
        TxtBuscarFactura.setPreferredSize(new Dimension(250, 30));
        TxtBuscarFactura.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Aplicar estilo
        TextPrompt placeHolder = new TextPrompt("Buscar facturas por ID, fecha o empleado...", TxtBuscarFactura);
        RoundTxt redondearTxt = new RoundTxt(20, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscarFactura.setBorder(redondearTxt);
        TxtBuscarFactura.setBackground(Color.WHITE);

        // Botón limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> {
            TxtBuscarFactura.setText("");
            cargarFacturasDesdeBD();
        });

        panelCampo.add(TxtBuscarFactura, BorderLayout.CENTER);
        panelCampo.add(btnLimpiar, BorderLayout.EAST);

        panel.add(panelCampo, BorderLayout.WEST);

        // Configurar eventos de búsqueda
        configurarEventosBusqueda();

        return panel;
    }

    private JPanel crearPanelCargaHorizontal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 200));

        JLabel lblCarga = new JLabel("<html><center>⏳<br>Cargando facturas...</center></html>");
        lblCarga.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblCarga.setForeground(new Color(70, 130, 180));
        lblCarga.setHorizontalAlignment(JLabel.CENTER);

        // Añadir spinner de carga
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel panelCarga = new JPanel(new BorderLayout());
        panelCarga.add(lblCarga, BorderLayout.CENTER);
        panelCarga.add(progressBar, BorderLayout.SOUTH);
        panelCarga.setOpaque(false);

        panel.add(panelCarga, BorderLayout.CENTER);
        return panel;
    }

    private void configurarEventosBusqueda() {
        // Limpiar listeners existentes para evitar duplicados
        java.awt.event.ActionListener[] listeners = TxtBuscarFactura.getActionListeners();
        for (java.awt.event.ActionListener listener : listeners) {
            TxtBuscarFactura.removeActionListener(listener);
        }

        // Agregar nuevo listener
        TxtBuscarFactura.addActionListener(e -> filtrarFacturas());

        // Búsqueda en tiempo real
        TxtBuscarFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            private javax.swing.Timer timer;

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (timer != null) {
                    timer.stop();
                }
                timer = new javax.swing.Timer(500, e -> filtrarFacturas());
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    private JPanel crearPanelSinResultadosHorizontal(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(248, 249, 250));
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel lblMensaje = new JLabel("<html><center>🧾<br><br>" + mensaje + "</center></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMensaje.setForeground(Color.GRAY);
        lblMensaje.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTituloCompacto(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setPreferredSize(new Dimension(1164, 50));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panel.add(lblTitulo, BorderLayout.WEST);

        return panel;
    }

    private void crearAreaContenidoHorizontal() {
        panelFacturasContenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFacturasContenedor.setBackground(Color.WHITE);

        scrollPaneFacturas = new JScrollPane(panelFacturasContenedor);
        scrollPaneFacturas.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneFacturas.setBackground(Color.WHITE);
        scrollPaneFacturas.getViewport().setBackground(Color.WHITE);
        scrollPaneFacturas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneFacturas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneFacturas.getHorizontalScrollBar().setUnitIncrement(16);

        int alturaDisponible = 453 - 100;
        scrollPaneFacturas.setPreferredSize(new Dimension(1164, alturaDisponible));
        scrollPaneFacturas.setMaximumSize(new Dimension(1164, alturaDisponible));

        modeloFacturas = new DefaultTableModel(new Object[]{"ID", "Fecha", "Empleado", "Total", "Productos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void cargarFacturasDesdeBD() {
        panelFacturasContenedor.removeAll();

        JPanel panelCarga = crearPanelCargaHorizontal();
        panelFacturasContenedor.add(panelCarga);
        panelFacturasContenedor.revalidate();
        panelFacturasContenedor.repaint();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql = "SELECT f.idfacturas, f.fecha, e.nombres, e.apellidos, "
                        + "COUNT(df.productos_idproductos) as productos, "
                        + "SUM(df.cantidad * df.precio_unitario) as subtotal "
                        + "FROM facturas f "
                        + "JOIN empleados e ON f.empleados_idempleados = e.idempleados "
                        + "JOIN detalle_facturas df ON f.idfacturas = df.facturas_idfacturas "
                        + "WHERE df.facturado = 1 "
                        + // Solo facturas realizadas
                        "GROUP BY f.idfacturas, f.fecha, e.nombres, e.apellidos "
                        + "ORDER BY f.fecha DESC";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                panelFacturasContenedor.removeAll();
                boolean hayFacturas = false;

                while (rs.next()) {
                    hayFacturas = true;

                    int id = rs.getInt("idfacturas");
                    Date fecha = rs.getTimestamp("fecha");
                    String empleado = rs.getString("nombres") + " " + rs.getString("apellidos");
                    int numProductos = rs.getInt("productos");
                    double subtotal = rs.getDouble("subtotal");
                    double total = subtotal * 1.16; // Aplicar IVA (16%)

                    JPanel tarjeta = crearTarjetaFactura(id, fecha, empleado, numProductos, total);
                    panelFacturasContenedor.add(tarjeta);
                }

                if (!hayFacturas) {
                    JPanel panelVacio = crearPanelSinResultadosHorizontal("No hay facturas realizadas");
                    panelFacturasContenedor.add(panelVacio);
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                System.err.println("Error SQL: " + ex.getMessage());
                panelFacturasContenedor.removeAll();
                JPanel panelError = crearPanelErrorHorizontal(ex.getMessage());
                panelFacturasContenedor.add(panelError);
                panelFacturasContenedor.revalidate();
                panelFacturasContenedor.repaint();
            }
        });
    }

    private JPanel crearTarjetaFactura(int id, Date fecha, String empleado, int numProductos, double total) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(220, 300));

        // Cambiar a GridLayout con más filas para dar más espacio
        JPanel panelInfo = new JPanel(new GridLayout(6, 1, 2, 8));
        panelInfo.setOpaque(false);

        // Formatear fecha y hora
        String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        String horaStr = new SimpleDateFormat("HH:mm").format(fecha);
        String totalStr = String.format("$%,.2f", total);

        // ID
        JLabel lblId = new JLabel("<html><center><br>FACT-" + String.format("%04d", id) + "</center></html>");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(new Color(70, 130, 180));
        lblId.setHorizontalAlignment(JLabel.CENTER);

        // Fecha y hora
        JLabel lblFecha = new JLabel("<html><center>📅 " + fechaStr + "<br>⏰ " + horaStr + "</center></html>");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(100, 100, 100));
        lblFecha.setHorizontalAlignment(JLabel.CENTER);

        // Empleado
        JLabel lblEmpleado = new JLabel("<html><center>👤<br>" + empleado + "</center></html>");
        lblEmpleado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEmpleado.setForeground(new Color(80, 80, 80));
        lblEmpleado.setHorizontalAlignment(JLabel.CENTER);

        // Separar productos y total en dos labels diferentes
        JLabel lblProductos = new JLabel("<html><center>📦 " + numProductos + " producto" + (numProductos != 1 ? "s" : "") + "</center></html>");
        lblProductos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProductos.setForeground(new Color(60, 150, 60));
        lblProductos.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblTotal = new JLabel("<html><center>💰 " + totalStr + "</center></html>");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotal.setForeground(new Color(60, 150, 60));
        lblTotal.setHorizontalAlignment(JLabel.CENTER);

        // Agregar todos los componentes
        panelInfo.add(lblId);
        panelInfo.add(lblFecha);
        panelInfo.add(lblEmpleado);
        panelInfo.add(lblProductos);
        panelInfo.add(lblTotal);
        panelInfo.add(new JLabel()); // Espacio vacío para separación

        // Botón de detalles
        JButton btnDetalles = new JButton("Ver Detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setBackground(new Color(70, 130, 180));
        btnDetalles.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btnDetalles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalles.setFocusPainted(false);
        btnDetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetalles.setBackground(new Color(50, 110, 160));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetalles.setBackground(new Color(70, 130, 180));
            }
        });
        btnDetalles.addActionListener(e -> mostrarDetallesFactura(id));

        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(btnDetalles, BorderLayout.SOUTH);
        return tarjeta;
    }

    private void mostrarDetallesFactura(int idFactura) {
        try (Connection conn = Farmacia.ConectarBD()) {
            String sql = "SELECT p.nombre, df.cantidad, df.precio_unitario, "
                    + "(df.cantidad * df.precio_unitario) as subtotal "
                    + "FROM detalle_facturas df "
                    + "JOIN productos p ON df.productos_idproductos = p.idproductos "
                    + "WHERE df.facturas_idfacturas = ? AND df.facturado = 1";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idFactura);
            ResultSet rs = pst.executeQuery();

            modeloFacturas.setRowCount(0);

            double subtotal = 0;
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio_unitario");
                double itemSubtotal = rs.getDouble("subtotal");
                subtotal += itemSubtotal; // Acumulamos el subtotal

                modeloFacturas.addRow(new Object[]{
                    nombre,
                    cantidad,
                    String.format("$%,.2f", precio),
                    String.format("$%,.2f", itemSubtotal)
                });
            }

            // Calcular IVA (16%)
            double iva = subtotal * 0.16;
            double total = subtotal + iva;

            // Añadir filas de resumen
            modeloFacturas.addRow(new Object[]{"", "", "Subtotal:", String.format("$%,.2f", subtotal)});
            modeloFacturas.addRow(new Object[]{"", "", "IVA (16%):", String.format("$%,.2f", iva)});
            modeloFacturas.addRow(new Object[]{"", "", "TOTAL:", String.format("$%,.2f", total)});

            JTable tabla = new JTable(modeloFacturas);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            // Configurar renderizador para alinear números a la derecha
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            for (int i = 2; i < modeloFacturas.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }

            // Resaltar filas de totales
            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Resaltar filas de totales
                    if (row >= modeloFacturas.getRowCount() - 3) {
                        c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        if (row == modeloFacturas.getRowCount() - 1) { // Última fila (TOTAL)
                            c.setForeground(new Color(0, 100, 0)); // Verde oscuro
                        } else {
                            c.setForeground(Color.BLACK);
                        }
                        c.setBackground(new Color(240, 240, 240)); // Fondo gris claro
                    } else {
                        c.setForeground(Color.BLACK);
                        c.setBackground(Color.WHITE);
                    }
                    return c;
                }
            });

            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setPreferredSize(new Dimension(650, 350));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Detalles de Factura FACT-" + idFactura,
                    JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo para filtrar las facturas
    private void filtrarFacturas() {
        String filtro = TxtBuscarFactura.getText().trim();

        panelFacturasContenedor.removeAll();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql;
                PreparedStatement pst;

                if (filtro.isEmpty()) {
                    cargarFacturasDesdeBD();
                    return;
                } else {
                    sql = "SELECT f.idfacturas, f.fecha, e.nombres, e.apellidos, "
                            + "COUNT(df.productos_idproductos) as productos, "
                            + "SUM(df.cantidad * df.precio_unitario) as subtotal "
                            + "FROM facturas f "
                            + "JOIN empleados e ON f.empleados_idempleados = e.idempleados "
                            + "JOIN detalle_facturas df ON f.idfacturas = df.facturas_idfacturas "
                            + "WHERE df.facturado = 1 AND "
                            + "(CAST(f.idfacturas as CHAR) LIKE ? OR "
                            + "(e.nombres LIKE ? OR e.apellidos LIKE ?) OR "
                            + "DATE_FORMAT(f.fecha, '%d/%m/%Y') LIKE ?) "
                            + "GROUP BY f.idfacturas, f.fecha, e.nombres, e.apellidos "
                            + "ORDER BY f.fecha DESC";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, "%" + filtro + "%");
                    pst.setString(2, "%" + filtro + "%");
                    pst.setString(3, "%" + filtro + "%");
                    pst.setString(4, "%" + filtro + "%");
                }

                ResultSet rs = pst.executeQuery();

                panelFacturasContenedor.removeAll();
                boolean hayResultados = false;

                while (rs.next()) {
                    hayResultados = true;

                    int id = rs.getInt("idfacturas");
                    Date fecha = rs.getTimestamp("fecha");
                    String empleado = rs.getString("nombres") + " " + rs.getString("apellidos");
                    int numProductos = rs.getInt("productos");
                    double subtotal = rs.getDouble("subtotal");
                    double total = subtotal * 1.16;

                    JPanel tarjeta = crearTarjetaFactura(id, fecha, empleado, numProductos, total);
                    panelFacturasContenedor.add(tarjeta);
                }

                if (!hayResultados) {
                    JPanel panelNoResultados = crearPanelSinResultadosHorizontal("No se encontraron facturas para: \"" + filtro + "\"");
                    panelFacturasContenedor.add(panelNoResultados);
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                System.err.println("Error en filtro: " + ex.getMessage());
                panelFacturasContenedor.removeAll();
                JPanel panelError = crearPanelErrorHorizontal(ex.getMessage());
                panelFacturasContenedor.add(panelError);
                actualizarDimensionesContenedor();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        BtnInventario = new javax.swing.JButton();
        BtnVentas = new javax.swing.JButton();
        BtnRequisiciones = new javax.swing.JButton();
        BtnProveedores = new javax.swing.JButton();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnFacturas = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnDevoluciones = new javax.swing.JButton();
        BtnInicio = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        TxtBuscarFactura = new javax.swing.JTextField();
        PnlNuevaFac = new javax.swing.JPanel();
        BtnNuevaFac = new javax.swing.JButton();
        PnlFacturas = new javax.swing.JPanel();

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

        BtnFacturas.setBackground(new java.awt.Color(242, 242, 242));
        BtnFacturas.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnFacturas.setForeground(new java.awt.Color(0, 119, 182));
        BtnFacturas.setText("Facturas");
        BtnFacturas.setBorder(null);
        BtnFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnFacturasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(BtnFacturas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSombraLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        BtnConfig.setBackground(new java.awt.Color(0, 119, 182));
        BtnConfig.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnConfig.setForeground(new java.awt.Color(255, 255, 255));
        BtnConfig.setText("Cerrar sesión");
        BtnConfig.setToolTipText("");
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
                            .addComponent(BtnProveedores)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnVentas)
                            .addComponent(BtnInventario)
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
                .addComponent(BtnProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Gestione las facturación y el control de pagos");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Facturas");

        TxtBuscarFactura.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarFacturaActionPerformed(evt);
            }
        });

        PnlNuevaFac.setBackground(new java.awt.Color(0, 119, 182));

        BtnNuevaFac.setBackground(new java.awt.Color(153, 153, 255));
        BtnNuevaFac.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        BtnNuevaFac.setForeground(new java.awt.Color(255, 255, 255));
        BtnNuevaFac.setText("Nueva factura");
        BtnNuevaFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNuevaFacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlNuevaFacLayout = new javax.swing.GroupLayout(PnlNuevaFac);
        PnlNuevaFac.setLayout(PnlNuevaFacLayout);
        PnlNuevaFacLayout.setHorizontalGroup(
            PnlNuevaFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaFac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlNuevaFacLayout.setVerticalGroup(
            PnlNuevaFacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaFac, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PnlFacturasLayout = new javax.swing.GroupLayout(PnlFacturas);
        PnlFacturas.setLayout(PnlFacturasLayout);
        PnlFacturasLayout.setHorizontalGroup(
            PnlFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1175, Short.MAX_VALUE)
        );
        PnlFacturasLayout.setVerticalGroup(
            PnlFacturasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblTitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PnlNuevaFac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LblTitulo1)
                                .addComponent(PnlFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TxtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(77, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LblTitulo)
                        .addGap(48, 48, 48)
                        .addComponent(TxtBuscarFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PnlFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(PnlNuevaFac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void TxtBuscarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarFacturaActionPerformed

    private void BtnNuevaFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNuevaFacActionPerformed
        NuevaFactura nuevaFac = new NuevaFactura();

        this.setVisible(false);

        nuevaFac.setVisible(true);
        nuevaFac.setLocationRelativeTo(null);
        nuevaFac.setTitle("Nueva factura");
    }//GEN-LAST:event_BtnNuevaFacActionPerformed

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnNuevaFac;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlFacturas;
    private javax.swing.JPanel PnlNuevaFac;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarFactura;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
