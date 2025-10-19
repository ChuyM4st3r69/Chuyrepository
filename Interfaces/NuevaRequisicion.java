package Interfaces;

import static Interfaces.Inicio_sesión.SesionUsuario.idEmpleado;
import farmacia.Farmacia;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.*;
import javax.swing.JFrame;
import metodos.BotonTransparente;
import metodos.RoundIzqPanel;
import metodos.RoundPanel;
import metodos.RoundTxt;
import metodos.TextPrompt;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import metodos.AccesoDenegado;
import metodos.InfoFarmaDialog;
import metodos.Tables;

/**
 *
 * @author Jesus Castillo
 */
public class NuevaRequisicion extends javax.swing.JFrame {

    public NuevaRequisicion() {
        initComponents();

        inicializarTabla();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        TxtBuscarProducto.addActionListener(e -> buscarProducto());

        LblPuesto.setText(Inicio_sesión.SesionUsuario.puesto);

        // TxtSobreTexto
        TextPrompt placeHolder = new TextPrompt("Buscar medicamentos, productos o empleados...", TxtBuscar);
        TextPrompt placeHolder2 = new TextPrompt("Buscar producto...", TxtBuscarProducto);

        // TxtRedondo
        RoundTxt redondearTxt = new RoundTxt(30, Color.getHSBColor(0.558f, 1.0f, 0.714f));
        TxtBuscar.setBorder(redondearTxt);
        TxtBuscarProducto.setBorder(redondearTxt);

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
        BotonTransparente.hacerBotonTransparente(BtnPRequisicion);

        // PnlRedondoIzq
        RoundIzqPanel.aplicarBordesRedondeadosIzquierda(PnlSombra, 40);

        // PnlRedondo
        RoundPanel.aplicarBordesRedondeados(PnlRequisicionBtn, 15, Color.getHSBColor(0.6667f, 0.02f, 0.89f));

        Tables.personalizarTabla(TblRequisiciones);

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

    // Variables de clase
    private DefaultTableModel modeloRequisicion;
    private int idProveedorActual = -1;

    // En el constructor o método de inicialización
    private void inicializarTabla() {
        modeloRequisicion = new DefaultTableModel(new Object[]{"ID", "Producto", "Dosis", "Unidad", "Cantidad", "Proveedor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo cantidad editable
            }
        };
        TblRequisiciones.setModel(modeloRequisicion);
    }

    private void buscarProducto() {
        String nombreProducto = TxtBuscarProducto.getText().trim();
        if (nombreProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = Farmacia.ConectarBD();
            String sql = "SELECT p.idproductos, p.nombre, p.existencia, pr.idproveedores, pr.nombre as proveedor, "
                    + "dm.dosis, dm.lote, tm.nombre as tipo_medicamento, u.nombre AS unidad "
                    + "FROM productos p "
                    + "JOIN proveedores pr ON p.proveedores_idproveedores = pr.idproveedores "
                    + "LEFT JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos "
                    + "LEFT JOIN tipos_medicamentos tm ON dm.tipos_medicamentos_idtipos_medicamentos = tm.idtipos_medicamentos "
                    + "LEFT JOIN unidades u ON p.unidades_idunidades = u.idunidades "
                    + "WHERE p.nombre LIKE ? AND p.activo = 1 "
                    + "ORDER BY p.nombre, dm.dosis";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + nombreProducto + "%");
            ResultSet rs = pst.executeQuery();

            List<Object[]> productosEncontrados = new ArrayList<>();

            while (rs.next()) {
                Object[] producto = new Object[]{
                    rs.getInt("idproductos"), // 0
                    rs.getString("nombre"), // 1
                    rs.getInt("existencia"), // 2
                    rs.getInt("idproveedores"), // 3
                    rs.getString("proveedor"), // 4
                    rs.getObject("dosis"), // 5 
                    rs.getObject("lote"), // 6
                    rs.getObject("tipo_medicamento"), // 7
                    rs.getString("unidad") // 8
                };
                productosEncontrados.add(producto);
            }

            if (productosEncontrados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Si hay solo un producto, procesarlo directamente
            if (productosEncontrados.size() == 1) {
                procesarProductoSeleccionado(productosEncontrados.get(0));
            } else {
                // Mostrar diálogo para seleccionar entre productos con mismo nombre pero diferente dosis
                mostrarSeleccionProductos(productosEncontrados);
            }

            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarSeleccionProductos(List<Object[]> productos) {
        // Crear modelo para el JTable de selección
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Seleccionar", "ID", "Nombre", "Dosis", "Unidad", "Lote", "Tipo", "Existencia"}, 0);

        for (Object[] producto : productos) {
            model.addRow(new Object[]{
                false,
                producto[0], // ID
                producto[1], // Nombre
                producto[5] != null ? producto[5] : "N/A", // Dosis
                producto[8] != null ? producto[8] : "N/A", // Unidad
                producto[6], // Lote
                producto[7], // Tipo medicamento
                producto[2] // Existencia
            });
        }

        JTable table = new JTable(model);
        table.getColumn("Seleccionar").setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumn("Seleccionar").setCellEditor(table.getDefaultEditor(Boolean.class));

        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));

        int result = JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Seleccione el producto (puede seleccionar varios)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((Boolean) model.getValueAt(i, 0)) {
                    procesarProductoSeleccionado(productos.get(i));
                }
            }
        }
    }

    // Método para procesar producto seleccionado
    private void procesarProductoSeleccionado(Object[] producto) {
        int idProveedor = (int) producto[3];

        // Verificar proveedor
        if (idProveedorActual == -1) {
            idProveedorActual = idProveedor;
        } else if (idProveedor != idProveedorActual) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar productos del mismo proveedor: " + producto[4],
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pedir cantidad
        String cantidadStr = JOptionPane.showInputDialog(this,
                "Ingrese la cantidad para " + producto[1]
                + (producto[5] != null ? " (" + producto[5] + " mg)" : "")
                + "\nExistencia actual: " + producto[2],
                "Cantidad", JOptionPane.QUESTION_MESSAGE);

        if (cantidadStr != null && !cantidadStr.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    throw new NumberFormatException();
                }

                // Agregar a la tabla de requisición con la dosis separada
                modeloRequisicion.addRow(new Object[]{
                    producto[0], // ID
                    producto[1], // Nombre
                    producto[5] != null ? producto[5].toString() : "N/A", // Dosis
                    producto[8] != null ? producto[8].toString() : "N/A", // Unidad
                    cantidad,
                    producto[4] // Proveedor
                });

                TxtBuscarProducto.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PnlCabecera = new javax.swing.JPanel();
        TxtBuscar = new javax.swing.JTextField();
        LblPuesto = new javax.swing.JLabel();
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
        LblTitulo = new javax.swing.JLabel();
        LblVentas = new javax.swing.JLabel();
        TxtBuscarProducto = new javax.swing.JTextField();
        PnlRequisicionBtn = new javax.swing.JPanel();
        BtnPRequisicion = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblRequisiciones = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
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

        LblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        LblTitulo.setForeground(new java.awt.Color(82, 82, 82));
        LblTitulo.setText("Genere  nuevas requisiciones.");
        LblTitulo.setOpaque(true);

        LblVentas.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        LblVentas.setForeground(new java.awt.Color(0, 48, 73));
        LblVentas.setText("Requisiciones");

        TxtBuscarProducto.setBackground(new java.awt.Color(242, 242, 242));
        TxtBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtBuscarProductoActionPerformed(evt);
            }
        });

        PnlRequisicionBtn.setBackground(new java.awt.Color(217, 217, 217));

        BtnPRequisicion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnPRequisicion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Requisicion.png"))); // NOI18N
        BtnPRequisicion.setText("Generar requisicion");
        BtnPRequisicion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnPRequisicionMouseClicked(evt);
            }
        });
        BtnPRequisicion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPRequisicionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PnlRequisicionBtnLayout = new javax.swing.GroupLayout(PnlRequisicionBtn);
        PnlRequisicionBtn.setLayout(PnlRequisicionBtnLayout);
        PnlRequisicionBtnLayout.setHorizontalGroup(
            PnlRequisicionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BtnPRequisicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PnlRequisicionBtnLayout.setVerticalGroup(
            PnlRequisicionBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlRequisicionBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BtnPRequisicion, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        TblRequisiciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TblRequisiciones);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlBarraL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PnlCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LblVentas)
                                .addContainerGap(1088, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LblTitulo)
                                            .addComponent(TxtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(PnlRequisicionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(51, 51, 51))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PnlCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LblVentas)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LblTitulo)
                        .addGap(18, 18, 18)
                        .addComponent(TxtBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(PnlRequisicionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(PnlBarraL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarActionPerformed

    }//GEN-LAST:event_TxtBuscarActionPerformed

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

    private void TxtBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtBuscarProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtBuscarProductoActionPerformed

    private void BtnPRequisicionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPRequisicionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPRequisicionActionPerformed

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

    private void BtnPRequisicionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnPRequisicionMouseClicked
        if (modeloRequisicion.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en la requisición", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de finalizar la requisición con " + modeloRequisicion.getRowCount() + " productos?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Connection conn = Farmacia.ConectarBD();
                conn.setAutoCommit(false); // Iniciar transacción

                // 1. Crear la requisición
                String sqlRequisicion = "INSERT INTO requisisiones (fecha) VALUES (CURDATE())";
                PreparedStatement pstRequisicion = conn.prepareStatement(sqlRequisicion, Statement.RETURN_GENERATED_KEYS);
                pstRequisicion.executeUpdate();

                ResultSet rs = pstRequisicion.getGeneratedKeys();
                int idRequisicion = -1;
                if (rs.next()) {
                    idRequisicion = rs.getInt(1);
                }

                // 2. Insertar los detalles de productos
                String sqlDetalle = "INSERT INTO detalle_requisisiones (cantidad, requisisiones_idrequisisiones, productos_idproductos) VALUES (?, ?, ?)";
                PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle);

                for (int i = 0; i < modeloRequisicion.getRowCount(); i++) {
                    int idProducto = (int) modeloRequisicion.getValueAt(i, 0);
                    int cantidad = Integer.parseInt(modeloRequisicion.getValueAt(i, 4).toString()); // columna de cantidad

                    pstDetalle.setInt(1, cantidad);
                    pstDetalle.setInt(2, idRequisicion);
                    pstDetalle.setInt(3, idProducto);
                    pstDetalle.executeUpdate();
                }

                conn.commit(); // Confirmar transacción
                JOptionPane.showMessageDialog(this, "Requisición guardada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                modeloRequisicion.setRowCount(0);
                idProveedorActual = -1;

                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar requisición: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_BtnPRequisicionMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnConfig;
    private javax.swing.JButton BtnDevoluciones1;
    private javax.swing.JButton BtnFacturas1;
    private javax.swing.JButton BtnInicio;
    private javax.swing.JButton BtnInventario;
    private javax.swing.JButton BtnPRequisicion;
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
    private javax.swing.JPanel PnlRequisicionBtn;
    private javax.swing.JPanel PnlSombra;
    private javax.swing.JTable TblRequisiciones;
    private javax.swing.JTextField TxtBuscar;
    private javax.swing.JTextField TxtBuscarProducto;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
