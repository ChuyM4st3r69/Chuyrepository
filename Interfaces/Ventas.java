package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;

/**
 *
 * @author Jesus Castillo
 */
public class Ventas extends javax.swing.JFrame {

    private JPanel panelVentasContenedor;
    private JScrollPane scrollPaneVentas;

    /**
     * Creates new form Ventas
     */
    public Ventas() {
        initComponents();

        actualizarTotalVentas();

        inicializarSistemaVentas();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // Actualizar cada 5 minutos (300,000 milisegundos)
        Timer timer = new Timer(300000, e -> actualizarTotalVentas());
        timer.start();

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnDevoluciones1);
        BotonTransparente.hacerBotonTransparente(BtnFacturas1);
        BotonTransparente.hacerBotonTransparente(BtnRequisiciones1);
        BotonTransparente.hacerBotonTransparente(BtnProveedores1);
        BotonTransparente.hacerBotonTransparente(BtnNuevaVenta);

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlVentasDia, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlNewVenta, 15, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        
        // ImgAjustable
        this.pintarImagen(LblLogo, "src/icons/Logo.png");
        
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
    
    private ImageIcon imagen;
    private ImageIcon icono;

    private void pintarImagen(JLabel lbl, String ruta) {
        // Crea un ImageIcon a partir de la ruta de la imagen
        this.imagen = new ImageIcon(ruta);

        // Escala la imagen al tamaño del JLabel manteniendo las proporciones
        this.icono = new ImageIcon(
                this.imagen.getImage().getScaledInstance(
                        lbl.getWidth(),     // Ancho deseado = ancho del JLabel
                        lbl.getHeight(),    // Alto deseado = alto del JLabel
                        Image.SCALE_DEFAULT // Algoritmo de escalado por defecto
                )
        );

        // Establece el icono escalado en el JLabel
        lbl.setIcon(this.icono);

        // Fuerza el repintado del componente para mostrar los cambios
        this.repaint();

    }

    private void inicializarSistemaVentas() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setPreferredSize(new Dimension(1164, 453));

        // Panel de título
        JPanel panelTitulo = crearPanelTitulo("Gestión de Ventas");

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();

        // Configurar área de contenido con scroll horizontal
        panelVentasContenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelVentasContenedor.setBackground(Color.WHITE);

        scrollPaneVentas = new JScrollPane(panelVentasContenedor);
        scrollPaneVentas.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneVentas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneVentas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneVentas.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPaneVentas.setPreferredSize(new Dimension(1164, 400));

        // Ensamblar componentes
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPaneVentas, BorderLayout.CENTER);

        // Reemplazar contenido del panel existente
        PnlVentas.removeAll();
        PnlVentas.setLayout(new BorderLayout());
        PnlVentas.add(panelPrincipal, BorderLayout.CENTER);
        PnlVentas.revalidate();
        PnlVentas.repaint();

        // Cargar datos iniciales
        cargarVentasDesdeBD();
    }

    private JPanel crearPanelTitulo(String titulo) {
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

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Campo de búsqueda
        JPanel panelCampo = new JPanel(new BorderLayout(10, 0));
        panelCampo.setOpaque(false);

        // Configurar campo de búsqueda más pequeño
        if (TxtBuscarVentas == null) {
            TxtBuscarVentas = new javax.swing.JTextField();
        }
        TxtBuscarVentas.setPreferredSize(new Dimension(250, 35));
        TxtBuscarVentas.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Aplicar estilo
        TextPrompt placeHolder = new TextPrompt("Buscar ventas por ID...", TxtBuscarVentas);
        RoundTxt redondearTxt = new RoundTxt(20, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscarVentas.setBorder(redondearTxt);
        TxtBuscarVentas.setBackground(Color.WHITE);

        // Botón limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());

        panelCampo.add(TxtBuscarVentas, BorderLayout.CENTER);
        panelCampo.add(btnLimpiar, BorderLayout.EAST);

        panel.add(panelCampo, BorderLayout.WEST);

        // Configurar eventos de búsqueda
        TxtBuscarVentas.addActionListener(e -> filtrarVentas());
        TxtBuscarVentas.addKeyListener(new java.awt.event.KeyAdapter() {
            private Timer timer;

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (timer != null) {
                    timer.stop();
                }
                timer = new Timer(500, e -> filtrarVentas());
                timer.setRepeats(false);
                timer.start();
            }
        });

        return panel;
    }

    private void cargarVentasDesdeBD() {
        panelVentasContenedor.removeAll();

        // Mostrar panel de carga mientras se obtienen los datos
        JPanel panelCarga = crearPanelMensaje("⏳", "Cargando ventas...");
        panelVentasContenedor.add(panelCarga);
        panelVentasContenedor.revalidate();
        panelVentasContenedor.repaint();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql = "SELECT f.idfacturas, f.fecha, "
                        + "(SELECT SUM(cantidad) FROM detalle_facturas WHERE facturas_idfacturas = f.idfacturas) as articulos, "
                        + "CalcularTotalFactura(f.idfacturas, 16) as total, "
                        + "CONCAT(e.nombres, ' ', e.apellidos) as vendedor "
                        + "FROM facturas f "
                        + "JOIN empleados e ON f.empleados_idempleados = e.idempleados "
                        + "ORDER BY f.fecha DESC, f.idfacturas DESC";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                panelVentasContenedor.removeAll();
                boolean hayVentas = false;
                DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

                while (rs.next()) {
                    hayVentas = true;
                    int id = rs.getInt("idfacturas");
                    Date fecha = rs.getTimestamp("fecha");
                    int articulos = rs.getInt("articulos");
                    double total = rs.getDouble("total");
                    String vendedor = rs.getString("vendedor");

                    JPanel tarjeta = crearTarjetaVenta(id, fecha, articulos, total, vendedor, formatoMoneda);
                    panelVentasContenedor.add(tarjeta);
                }

                if (!hayVentas) {
                    panelVentasContenedor.add(crearPanelMensaje("📋", "No hay ventas registradas"));
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                panelVentasContenedor.removeAll();
                panelVentasContenedor.add(crearPanelMensaje("❌", "Error al cargar ventas"));
                actualizarDimensionesContenedor();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JPanel crearTarjetaVenta(int id, Date fecha, int articulos, double total, String vendedor, DecimalFormat formatoMoneda) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(220, 280));

        // Panel de información
        JPanel panelInfo = new JPanel(new GridLayout(5, 1, 0, 8));
        panelInfo.setOpaque(false);

        // Formatear fecha
        String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        String horaStr = new SimpleDateFormat("HH:mm").format(fecha);

        // Componentes de la tarjeta
        JLabel lblId = new JLabel("<html><center>🛒<br>VENTA-" + String.format("%04d", id) + "</center></html>");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(new Color(70, 130, 180));
        lblId.setHorizontalAlignment(JLabel.CENTER);
        lblId.setVerticalAlignment(JLabel.CENTER);

        JLabel lblFecha = new JLabel("<html><center>📅 " + fechaStr + "<br>🕒 " + horaStr + "</center></html>");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setHorizontalAlignment(JLabel.CENTER);
        lblFecha.setVerticalAlignment(JLabel.CENTER);

        JLabel lblArticulos = new JLabel("<html><center>📦 " + articulos + " artículos</center></html>");
        lblArticulos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblArticulos.setHorizontalAlignment(JLabel.CENTER);
        lblArticulos.setVerticalAlignment(JLabel.CENTER);

        JLabel lblTotal = new JLabel("<html><center>💰 " + formatoMoneda.format(total) + "</center></html>");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotal.setForeground(new Color(0, 100, 0));
        lblTotal.setHorizontalAlignment(JLabel.CENTER);
        lblTotal.setVerticalAlignment(JLabel.CENTER);

        JLabel lblVendedor = new JLabel("<html><center>👤 " + vendedor + "</center></html>");
        lblVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVendedor.setHorizontalAlignment(JLabel.CENTER);
        lblVendedor.setVerticalAlignment(JLabel.CENTER);

        // Botón de detalles
        JButton btnDetalles = new JButton("Ver Detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setBackground(new Color(70, 130, 180));
        btnDetalles.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btnDetalles.addActionListener(e -> mostrarDetallesVenta(id));

        // Efecto hover
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

        // Ensamblar tarjeta
        panelInfo.add(lblId);
        panelInfo.add(lblFecha);
        panelInfo.add(lblArticulos);
        panelInfo.add(lblTotal);
        panelInfo.add(lblVendedor);

        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(btnDetalles, BorderLayout.SOUTH);

        return tarjeta;
    }

    private JPanel crearPanelMensaje(String icono, String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(248, 249, 250));
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel lblMensaje = new JLabel("<html><center>" + icono + "<br><br>" + mensaje + "</center></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarDimensionesContenedor() {
        int numTarjetas = panelVentasContenedor.getComponentCount();
        int anchoNecesario = (numTarjetas * 230) + (numTarjetas * 15) + 20;
        panelVentasContenedor.setPreferredSize(new Dimension(anchoNecesario, 300));
        panelVentasContenedor.revalidate();
        panelVentasContenedor.repaint();
    }

    private void mostrarDetallesVenta(int idVenta) {
        try (Connection conn = Farmacia.ConectarBD()) {
            // Consulta para obtener los detalles principales de la venta
            String sqlVenta = "SELECT f.idfacturas, f.fecha, "
                    + "CONCAT(e.nombres, ' ', e.apellidos) as vendedor, "
                    + "CalcularTotalFactura(f.idfacturas, 16) as total "
                    + "FROM facturas f "
                    + "JOIN empleados e ON f.empleados_idempleados = e.idempleados "
                    + "WHERE f.idfacturas = ?";

            // Consulta para obtener los productos vendidos
            String sqlProductos = "SELECT p.nombre, df.cantidad, df.precio_unitario, "
                    + "(df.cantidad * df.precio_unitario) as subtotal "
                    + "FROM detalle_facturas df "
                    + "JOIN productos p ON df.productos_idproductos = p.idproductos "
                    + "WHERE df.facturas_idfacturas = ?";

            // Obtener datos principales de la venta
            PreparedStatement pstVenta = conn.prepareStatement(sqlVenta);
            pstVenta.setInt(1, idVenta);
            ResultSet rsVenta = pstVenta.executeQuery();

            if (!rsVenta.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró la venta especificada",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Formatear datos de la venta
            Date fechaVenta = rsVenta.getTimestamp("fecha");
            String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
            String horaStr = new SimpleDateFormat("HH:mm:ss").format(fechaVenta);
            String vendedor = rsVenta.getString("vendedor");
            double total = rsVenta.getDouble("total");

            // Obtener productos vendidos
            PreparedStatement pstProductos = conn.prepareStatement(sqlProductos);
            pstProductos.setInt(1, idVenta);
            ResultSet rsProductos = pstProductos.executeQuery();

            // Crear modelo de tabla para los productos
            DefaultTableModel modeloTabla = new DefaultTableModel(
                    new Object[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

            while (rsProductos.next()) {
                modeloTabla.addRow(new Object[]{
                    rsProductos.getString("nombre"),
                    rsProductos.getDouble("cantidad"),
                    formatoMoneda.format(rsProductos.getDouble("precio_unitario")),
                    formatoMoneda.format(rsProductos.getDouble("subtotal"))
                });
            }

            // Crear la tabla de productos
            JTable tablaProductos = new JTable(modeloTabla);
            tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            // Configurar el renderizado de las columnas
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            // Aplicar alineación derecha a las columnas numéricas
            for (int i = 1; i < tablaProductos.getColumnCount(); i++) {
                tablaProductos.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }

            // Crear panel principal para el diálogo
            JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Panel de información de la venta
            JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
            panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Venta"));

            panelInfo.add(new JLabel("Venta #: VENTA-" + String.format("%04d", idVenta)));
            panelInfo.add(new JLabel("Fecha: " + fechaStr));
            panelInfo.add(new JLabel("Hora: " + horaStr));
            panelInfo.add(new JLabel("Vendedor: " + vendedor));
            panelInfo.add(new JLabel("Total: " + formatoMoneda.format(total)));

            // Panel de productos
            JPanel panelProductos = new JPanel(new BorderLayout());
            panelProductos.setBorder(BorderFactory.createTitledBorder("Productos Vendidos"));
            panelProductos.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

            // Ensamblar el diálogo
            panelPrincipal.add(panelInfo, BorderLayout.NORTH);
            panelPrincipal.add(panelProductos, BorderLayout.CENTER);

            // Mostrar el diálogo
            JOptionPane.showMessageDialog(
                    this,
                    panelPrincipal,
                    "Detalles de Venta VENTA-" + idVenta,
                    JOptionPane.PLAIN_MESSAGE
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar detalles de la venta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void filtrarVentas() {
        String filtro = TxtBuscarVentas.getText().trim();
        panelVentasContenedor.removeAll();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql;
                PreparedStatement pst;

                if (filtro.isEmpty()) {
                    cargarVentasDesdeBD();
                    return;
                }

                sql = "SELECT f.idfacturas, f.fecha, "
                        + "(SELECT SUM(cantidad) FROM detalle_facturas WHERE facturas_idfacturas = f.idfacturas) as articulos, "
                        + "CalcularTotalFactura(f.idfacturas, 16) as total, "
                        + "CONCAT(e.nombres, ' ', e.apellidos) as vendedor "
                        + "FROM facturas f "
                        + "JOIN empleados e ON f.empleados_idempleados = e.idempleados "
                        + "WHERE CAST(f.idfacturas as CHAR) LIKE ? "
                        + "ORDER BY f.fecha DESC";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + filtro + "%");
                ResultSet rs = pst.executeQuery();

                boolean hayResultados = false;
                DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

                while (rs.next()) {
                    hayResultados = true;
                    int id = rs.getInt("idfacturas");
                    Date fecha = rs.getTimestamp("fecha");
                    int articulos = rs.getInt("articulos");
                    double total = rs.getDouble("total");
                    String vendedor = rs.getString("vendedor");

                    JPanel tarjeta = crearTarjetaVenta(id, fecha, articulos, total, vendedor, formatoMoneda);
                    panelVentasContenedor.add(tarjeta);
                }

                if (!hayResultados) {
                    JPanel panelNoResultados = crearPanelMensaje("🔍", "No se encontraron ventas para: \"" + filtro + "\"");
                    panelVentasContenedor.add(panelNoResultados);
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                panelVentasContenedor.add(crearPanelMensaje("❌", "Error al buscar ventas"));
                actualizarDimensionesContenedor();
            }
        });
    }

    private void limpiarBusqueda() {
        TxtBuscarVentas.setText("");
        cargarVentasDesdeBD();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlBarraL = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnVentas = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnInicio = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        BtnRequisiciones1 = new javax.swing.JButton();
        BtnProveedores1 = new javax.swing.JButton();
        BtnFacturas1 = new javax.swing.JButton();
        BtnDevoluciones1 = new javax.swing.JButton();
        BtnInventario = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblVentas = new javax.swing.JLabel();
        PnlVentasDia = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        LblCantidad = new javax.swing.JLabel();
        PnlVentas = new javax.swing.JPanel();
        TxtBuscarVentas = new javax.swing.JTextField();
        PnlNewVenta = new javax.swing.JPanel();
        BtnNuevaVenta = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PnlBarraL.setBackground(new java.awt.Color(0, 119, 182));

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

        BtnVentas.setBackground(new java.awt.Color(242, 242, 242));
        BtnVentas.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnVentas.setForeground(new java.awt.Color(0, 119, 182));
        BtnVentas.setText("Ventas");
        BtnVentas.setBorder(null);
        BtnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnVentasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(BtnVentas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        BtnUsers.setBackground(new java.awt.Color(0, 119, 182));
        BtnUsers.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnUsers.setForeground(new java.awt.Color(244, 244, 244));
        BtnUsers.setText("Usuarios");
        BtnUsers.setBorder(null);
        BtnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsersActionPerformed(evt);
            }
        });

        BtnRequisiciones1.setBackground(new java.awt.Color(0, 119, 182));
        BtnRequisiciones1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnRequisiciones1.setForeground(new java.awt.Color(255, 255, 255));
        BtnRequisiciones1.setText("Requisiciones");
        BtnRequisiciones1.setBorder(null);
        BtnRequisiciones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRequisiciones1ActionPerformed(evt);
            }
        });

        BtnProveedores1.setBackground(new java.awt.Color(0, 119, 182));
        BtnProveedores1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnProveedores1.setForeground(new java.awt.Color(255, 255, 255));
        BtnProveedores1.setText("Proveedores");
        BtnProveedores1.setBorder(null);
        BtnProveedores1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProveedores1ActionPerformed(evt);
            }
        });

        BtnFacturas1.setBackground(new java.awt.Color(0, 119, 182));
        BtnFacturas1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnFacturas1.setForeground(new java.awt.Color(255, 255, 255));
        BtnFacturas1.setText("Facturas");
        BtnFacturas1.setBorder(null);
        BtnFacturas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnFacturas1ActionPerformed(evt);
            }
        });

        BtnDevoluciones1.setBackground(new java.awt.Color(0, 119, 182));
        BtnDevoluciones1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnDevoluciones1.setForeground(new java.awt.Color(255, 255, 255));
        BtnDevoluciones1.setText("Devoluciones");
        BtnDevoluciones1.setBorder(null);
        BtnDevoluciones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDevoluciones1ActionPerformed(evt);
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

        javax.swing.GroupLayout PnlBarraLLayout = new javax.swing.GroupLayout(PnlBarraL);
        PnlBarraL.setLayout(PnlBarraLLayout);
        PnlBarraLLayout.setHorizontalGroup(
            PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBarraLLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PnlBarraLLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers))))
                .addGap(0, 71, Short.MAX_VALUE))
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBarraLLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PnlBarraLLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(BtnInicio)
                                .addComponent(BtnRequisiciones1)
                                .addComponent(BtnProveedores1)
                                .addComponent(BtnFacturas1)
                                .addComponent(BtnDevoluciones1)
                                .addComponent(BtnInventario))
                            .addComponent(LblFarmacia))))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlBarraLLayout.setVerticalGroup(
            PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnRequisiciones1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnProveedores1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnFacturas1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnDevoluciones1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
        LblTitulo.setText("Registre ventas y gestione el historial de transacciones");
        LblTitulo.setOpaque(true);

        LblVentas.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblVentas.setForeground(new java.awt.Color(0, 48, 73));
        LblVentas.setText("Ventas");

        PnlVentasDia.setBackground(new java.awt.Color(0, 119, 182));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Ventas del día");

        LblCantidad.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        LblCantidad.setForeground(new java.awt.Color(255, 255, 255));
        LblCantidad.setText("$2456.75");

        javax.swing.GroupLayout PnlVentasDiaLayout = new javax.swing.GroupLayout(PnlVentasDia);
        PnlVentasDia.setLayout(PnlVentasDiaLayout);
        PnlVentasDiaLayout.setHorizontalGroup(
            PnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlVentasDiaLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(LblCantidad)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlVentasDiaLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel54)
                .addGap(19, 19, 19))
        );
        PnlVentasDiaLayout.setVerticalGroup(
            PnlVentasDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlVentasDiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout PnlVentasLayout = new javax.swing.GroupLayout(PnlVentas);
        PnlVentas.setLayout(PnlVentasLayout);
        PnlVentasLayout.setHorizontalGroup(
            PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlVentasLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(TxtBuscarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(998, Short.MAX_VALUE))
        );
        PnlVentasLayout.setVerticalGroup(
            PnlVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlVentasLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(TxtBuscarVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        PnlNewVenta.setBackground(new java.awt.Color(0, 119, 182));

        BtnNuevaVenta.setBackground(new java.awt.Color(0, 119, 182));
        BtnNuevaVenta.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        BtnNuevaVenta.setForeground(new java.awt.Color(255, 255, 255));
        BtnNuevaVenta.setText("Nueva venta");
        BtnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNuevaVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlNewVentaLayout = new javax.swing.GroupLayout(PnlNewVenta);
        PnlNewVenta.setLayout(PnlNewVentaLayout);
        PnlNewVentaLayout.setHorizontalGroup(
            PnlNewVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaVenta)
        );
        PnlNewVentaLayout.setVerticalGroup(
            PnlNewVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBarraL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblTitulo)
                                .addGap(468, 468, 468)
                                .addComponent(PnlNewVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LblVentas)
                                .addComponent(PnlVentasDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(PnlVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(90, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblVentas)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(PnlNewVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlVentasDia, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(PnlVentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PnlBarraL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInventarioActionPerformed
        Inventario inv = new Inventario();

        this.setVisible(false);

        inv.setVisible(true);
        inv.setLocationRelativeTo(null);
        inv.setTitle("Inventario");
    }//GEN-LAST:event_BtnInventarioActionPerformed

    private void BtnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnConfigActionPerformed

    }//GEN-LAST:event_BtnConfigActionPerformed

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

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

    private void BtnRequisiciones1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRequisiciones1ActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Requisiciones req = new Requisiciones();

            this.setVisible(false);

            req.setVisible(true);
            req.setLocationRelativeTo(null);
            req.setTitle("Requisiciones");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }
    }//GEN-LAST:event_BtnRequisiciones1ActionPerformed

    private void BtnProveedores1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnProveedores1ActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            Proveedores prov = new Proveedores();

            this.setState(JFrame.ICONIFIED);

            prov.setVisible(true);
            prov.setLocationRelativeTo(null);
            prov.setTitle("Proveedores");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }  
    }//GEN-LAST:event_BtnProveedores1ActionPerformed

    private void BtnFacturas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnFacturas1ActionPerformed
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
    }//GEN-LAST:event_BtnFacturas1ActionPerformed

    private void BtnDevoluciones1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDevoluciones1ActionPerformed
        if (null != Inicio_sesión.SesionUsuario.puesto) switch (Inicio_sesión.SesionUsuario.puesto) {
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
    }//GEN-LAST:event_BtnDevoluciones1ActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNuevaVentaActionPerformed
        NuevasVentas nuevaVent = new NuevasVentas();

        this.setVisible(false);

        nuevaVent.setVisible(true);
        nuevaVent.setLocationRelativeTo(null);
        nuevaVent.setTitle("Nueva venta");
    }//GEN-LAST:event_BtnNuevaVentaActionPerformed

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

    private void actualizarTotalVentas() {
        double total = new Farmacia().obtenerTotalVentasHoy();
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        LblCantidad.setText(formato.format(total));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnNuevaVenta;
    private javax.swing.JButton BtnProveedores1;
    private javax.swing.JButton BtnRequisiciones1;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblCantidad;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblVentas;
    private javax.swing.JPanel PnlBarraL;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlNewVenta;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JPanel PnlVentas;
    private javax.swing.JPanel PnlVentasDia;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarVentas;
    private javax.swing.JLabel jLabel54;
    // End of variables declaration//GEN-END:variables
}
