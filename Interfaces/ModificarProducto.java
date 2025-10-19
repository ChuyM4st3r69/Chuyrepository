package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import metodos.AccesoDenegado;
import metodos.BotonTransparente;
import metodos.DateChooserDesign;
import metodos.InfoFarmaDialog;
import metodos.JOptionPaneError;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import metodos.estiloComboBox;

/**
 *
 * @author Jesus Castillo
 */
public class ModificarProducto extends javax.swing.JFrame {

    private ImageIcon imagen;
    private ImageIcon icono;

    public ModificarProducto() {
        initComponents();
        cargarProveedoresEnCombo();

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Buscar medicamentos por nombre...", TxtBuscarProd);
        TextPrompt placeHolder3 = new TextPrompt("Descripcion del producto...", TxtDescripcion);
        TextPrompt placeHolder4 = new TextPrompt("Nombre del producto...", TxtNomProd);
        TextPrompt placeHolder5 = new TextPrompt("Precio del producto...", TxtPrecio);
        TextPrompt placeHolder6 = new TextPrompt("Existencia del producto...", TxtStock);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);
        TxtBuscarProd.setBorder(redondearTxt);
        TxtDescripcion.setBorder(redondearTxt);
        TxtNomProd.setBorder(redondearTxt);
        TxtPrecio.setBorder(redondearTxt);
        TxtStock.setBorder(redondearTxt);

        this.pintarImagen(LblLogo, "src/icons/Logo.png");

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        estiloComboBox.aplicarEstiloComboBox(CboxProveedor);

        DateChooserDesign.aplicarEstiloDateChooser(DtcVencimientoNuevo);

        // BtnTransparente
        BotonTransparente.hacerBotonTransparente(BtnBuscar);
        BotonTransparente.hacerBotonTransparente(BtnModificarP);

        // PnlRedondo
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);
        RoundPanel.aplicarBordesRedondeados(PnlFarmaLab, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlBuscar, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));
        RoundPanel.aplicarBordesRedondeados(PnlModificarBtn, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

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

    private void buscarProductoPorNombre() {
        String nombre = TxtBuscarProd.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto.");
            return;
        }

        try (Connection con = farmacia.Farmacia.ConectarBD()) {
            // Consulta que incluye información de dosis
            String sql = """
            SELECT p.*, pr.nombre AS nombre_proveedor, 
                   dm.dosis, tm.nombre AS tipo_medicamento
            FROM productos p
            JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores
            LEFT JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
            LEFT JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos
            WHERE p.nombre = ?
            ORDER BY dm.dosis
        """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            List<ProductoInfo> productos = new ArrayList<>();

            while (rs.next()) {
                ProductoInfo producto = new ProductoInfo(
                        rs.getInt("idproductos"),
                        rs.getString("nombre"),
                        rs.getFloat("precio_unitario"),
                        rs.getInt("existencia"),
                        rs.getString("descripcion"),
                        rs.getBoolean("activo"),
                        rs.getString("nombre_proveedor"),
                        rs.getDate("fecha_caducidad"),
                        rs.getFloat("dosis"),
                        rs.getString("tipo_medicamento")
                );
                productos.add(producto);
            }

            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                return;
            }

            // Si solo hay un producto, mostrarlo directamente
            if (productos.size() == 1) {
                mostrarProducto(productos.get(0));
            } else {
                // Mostrar diálogo de selección para múltiples productos con mismo nombre
                mostrarSeleccionProductos(productos);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarSeleccionProductos(List<ProductoInfo> productos) {
        // Crear modelo para la tabla de selección
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Seleccionar", "ID", "Dosis", "Tipo", "Proveedor"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
            }
        };

        for (ProductoInfo producto : productos) {
            model.addRow(new Object[]{
                false, // Checkbox de selección
                producto.id,
                producto.dosis,
                producto.tipoMedicamento != null ? producto.tipoMedicamento : "N/A",
                producto.proveedor
            });
        }

        // Crear tabla con el modelo
        JTable tabla = new JTable(model);
        tabla.setPreferredScrollableViewportSize(new Dimension(500, 150));
        JScrollPane scrollPane = new JScrollPane(tabla);

        // Mostrar diálogo de selección
        int result = JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Seleccione el producto a modificar",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Buscar el producto seleccionado
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((Boolean) model.getValueAt(i, 0)) {
                    mostrarProducto(productos.get(i));
                    break;
                }
            }
        }
    }
    
    int idProd;

    private void mostrarProducto(ProductoInfo producto) {
        // Mostrar información en los campos
        LblNom.setText(producto.nombre);
        TxtNomProd.setText(producto.nombre);
        TxtDescripcion.setText(producto.descripcion);
        TxtPrecio.setText("$" + producto.precio);
        TxtStock.setText(producto.stock + " unid.");
        LblDescripB.setText(producto.descripcion);
        LblPrecioB.setText("$" + producto.precio);
        LblStockB.setText(producto.stock + " unid.");
        idProd = producto.id;

        // Estado
        RbnActivo.setSelected(producto.activo);
        RbnInactivo.setSelected(!producto.activo);

        // Fecha de vencimiento
        if (producto.fechaCaducidad != null) {
            DtcVencimientoNuevo.setDate(new java.util.Date(producto.fechaCaducidad.getTime()));
            LblVencimientoB.setText(producto.fechaCaducidad.toString());
        } else {
            DtcVencimientoNuevo.setDate(null);
            LblVencimientoB.setText("Sin fecha");
        }

        // Proveedor
        CboxProveedor.setSelectedItem(producto.proveedor);
        LblProv.setText(producto.proveedor);

        // Mostrar dosis en un label
        if (producto.dosis != null) {
            LblDosis.setText(" " + producto.dosis);
            LblDosis.setVisible(true);
        } else {
            LblDosis.setVisible(false);
        }

        // Mostrar tipo de medicamento si existe
        if (producto.tipoMedicamento != null) {
            LblTipoMedicamento.setText(producto.tipoMedicamento);
            LblTipoMedicamento.setVisible(true);
        } else {
            LblTipoMedicamento.setVisible(false);
        }
    }

    private void cargarProveedoresEnCombo() {
        try {
            Connection con = farmacia.Farmacia.ConectarBD();
            String sql = "SELECT nombre FROM proveedores WHERE activo = 1 ORDER BY nombre";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            CboxProveedor.removeAllItems(); // Limpiar antes

            while (rs.next()) {
                CboxProveedor.addItem(rs.getString("nombre")); // Agrega el nombre del proveedor
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void modificarProducto() {
        String nombreActual = LblNom.getText().trim();
        String nombreNuevo = TxtNomProd.getText().trim();
        String descripcion = TxtDescripcion.getText().trim();
        String proveedorNombre = CboxProveedor.getSelectedItem().toString();
        String precioTexto = TxtPrecio.getText().replace("$", "").trim();
        String stockTexto = TxtStock.getText().replace(" unid.", "").trim();
        boolean activo = RbnActivo.isSelected();

        if (nombreActual.isEmpty()) {
            JOptionPaneError.showError(this, "Busca un producto primero para modificar.");
            return;
        }

        // Validar que sea solo números con decimales
        if (!precioTexto.matches("^\\d+(\\.\\d{1,2})?$")) {
            JOptionPaneError.showError(this, "El precio nada mas debe de contener numeros");
            return;
        }

        java.util.Date utilDate = DtcVencimientoNuevo.getDate();
        if (utilDate == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una fecha de vencimiento.");
            return;
        }
        if (utilDate.before(new java.util.Date())) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede ser anterior a hoy.");
            return;
        }
        java.sql.Date fechaSQL = new java.sql.Date(utilDate.getTime());

        try (Connection con = farmacia.Farmacia.ConectarBD()) {
            int idProveedor = 0;
            PreparedStatement psProv = con.prepareStatement("SELECT idproveedores FROM proveedores WHERE nombre = ?");
            psProv.setString(1, proveedorNombre);
            ResultSet rsProv = psProv.executeQuery();
            if (rsProv.next()) {
                idProveedor = rsProv.getInt("idproveedores");
            } else {
                throw new SQLException("Proveedor no encontrado.");
            }

            String sqlUpdate = """
            UPDATE productos SET
                nombre = ?, descripcion = ?, precio_unitario = ?,
                existencia = ?, fecha_caducidad = ?, activo = ?, proveedores_idproveedores = ?
            WHERE idproductos = ?;
        """;
            PreparedStatement ps = con.prepareStatement(sqlUpdate);
            ps.setString(1, nombreNuevo);
            ps.setString(2, descripcion);
            ps.setFloat(3, Float.parseFloat(precioTexto));
            ps.setInt(4, Integer.parseInt(stockTexto));
            ps.setDate(5, fechaSQL);
            ps.setBoolean(6, activo);
            ps.setInt(7, idProveedor);
            ps.setInt(8, idProd);
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Producto modificado exitosamente.");
                buscarProductoPorNombre();
            } else {
                JOptionPane.showMessageDialog(this, "No se modificó el producto.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar producto: " + e.getMessage());
        }
    }
    

    // Clase auxiliar para almacenar información del producto
    private static class ProductoInfo {

        int id;
        String nombre;
        float precio;
        int stock;
        String descripcion;
        boolean activo;
        String proveedor;
        Date fechaCaducidad;
        Float dosis;
        String tipoMedicamento;
        
        public ProductoInfo(int id, String nombre, float precio, int stock, String descripcion,
                boolean activo, String proveedor, Date fechaCaducidad,
                Float dosis, String tipoMedicamento) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
            this.descripcion = descripcion;
            this.activo = activo;
            this.proveedor = proveedor;
            this.fechaCaducidad = fechaCaducidad;
            this.dosis = dosis;
            this.tipoMedicamento = tipoMedicamento;
        }
    }

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BtgActividad = new javax.swing.ButtonGroup();
        TxtPrecio = new javax.swing.JTextField();
        CboxProveedor = new javax.swing.JComboBox<>();
        TxtStock = new javax.swing.JTextField();
        LblTel = new javax.swing.JLabel();
        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        TxtDescripcion = new javax.swing.JTextField();
        LblEmpleado = new javax.swing.JLabel();
        LblRFC1 = new javax.swing.JLabel();
        TxtNomProd = new javax.swing.JTextField();
        LblDireccion = new javax.swing.JLabel();
        PnlModificarBtn = new javax.swing.JPanel();
        BtnModificarP = new javax.swing.JButton();
        PnlBuscar = new javax.swing.JPanel();
        BtnBuscar = new javax.swing.JButton();
        PnlFarmaLab = new javax.swing.JPanel();
        LblNom = new javax.swing.JLabel();
        LblCorreoProv1 = new javax.swing.JLabel();
        PnlFinal = new javax.swing.JPanel();
        LblRFC = new javax.swing.JLabel();
        LblProv = new javax.swing.JLabel();
        LblCorreoB = new javax.swing.JLabel();
        LblDescripB = new javax.swing.JLabel();
        PnlRbn = new javax.swing.JPanel();
        RbnActivo = new javax.swing.JRadioButton();
        RbnInactivo = new javax.swing.JRadioButton();
        LblPrecio = new javax.swing.JLabel();
        LblStock = new javax.swing.JLabel();
        LblVencimientoProd = new javax.swing.JLabel();
        LblPrecioB = new javax.swing.JLabel();
        LblStockB = new javax.swing.JLabel();
        LblVencimientoB = new javax.swing.JLabel();
        LblMDosis = new javax.swing.JLabel();
        LblDosis = new javax.swing.JLabel();
        LblTipo = new javax.swing.JLabel();
        LblTipoMedicamento = new javax.swing.JLabel();
        TxtBuscarProd = new javax.swing.JTextField();
        LblTitulo = new javax.swing.JLabel();
        LblTitulo1 = new javax.swing.JLabel();
        PnlBtn = new javax.swing.JPanel();
        PnlDecoracion = new javax.swing.JPanel();
        BtnVentas = new javax.swing.JButton();
        BtnRequisiciones = new javax.swing.JButton();
        LblFarmacia = new javax.swing.JLabel();
        PnlSombra = new javax.swing.JPanel();
        BtnInventario = new javax.swing.JButton();
        BtnConfig = new javax.swing.JButton();
        BtnUsers = new javax.swing.JButton();
        LblLogo = new javax.swing.JLabel();
        BtnFacturas = new javax.swing.JButton();
        BtnDevoluciones = new javax.swing.JButton();
        BtnInicio = new javax.swing.JButton();
        BtnProveedores = new javax.swing.JButton();
        LblVencimiento = new javax.swing.JLabel();
        DtcVencimientoNuevo = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        TxtPrecio.setBackground(new java.awt.Color(242, 242, 242));
        TxtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtPrecioActionPerformed(evt);
            }
        });

        CboxProveedor.setBackground(new java.awt.Color(242, 242, 242));

        TxtStock.setBackground(new java.awt.Color(242, 242, 242));

        LblTel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTel.setText("Precio:");

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Stock:");

        TxtDescripcion.setBackground(new java.awt.Color(242, 242, 242));

        LblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblEmpleado.setText("Nombre Producto:");

        LblRFC1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFC1.setText("Descripcion:");
        LblRFC1.setToolTipText("");

        TxtNomProd.setBackground(new java.awt.Color(242, 242, 242));
        TxtNomProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtNomProdActionPerformed(evt);
            }
        });

        LblDireccion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblDireccion.setText("Proveedor:");

        PnlModificarBtn.setBackground(new java.awt.Color(217, 217, 217));

        BtnModificarP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnModificarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnModificarP.setText("Modificar Producto");
        BtnModificarP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnModificarPMouseClicked(evt);
            }
        });
        BtnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnModificarPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlModificarBtnLayout = new javax.swing.GroupLayout(PnlModificarBtn);
        PnlModificarBtn.setLayout(PnlModificarBtnLayout);
        PnlModificarBtnLayout.setHorizontalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnModificarP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlModificarBtnLayout.setVerticalGroup(
            PnlModificarBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlModificarBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnModificarP, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PnlBuscar.setBackground(new java.awt.Color(217, 217, 217));

        BtnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/altaProv.png"))); // NOI18N
        BtnBuscar.setText("Buscar Producto");
        BtnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnBuscarMouseClicked(evt);
            }
        });
        BtnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlBuscarLayout = new javax.swing.GroupLayout(PnlBuscar);
        PnlBuscar.setLayout(PnlBuscarLayout);
        PnlBuscarLayout.setHorizontalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlBuscarLayout.setVerticalGroup(
            PnlBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlBuscarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PnlFarmaLab.setBackground(new java.awt.Color(255, 255, 255));

        LblNom.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N

        LblCorreoProv1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblCorreoProv1.setText("Descripcion:");

        PnlFinal.setBackground(new java.awt.Color(217, 217, 217));

        javax.swing.GroupLayout PnlFinalLayout = new javax.swing.GroupLayout(PnlFinal);
        PnlFinal.setLayout(PnlFinalLayout);
        PnlFinalLayout.setHorizontalGroup(
            PnlFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );
        PnlFinalLayout.setVerticalGroup(
            PnlFinalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        LblRFC.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblRFC.setText("Proveedor: ");

        PnlRbn.setBackground(new java.awt.Color(255, 255, 255));

        RbnActivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnActivo);
        RbnActivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnActivo.setText("Activo");
        RbnActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbnActivoActionPerformed(evt);
            }
        });

        RbnInactivo.setBackground(new java.awt.Color(255, 255, 255));
        BtgActividad.add(RbnInactivo);
        RbnInactivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        RbnInactivo.setText("Inactivo");
        RbnInactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbnInactivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlRbnLayout = new javax.swing.GroupLayout(PnlRbn);
        PnlRbn.setLayout(PnlRbnLayout);
        PnlRbnLayout.setHorizontalGroup(
            PnlRbnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlRbnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RbnActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RbnInactivo)
                .addContainerGap(148, Short.MAX_VALUE))
        );
        PnlRbnLayout.setVerticalGroup(
            PnlRbnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRbnLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(PnlRbnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbnInactivo)
                    .addComponent(RbnActivo))
                .addGap(27, 27, 27))
        );

        LblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblPrecio.setText("Precio:");

        LblStock.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblStock.setText("Stock:");

        LblVencimientoProd.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblVencimientoProd.setText("Vencimiento:");

        LblMDosis.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblMDosis.setText("Dosis:");

        LblTipo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTipo.setText("Tipo:");

        javax.swing.GroupLayout PnlFarmaLabLayout = new javax.swing.GroupLayout(PnlFarmaLab);
        PnlFarmaLab.setLayout(PnlFarmaLabLayout);
        PnlFarmaLabLayout.setHorizontalGroup(
            PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(LblNom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PnlRbn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PnlFarmaLabLayout.createSequentialGroup()
                            .addComponent(LblTipo)
                            .addGap(113, 113, 113)
                            .addComponent(LblTipoMedicamento, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PnlFarmaLabLayout.createSequentialGroup()
                            .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFarmaLabLayout.createSequentialGroup()
                                    .addComponent(LblMDosis)
                                    .addGap(104, 104, 104))
                                .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                                    .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(LblStock, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(LblPrecio, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(LblVencimientoProd, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGap(36, 36, 36)))
                            .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(LblVencimientoB, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                .addComponent(LblDosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblStockB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LblPrecioB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LblCorreoProv1)
                            .addComponent(LblRFC))
                        .addGap(44, 44, 44)
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                                .addComponent(LblProv, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(146, 146, 146)
                                .addComponent(LblCorreoB, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(LblDescripB, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlFarmaLabLayout.setVerticalGroup(
            PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(LblNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                                .addComponent(LblRFC)
                                .addGap(18, 18, 18)
                                .addComponent(LblCorreoProv1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFarmaLabLayout.createSequentialGroup()
                                .addComponent(LblProv, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(LblDescripB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(LblCorreoB, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblPrecioB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblPrecio))
                .addGap(18, 18, 18)
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblStockB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblStock))
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlFarmaLabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LblVencimientoB, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlFarmaLabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LblVencimientoProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblDosis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblMDosis))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(PnlFarmaLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblTipoMedicamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblTipo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlRbn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        TxtBuscarProd.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarProdActionPerformed(evt);
            }
        });

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Modificar información sobre productos.");
        LblTitulo.setOpaque(true);

        LblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblTitulo1.setForeground(new java.awt.Color(0, 48, 73));
        LblTitulo1.setText("Producto");

        PnlBtn.setBackground(new java.awt.Color(0, 119, 182));

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

        BtnInventario.setBackground(new java.awt.Color(242, 242, 242));
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
                .addGap(31, 31, 31)
                .addComponent(BtnInventario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PnlSombraLayout.setVerticalGroup(
            PnlSombraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlSombraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(BtnInventario)
                .addContainerGap(11, Short.MAX_VALUE))
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

        javax.swing.GroupLayout PnlBtnLayout = new javax.swing.GroupLayout(PnlBtn);
        PnlBtn.setLayout(PnlBtnLayout);
        PnlBtnLayout.setHorizontalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(LblFarmacia))
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(PnlDecoracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PnlSombra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnRequisiciones)
                            .addComponent(BtnVentas)
                            .addComponent(BtnFacturas)
                            .addComponent(BtnDevoluciones)
                            .addComponent(BtnInicio)
                            .addComponent(BtnProveedores)))
                    .addGroup(PnlBtnLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnConfig)
                            .addComponent(BtnUsers)
                            .addComponent(LblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PnlBtnLayout.setVerticalGroup(
            PnlBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnlBtnLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LblFarmacia)
                .addGap(105, 105, 105)
                .addComponent(BtnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnlSombra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnRequisiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                .addGap(124, 124, 124))
        );

        LblVencimiento.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblVencimiento.setText("Vencimiento:");

        DtcVencimientoNuevo.setBackground(new java.awt.Color(242, 242, 242));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblTitulo1)
                                .addGap(31, 31, 31))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TxtBuscarProd, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblTitulo)
                                    .addComponent(PnlFarmaLab, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(112, 112, 112)
                                        .addComponent(LblEmpleado)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                        .addComponent(TxtNomProd, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 32, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 148, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(LblTel)
                                                                .addGap(18, 18, 18))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(LblRFC1)
                                                                    .addComponent(LblDireccion))
                                                                .addGap(18, 18, 18)))
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(CboxProveedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(TxtPrecio, javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(TxtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel1)
                                                            .addComponent(LblVencimiento))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(TxtStock, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                                            .addComponent(DtcVencimientoNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                        .addGap(31, 31, 31))))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LblTitulo1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PnlModificarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PnlBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtNomProd, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblEmpleado))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblDireccion)
                            .addComponent(CboxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblRFC1)
                            .addComponent(TxtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblTel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TxtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DtcVencimientoNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(LblVencimiento)))
                        .addGap(52, 52, 52))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LblTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(TxtBuscarProd, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PnlFarmaLab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(56, 56, 56))))
            .addComponent(PnlBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 854, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

    private void TxtNomProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtNomProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtNomProdActionPerformed

    private void BtnModificarPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnModificarPMouseClicked
        modificarProducto();
    }//GEN-LAST:event_BtnModificarPMouseClicked

    private void BtnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnModificarPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnModificarPActionPerformed

    private void RbnActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbnActivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RbnActivoActionPerformed

    private void RbnInactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbnInactivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RbnInactivoActionPerformed

    private void TxtBuscarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarProdActionPerformed

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

    private void BtnInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInicioActionPerformed
        this.setVisible(false);

        Produccion.instancia.setState(JFrame.NORMAL);
    }//GEN-LAST:event_BtnInicioActionPerformed

    private void BtnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnBuscarMouseClicked
        buscarProductoPorNombre();
    }//GEN-LAST:event_BtnBuscarMouseClicked

    private void BtnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnBuscarActionPerformed

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

    private void TxtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtPrecioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BtgActividad;
    private javax.swing.JButton BtnBuscar;
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones;
    private javax.swing.JButton BtnFacturas;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnModificarP;
    private javax.swing.JButton BtnProveedores;
    private javax.swing.JButton BtnRequisiciones;
    private javax.swing.JButton BtnUsers;
    private javax.swing.JButton BtnVentas;
    private javax.swing.JComboBox<String> CboxProveedor;
    private com.toedter.calendar.JDateChooser DtcVencimientoNuevo;
    private javax.swing.JLabel LblCorreoB;
    private javax.swing.JLabel LblCorreoProv1;
    private javax.swing.JLabel LblDescripB;
    private javax.swing.JLabel LblDireccion;
    private javax.swing.JLabel LblDosis;
    private javax.swing.JLabel LblEmpleado;
    private javax.swing.JLabel LblFarmacia;
    private javax.swing.JLabel LblLogo;
    private javax.swing.JLabel LblMDosis;
    private javax.swing.JLabel LblNom;
    private javax.swing.JLabel LblPrecio;
    private javax.swing.JLabel LblPrecioB;
    private javax.swing.JLabel LblProv;
    private javax.swing.JLabel LblPuesto;
    private javax.swing.JLabel LblRFC;
    private javax.swing.JLabel LblRFC1;
    private javax.swing.JLabel LblStock;
    private javax.swing.JLabel LblStockB;
    private javax.swing.JLabel LblTel;
    private javax.swing.JLabel LblTipo;
    private javax.swing.JLabel LblTipoMedicamento;
    private javax.swing.JLabel LblTitulo;
    private javax.swing.JLabel LblTitulo1;
    private javax.swing.JLabel LblVencimiento;
    private javax.swing.JLabel LblVencimientoB;
    private javax.swing.JLabel LblVencimientoProd;
    private javax.swing.JPanel PnlBtn;
    private javax.swing.JPanel PnlBuscar;
    private javax.swing.JPanel PnlCabecera;
    private javax.swing.JPanel PnlDecoracion;
    private javax.swing.JPanel PnlFarmaLab;
    private javax.swing.JPanel PnlFinal;
    private javax.swing.JPanel PnlModificarBtn;
    private javax.swing.JPanel PnlRbn;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JRadioButton RbnActivo;
    private javax.swing.JRadioButton RbnInactivo;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarProd;
    private javax.swing.JTextField TxtDescripcion;
    private javax.swing.JTextField TxtNomProd;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JTextField TxtStock;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
