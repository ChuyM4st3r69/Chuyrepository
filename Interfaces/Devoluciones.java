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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;

public class Devoluciones extends javax.swing.JFrame {

    private JPanel panelDevolucionesContenedor;
    private JScrollPane scrollPaneDevoluciones;
    private JTextField TxtBuscarDevoluciones;
    private ImageIcon imagen;
    private Icon icono;

    public Devoluciones() {
        initComponents();

        inicializarSistemaDevoluciones();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar devoluciones por ID...", TxtBuscarDevoluciones);

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnProv);
        BotonTransparente.hacerBotonTransparente(BtnCli);

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlEliminarProv, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlNuevoProv, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        
        // ImgAjustable
        this.pintarImagen(this.LblLogo, "src/icons/Logo.png");
        
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

    private void inicializarSistemaDevoluciones() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setPreferredSize(new Dimension(1164, 453));

        // Panel de título
        JPanel panelTitulo = crearPanelTitulo("Gestión de Devoluciones");

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();

        // Configurar área de contenido con scroll horizontal
        panelDevolucionesContenedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelDevolucionesContenedor.setBackground(Color.WHITE);

        scrollPaneDevoluciones = new JScrollPane(panelDevolucionesContenedor);
        scrollPaneDevoluciones.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneDevoluciones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneDevoluciones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneDevoluciones.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPaneDevoluciones.setPreferredSize(new Dimension(1164, 400));

        // Ensamblar componentes
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPaneDevoluciones, BorderLayout.CENTER);

        // Reemplazar contenido del panel existente
        PnlDevoluciones.removeAll();
        PnlDevoluciones.setLayout(new BorderLayout());
        PnlDevoluciones.add(panelPrincipal, BorderLayout.CENTER);
        PnlDevoluciones.revalidate();
        PnlDevoluciones.repaint();

        // Cargar datos iniciales
        cargarDevolucionesDesdeBD();
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
        if (TxtBuscarDevoluciones == null) {
            TxtBuscarDevoluciones = new JTextField();
        }
        TxtBuscarDevoluciones.setPreferredSize(new Dimension(250, 35));
        TxtBuscarDevoluciones.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Aplicar estilo
        TextPrompt placeHolder = new TextPrompt("Buscar devoluciones por ID...", TxtBuscarDevoluciones);
        RoundTxt redondearTxt = new RoundTxt(20, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscarDevoluciones.setBorder(redondearTxt);
        TxtBuscarDevoluciones.setBackground(Color.WHITE);

        // Botón limpiar
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnLimpiar.addActionListener(e -> limpiarBusqueda());

        panelCampo.add(TxtBuscarDevoluciones, BorderLayout.CENTER);
        panelCampo.add(btnLimpiar, BorderLayout.EAST);

        panel.add(panelCampo, BorderLayout.WEST);

        // Configurar eventos de búsqueda
        TxtBuscarDevoluciones.addActionListener(e -> filtrarDevoluciones());
        TxtBuscarDevoluciones.addKeyListener(new java.awt.event.KeyAdapter() {
            private Timer timer;

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (timer != null) {
                    timer.stop();
                }
                timer = new Timer(500, e -> filtrarDevoluciones());
                timer.setRepeats(false);
                timer.start();
            }
        });

        return panel;
    }

    private void cargarDevolucionesDesdeBD() {
        panelDevolucionesContenedor.removeAll();

        // Mostrar panel de carga mientras se obtienen los datos
        JPanel panelCarga = crearPanelMensaje("⏳", "Cargando devoluciones...");
        panelDevolucionesContenedor.add(panelCarga);
        panelDevolucionesContenedor.revalidate();
        panelDevolucionesContenedor.repaint();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql = "SELECT d.idretornos, d.fecha, "
                        + "p.nombre as producto, d.canatidad "
                        + "FROM retornos d "
                        + "JOIN productos p ON d.productos_idproductos = p.idproductos "
                        + "ORDER BY d.fecha DESC, d.idretornos DESC";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                panelDevolucionesContenedor.removeAll();
                boolean hayDevoluciones = false;
                DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

                while (rs.next()) {
                    hayDevoluciones = true;
                    int id = rs.getInt("idretornos");
                    Date fecha = rs.getTimestamp("fecha");
                    String producto = rs.getString("producto");
                    int cantidad = rs.getInt("canatidad");

                    JPanel tarjeta = crearTarjetaDevolucion(id, fecha, producto, cantidad, formatoMoneda);
                    panelDevolucionesContenedor.add(tarjeta);
                }

                if (!hayDevoluciones) {
                    panelDevolucionesContenedor.add(crearPanelMensaje("📋", "No hay devoluciones registradas"));
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                panelDevolucionesContenedor.removeAll();
                panelDevolucionesContenedor.add(crearPanelMensaje("❌", "Error al cargar devoluciones"));
                actualizarDimensionesContenedor();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JPanel crearTarjetaDevolucion(int id, Date fecha, String producto, int cantidad, DecimalFormat formatoMoneda) {
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
        JLabel lblId = new JLabel("<html><center>🛒<br>DEVOLUCIÓN-" + String.format("%04d", id) + "</center></html>");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblId.setForeground(new Color(70, 130, 180));
        lblId.setHorizontalAlignment(JLabel.CENTER);
        lblId.setVerticalAlignment(JLabel.CENTER);

        JLabel lblFecha = new JLabel("<html><center>📅 " + fechaStr + "<br>🕒 " + horaStr + "</center></html>");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setHorizontalAlignment(JLabel.CENTER);
        lblFecha.setVerticalAlignment(JLabel.CENTER);

        JLabel lblProducto = new JLabel("<html><center>📦 " + producto + "</center></html>");
        lblProducto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblProducto.setHorizontalAlignment(JLabel.CENTER);
        lblProducto.setVerticalAlignment(JLabel.CENTER);

        JLabel lblCantidad = new JLabel("<html><center>🔢 " + cantidad + " unidades</center></html>");
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCantidad.setHorizontalAlignment(JLabel.CENTER);
        lblCantidad.setVerticalAlignment(JLabel.CENTER);

        // Botón de detalles
        JButton btnDetalles = new JButton("Ver Detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setBackground(new Color(70, 130, 180));
        btnDetalles.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btnDetalles.addActionListener(e -> mostrarDetallesDevolucion(id));

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
        panelInfo.add(lblProducto);
        panelInfo.add(lblCantidad);

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
        int numTarjetas = panelDevolucionesContenedor.getComponentCount();
        int anchoNecesario = (numTarjetas * 230) + (numTarjetas * 15) + 20;
        panelDevolucionesContenedor.setPreferredSize(new Dimension(anchoNecesario, 300));
        panelDevolucionesContenedor.revalidate();
        panelDevolucionesContenedor.repaint();
    }

    private void mostrarDetallesDevolucion(int idDevolucion) {
        try (Connection conn = Farmacia.ConectarBD()) {
            // Consulta para obtener los detalles principales de la devolución
            String sqlDevolucion = "SELECT d.idretornos, d.fecha, "
                    + "p.nombre as producto, d.canatidad, d.tipo "
                    + "FROM retornos d "
                    + "JOIN productos p ON d.productos_idproductos = p.idproductos "
                    + "WHERE d.idretornos = ?";

            // Obtener datos principales de la devolución
            PreparedStatement pstDevolucion = conn.prepareStatement(sqlDevolucion);
            pstDevolucion.setInt(1, idDevolucion);
            ResultSet rsDevolucion = pstDevolucion.executeQuery();

            if (!rsDevolucion.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró la devolución especificada",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Formatear datos de la devolución
            Date fechaDevolucion = rsDevolucion.getTimestamp("fecha");
            String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fechaDevolucion);
            String horaStr = new SimpleDateFormat("HH:mm:ss").format(fechaDevolucion);
            String producto = rsDevolucion.getString("producto");
            int cantidad = rsDevolucion.getInt("canatidad");
            int tipo = rsDevolucion.getInt("tipo");
            String tipoStr = "";

            // Mapa de tipos de devolución
            switch (tipo) {
                case 1:
                    tipoStr = "Dosis Incorrecta";
                    break;
                case 2:
                    tipoStr = "Medicamento Equivocado";
                    break;
                case 3:
                    tipoStr = "Cantidad Solicitada Errónea";
                    break;
                case 4:
                    tipoStr = "Producto Dañado";
                    break;
                case 5:
                    tipoStr = "Cambio de Opinion";
                    break;
                case 6:
                    tipoStr = "Otro";
                    break;
                case 10:
                    tipoStr = "Producto Defectuoso";
                    break;
                case 11:
                    tipoStr = "Por Caducidad";
                    break;
                case 12:
                    tipoStr = "Cantidad Erronea";
                    break;
                case 13:
                    tipoStr = "Producto Equivocado";
                    break;
                case 14:
                    tipoStr = "Otro Motivo";
                    break;
                default:
                    tipoStr = "Desconocido";
                    break;
            }

            // Crear panel principal para el diálogo
            JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Panel de información de la devolución
            JPanel panelInfo = new JPanel(new GridLayout(0, 1, 5, 5));
            panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Devolución"));

            panelInfo.add(new JLabel("Devolución #: DEVOLUCIÓN-" + String.format("%04d", idDevolucion)));
            panelInfo.add(new JLabel("Fecha: " + fechaStr));
            panelInfo.add(new JLabel("Hora: " + horaStr));
            panelInfo.add(new JLabel("Producto: " + producto));
            panelInfo.add(new JLabel("Cantidad: " + cantidad));
            panelInfo.add(new JLabel("Tipo de Devolución: " + tipoStr));

            // Ensamblar el diálogo
            panelPrincipal.add(panelInfo, BorderLayout.CENTER);

            // Mostrar el diálogo
            JOptionPane.showMessageDialog(
                    this,
                    panelPrincipal,
                    "Detalles de Devolución DEVOLUCIÓN-" + idDevolucion,
                    JOptionPane.PLAIN_MESSAGE
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar detalles de la devolución: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Metodo para filtrar las devoluciones
    private void filtrarDevoluciones() {
        String filtro = TxtBuscarDevoluciones.getText().trim();
        panelDevolucionesContenedor.removeAll();

        SwingUtilities.invokeLater(() -> {
            try (Connection conn = Farmacia.ConectarBD()) {
                String sql;
                PreparedStatement pst;

                if (filtro.isEmpty()) {
                    cargarDevolucionesDesdeBD();
                    return;
                }

                sql = "SELECT d.idretornos, d.fecha, "
                        + "p.nombre as producto, d.canatidad "
                        + "FROM retornos d "
                        + "JOIN productos p ON d.productos_idproductos = p.idproductos "
                        + "WHERE CAST(d.idretornos as CHAR) LIKE ? "
                        + "ORDER BY d.fecha DESC";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + filtro + "%");
                ResultSet rs = pst.executeQuery();

                boolean hayResultados = false;
                DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");

                while (rs.next()) {
                    hayResultados = true;
                    int id = rs.getInt("idretornos");
                    Date fecha = rs.getTimestamp("fecha");
                    String producto = rs.getString("producto");
                    int cantidad = rs.getInt("canatidad");

                    JPanel tarjeta = crearTarjetaDevolucion(id, fecha, producto, cantidad, formatoMoneda);
                    panelDevolucionesContenedor.add(tarjeta);
                }

                if (!hayResultados) {
                    JPanel panelNoResultados = crearPanelMensaje("🔍", "No se encontraron devoluciones para: \"" + filtro + "\"");
                    panelDevolucionesContenedor.add(panelNoResultados);
                }

                actualizarDimensionesContenedor();

            } catch (SQLException ex) {
                panelDevolucionesContenedor.add(crearPanelMensaje("❌", "Error al buscar devoluciones"));
                actualizarDimensionesContenedor();
            }
        });
    }

    private void limpiarBusqueda() {
        TxtBuscarDevoluciones.setText("");
        cargarDevolucionesDesdeBD();
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
        BtnDevoluciones = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnFacturas = new javax.swing.JButton();
        BtnInicio = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlNuevoProv = new javax.swing.JPanel();
        BtnCli = new javax.swing.JButton();
        PnlEliminarProv = new javax.swing.JPanel();
        BtnProv = new javax.swing.JButton();
        PnlDevoluciones = new javax.swing.JPanel();

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

        BtnDevoluciones.setBackground(new java.awt.Color(242, 242, 242));
        BtnDevoluciones.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnDevoluciones.setForeground(new java.awt.Color(0, 119, 182));
        BtnDevoluciones.setText("Devoluciones");
        BtnDevoluciones.setBorder(null);
        BtnDevoluciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDevolucionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(BtnDevoluciones)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(23, 23, 23)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(LblFarmacia)))
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
                            .addComponent(BtnFacturas)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers)
                            .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(BtnVentas))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnInicio)
                            .addComponent(BtnInventario))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 328, Short.MAX_VALUE)
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
        LblTitulo.setText("Procese devoluciones de productos y reembolsos a clientes");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Devoluciones");

        PnlNuevoProv.setBackground(new java.awt.Color(0, 119, 182));

        BtnCli.setBackground(new java.awt.Color(153, 153, 255));
        BtnCli.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        BtnCli.setForeground(new java.awt.Color(255, 255, 255));
        BtnCli.setText("Devolución al cliente");
        BtnCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlNuevoProvLayout = new javax.swing.GroupLayout(PnlNuevoProv);
        PnlNuevoProv.setLayout(PnlNuevoProvLayout);
        PnlNuevoProvLayout.setHorizontalGroup(
            PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnCli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlNuevoProvLayout.setVerticalGroup(
            PnlNuevoProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnCli, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        PnlEliminarProv.setBackground(new java.awt.Color(0, 119, 182));

        BtnProv.setBackground(new java.awt.Color(153, 153, 255));
        BtnProv.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        BtnProv.setForeground(new java.awt.Color(255, 255, 255));
        BtnProv.setText("Devolución al proveedor");
        BtnProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnProvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlEliminarProvLayout = new javax.swing.GroupLayout(PnlEliminarProv);
        PnlEliminarProv.setLayout(PnlEliminarProvLayout);
        PnlEliminarProvLayout.setHorizontalGroup(
            PnlEliminarProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnProv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlEliminarProvLayout.setVerticalGroup(
            PnlEliminarProvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnProv, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
        );

        PnlDevoluciones.setBackground(new java.awt.Color(204, 204, 0));
        PnlDevoluciones.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout PnlDevolucionesLayout = new javax.swing.GroupLayout(PnlDevoluciones);
        PnlDevoluciones.setLayout(PnlDevolucionesLayout);
        PnlDevolucionesLayout.setHorizontalGroup(
            PnlDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PnlDevolucionesLayout.setVerticalGroup(
            PnlDevolucionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 477, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PnlDevoluciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblTitulo1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblTitulo)
                                .addGap(136, 136, 136)
                                .addComponent(PnlNuevoProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(PnlEliminarProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 24, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(LblTitulo)
                                .addGap(81, 81, 81))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PnlEliminarProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)))
                        .addComponent(PnlDevoluciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PnlNuevoProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 844, Short.MAX_VALUE)
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
    }//GEN-LAST:event_BtnDevolucionesActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCliActionPerformed
        DevolucionClientes DevCli = new DevolucionClientes();

        this.setVisible(false);

        DevCli.setVisible(true);
        DevCli.setLocationRelativeTo(null);
        DevCli.setTitle("Devolución cliente");
    }//GEN-LAST:event_BtnCliActionPerformed

    private void BtnProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnProvActionPerformed
        DevolucionProveedor DevPro = new DevolucionProveedor();

        this.setVisible(false);

        DevPro.setVisible(true);
        DevPro.setLocationRelativeTo(null);
        DevPro.setTitle("Devolución cliente");
    }//GEN-LAST:event_BtnProvActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnCli;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnProv;
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
    private javax.swing.JPanel PnlDevoluciones;
    private javax.swing.JPanel PnlEliminarProv;
    private javax.swing.JPanel PnlNuevoProv;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
