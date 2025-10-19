package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JFrame;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.paintBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import javax.swing.JOptionPane;
import java.sql.Types;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;
import metodos.Tables;

public class NuevasVentas extends javax.swing.JFrame {

    public NuevasVentas() {
        initComponents();
        configurarEventos();
        configurarTablaVenta();
        inicializarUI();

        Tables.personalizarTabla(tblDetalleVenta);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);
        
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

    private void inicializarUI() {
        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // Configurar placeholders
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Nombre del producto...", TxtBuscarID);

        // Configurar elementos visuales
        configurarElementosVisuales();
    }

    private void configurarElementosVisuales() {
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0f, 0f, 85.1f));
        TxtBuscar.setBorder(redondearTxt);
        TxtBuscarID.setBorder(redondearTxt);

        // Configurar botones
        BtnGenerar.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnGenerar.setFocusPainted(false);
        BtnCancelar.setBorder(new paintBorder(20, Color.getHSBColor(0.558f, 1.0f, 0.714f)));
        BtnCancelar.setFocusPainted(false);
        BtnBorrarProd.setBorder(new paintBorder(5, Color.getHSBColor(0.f, 0f, 0f)));
        BtnBorrarProd.setFocusPainted(false);

        // Hacer botones transparentes
        BotonTransparente.hacerBotonTransparente(BtnGenerar);
        BotonTransparente.hacerBotonTransparente(BtnCancelar);
        BotonTransparente.hacerBotonTransparente(BtnBorrarProd);
        BotonTransparente.hacerBotonTransparente(BtnInicio);
        BotonTransparente.hacerBotonTransparente(BtnInventario);
        BotonTransparente.hacerBotonTransparente(BtnVentas);
        BotonTransparente.hacerBotonTransparente(BtnUsers);
        BotonTransparente.hacerBotonTransparente(BtnConfig);
        BotonTransparente.hacerBotonTransparente(BtnDevoluciones1);
        BotonTransparente.hacerBotonTransparente(BtnFacturas1);
        BotonTransparente.hacerBotonTransparente(BtnRequisiciones1);
        BotonTransparente.hacerBotonTransparente(BtnProveedores1);
        BotonTransparente.hacerBotonTransparente(BtnBorrarProd);

        // Configurar paneles
        RoundPanel.aplicarBordesRedondeados(PnlGenerarV, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlCancelar, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBorrarProd, 5, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlBorrarProd, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            BtnUsers.setText("Usuarios");
        } else {
            BtnUsers.setText("Ayuda");
        }
    }

    private void configurarEventos() {
        TxtBuscarID.addActionListener(e -> buscarProducto());
        BtnGenerar.addActionListener(e -> procesarVenta());
    }

    private void configurarTablaVenta() {
        String[] columnNames = {
            "ID",           // 0 - ID Producto (oculto)
            "Producto",     // 1 - Nombre del producto
            "Precio Unit.", // 2
            "Cantidad",     // 3
            "Subtotal",     // 4
            "Lote",         // 5 (oculto)
            "Dosis",        // 6
            "Unidad"        // 7
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 3 ->
                        Integer.class;
                    case 2, 4 ->
                        Double.class;
                    default ->
                        String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDetalleVenta.setModel(model);

        // Ocultar columnas que no necesita ver el usuario
        tblDetalleVenta.getColumnModel().getColumn(0).setMinWidth(0);
        tblDetalleVenta.getColumnModel().getColumn(0).setMaxWidth(0);
        tblDetalleVenta.getColumnModel().getColumn(5).setMinWidth(0);
        tblDetalleVenta.getColumnModel().getColumn(5).setMaxWidth(0);
    }

    private void buscarProducto() {
        String busqueda = TxtBuscarID.getText().trim();
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un término de búsqueda",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductosDialog dialog = new ProductosDialog(this);
        dialog.cargarProductos(busqueda);
        dialog.setVisible(true);

        Object[] producto = dialog.getProductoSeleccionado();
        if (producto != null) {
            agregarProductoAVenta(producto);
            TxtBuscarID.setText("");
        }
    }

    private void agregarProductoAVenta(Object[] producto) {
        try {
            // Extraer datos del producto según el nuevo formato
            Integer idProducto = (Integer) producto[0];
            String nombre = (String) producto[1];
            Double precio = (Double) producto[2];
            Integer cantidad = (Integer) producto[3];
            Integer stock = (Integer) producto[4];
            Float dosis = (Float) producto[5];
            String unidad = (String) producto[6];
            String lote = (String) producto[7];
            String tipoMed = (String) producto[8];

            // Validar stock
            if (cantidad > stock) {
                JOptionPane.showMessageDialog(this,
                        "Cantidad supera el stock disponible (" + stock + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();

            // Crear nombre completo para mostrar
            String nombreCompleto = nombre;
            if (dosis != null && unidad != null && !unidad.equals("N/A")) {
                nombreCompleto += " (" + dosis + " " + unidad + ")";
            }
            if (lote != null && !lote.trim().isEmpty()) {
                nombreCompleto += " [Lote: " + lote + "]";
            }

            // Buscar si el producto ya existe en la venta
            for (int i = 0; i < model.getRowCount(); i++) {
                Integer rowIdProducto = (Integer) model.getValueAt(i, 0);
                String rowLote = (String) model.getValueAt(i, 5);

                // Comparar por ID y lote
                if (idProducto.equals(rowIdProducto)
                        && ((lote == null && rowLote == null)
                        || (lote != null && lote.equals(rowLote)))) {

                    // Producto existente - sumar cantidad
                    Integer cantidadActual = (Integer) model.getValueAt(i, 3);
                    Integer nuevaCantidad = cantidadActual + cantidad;

                    // Verificar stock total
                    if (nuevaCantidad > stock) {
                        JOptionPane.showMessageDialog(this,
                                "La cantidad total (" + nuevaCantidad
                                + ") supera el stock disponible (" + stock + ")",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    model.setValueAt(nuevaCantidad, i, 3);
                    model.setValueAt(precio * nuevaCantidad, i, 4);
                    actualizarTotal();
                    return;
                }
            }

            // Producto nuevo - agregarlo
            double subtotal = precio * cantidad;
            model.addRow(new Object[]{
                idProducto,                                 // 0: ID (oculto)
                nombreCompleto,                             // 1: Nombre completo
                precio,                                     // 2: Precio unitario
                cantidad,                                   // 3: Cantidad
                subtotal,                                   // 4: Subtotal
                lote,                                       // 5: Lote (oculto)
                dosis != null ? dosis.toString() : null,    // 6: Dosis (oculto)
                unidad                                      // 7: Unidad (oculto)
            });

            actualizarTotal();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar producto: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarTotal() {
        double total = 0;
        DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Double subtotal = (Double) model.getValueAt(i, 4);
            total += subtotal;
        }
    }

    private void procesarVenta() {
        DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarStocks()) {
            return;
        }

        int idEmpleado = Inicio_sesión.SesionUsuario.idEmpleado;

        try (Connection conn = Farmacia.ConectarBD()) {
            conn.setAutoCommit(false);

            // 1. Registrar la factura
            int idFactura = registrarFactura(conn, idEmpleado);

            // 2. Registrar detalles
            registrarDetallesVenta(conn, idFactura);

            // 3. Calcular total con IVA
            double totalConIVA = calcularTotalDesdeBD(conn, idFactura);

            conn.commit();

            // 4. Mostrar confirmación con el diálogo personalizado
            mostrarConfirmacionVenta(idFactura, totalConIVA);

            // 5. Limpiar tabla solo si la venta fue confirmada
            // (esto se maneja dentro del diálogo)
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la venta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarConfirmacionVenta(int idFactura, double totalConIVA) {
        try {
            // Crear y mostrar el diálogo de confirmación
            ConfirmacionVentaDialog confirmDialog = new ConfirmacionVentaDialog(
                    this,
                    totalConIVA,
                    tblDetalleVenta,
                    idFactura
            );

            // Centrar el diálogo
            confirmDialog.setLocationRelativeTo(this);

            // Mostrar el diálogo (modal)
            confirmDialog.setVisible(true);

            // Verificar si la venta fue confirmada
            if (confirmDialog.isConfirmed()) {
                // Limpiar tabla después de confirmar
                limpiarTablaVenta();

            } else {
                // Si se canceló, podría revertir la transacción aquí
                // Por ahora solo mostramos un mensaje
                JOptionPane.showMessageDialog(this,
                        "La venta fue cancelada por el usuario.",
                        "Venta Cancelada",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al mostrar confirmación de venta: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int registrarFactura(Connection conn, int idEmpleado) throws SQLException {
        try (CallableStatement stmt = conn.prepareCall("{call RegistrarVenta(?, ?, ?)}")) {
            stmt.setInt(1, idEmpleado);
            stmt.registerOutParameter(2, Types.INTEGER); // idFactura
            stmt.registerOutParameter(3, Types.DECIMAL); // total (no usado)
            stmt.execute();
            return stmt.getInt(2);
        }
    }

    private void registrarDetallesVenta(Connection conn, int idFactura) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Integer idProducto = (Integer) model.getValueAt(i, 0);
            Double precio = (Double) model.getValueAt(i, 2);
            Integer cantidad = (Integer) model.getValueAt(i, 3);

            try (CallableStatement stmt = conn.prepareCall("{call RegistrarDetalleVenta(?, ?, ?, ?)}")) {
                stmt.setInt(1, idFactura);
                stmt.setInt(2, idProducto);
                stmt.setDouble(3, cantidad.doubleValue());
                stmt.setDouble(4, precio);
                stmt.execute();
            }
        }
    }

    private double calcularTotalDesdeBD(Connection conn, int idFactura) throws SQLException {
        String sql = "SELECT CalcularTotalFactura(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setDouble(2, 16.0); // IVA del 16%

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
                throw new SQLException("No se pudo calcular el total");
            }
        }
    }

    private boolean validarStocks() {
        DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Integer idProducto = (Integer) model.getValueAt(i, 0);
            Integer cantidadVenta = (Integer) model.getValueAt(i, 3);

            try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                    "SELECT existencia, nombre FROM productos WHERE idproductos = ?")) {

                stmt.setInt(1, idProducto);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int stockActual = rs.getInt("existencia");
                        String nombre = rs.getString("nombre");

                        if (cantidadVenta > stockActual) {
                            JOptionPane.showMessageDialog(this,
                                    "El producto '" + nombre + "' solo tiene "
                                    + stockActual + " unidades disponibles",
                                    "Error de Stock", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al verificar el stock: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void limpiarTablaVenta() {
        DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();
        model.setRowCount(0);
        actualizarTotal();
    }

    private void borrarProductoSeleccionado() {
        int filaSeleccionada = tblDetalleVenta.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un producto de la tabla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este producto de la venta?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tblDetalleVenta.getModel();
            model.removeRow(filaSeleccionada);
            actualizarTotal();
        }
    }

    /**
     * Valida credenciales de administrador Nota: En producción usar hash
     * SHA-256 para las contraseñas
     */
    private boolean validarAdministrador(String usuario, String clave) {
        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM usuarios u "
                + "INNER JOIN empleados e ON u.empleados_idempleados = e.idempleados "
                + "WHERE u.nombre = ? AND u.clave = SHA2(?, 256) AND u.tipo_usuario = 1 AND u.activo = 1")) {

            stmt.setString(1, usuario);
            stmt.setString(2, clave);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al validar administrador: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlFondo = new javax.swing.JPanel();
        TxtBuscarID = new javax.swing.JTextField();
        PnlBorrarProd = new javax.swing.JPanel();
        BtnBorrarProd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalleVenta = new javax.swing.JTable();
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
        LblTitulo2 = new javax.swing.JLabel();
        LblTitulo3 = new javax.swing.JLabel();
        PnlGenerarV = new javax.swing.JPanel();
        BtnGenerar = new javax.swing.JButton();
        PnlCancelar = new javax.swing.JPanel();
        BtnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PnlFondo.setBackground(new java.awt.Color(255, 255, 255));

        TxtBuscarID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarIDActionPerformed(evt);
            }
        });

        PnlBorrarProd.setBackground(new java.awt.Color(255, 255, 255));

        BtnBorrarProd.setBackground(new java.awt.Color(255, 255, 255));
        BtnBorrarProd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnBorrarProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Borrar.png"))); // NOI18N
        BtnBorrarProd.setText("Borrar producto");
        BtnBorrarProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnBorrarProdMouseClicked(evt);
            }
        });
        BtnBorrarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBorrarProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBorrarProdLayout = new javax.swing.GroupLayout(PnlBorrarProd);
        PnlBorrarProd.setLayout(PnlBorrarProdLayout);
        PnlBorrarProdLayout.setHorizontalGroup(
            PnlBorrarProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnBorrarProd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlBorrarProdLayout.setVerticalGroup(
            PnlBorrarProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnBorrarProd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tblDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Producto", "Nombre", "Precio Unitario", "Cantidad", "Subtotal", "Title 6", "Title 7", "Title 8"
            }
        ));
        jScrollPane1.setViewportView(tblDetalleVenta);

        javax.swing.GroupLayout PnlFondoLayout = new javax.swing.GroupLayout(PnlFondo);
        PnlFondo.setLayout(PnlFondoLayout);
        PnlFondoLayout.setHorizontalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlFondoLayout.createSequentialGroup()
                        .addComponent(TxtBuscarID, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(PnlBorrarProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        PnlFondoLayout.setVerticalGroup(
            PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFondoLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(PnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtBuscarID, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnlBorrarProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
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

        LblTitulo2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo2.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo2.setText("Registre nuevas ventas.");
        LblTitulo2.setOpaque(true);

        LblTitulo3.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo3.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo3.setText("Ventas");

        PnlGenerarV.setBackground(new java.awt.Color(217, 217, 217));

        BtnGenerar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Generar.png"))); // NOI18N
        BtnGenerar.setText("Generar venta");
        BtnGenerar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnGenerarMouseClicked(evt);
            }
        });
        BtnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGenerarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlGenerarVLayout = new javax.swing.GroupLayout(PnlGenerarV);
        PnlGenerarV.setLayout(PnlGenerarVLayout);
        PnlGenerarVLayout.setHorizontalGroup(
            PnlGenerarVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnGenerar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlGenerarVLayout.setVerticalGroup(
            PnlGenerarVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnGenerar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );

        PnlCancelar.setBackground(new java.awt.Color(217, 217, 217));

        BtnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Cancelar.png"))); // NOI18N
        BtnCancelar.setText("Borrar venta");
        BtnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnCancelarMouseClicked(evt);
            }
        });
        BtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlCancelarLayout = new javax.swing.GroupLayout(PnlCancelar);
        PnlCancelar.setLayout(PnlCancelarLayout);
        PnlCancelarLayout.setHorizontalGroup(
            PnlCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );
        PnlCancelarLayout.setVerticalGroup(
            PnlCancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblTitulo3)
                                    .addComponent(LblTitulo2))
                                .addGap(507, 507, 507)
                                .addComponent(PnlGenerarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(PnlCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlGenerarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LblTitulo3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblTitulo2))
                    .addComponent(PnlCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addComponent(PnlFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void BtnInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInventarioActionPerformed
        Inventario inv = new Inventario();

        this.setVisible(false);

        inv.setVisible(true);
        inv.setLocationRelativeTo(null);
        inv.setTitle("Inventario");
    }//GEN-LAST:event_BtnInventarioActionPerformed

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void BtnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGenerarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGenerarActionPerformed

    private void BtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCancelarActionPerformed

    private void TxtBuscarIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarIDActionPerformed

    private void BtnBorrarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBorrarProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBorrarProdActionPerformed

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

    private void BtnGenerarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnGenerarMouseClicked
        if (tblDetalleVenta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos en la venta",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validarStocks()) {
            procesarVenta();
        }
    }//GEN-LAST:event_BtnGenerarMouseClicked

    private void BtnBorrarProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnBorrarProdMouseClicked
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            borrarProductoSeleccionado();
        } else {
            // Panel para ingresar credenciales
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField txtUsuario = new JTextField();
            JPasswordField txtClave = new JPasswordField();

            panel.add(new JLabel("Usuario Administrador:"));
            panel.add(txtUsuario);
            panel.add(new JLabel("Contraseña:"));
            panel.add(txtClave);
            panel.add(new JLabel("")); // Espacio vacío
            panel.add(new JLabel("")); // Espacio vacío

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Ingrese credenciales de administrador",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String usuario = txtUsuario.getText().trim();
                String clave = new String(txtClave.getPassword());

                if (validarAdministrador(usuario, clave)) {
                    borrarProductoSeleccionado();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Credenciales incorrectas o no tiene privilegios",
                            "Acceso denegado",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_BtnBorrarProdMouseClicked

    private void BtnCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnCancelarMouseClicked
        if ("Administrador".equals(Inicio_sesión.SesionUsuario.puesto)) {
            limpiarTablaVenta();
        } else {
            // Panel para ingresar credenciales
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField txtUsuario = new JTextField();
            JPasswordField txtClave = new JPasswordField();

            panel.add(new JLabel("Usuario Administrador:"));
            panel.add(txtUsuario);
            panel.add(new JLabel("Contraseña:"));
            panel.add(txtClave);
            panel.add(new JLabel("")); // Espacio vacío
            panel.add(new JLabel("")); // Espacio vacío

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Ingrese credenciales de administrador",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String usuario = txtUsuario.getText().trim();
                String clave = new String(txtClave.getPassword());

                if (validarAdministrador(usuario, clave)) {
                    borrarProductoSeleccionado();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Credenciales incorrectas o no tiene privilegios",
                            "Acceso denegado",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_BtnCancelarMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBorrarProd;
    private javax.swing.JButton BtnCancelar;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnGenerar;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnProveedores1;
    private javax.swing.JButton BtnRequisiciones1;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblTitulo2;
    private javax.swing.JLabel LblTitulo3;
    private javax.swing.JPanel PnlBarraL;
    private javax.swing.JPanel PnlBorrarProd;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlCancelar;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlFondo;
    private javax.swing.JPanel PnlGenerarV;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarID;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDetalleVenta;
    // End of variables declaration//GEN-END:variables
}
