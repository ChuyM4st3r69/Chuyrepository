package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import java.sql.CallableStatement;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import metodos.BotonTransparente;
import metodos.MultiLineCellRenderer;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.DateChooserDesign;
import metodos.InfoFarmaDialog;
import metodos.JOptionPaneError;
import metodos.JOptionPaneMensaje;
import metodos.SpinnerDesign;
import metodos.Tables;
import metodos.estiloComboBox;

/**
 *
 * @author Jesus Castillo
 */
public class AltaProducto extends javax.swing.JFrame {

    public AltaProducto() {
        initComponents();
        cargarUnidadesEnCombo();
        cargarTiposMedicamentosEnCombo();
        cargarProveedoresEnCombo();
        mostrarProductos();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        //Tables.personalizarTabla(TblProductos);
        TblProductos.getTableHeader().setReorderingAllowed(false);

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnInicio);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Descripción del producto...", TxtDescripcionProd);
        TextPrompt placeHolder3 = new TextPrompt("Lote del producto...", TxtLote);
        TextPrompt placeHolder4 = new TextPrompt("Nombre del producto...", TxtNombreProd);
        TextPrompt placeHolder5 = new TextPrompt("Precio del producto...", TxtPrecio);

        estiloComboBox.aplicarEstiloComboBox(CbdTipo);
        estiloComboBox.aplicarEstiloComboBox(CbdProveedores);
        estiloComboBox.aplicarEstiloComboBox(CbdUnidades);

        SpinnerDesign.aplicarEstiloSpinner(SpnDosis);
        SpinnerDesign.aplicarEstiloSpinner(SpnStock);
        SpinnerDesign.aplicarEstiloSpinner(SpnCant);

        DateChooserDesign.aplicarEstiloDateChooser(DtcAltaProd);

        Tables.personalizarTabla(TblProductos);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);
        TxtDescripcionProd.setBorder(redondearTxt);
        TxtNombreProd.setBorder(redondearTxt);
        TxtPrecio.setBorder(redondearTxt);
        TxtLote.setBorder(redondearTxt);

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

        // PnlRedondo
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlAltaProd, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        BotonTransparente.hacerBotonTransparente(BtnAltaProd);

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
                        lbl.getWidth(), // Ancho deseado = ancho del JLabel
                        lbl.getHeight(), // Alto deseado = alto del JLabel
                        Image.SCALE_DEFAULT // Algoritmo de escalado por defecto
                )
        );

        // Establece el icono escalado en el JLabel
        lbl.setIcon(this.icono);

        // Fuerza el repintado del componente para mostrar los cambios
        this.repaint();

    }

    public void altaProductoMedicamento() {
        Connection conn = null;

        try {
            // Validaciones de campos vacíos
            if (TxtNombreProd.getText().trim().isEmpty()
                    || TxtDescripcionProd.getText().trim().isEmpty()
                    || TxtPrecio.getText().trim().isEmpty()
                    || TxtLote.getText().trim().isEmpty()) {
                JOptionPaneError.showError(this, "Todos los campos de texto deben estar llenos.");
                return;
            }

            // Validar formato y valor del precio
            float precio;
            try {
                precio = Float.parseFloat(TxtPrecio.getText().trim());
                if (precio <= 0) {
                    JOptionPaneError.showError(this, "El precio debe ser mayor que cero.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPaneError.showError(this, "El precio no es válido.");
                return;
            }

            // Validar valores de los spinners (cantidad y stock)
            float cantidad = ((Number) SpnDosis.getValue()).floatValue();
            int stock = ((Number) SpnStock.getValue()).intValue();
            if (cantidad <= 0 || stock <= 0) {
                JOptionPaneError.showError(this, "La dosis y el stock deben ser mayores a cero.");
                return;
            }

            // Validar fecha de vencimiento
            java.util.Date utilDate = DtcAltaProd.getDate();
            if (utilDate == null) {
                JOptionPaneError.showError(this, "Selecciona una fecha de vencimiento.");
                return;
            }
            if (utilDate.before(new java.util.Date())) {
                JOptionPaneError.showError(this, "La fecha de vencimiento no puede ser anterior a hoy.");
                return;
            }
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            // Validar ComboBoxes
            if (CbdProveedores.getSelectedItem() == null || CbdUnidades.getSelectedItem() == null || CbdTipo.getSelectedItem() == null) {
                JOptionPaneError.showError(this, "Selecciona proveedor, unidad y tipo de medicamento.");
                return;
            }

            // Obtener valores seleccionados en comboboxes
            String proveedorSeleccionado = CbdProveedores.getSelectedItem().toString();
            String unidadSeleccionada = CbdUnidades.getSelectedItem().toString();
            String tipoMedSeleccionado = CbdTipo.getSelectedItem().toString();

            // Si pasó todas las validaciones, continúa con la conexión
            conn = Farmacia.ConectarBD();

            // Preparar llamada al procedimiento almacenado
            CallableStatement cs = conn.prepareCall("{CALL AltaProducto(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            // Establecer parámetros del procedimiento
            cs.setFloat(1, precio);
            cs.setFloat(2, cantidad);
            cs.setInt(3, stock);
            cs.setString(4, TxtNombreProd.getText().trim());
            cs.setString(5, TxtDescripcionProd.getText().trim());
            cs.setDate(6, sqlDate);
            cs.setInt(7, obtenerIdProveedor(proveedorSeleccionado));
            cs.setInt(8, obtenerIdUnidad(unidadSeleccionada));
            cs.setString(9, TxtLote.getText().trim());
            cs.setInt(10, obtenerIdTipoMedicamento(tipoMedSeleccionado));
            float dosis = ((Number) SpnDosis.getValue()).floatValue();

            // Establecer el parámetro de dosis
            cs.setFloat(11, dosis);

            // Ejecutar procedimiento
            boolean tieneResultado = cs.execute();
            if (tieneResultado) {
                ResultSet rs = cs.getResultSet();
                if (rs.next()) {
                    int idProducto = rs.getInt("idproductos");
                    JOptionPaneMensaje dialog = new JOptionPaneMensaje(this, TxtNombreProd.getText().trim(), "Medicamento registrado con ID: " + idProducto);
                    dialog.setVisible(true);
                    mostrarProductos(); // Actualizar vista
                    mostrarProductos();
                    limpiarCamposProducto(); // Limpiar formulario
                } else {
                    
                    JOptionPaneError.showError(this, "No se pudo obtener el ID del producto insertado.");
                }
            }
            
            mostrarProductos();

        } catch (SQLException e) {
            JOptionPaneError.showError(this, "Error al registrar medicamento: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close(); // Cerrar conexión
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCamposProducto() {
        TxtNombreProd.setText("");          // Limpia el campo de nombre del producto
        TxtDescripcionProd.setText("");     // Limpia el campo de descripción
        TxtPrecio.setText("");              // Limpia el campo de precio
        TxtLote.setText("");                // Limpia el campo de número de lote

        // Reiniciar selector de fecha
        DtcAltaProd.setDate(null);          // Elimina la fecha seleccionada

        // Restablecer valores de spinners
        SpnDosis.setValue(0f);                // Establece cantidad (float) a 0
        SpnStock.setValue(0);               // Establece stock (entero) a 0

        // Reiniciar comboboxes (si tienen elementos)
        if (CbdProveedores.getItemCount() > 0) {
            CbdProveedores.setSelectedIndex(0);     // Selecciona el primer proveedor
        }
        if (CbdUnidades.getItemCount() > 0) {
            CbdUnidades.setSelectedIndex(0);        // Selecciona la primera unidad
        }
        if (CbdTipo.getItemCount() > 0) {
            CbdTipo.setSelectedIndex(0);            // Selecciona el primer tipo de medicamento
        }
    }

    private int obtenerIdUnidad(String nombreUnidad) throws SQLException {
        // Establecer conexión con la base de datos
        Connection con = Farmacia.ConectarBD();

        // Preparar consulta SQL parametrizada
        String sql = "SELECT idunidades FROM unidades WHERE nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        // Asignar parámetro a la consulta
        ps.setString(1, nombreUnidad);

        // Ejecutar consulta y obtener resultados
        ResultSet rs = ps.executeQuery();

        // Procesar resultados
        if (rs.next()) {
            return rs.getInt(1); // Obtener el ID de la primera columna
        }

        // Lanzar excepción si la unidad no existe
        throw new SQLException("Unidad no encontrada");
    }

    private int obtenerIdTipoMedicamento(String tipo) throws SQLException {
        // Establecer conexión a la base de datos
        Connection con = Farmacia.ConectarBD();

        // Consulta SQL parametrizada para buscar por nombre
        String sql = "SELECT idtipos_medicamentos FROM tipos_medicamentos WHERE nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        // Asignar parámetro a la consulta
        ps.setString(1, tipo);

        // Ejecutar consulta
        ResultSet rs = ps.executeQuery();

        // Procesar resultados
        if (rs.next()) {
            return rs.getInt(1); // Obtener ID de la primera columna
        }

        // Lanzar excepción si no se encuentra el tipo
        throw new SQLException("Tipo medicamento no encontrado");
    }

    private int obtenerIdProveedor(String proveedor) throws SQLException {
        // Establecer conexión con la base de datos
        Connection con = Farmacia.ConectarBD();

        // Consulta SQL parametrizada para buscar por nombre
        String sql = "SELECT idproveedores FROM proveedores WHERE nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        // Asignar parámetro a la consulta
        ps.setString(1, proveedor);

        // Ejecutar consulta
        ResultSet rs = ps.executeQuery();

        // Procesar resultados
        if (rs.next()) {
            return rs.getInt(1);
        }

        // Lanzar excepción si no se encuentra el proveedor
        throw new SQLException("Proveedor no encontrado");
    }

    public void mostrarProductos() {
        // Obtener modelo de tabla y limpiar datos existentes
        DefaultTableModel modelo = (DefaultTableModel) TblProductos.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        try (Connection conn = Farmacia.ConectarBD()) {
            // Consulta SQL actualizada para incluir la dosis
            String sql = """
        SELECT 
            p.nombre AS producto,
            dm.lote,
            dm.dosis,
            tm.nombre AS categoria,
            p.existencia,
            p.precio_unitario,
            pr.nombre AS proveedor,
            p.fecha_caducidad,
            CASE
                WHEN p.existencia <= 10 THEN 'Crítico'
                WHEN p.existencia <= 30 THEN 'Bajo'
                ELSE 'Normal'
            END AS estado
        FROM productos p
        JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
        JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos
        JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores
        WHERE pr.activo = 1;
        """;

            // Ejecutar consulta preparada
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Procesar cada fila de resultados
            while (rs.next()) {
                // Formatear datos para visualización
                String producto = rs.getString("producto")
                        + "\nLote: " + rs.getString("lote")
                        + "\nDosis: " + rs.getFloat("dosis") + " mg";
                String categoria = rs.getString("categoria");
                String stock = rs.getInt("existencia") + " unid.";
                String precio = "$" + rs.getFloat("precio_unitario");
                String proveedor = rs.getString("proveedor");

                // Tomar la fecha real de la base de datos
                java.sql.Date fecha = rs.getDate("fecha_caducidad");
                String vencimiento = (fecha != null)
                        ? new SimpleDateFormat("dd-MM-yyyy").format(fecha)
                        : "N/D";

                String estado = rs.getString("estado");

                // Agregar fila al modelo de tabla
                modelo.addRow(new Object[]{
                    producto, categoria, stock, precio, proveedor, vencimiento, estado
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + ex.getMessage());
        }

        // Configurar renderizado especial para celdas (multilínea)
        for (int i = 0; i < TblProductos.getColumnCount(); i++) {
            TblProductos.getColumnModel().getColumn(i).setCellRenderer(new MultiLineCellRenderer());
        }
        Tables.personalizarTabla(TblProductos);
    }

    public void actualizarResumenProductos() {
        // Contadores para cada categoría
        int total = 0;      // Total de productos
        int normal = 0;     // Productos con existencia > 30
        int bajo = 0;       // Productos con existencia ≤ 30
        int critico = 0;    // Productos con existencia ≤ 10

        try (Connection conn = Farmacia.ConectarBD()) {
            // Consulta para obtener las existencias de todos los productos
            String sql = "SELECT existencia FROM productos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Procesar cada producto
            while (rs.next()) {
                int existencia = rs.getInt("existencia");
                total++; // Incrementar contador total

                // Clasificar por nivel de existencia
                if (existencia <= 10) {
                    critico++;
                } else if (existencia <= 30) {
                    bajo++;
                } else {
                    normal++;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar resumen: " + e.getMessage());
        }
    }

    private void cargarUnidadesEnCombo() {
        try {
            Connection con = farmacia.Farmacia.ConectarBD();

            // Consulta SQL para obtener nombres de unidades ordenados
            String sql = "SELECT nombre FROM unidades ORDER BY nombre";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            CbdUnidades.removeAllItems(); // Limpiar comboBox antes de cargar nuevos items

            // Recorrer resultados y agregar al comboBox
            while (rs.next()) {
                CbdUnidades.addItem(rs.getString("nombre")); // Agrega cada unidad
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar unidades: " + e.getMessage());
        }
    }

    private void cargarTiposMedicamentosEnCombo() {
        try {
            Connection con = farmacia.Farmacia.ConectarBD();
            String sql = "SELECT nombre FROM tipos_medicamentos ORDER BY nombre";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            CbdTipo.removeAllItems();  // Limpiar el comboBox antes de cargar nuevos items

            // Recorrer los resultados y agregar cada tipo al comboBox
            while (rs.next()) {
                CbdTipo.addItem(rs.getString("nombre")); // Agrega cada tipo
            }

            con.close();
        } catch (SQLException e) {
            // Mostrar mensaje de error con más detalles
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de medicamentos: " + e.getMessage());
        }
    }

    private void cargarProveedoresEnCombo() {
        try {
            Connection con = farmacia.Farmacia.ConectarBD();
            String sql = "SELECT nombre FROM proveedores WHERE activo = 1 ORDER BY nombre";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            CbdProveedores.removeAllItems(); // Limpiar el comboBox antes de cargar nuevos items

            // Recorrer resultados y agregar cada proveedor al comboBox
            while (rs.next()) {
                CbdProveedores.addItem(rs.getString("nombre")); // Agrega el nombre del proveedor
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        LblTitulo1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TxtNombreProd = new javax.swing.JTextField();
        TxtDescripcionProd = new javax.swing.JTextField();
        LblStock = new javax.swing.JLabel();
        LblPrecio = new javax.swing.JLabel();
        LblProveedor = new javax.swing.JLabel();
        TxtPrecio = new javax.swing.JTextField();
        LblVencimiento = new javax.swing.JLabel();
        PnlAltaProd = new javax.swing.JPanel();
        BtnAltaProd = new javax.swing.JButton();
        LblUnidad = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TxtLote = new javax.swing.JTextField();
        CbdUnidades = new javax.swing.JComboBox<>();
        CbdTipo = new javax.swing.JComboBox<>();
        CbdProveedores = new javax.swing.JComboBox<>();
        DtcAltaProd = new com.toedter.calendar.JDateChooser();
        SpnDosis = new javax.swing.JSpinner();
        SpnStock = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        SpnCant = new javax.swing.JSpinner();

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

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Alta Producto");

        TblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Producto", "Categoria", "Stock", "Precio", "Proveedor", "Vencimiento", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TblProductos);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Nombre:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setText("Descripcion:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel3.setText("Dosis:");

        TxtNombreProd.setBackground(new java.awt.Color(242, 242, 242));
        TxtNombreProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNombreProdActionPerformed(evt);
            }
        });

        TxtDescripcionProd.setBackground(new java.awt.Color(242, 242, 242));

        LblStock.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblStock.setText("Stock:");

        LblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblPrecio.setText("Precio:");

        LblProveedor.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblProveedor.setText("Proveedor:");

        TxtPrecio.setBackground(new java.awt.Color(242, 242, 242));

        LblVencimiento.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblVencimiento.setText("Vencimiento:");

        PnlAltaProd.setBackground(new java.awt.Color(217, 217, 217));

        BtnAltaProd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnAltaProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProd.png"))); // NOI18N
        BtnAltaProd.setText("Alta Producto");
        BtnAltaProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAltaProdMouseClicked(evt);
            }
        });
        BtnAltaProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAltaProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlAltaProdLayout = new javax.swing.GroupLayout(PnlAltaProd);
        PnlAltaProd.setLayout(PnlAltaProdLayout);
        PnlAltaProdLayout.setHorizontalGroup(
            PnlAltaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlAltaProdLayout.setVerticalGroup(
            PnlAltaProdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnAltaProd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );

        LblUnidad.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblUnidad.setText("Unidad:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel5.setText("Lote:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setText("Tipo:");

        TxtLote.setBackground(new java.awt.Color(242, 242, 242));

        CbdUnidades.setBackground(new java.awt.Color(242, 242, 242));
        CbdUnidades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CbdTipo.setBackground(new java.awt.Color(242, 242, 242));
        CbdTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        CbdProveedores.setBackground(new java.awt.Color(242, 242, 242));
        CbdProveedores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CbdProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbdProveedoresActionPerformed(evt);
            }
        });

        SpnDosis.setModel(new javax.swing.SpinnerNumberModel(0.0f, 0.0f, null, 1.0f));

        SpnStock.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setText("Cantidad");

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(80, 80, 80)
                                        .addComponent(jLabel2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(LblVencimiento, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TxtDescripcionProd, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .addComponent(TxtNombreProd)
                                    .addComponent(DtcAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SpnDosis))
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LblProveedor)
                                            .addComponent(LblPrecio))
                                        .addGap(40, 40, 40)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(CbdProveedores, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(TxtPrecio)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LblUnidad)
                                            .addComponent(jLabel5))
                                        .addGap(70, 70, 70)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TxtLote, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                            .addComponent(CbdUnidades, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4)
                                    .addComponent(LblStock))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SpnCant, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(CbdTipo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SpnStock)
                                    .addComponent(PnlAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(LblTitulo1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(77, 77, 77))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TxtNombreProd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblPrecio)
                    .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblStock)
                    .addComponent(SpnStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblVencimiento)
                            .addComponent(TxtLote, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(TxtDescripcionProd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblProveedor)
                            .addComponent(CbdProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(CbdTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(DtcAltaProd, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SpnCant, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PnlAltaProd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(SpnDosis, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(LblUnidad)
                                .addComponent(CbdUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
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

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void TxtNombreProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNombreProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNombreProdActionPerformed

    private void BtnAltaProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAltaProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAltaProdActionPerformed

    private void BtnAltaProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAltaProdMouseClicked
        altaProductoMedicamento();
    }//GEN-LAST:event_BtnAltaProdMouseClicked

    private void CbdProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbdProveedoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbdProveedoresActionPerformed

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
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnProveedores1;
    private javax.swing.JButton BtnRequisiciones1;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CbdProveedores;
    private javax.swing.JComboBox<String> CbdTipo;
    private javax.swing.JComboBox<String> CbdUnidades;
    private com.toedter.calendar.JDateChooser DtcAltaProd;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblPrecio;
    private javax.swing.JLabel LblProveedor;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblStock;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblUnidad;
    private javax.swing.JLabel LblVencimiento;
    private javax.swing.JPanel PnlAltaProd;
    private javax.swing.JPanel PnlBarraL;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JSpinner SpnCant;
    private javax.swing.JSpinner SpnDosis;
    private javax.swing.JSpinner SpnStock;
    private javax.swing.JTable TblProductos;
    public javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtDescripcionProd;
    private javax.swing.JTextField TxtLote;
    private javax.swing.JTextField TxtNombreProd;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
