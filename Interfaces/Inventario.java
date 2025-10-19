package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import static farmacia.Farmacia.ConectarBD;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.Tables;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.sql.CallableStatement;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;

/**
 *
 * @author Jesus Castillo
 */
public class Inventario extends javax.swing.JFrame {

    public Inventario() {
        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        Tables.personalizarTabla(TblProductos);
        TblProductos.getTableHeader().setReorderingAllowed(false);

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnInicio);

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
        BotonTransparente.hacerBotonTransparente(BtnAltaProd);
        BotonTransparente.hacerBotonTransparente(BtnModProd);

        // PnlRedondo
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlTotal, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlNormal, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlCritico, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBajo, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlAltaProd, 15, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        RoundPanel.aplicarBordesRedondeados(PnlModProd, 15, Color.getHSBColor(0.558f, 1.0f, 0.714f));

        // ImgAjustable
        this.pintarImagen(this.LblIconTotal, "src/icons/Total.png");
        this.pintarImagen(LblCantNormal, "src/icons/Normal.png");
        this.pintarImagen(LblIconBajo, "src/icons/BajoIcon.png");
        this.pintarImagen(LblIconCritico, "src/icons/alerta.png");
        this.pintarImagen(LblLogo, "src/icons/Logo.png");

        actualizarResumenProductos();
        mostrarProductos();

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

    public DefaultTableModel obtenerProductosActivos() {
        // Configurar columnas del modelo
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Producto");
        model.addColumn("Descripción");
        model.addColumn("Cantidad");
        model.addColumn("Existencia");
        model.addColumn("Caducidad");
        model.addColumn("Proveedor Activo");

        // Obtener datos de la base de datos
        try (Connection conn = ConectarBD(); CallableStatement cstmt = conn.prepareCall("{call ProductosActivos()}")) {

            ResultSet rs = cstmt.executeQuery();

            // Procesar cada registro del resultado
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("Producto"),
                    rs.getString("Descripcion"),
                    rs.getDouble("Cantidad"),
                    rs.getInt("Existencia"),
                    rs.getDate("Caducidad"),
                    rs.getBoolean("Esta Activo") ? "Sí" : "No"
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            // Manejo de errores
            JOptionPane.showMessageDialog(null,
                    "Error al cargar productos activos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }

    public void actualizarResumenProductos() {
        // Inicialización de contadores
        int total = 0;
        int normal = 0;
        int bajo = 0;
        int critico = 0;

        try (Connection conn = Farmacia.ConectarBD()) {
            String sql = "SELECT existencia FROM productos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Procesamiento de cada registro del resultado
            while (rs.next()) {
                int existencia = rs.getInt("existencia");
                total++;

                // Clasificar el producto según su nivel de stock
                if (existencia <= 10) {
                    critico++;
                } else if (existencia <= 30) {
                    bajo++;
                } else {
                    normal++;
                }
            }

            /// Configurar los textos de las etiquetas
            LblCant.setText(String.valueOf(total));
            LblCantN.setText(String.valueOf(normal));
            LblCantB.setText(String.valueOf(bajo));
            LblCantC.setText(String.valueOf(critico));

            // Establecer colores distintivos para cada categoría
            LblCantN.setForeground(new java.awt.Color(0, 128, 0));      // Verde
            LblCantB.setForeground(new java.awt.Color(255, 165, 0));    // Naranja
            LblCantC.setForeground(new java.awt.Color(200, 0, 0));      // Rojo

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar resumen: " + e.getMessage());
        }
    }

    // Método para mostrar los productos activos
    private void mostrarProductos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columnas = {
            "Producto", "Dosis", "Tipo", "Stock",
            "Precio", "Proveedor", "Vencimiento", "Estado"
        };
        modelo.setColumnIdentifiers(columnas);
        TblProductos.setModel(modelo);

        try (Connection con = Farmacia.ConectarBD()) {
            String sql = """
            SELECT 
                p.nombre AS producto,
                dm.lote,
                CONCAT(FORMAT(dm.dosis, 2), ' ', 
                    CASE
                        WHEN LOWER(tm.nombre) LIKE '%jarabe%' THEN 'ml'
                        WHEN LOWER(tm.nombre) LIKE '%crema%' THEN 'g'
                        WHEN LOWER(tm.nombre) LIKE '%gotas%' THEN 'gotas'
                        ELSE 'mg'
                    END) AS dosis,
                tm.nombre AS tipo,
                p.existencia,
                p.precio_unitario,
                pr.nombre AS proveedor,
                DATE_FORMAT(p.fecha_caducidad, '%d/%m/%Y') AS vencimiento,
                CASE
                    WHEN p.existencia <= 10 THEN 'Crítico'
                    WHEN p.existencia <= 30 THEN 'Bajo'
                    ELSE 'Normal'
                END AS estado
            FROM productos p
            JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
            JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos
            JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores
            WHERE p.activo = 1 AND pr.activo = 1
            ORDER BY p.fecha_caducidad ASC, p.existencia ASC
            """;

            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    modelo.addRow(new Object[]{
                        rs.getString("producto"),
                        rs.getString("dosis"),
                        rs.getString("tipo"),
                        rs.getInt("existencia") + " unid.",
                        String.format("$%.2f", rs.getDouble("precio_unitario")),
                        rs.getString("proveedor"),
                        rs.getString("vencimiento"),
                        rs.getString("estado")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        configurarRenderizadoTabla();
    }

    // Método para filtrar los productos por estado con JLabel
    private void filtrarProductosPorEstado(String estadoFiltro) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columnas = {
            "Producto", "Dosis", "Tipo", "Stock",
            "Precio", "Proveedor", "Vencimiento", "Estado"
        };
        modelo.setColumnIdentifiers(columnas);
        TblProductos.setModel(modelo);

        try (Connection con = Farmacia.ConectarBD()) {
            String sql = """
            SELECT 
                p.nombre AS producto,
                dm.lote,
                CONCAT(FORMAT(dm.dosis, 2), ' ', 
                    CASE
                        WHEN LOWER(tm.nombre) LIKE '%jarabe%' THEN 'ml'
                        WHEN LOWER(tm.nombre) LIKE '%crema%' THEN 'g'
                        WHEN LOWER(tm.nombre) LIKE '%gotas%' THEN 'gotas'
                        ELSE 'mg'
                    END) AS dosis,
                tm.nombre AS tipo,
                p.existencia,
                p.precio_unitario,
                pr.nombre AS proveedor,
                DATE_FORMAT(p.fecha_caducidad, '%d/%m/%Y') AS vencimiento,
                CASE
                    WHEN p.existencia <= 10 THEN 'Crítico'
                    WHEN p.existencia <= 30 THEN 'Bajo'
                    ELSE 'Normal'
                END AS estado
            FROM productos p
            JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
            JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos
            JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores
            WHERE p.activo = 1 
              AND pr.activo = 1
              AND (
                CASE
                    WHEN p.existencia <= 10 THEN 'Crítico'
                    WHEN p.existencia <= 30 THEN 'Bajo'
                    ELSE 'Normal'
                END = ?
              )
            """ + (estadoFiltro.equals("Crítico")
                    ? " OR p.fecha_caducidad <= DATE_ADD(CURDATE(), INTERVAL 3 MONTH)" : "") + """
            ORDER BY p.fecha_caducidad ASC, p.existencia ASC
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, estadoFiltro);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("producto"),
                    rs.getString("dosis"),
                    rs.getString("tipo"),
                    rs.getInt("existencia") + " unid.",
                    String.format("$%.2f", rs.getDouble("precio_unitario")),
                    rs.getString("proveedor"),
                    rs.getString("vencimiento"),
                    rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        configurarRenderizadoTabla();
    }

    private void configurarRenderizadoTabla() {
        TblProductos.setDefaultRenderer(Object.class, new VencimientoRenderer());

        // Ajustar anchos
        int[] anchos = {150, 80, 120, 70, 80, 120, 90, 70};
        for (int i = 0; i < TblProductos.getColumnCount(); i++) {
            TblProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    // Clase render para determinar la fecha de y stock del producto
    private class VencimientoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setHorizontalAlignment(column == 0 ? SwingConstants.LEFT : SwingConstants.CENTER);

            if (!isSelected) {
                // Obtener la fecha de vencimiento (columna 6)
                String fechaStr = (String) table.getValueAt(row, 6); // Formato: dd/MM/yyyy
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date fecha = sdf.parse(fechaStr);
                    java.util.Calendar hoy = java.util.Calendar.getInstance();
                    java.util.Calendar dentroDeTresMeses = java.util.Calendar.getInstance();
                    dentroDeTresMeses.add(java.util.Calendar.MONTH, 3);

                    if (fecha.before(dentroDeTresMeses.getTime()) && fecha.after(hoy.getTime())) {
                        // Vence en menos de 3 meses
                        c.setBackground(new Color(220, 20, 60)); // Rojo fuerte
                        c.setForeground(Color.WHITE);
                    } else {
                        // Según estado
                        String estado = (String) table.getValueAt(row, 7);
                        if (estado.equals("Crítico")) {
                            c.setBackground(new Color(255, 200, 200));
                            c.setForeground(Color.BLACK);
                        } else if (estado.equals("Bajo")) {
                            c.setBackground(new Color(255, 255, 200));
                            c.setForeground(Color.BLACK);
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pnl1 = new javax.swing.JPanel();
        BtnRequisiciones = new javax.swing.JButton();
        BtnProveedores = new javax.swing.JButton();
        BtnFacturas = new javax.swing.JButton();
        BtnDevoluciones = new javax.swing.JButton();
        PnlBarraL = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        BtnVentas = new javax.swing.JButton();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnInventario = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnInicio = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        BtnRequisiciones1 = new javax.swing.JButton();
        BtnProveedores1 = new javax.swing.JButton();
        BtnFacturas1 = new javax.swing.JButton();
        BtnDevoluciones1 = new javax.swing.JButton();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlFondo = new javax.swing.JPanel();
        PnlTotal = new javax.swing.JPanel();
        LblTotal = new javax.swing.JLabel();
        LblIconTotal = new javax.swing.JLabel();
        LblCant = new javax.swing.JLabel();
        PnlNormal = new javax.swing.JPanel();
        LblNormal = new javax.swing.JLabel();
        LblCantN = new javax.swing.JLabel();
        LblCantNormal = new javax.swing.JLabel();
        PnlBajo = new javax.swing.JPanel();
        LblBajo = new javax.swing.JLabel();
        LblIconBajo = new javax.swing.JLabel();
        LblCantB = new javax.swing.JLabel();
        PnlCritico = new javax.swing.JPanel();
        LblCritico = new javax.swing.JLabel();
        LblCantC = new javax.swing.JLabel();
        LblIconCritico = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblProductos = new javax.swing.JTable();
        PnlAltaProd = new javax.swing.JPanel();
        BtnAltaProd = new javax.swing.JButton();
        PnlModProd = new javax.swing.JPanel();
        BtnModProd = new javax.swing.JButton();

        javax.swing.GroupLayout Pnl1Layout = new javax.swing.GroupLayout(Pnl1);
        Pnl1.setLayout(Pnl1Layout);
        Pnl1Layout.setHorizontalGroup(
            Pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1042, Short.MAX_VALUE)
        );
        Pnl1Layout.setVerticalGroup(
            Pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

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

        LblFarmacia.setBackground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        LblFarmacia.setForeground(new java.awt.Color(255, 255, 255));
        LblFarmacia.setText("FarmaCode");

        BtnInventario.setBackground(new java.awt.Color(244, 244, 244));
        BtnInventario.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        BtnInventario.setForeground(new java.awt.Color(0, 119, 182));
        BtnInventario.setText("Inventario");
        BtnInventario.setBorder(null);
        BtnInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInventarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlSombraLayout = new javax.swing.GroupLayout(PnlSombra);
        PnlSombra.setLayout(PnlSombraLayout);
        PnlSombraLayout.setHorizontalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(BtnInventario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlSombraLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        javax.swing.GroupLayout PnlBarraLLayout = new javax.swing.GroupLayout(PnlBarraL);
        PnlBarraL.setLayout(PnlBarraLLayout);
        PnlBarraLLayout.setHorizontalGroup(
            PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addComponent(BtnVentas)
                                .addComponent(BtnRequisiciones1)
                                .addComponent(BtnProveedores1)
                                .addComponent(BtnFacturas1)
                                .addComponent(BtnDevoluciones1))
                            .addComponent(LblFarmacia))))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBarraLLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(BtnConfig)
                    .addComponent(BtnUsers))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlBarraLLayout.setVerticalGroup(
            PnlBarraLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBarraLLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
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
        LblTitulo.setText("Gestione el stock de medicamentos y productos farmaceuticos.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Inventario");

        PnlFondo.setBackground(new java.awt.Color(255, 255, 255));

        LblTotal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblTotal.setText("Total de productos");
        LblTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LblTotalMouseClicked(evt);
            }
        });

        LblCant.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        LblCant.setText("5");

        javax.swing.GroupLayout PnlTotalLayout = new javax.swing.GroupLayout(PnlTotal);
        PnlTotal.setLayout(PnlTotalLayout);
        PnlTotalLayout.setHorizontalGroup(
            PnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlTotalLayout.createSequentialGroup()
                .addGroup(PnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlTotalLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(LblTotal))
                    .addGroup(PnlTotalLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(LblIconTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(LblCant)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        PnlTotalLayout.setVerticalGroup(
            PnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblIconTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblCant, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnlNormal.setBackground(new java.awt.Color(198, 247, 177));

        LblNormal.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblNormal.setText("Stock normal");
        LblNormal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LblNormalMouseClicked(evt);
            }
        });

        LblCantN.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        LblCantN.setText("3");

        javax.swing.GroupLayout PnlNormalLayout = new javax.swing.GroupLayout(PnlNormal);
        PnlNormal.setLayout(PnlNormalLayout);
        PnlNormalLayout.setHorizontalGroup(
            PnlNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlNormalLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(PnlNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlNormalLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(LblCantNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(LblCantN))
                    .addComponent(LblNormal))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        PnlNormalLayout.setVerticalGroup(
            PnlNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlNormalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblNormal)
                .addGroup(PnlNormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlNormalLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(LblCantNormal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PnlNormalLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(LblCantN, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnlBajo.setBackground(new java.awt.Color(255, 179, 116));

        LblBajo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblBajo.setText("Stock bajo");
        LblBajo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LblBajoMouseClicked(evt);
            }
        });

        LblCantB.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        LblCantB.setText("1");

        javax.swing.GroupLayout PnlBajoLayout = new javax.swing.GroupLayout(PnlBajo);
        PnlBajo.setLayout(PnlBajoLayout);
        PnlBajoLayout.setHorizontalGroup(
            PnlBajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBajoLayout.createSequentialGroup()
                .addGroup(PnlBajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBajoLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(LblIconBajo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LblCantB))
                    .addGroup(PnlBajoLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(LblBajo)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        PnlBajoLayout.setVerticalGroup(
            PnlBajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBajoLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(LblBajo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlBajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblIconBajo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblCantB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnlCritico.setBackground(new java.awt.Color(255, 95, 95));

        LblCritico.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LblCritico.setText("Stock Critico");
        LblCritico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LblCriticoMouseClicked(evt);
            }
        });

        LblCantC.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        LblCantC.setText("1");

        javax.swing.GroupLayout PnlCriticoLayout = new javax.swing.GroupLayout(PnlCritico);
        PnlCritico.setLayout(PnlCriticoLayout);
        PnlCriticoLayout.setHorizontalGroup(
            PnlCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCriticoLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(LblCritico)
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlCriticoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LblIconCritico, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(LblCantC)
                .addGap(86, 86, 86))
        );
        PnlCriticoLayout.setVerticalGroup(
            PnlCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlCriticoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LblCritico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlCriticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LblCantC, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblIconCritico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        TblProductos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Producto", "Categoria", "Stock", "Precio", "Proveedor", "Dia", "Vencimiento", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TblProductos);

        javax.swing.GroupLayout PnlFondoLayout = new javax.swing.GroupLayout(PnlFondo);
        PnlFondo.setLayout(PnlFondoLayout);
        PnlFondoLayout.setHorizontalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(PnlFondoLayout.createSequentialGroup()
                        .addComponent(PnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PnlNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlBajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlCritico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        PnlFondoLayout.setVerticalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(PnlBajo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(PnlNormal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(PnlTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(PnlCritico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        PnlAltaProd.setBackground(new java.awt.Color(0, 119, 182));

        BtnAltaProd.setBackground(new java.awt.Color(0, 0, 0));
        BtnAltaProd.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        BtnAltaProd.setForeground(new java.awt.Color(255, 255, 255));
        BtnAltaProd.setText("Dar Alta Producto");
        BtnAltaProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAltaProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlAltaProdLayout = new javax.swing.GroupLayout(PnlAltaProd);
        PnlAltaProd.setLayout(PnlAltaProdLayout);
        PnlAltaProdLayout.setHorizontalGroup(
            PnlAltaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        PnlAltaProdLayout.setVerticalGroup(
            PnlAltaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        PnlModProd.setBackground(new java.awt.Color(0, 119, 182));

        BtnModProd.setBackground(new java.awt.Color(0, 0, 0));
        BtnModProd.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        BtnModProd.setForeground(new java.awt.Color(255, 255, 255));
        BtnModProd.setText("Modificar Producto");
        BtnModProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModProdMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PnlModProdLayout = new javax.swing.GroupLayout(PnlModProd);
        PnlModProd.setLayout(PnlModProdLayout);
        PnlModProdLayout.setHorizontalGroup(
            PnlModProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnModProd, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );
        PnlModProdLayout.setVerticalGroup(
            PnlModProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnModProd, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBarraL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblTitulo)
                            .addComponent(LblTitulo1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(PnlAltaProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(PnlModProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(44, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlModProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PnlAltaProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(86, 86, 86))
            .addComponent(PnlBarraL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnRequisicionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRequisicionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnRequisicionesActionPerformed

    private void BtnProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnProveedoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnProveedoresActionPerformed

    private void BtnFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnFacturasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnFacturasActionPerformed

    private void BtnDevolucionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDevolucionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDevolucionesActionPerformed

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
    }//GEN-LAST:event_BtnDevoluciones1ActionPerformed

    private void BtnAltaProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAltaProdActionPerformed
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto) || "Almacenista".equals(Inicio_sesión.SesionUsuario.puesto)) {
            AltaProducto altaProd = new AltaProducto();

            this.setVisible(false);

            altaProd.setVisible(true);
            altaProd.setLocationRelativeTo(null);
            altaProd.setTitle("Alta Producto");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }

    }//GEN-LAST:event_BtnAltaProdActionPerformed

    private void LblCriticoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LblCriticoMouseClicked
        LblCritico.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filtrarProductosPorEstado("Crítico");
            }
        });
    }//GEN-LAST:event_LblCriticoMouseClicked

    private void LblBajoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LblBajoMouseClicked
        LblBajo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filtrarProductosPorEstado("Bajo");
            }
        });
    }//GEN-LAST:event_LblBajoMouseClicked

    private void LblNormalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LblNormalMouseClicked
        LblNormal.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                filtrarProductosPorEstado("Normal");
            }
        });
    }//GEN-LAST:event_LblNormalMouseClicked

    private void LblTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LblTotalMouseClicked
        LblTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actualizarResumenProductos();     // refresca los conteos
                mostrarProductos();
            }
        });
    }//GEN-LAST:event_LblTotalMouseClicked

    private void BtnModProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModProdMouseClicked
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            ModificarProducto mod = new ModificarProducto();

            this.setVisible(false);

            mod.setVisible(true);
            mod.setLocationRelativeTo(null);
            mod.setTitle("Modificar Producto");
        } else {
            AccesoDenegado.showAccesoDenegado(this);
        }

    }//GEN-LAST:event_BtnModProdMouseClicked

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
    private javax.swing.JButton BtnAltaProd;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnModProd;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnProveedores1;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnRequisiciones1;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblBajo;
    private javax.swing.JLabel LblCant;
    private javax.swing.JLabel LblCantB;
    private javax.swing.JLabel LblCantC;
    private javax.swing.JLabel LblCantN;
    private javax.swing.JLabel LblCantNormal;
    private javax.swing.JLabel LblCritico;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblIconBajo;
    private javax.swing.JLabel LblIconCritico;
    private javax.swing.JLabel LblIconTotal;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblNormal;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblTotal;
    private javax.swing.JPanel Pnl1;
    private javax.swing.JPanel PnlAltaProd;
    private javax.swing.JPanel PnlBajo;
    private javax.swing.JPanel PnlBarraL;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlCritico;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlFondo;
    private javax.swing.JPanel PnlModProd;
    private javax.swing.JPanel PnlNormal;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JPanel PnlTotal;
    private javax.swing.JTable TblProductos;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
