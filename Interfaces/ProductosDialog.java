package Interfaces;

import farmacia.Farmacia;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.table.*;

/**
 * Diálogo para selección de productos con datos corregidos según la BD
 *
 * @author Jesus Castillo
 */
public class ProductosDialog extends javax.swing.JDialog {

    private JTable tblProductos;
    private JSpinner spnCantidad;
    private JButton btnAgregar;
    private JButton btnCancelar;
    private boolean productoSeleccionado = false;

    public ProductosDialog(JFrame parent) {
        super(parent, "Seleccionar Producto", true);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        tblProductos = new JTable();
        configurarTabla();

        JPanel panelControles = new JPanel();
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        btnAgregar = new JButton("Agregar a Venta");
        btnCancelar = new JButton("Cancelar");

        panelControles.add(new JLabel("Cantidad:"));
        panelControles.add(spnCantidad);
        panelControles.add(btnAgregar);
        panelControles.add(btnCancelar);

        add(new JScrollPane(tblProductos), BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        configurarEventos();
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> {
            if (tblProductos.getSelectedRow() >= 0) {
                productoSeleccionado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() >= 0) {
                int stock = safeGetInt(tblProductos.getValueAt(tblProductos.getSelectedRow(), 2));
                int cantidad = 1;
                int minimo = 1;
                int maximo = Math.max(1, stock);
                cantidad = Math.min(cantidad, maximo);

                spnCantidad.setModel(new SpinnerNumberModel(cantidad, minimo, maximo, 1));
                spnCantidad.setToolTipText("Stock disponible: " + stock);
            }
        });

        spnCantidad.addChangeListener(e -> {
            if (tblProductos.getSelectedRow() >= 0) {
                int stock = safeGetInt(tblProductos.getValueAt(tblProductos.getSelectedRow(), 2));
                int cantidad = (int) spnCantidad.getValue();

                if (cantidad > stock) {
                    JOptionPane.showMessageDialog(this,
                            "La cantidad no puede exceder el stock disponible (" + stock + ")",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    spnCantidad.setValue(stock);
                }
            }
        });
    }

    private void configurarTabla() {
        // Modelo corregido según estructura de la BD
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID Producto", "Nombre", "Stock", "Precio", "Tipo Med.", "Dosis", "Unidad", "Lote", "Caducidad"}, 0) {

            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0 ->
                        Integer.class;    // ID Producto
                    case 2 ->
                        Integer.class;    // Stock
                    case 3 ->
                        Double.class;     // Precio
                    case 5 ->
                        Float.class;      // Dosis
                    default ->
                        String.class;     // Resto como String
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProductos.setModel(model);
        tblProductos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = tblProductos.getColumnModel();

        // Ocultar ID (primera columna)
        tblProductos.removeColumn(columnModel.getColumn(0));

        // Ajustar anchos de columnas visibles
        columnModel.getColumn(0).setPreferredWidth(200); // Nombre
        columnModel.getColumn(1).setPreferredWidth(60);  // Stock
        columnModel.getColumn(2).setPreferredWidth(80);  // Precio
        columnModel.getColumn(3).setPreferredWidth(100); // Tipo
        columnModel.getColumn(4).setPreferredWidth(80);  // Dosis
        columnModel.getColumn(5).setPreferredWidth(60);  // Unidad
        columnModel.getColumn(6).setPreferredWidth(80);  // Lote
        columnModel.getColumn(7).setPreferredWidth(100); // Caducidad

        configurarRenderers(columnModel);
    }

    private void configurarRenderers(TableColumnModel columnModel) {
        // Renderer para precio
        columnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            private final DecimalFormat formatter = new DecimalFormat("$#,##0.00");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                String formattedValue = "$0.00";
                if (value instanceof Number) {
                    formattedValue = formatter.format(((Number) value).doubleValue());
                }

                Component c = super.getTableCellRendererComponent(table, formattedValue,
                        isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });

        // Renderer para stock con alertas
        columnModel.getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                int stock = safeGetInt(value);
                Component c = super.getTableCellRendererComponent(table, stock,
                        isSelected, hasFocus, row, column);

                if (stock < 5) {
                    c.setBackground(new Color(255, 200, 200));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setForeground(stock == 0 ? Color.RED : Color.BLACK);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    public void cargarProductos(String nombre) {
        DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();
        model.setRowCount(0);

        // Query con filtro: solo productos cuya fecha de caducidad > 3 meses desde hoy
        String sql = """
        SELECT 
            p.idproductos,
            p.nombre,
            p.existencia,
            p.precio_unitario,
            COALESCE(tm.nombre, 'General') AS tipo_medicamento,
            dm.dosis,
            COALESCE(u.nombre, 'N/A') AS unidad_medida,
            dm.lote,
            DATE_FORMAT(p.fecha_caducidad, '%d/%m/%Y') AS fecha_caducidad
        FROM productos p
        LEFT JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
        LEFT JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos
        LEFT JOIN unidades u ON p.unidades_idunidades = u.idunidades
        INNER JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores
        WHERE p.nombre LIKE ? 
            AND p.existencia > 0 
            AND p.activo = 1 
            AND pr.activo = 1
            AND p.fecha_caducidad > CURDATE() + INTERVAL 3 MONTH
        ORDER BY p.nombre
        """;

        try (Connection conn = Farmacia.ConectarBD(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getInt("idproductos"),
                    rs.getString("nombre"),
                    rs.getInt("existencia"),
                    rs.getDouble("precio_unitario"),
                    rs.getString("tipo_medicamento"),
                    rs.getObject("dosis") != null ? rs.getFloat("dosis") : null,
                    rs.getString("unidad_medida"),
                    rs.getString("lote"),
                    rs.getString("fecha_caducidad")
                };
                model.addRow(fila);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Retorna los datos del producto seleccionado
     *
     * @return Array con: [idProducto, nombre, precio, cantidad, stock, dosis,
     * unidad, lote]
     */
    public Object[] getProductoSeleccionado() {
        if (!productoSeleccionado || tblProductos.getSelectedRow() < 0) {
            return null;
        }

        try {
            int row = tblProductos.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) tblProductos.getModel();

            // Obtener datos desde el modelo (recordar que ID está oculto pero presente)
            Integer idProducto = (Integer) model.getValueAt(row, 0);
            String nombre = (String) model.getValueAt(row, 1);
            Integer stock = (Integer) model.getValueAt(row, 2);
            Double precio = (Double) model.getValueAt(row, 3);
            String tipoMed = (String) model.getValueAt(row, 4);
            Float dosis = (Float) model.getValueAt(row, 5);
            String unidad = (String) model.getValueAt(row, 6);
            String lote = (String) model.getValueAt(row, 7);

            int cantidad = (int) spnCantidad.getValue();

            return new Object[]{
                idProducto, // 0: ID del producto
                nombre,     // 1: Nombre
                precio,     // 2: Precio unitario
                cantidad,   // 3: Cantidad seleccionada
                stock,      // 4: Stock disponible
                dosis,      // 5: Dosis
                unidad,     // 6: Unidad
                lote,       // 7: Lote
                tipoMed     // 8: Tipo de medicamento
            };

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener producto: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private int safeGetInt(Object value) {
        if (value == null) {
            return 0;
        }

        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            } else {
                String str = value.toString().trim();
                return str.isEmpty() ? 0 : Integer.parseInt(str);
            }
        } catch (Exception e) {
            System.err.println("Error convirtiendo a int: " + value + " - " + e.getMessage());
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
