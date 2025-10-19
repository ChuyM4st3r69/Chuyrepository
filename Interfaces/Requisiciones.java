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
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;
import metodos.RoundPanel;

/**
 *
 * @author Jesus Castillo
 */
public class Requisiciones extends javax.swing.JFrame {

    public int idProductoBD;
    private JPanel panelRequisicionesContenedor;
    private JScrollPane scrollPaneRequisiciones;
    private DefaultTableModel modeloRequisiciones;
    private JPanel panelPrincipalRequisiciones;

    public Requisiciones() {

        initComponents();

        inicializarSistemaRequisiciones();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        // Configurar el botón de búsqueda
        TxtBuscarRequisicion.addActionListener(e -> filtrarRequisiciones());

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Buscar requisiciones...", TxtBuscarRequisicion);

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
        BotonTransparente.hacerBotonTransparente(BtnNuevaRequisicion);
       

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlNuevaRequisicion, 15, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        
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

    private void inicializarSistemaRequisiciones() {
        // Crear el panel principal que contendrá todo
        panelPrincipalRequisiciones = new JPanel(new BorderLayout());
        panelPrincipalRequisiciones.setBackground(Color.WHITE);
        panelPrincipalRequisiciones.setPreferredSize(new Dimension(1164, 453));
        panelPrincipalRequisiciones.setMaximumSize(new Dimension(1164, 453));

        // Crear panel de título más compacto
        JPanel panelTitulo = crearPanelTituloCompacto();

        // Crear panel de búsqueda más compacto
        JPanel panelBusqueda = crearPanelBusquedaCompacto();

        // Crear área de contenido horizontal
        crearAreaContenidoHorizontal();

        // Ensamblar todo con dimensiones controladas
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setPreferredSize(new Dimension(1164, 100)); // Altura fija para título y búsqueda
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        panelPrincipalRequisiciones.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipalRequisiciones.add(scrollPaneRequisiciones, BorderLayout.CENTER);

        // Reemplazar el contenido del panel existente
        PnlRequisiciones.removeAll();
        PnlRequisiciones.setLayout(new BorderLayout());
        PnlRequisiciones.add(panelPrincipalRequisiciones, BorderLayout.CENTER);
        PnlRequisiciones.revalidate();
        PnlRequisiciones.repaint();

        // Cargar datos iniciales
        cargarRequisicionesDesdeBD();
    }

    private JPanel crearPanelTituloCompacto() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setPreferredSize(new Dimension(1164, 50));

        JLabel lblTitulo = new JLabel("Gestión de Requisiciones");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panel.add(lblTitulo, BorderLayout.WEST);

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

        // Configurar campo de búsqueda más pequeño
        if (TxtBuscarRequisicion == null) {
            TxtBuscarRequisicion = new javax.swing.JTextField();
        }
        TxtBuscarRequisicion.setPreferredSize(new Dimension(250, 30));
        TxtBuscarRequisicion.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Aplicar estilo
        TextPrompt placeHolder = new TextPrompt("Buscar requisiciones...", TxtBuscarRequisicion);
        RoundTxt redondearTxt = new RoundTxt(20, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscarRequisicion.setBorder(redondearTxt);
        TxtBuscarRequisicion.setBackground(Color.WHITE);

        // Botón limpiar más pequeño
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());

        panelCampo.add(TxtBuscarRequisicion, BorderLayout.CENTER);
        panelCampo.add(btnLimpiar, BorderLayout.EAST);

        panel.add(panelCampo, BorderLayout.WEST);

        // Configurar eventos
        configurarEventosBusqueda();

        return panel;
    }

    private void crearAreaContenidoHorizontal() {
        // Panel contenedor con FlowLayout para disposición horizontal
        panelRequisicionesContenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelRequisicionesContenedor.setBackground(Color.WHITE);

        // ScrollPane configurado para scroll HORIZONTAL
        scrollPaneRequisiciones = new JScrollPane(panelRequisicionesContenedor);
        scrollPaneRequisiciones.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneRequisiciones.setBackground(Color.WHITE);
        scrollPaneRequisiciones.getViewport().setBackground(Color.WHITE);

        // CONFIGURACIÓN CLAVE PARA SCROLL HORIZONTAL
        scrollPaneRequisiciones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneRequisiciones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneRequisiciones.getHorizontalScrollBar().setUnitIncrement(16);

        // Dimensiones específicas para tu frame
        int alturaDisponible = 453 - 100; // Total menos título y búsqueda
        scrollPaneRequisiciones.setPreferredSize(new Dimension(1164, alturaDisponible));
        scrollPaneRequisiciones.setMaximumSize(new Dimension(1164, alturaDisponible));

        // Configurar modelo de tabla para detalles
        modeloRequisiciones = new DefaultTableModel(new Object[]{"ID", "Producto", "Cantidad", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

// Configurar eventos de búsqueda
    private void configurarEventosBusqueda() {
        // Limpiar listeners existentes para evitar duplicados
        java.awt.event.ActionListener[] listeners = TxtBuscarRequisicion.getActionListeners();
        for (java.awt.event.ActionListener listener : listeners) {
            TxtBuscarRequisicion.removeActionListener(listener);
        }

        // Agregar nuevo listener
        TxtBuscarRequisicion.addActionListener(e -> filtrarRequisiciones());

        // Búsqueda en tiempo real
        TxtBuscarRequisicion.addKeyListener(new java.awt.event.KeyAdapter() {
            private javax.swing.Timer timer;

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (timer != null) {
                    timer.stop();
                }
                timer = new javax.swing.Timer(500, e -> filtrarRequisiciones());
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    // Método mejorado para cargar requisiciones
    private void cargarRequisicionesDesdeBD() {

        // Limpiar contenedor
        panelRequisicionesContenedor.removeAll();

        // Mostrar indicador de carga
        JPanel panelCarga = crearPanelCargaHorizontal();
        panelRequisicionesContenedor.add(panelCarga);
        panelRequisicionesContenedor.revalidate();
        panelRequisicionesContenedor.repaint();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {

                String sql = "SELECT r.idrequisisiones, r.fecha, COUNT(d.productos_idproductos) as productos "
                        + "FROM requisisiones r "
                        + "LEFT JOIN detalle_requisisiones d ON r.idrequisisiones = d.requisisiones_idrequisisiones "
                        + "GROUP BY r.idrequisisiones, r.fecha "
                        + "ORDER BY r.fecha DESC";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                // Limpiar panel de carga
                panelRequisicionesContenedor.removeAll();

                boolean hayRequisiciones = false;
                int contador = 0;

                while (rs.next()) {
                    hayRequisiciones = true;
                    contador++;

                    int id = rs.getInt("idrequisisiones");
                    Date fecha = rs.getDate("fecha");
                    int numProductos = rs.getInt("productos");

                    // Crear panel individual VERTICAL (tarjeta)
                    JPanel panelReq = crearTarjetaRequisicion(id, fecha, numProductos);
                    panelRequisicionesContenedor.add(panelReq);
                }

                if (!hayRequisiciones) {
                    JPanel panelVacio = crearPanelSinResultadosHorizontal();
                    panelRequisicionesContenedor.add(panelVacio);
                }

                // Actualizar dimensiones del contenedor para scroll horizontal
                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                System.err.println("Error SQL: " + ex.getMessage());
                panelRequisicionesContenedor.removeAll();
                JPanel panelError = crearPanelErrorHorizontal(ex.getMessage());
                panelRequisicionesContenedor.add(panelError);
                panelRequisicionesContenedor.revalidate();
                panelRequisicionesContenedor.repaint();
            }
        });
    }

    private JPanel crearTarjetaRequisicion(int id, Date fecha, int numProductos) {
        // Panel como tarjeta vertical
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setBackground(Color.WHITE);

        // DIMENSIONES FIJAS para tarjetas uniformes
        tarjeta.setPreferredSize(new Dimension(200, 280));
        tarjeta.setMinimumSize(new Dimension(200, 280));
        tarjeta.setMaximumSize(new Dimension(200, 280));

        // Panel de información (vertical)
        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 0, 10));
        panelInfo.setOpaque(false);

        // Formatear fecha
        String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        String horaStr = new SimpleDateFormat("HH:mm").format(fecha);

        // ID con estilo destacado
        JLabel lblId = new JLabel("<html><center>📋<br>REQ-" + String.format("%04d", id) + "</center></html>");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(new Color(70, 130, 180));
        lblId.setHorizontalAlignment(JLabel.CENTER);

        // Fecha
        JLabel lblFecha = new JLabel("<html><center>📅<br>" + fechaStr + "</center></html>");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(100, 100, 100));
        lblFecha.setHorizontalAlignment(JLabel.CENTER);

        // Productos
        JLabel lblProductos = new JLabel("<html><center>📦<br>" + numProductos + " producto" + (numProductos != 1 ? "s" : "") + "</center></html>");
        lblProductos.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblProductos.setForeground(new Color(60, 150, 60));
        lblProductos.setHorizontalAlignment(JLabel.CENTER);

        panelInfo.add(lblId);
        panelInfo.add(lblFecha);
        panelInfo.add(lblProductos);

        // Botón de detalles
        JButton btnDetalles = new JButton("Ver Detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setBackground(new Color(70, 130, 180));
        btnDetalles.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btnDetalles.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalles.setFocusPainted(false);

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

        btnDetalles.addActionListener(e -> mostrarDetallesRequisicion(id));

        // Ensamblar tarjeta
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(btnDetalles, BorderLayout.SOUTH);

        return tarjeta;
    }

    // Panel de carga horizontal
    private JPanel crearPanelCargaHorizontal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 200));

        JLabel lblCarga = new JLabel("<html><center>⏳<br>Cargando...</center></html>");
        lblCarga.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblCarga.setForeground(new Color(70, 130, 180));
        lblCarga.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblCarga, BorderLayout.CENTER);
        return panel;
    }

    // Panel sin resultados horizontal
    private JPanel crearPanelSinResultadosHorizontal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(248, 249, 250));
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel lblMensaje = new JLabel("<html><center>📋<br><br>No hay requisiciones<br>registradas</center></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMensaje.setForeground(Color.GRAY);
        lblMensaje.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblMensaje, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelErrorHorizontal(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 245, 245));
        panel.setPreferredSize(new Dimension(400, 200));

        JLabel lblError = new JLabel("<html><center>❌<br><br>Error al cargar<br>requisiciones</center></html>");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(new Color(200, 50, 50));
        lblError.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblError, BorderLayout.CENTER);
        return panel;
    }

    // Actualizar dimensiones del contenedor para permitir scroll horizontal
    private void actualizarDimensionesContenedor() {
        int numTarjetas = panelRequisicionesContenedor.getComponentCount();
        int anchoNecesario = (numTarjetas * 210) + (numTarjetas * 10) + 20; // 210px por tarjeta + espacios

        panelRequisicionesContenedor.setPreferredSize(new Dimension(anchoNecesario, 300));
        panelRequisicionesContenedor.revalidate();
        panelRequisicionesContenedor.repaint();
        scrollPaneRequisiciones.revalidate();
        scrollPaneRequisiciones.repaint();
    }

    private void mostrarDetallesRequisicion(int idRequisicion) {
        try (Connection conn = Farmacia.ConectarBD()) {
            // Consulta para obtener detalles
            String sql = "SELECT p.nombre, d.cantidad, pr.nombre as proveedor "
                    + "FROM detalle_requisisiones d "
                    + "JOIN productos p ON d.productos_idproductos = p.idproductos "
                    + "JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores "
                    + "WHERE d.requisisiones_idrequisisiones = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idRequisicion);
            ResultSet rs = pst.executeQuery();

            // Configurar modelo de tabla
            modeloRequisiciones.setRowCount(0);

            while (rs.next()) {
                modeloRequisiciones.addRow(new Object[]{
                    idRequisicion,
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getString("proveedor")
                });
            }

            // Crear diálogo con los detalles
            JTable tabla = new JTable(modeloRequisiciones);
            tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Detalles de Requisición REQ-" + idRequisicion,
                    JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpiar búsqueda
    private void limpiarBusqueda() {
        TxtBuscarRequisicion.setText("");
        cargarRequisicionesDesdeBD();
    }

    // Filtrar requisiciones mejorado
    private void filtrarRequisiciones() {
        String filtro = TxtBuscarRequisicion.getText().trim();

        panelRequisicionesContenedor.removeAll();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql;
                PreparedStatement pst;

                if (filtro.isEmpty()) {
                    cargarRequisicionesDesdeBD();
                    return;
                } else {
                    sql = "SELECT r.idrequisisiones, r.fecha, COUNT(d.productos_idproductos) as productos "
                            + "FROM requisisiones r "
                            + "LEFT JOIN detalle_requisisiones d ON r.idrequisisiones = d.requisisiones_idrequisisiones "
                            + "WHERE CAST(r.idrequisisiones as CHAR) LIKE ? "
                            + "GROUP BY r.idrequisisiones, r.fecha "
                            + "ORDER BY r.fecha DESC";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, "%" + filtro + "%");
                }

                ResultSet rs = pst.executeQuery();

                panelRequisicionesContenedor.removeAll();
                boolean hayResultados = false;

                while (rs.next()) {
                    hayResultados = true;
                    int id = rs.getInt("idrequisisiones");
                    Date fecha = rs.getDate("fecha");
                    int numProductos = rs.getInt("productos");

                    JPanel tarjeta = crearTarjetaRequisicion(id, fecha, numProductos);
                    panelRequisicionesContenedor.add(tarjeta);
                }

                if (!hayResultados) {
                    JPanel panelNoResultados = crearPanelSinResultadosHorizontal();
                    ((JLabel) ((BorderLayout) panelNoResultados.getLayout()).getLayoutComponent(BorderLayout.CENTER))
                            .setText("<html><center>🔍<br><br>No se encontraron<br>requisiciones para:<br>\"" + filtro + "\"</center></html>");
                    panelRequisicionesContenedor.add(panelNoResultados);
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                System.err.println("Error en filtro: " + ex.getMessage());
                panelRequisicionesContenedor.removeAll();
                JPanel panelError = crearPanelErrorHorizontal(ex.getMessage());
                panelRequisicionesContenedor.add(panelError);
                actualizarDimensionesContenedor();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LblTitulo = new javax.swing.JLabel();
        LblVentas = new javax.swing.JLabel();
        PnlBarraL = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnRequisiciones1 = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnInicio = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        BtnProveedores1 = new javax.swing.JButton();
        BtnFacturas1 = new javax.swing.JButton();
        BtnDevoluciones1 = new javax.swing.JButton();
        BtnInventario = new javax.swing.JButton();
        BtnVentas = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        TxtBuscarRequisicion = new javax.swing.JTextField();
        PnlRequisiciones = new javax.swing.JPanel();
        PnlNuevaRequisicion = new javax.swing.JPanel();
        BtnNuevaRequisicion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Gestione las solicitudes de productos a proveedores");
        LblTitulo.setOpaque(true);

        LblVentas.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblVentas.setForeground(new java.awt.Color(0, 48, 73));
        LblVentas.setText("Requisiciones");

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

        BtnRequisiciones1.setBackground(new java.awt.Color(242, 242, 242));
        BtnRequisiciones1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnRequisiciones1.setForeground(new java.awt.Color(0, 119, 182));
        BtnRequisiciones1.setText("Requisiciones");
        BtnRequisiciones1.setBorder(null);
        BtnRequisiciones1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRequisiciones1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(BtnRequisiciones1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSombraLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnRequisiciones1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addGroup(PnlBarraLLayout.createSequentialGroup()
                                .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnInicio)
                                    .addComponent(BtnProveedores1)
                                    .addComponent(BtnFacturas1)
                                    .addComponent(BtnDevoluciones1)
                                    .addComponent(BtnInventario)
                                    .addComponent(BtnVentas))
                                .addGap(2, 2, 2))
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
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        TxtBuscarRequisicion.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarRequisicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarRequisicionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlRequisicionesLayout = new javax.swing.GroupLayout(PnlRequisiciones);
        PnlRequisiciones.setLayout(PnlRequisicionesLayout);
        PnlRequisicionesLayout.setHorizontalGroup(
            PnlRequisicionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1164, Short.MAX_VALUE)
        );
        PnlRequisicionesLayout.setVerticalGroup(
            PnlRequisicionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );

        PnlNuevaRequisicion.setBackground(new java.awt.Color(0, 119, 182));

        BtnNuevaRequisicion.setBackground(new java.awt.Color(0, 119, 182));
        BtnNuevaRequisicion.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        BtnNuevaRequisicion.setForeground(new java.awt.Color(255, 255, 255));
        BtnNuevaRequisicion.setText("Nueva requisición");
        BtnNuevaRequisicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNuevaRequisicionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlNuevaRequisicionLayout = new javax.swing.GroupLayout(PnlNuevaRequisicion);
        PnlNuevaRequisicion.setLayout(PnlNuevaRequisicionLayout);
        PnlNuevaRequisicionLayout.setHorizontalGroup(
            PnlNuevaRequisicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaRequisicion)
        );
        PnlNuevaRequisicionLayout.setVerticalGroup(
            PnlNuevaRequisicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnNuevaRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TxtBuscarRequisicion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LblVentas)
                                .addComponent(PnlRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(LblTitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PnlNuevaRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(88, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblVentas)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(LblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(PnlNuevaRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(TxtBuscarRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(PnlRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

            this.setState(JFrame.ICONIFIED);

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

    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInventarioActionPerformed
        Inventario inv = new Inventario();

        this.setVisible(false);

        inv.setVisible(true);
        inv.setLocationRelativeTo(null);
        inv.setTitle("Inventario");
    }//GEN-LAST:event_BtnInventarioActionPerformed

    private void BtnNuevaRequisicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNuevaRequisicionActionPerformed
        NuevaRequisicion nuevaReq = new NuevaRequisicion();

        this.setVisible(false);

        nuevaReq.setVisible(true);
        nuevaReq.setLocationRelativeTo(null);
        nuevaReq.setTitle("Nueva requisición");
    }//GEN-LAST:event_BtnNuevaRequisicionActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void TxtBuscarRequisicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarRequisicionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarRequisicionActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnNuevaRequisicion;
    private javax.swing.JButton BtnProveedores1;
    private javax.swing.JButton BtnRequisiciones1;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblVentas;
    private javax.swing.JPanel PnlBarraL;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlNuevaRequisicion;
    private javax.swing.JPanel PnlRequisiciones;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarRequisicion;
    // End of variables declaration//GEN-END:variables
}
