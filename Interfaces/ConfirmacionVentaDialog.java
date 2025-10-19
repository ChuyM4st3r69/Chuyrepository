package Interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;

/**
 * Diálogo de confirmación para ventas con cálculo correcto de totales
 *
 * @author Jesus Castillo
 */
public class ConfirmacionVentaDialog extends JDialog {

    // Constante para el porcentaje de IVA (16%)
    public static final int porcentajeIva = 16; // IVA del 16%

    // Bandera para saber si se confirmó la venta
    private boolean confirmed = false;
    
    // Formatos para mostrar valores monetarios y porcentajes
    private final DecimalFormat formatoMoneda = new DecimalFormat("$#,##0.00");
    private final DecimalFormat formatoPorcentaje = new DecimalFormat("#0.00%");

    // Componentes de la interfaz
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;
    private JLabel lblFacturaNo;
    private JTable tblResumen;
    private JButton btnConfirmar;
    private JButton btnCancelar;

    // Valores numéricos de la venta
    private double subtotal;
    private double iva;
    private double total;
    private int numeroFactura;

    /**
     * Constructor del diálogo de confirmación
     * @param parent Ventana padre
     * @param totalConIva Total de la venta incluyendo IVA
     * @param tablaVenta Tabla con los productos de la venta
     * @param idFactura Número de factura
     */
    public ConfirmacionVentaDialog(JFrame parent, double totalConIva, JTable tablaVenta, int idFactura) {
        super(parent, "Confirmar Venta", true);
        this.numeroFactura = idFactura;

        // Configurar icono de la ventana
        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        this.setIconImage(icon);

        // Calcular valores desde el total con IVA
        calcularDesgloseDesdeTotal(totalConIva);

        // Inicializar componentes y configurar interfaz
        initComponents();
        inicio();
        configurarTablaResumen(tablaVenta);
        actualizarLabels();

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void calcularDesgloseDesdeTotal(double totalConIva) {
        this.total = totalConIva;
        // Si total incluye IVA del 16%: subtotal = total / 1.16
        this.subtotal = totalConIva / (1 + (porcentajeIva / 100.0));
        this.iva = totalConIva - subtotal;
    }

    private void inicio() {
        setLayout(new BorderLayout());

        // Panel superior - información de factura
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de Venta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Número de factura
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInfo.add(new JLabel("Factura No:"), gbc);
        gbc.gridx = 1;
        lblFacturaNo = new JLabel();
        lblFacturaNo.setFont(lblFacturaNo.getFont().deriveFont(Font.BOLD));
        panelInfo.add(lblFacturaNo, gbc);

        // Empleado
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInfo.add(new JLabel("Empleado:"), gbc);
        gbc.gridx = 1;
        String empleadoInfo = Inicio_sesión.SesionUsuario.nombreCompleto != null
                ? Inicio_sesión.SesionUsuario.nombreCompleto : "Usuario Actual";
        panelInfo.add(new JLabel(empleadoInfo), gbc);

        // Fecha
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInfo.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        panelInfo.add(new JLabel(java.time.LocalDate.now().toString()), gbc);

        add(panelInfo, BorderLayout.NORTH);

        // Panel central - tabla de productos
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Productos"));

        tblResumen = new JTable();
        tblResumen.setEnabled(false); // Solo lectura
        JScrollPane scrollPane = new JScrollPane(tblResumen);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // Panel inferior - totales y botones
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Subpanel de totales
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 10, 3, 10);
        gbc.anchor = GridBagConstraints.EAST;

        // Subtotal
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelTotales.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 1;
        lblSubtotal = new JLabel();
        panelTotales.add(lblSubtotal, gbc);

        // IVA
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelTotales.add(new JLabel("IVA (" + porcentajeIva + "%):"), gbc);
        gbc.gridx = 1;
        lblIva = new JLabel();
        panelTotales.add(lblIva, gbc);

        // Línea separadora
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JSeparator separator = new JSeparator();
        panelTotales.add(separator, gbc);

        // Total
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lblTotalTexto = new JLabel("TOTAL:");
        lblTotalTexto.setFont(lblTotalTexto.getFont().deriveFont(Font.BOLD, 14f));
        panelTotales.add(lblTotalTexto, gbc);

        gbc.gridx = 1;
        lblTotal = new JLabel();
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));
        lblTotal.setForeground(new Color(0, 120, 0));
        panelTotales.add(lblTotal, gbc);

        panelInferior.add(panelTotales, BorderLayout.CENTER);

        // Subpanel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnConfirmar = new JButton("Confirmar Venta");
        btnCancelar = new JButton("Cancelar");

        btnConfirmar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.setPreferredSize(new Dimension(120, 35));

        btnConfirmar.setBackground(new Color(0, 150, 0));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(180, 0, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        panelInferior.add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // Configurar eventos
        configurarEventos();
    }

    private void configurarEventos() {
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Confirmar la venta
                    confirmed = true;

                    // Generar el ticket PDF
                    generarTicketVenta();

                    // Mostrar mensaje de confirmación
                    JOptionPane.showMessageDialog(
                            ConfirmacionVentaDialog.this,
                            "Venta confirmada exitosamente.\nTicket generado correctamente.",
                            "Venta Confirmada",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();

                } catch (Exception ex) {
                    // En caso de error al generar el ticket
                    JOptionPane.showMessageDialog(
                            ConfirmacionVentaDialog.this,
                            "Error al generar el ticket: " + ex.getMessage()
                            + "\nLa venta se ha confirmado pero el ticket no se pudo generar.",
                            "Error al generar ticket",
                            JOptionPane.WARNING_MESSAGE
                    );

                    confirmed = true; // La venta sigue siendo válida
                    dispose();
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        ConfirmacionVentaDialog.this,
                        "¿Está seguro que desea cancelar la venta?",
                        "Confirmar Cancelación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    confirmed = false;
                    dispose();
                }
            }
        });

        // Configurar teclas de acceso rápido
        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
        KeyStroke escapeKey = KeyStroke.getKeyStroke("ESCAPE");

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKey, "confirmar");
        getRootPane().getActionMap().put("confirmar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnConfirmar.doClick();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, "cancelar");
        getRootPane().getActionMap().put("cancelar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCancelar.doClick();
            }
        });
    }

    private void generarTicketVenta() {
        try {

            // Crear el nombre del archivo con timestamp
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String rutaPDF = "tickets/ticket_" + timestamp + ".pdf";

            // Crear el directorio si no existe
            java.io.File directorio = new java.io.File("tickets");

            // Verificar que el directorio sea escribible
            if (!directorio.canWrite()) {
                throw new RuntimeException("No se puede escribir en el directorio: " + directorio.getAbsolutePath());
            }

            // Obtener información del empleado
            String nombreVendedor = Inicio_sesión.SesionUsuario.nombreCompleto != null
                    ? Inicio_sesión.SesionUsuario.nombreCompleto : "Usuario Actual";
            String puestoVendedor = Inicio_sesión.SesionUsuario.puesto;

            // Preparar los datos para el PDF de manera más segura
            PrepararDatosParaPDF datosPreparados = prepararDatosTabla();

            // Llamar al generador de PDF con datos preparados
            metodos.PDFGenerator.generarFacturaPDFSeguro(
                    rutaPDF,
                    datosPreparados.productos,
                    datosPreparados.cantidades,
                    datosPreparados.preciosUnitarios,
                    datosPreparados.subtotales,
                    nombreVendedor,
                    puestoVendedor,
                    subtotal,
                    iva,
                    total,
                    numeroFactura,
                    porcentajeIva
            );

            // Verificar que el archivo se creó
            java.io.File archivoPDF = new java.io.File(rutaPDF);
            if (archivoPDF.exists()) {
                // Opcional: Abrir el PDF generado
                abrirPDF(rutaPDF);
            } else {
                throw new RuntimeException("El archivo PDF no se creó: " + rutaPDF);
            }

        } catch (Exception e) {
            System.err.println("ERROR al generar ticket:");
            e.printStackTrace();
            throw new RuntimeException("Error al generar el ticket: " + e.getMessage(), e);
        }
    }

    // Clase interna para datos preparados
    private static class PrepararDatosParaPDF {

        String[] productos;
        int[] cantidades;
        double[] preciosUnitarios;
        double[] subtotales;
    }

    private PrepararDatosParaPDF prepararDatosTabla() {
        PrepararDatosParaPDF datos = new PrepararDatosParaPDF();

        int numFilas = tblResumen.getRowCount();
        datos.productos = new String[numFilas];
        datos.cantidades = new int[numFilas];
        datos.preciosUnitarios = new double[numFilas];
        datos.subtotales = new double[numFilas];

        for (int i = 0; i < numFilas; i++) {
            // Producto (columna 0)
            datos.productos[i] = (String) tblResumen.getValueAt(i, 0);

            // Cantidad (columna 1) - asegurar que sea int
            Object cantidadObj = tblResumen.getValueAt(i, 1);
            if (cantidadObj instanceof Integer) {
                datos.cantidades[i] = (Integer) cantidadObj;
            } else if (cantidadObj instanceof String) {
                try {
                    datos.cantidades[i] = Integer.parseInt((String) cantidadObj);
                } catch (NumberFormatException e) {
                    datos.cantidades[i] = 1; // valor por defecto
                }
            } else {
                datos.cantidades[i] = 1; // valor por defecto
            }

            // Precio unitario (columna 2) - convertir de String con formato a double
            Object precioObj = tblResumen.getValueAt(i, 2);
            if (precioObj instanceof String) {
                String precioStr = (String) precioObj;
                // Remover símbolos de moneda y comas
                precioStr = precioStr.replace("$", "").replace(",", "");
                try {
                    datos.preciosUnitarios[i] = Double.parseDouble(precioStr);
                } catch (NumberFormatException e) {
                    datos.preciosUnitarios[i] = 0.0;
                }
            } else if (precioObj instanceof Double) {
                datos.preciosUnitarios[i] = (Double) precioObj;
            } else {
                datos.preciosUnitarios[i] = 0.0;
            }

            // Subtotal (columna 3) - convertir de String con formato a double
            Object subtotalObj = tblResumen.getValueAt(i, 3);
            if (subtotalObj instanceof String) {
                String subtotalStr = (String) subtotalObj;
                // Remover símbolos de moneda y comas
                subtotalStr = subtotalStr.replace("$", "").replace(",", "");
                try {
                    datos.subtotales[i] = Double.parseDouble(subtotalStr);
                } catch (NumberFormatException e) {
                    datos.subtotales[i] = datos.cantidades[i] * datos.preciosUnitarios[i];
                }
            } else if (subtotalObj instanceof Double) {
                datos.subtotales[i] = (Double) subtotalObj;
            } else {
                datos.subtotales[i] = datos.cantidades[i] * datos.preciosUnitarios[i];
            }
        }

        return datos;
    }

    // Método opcional para abrir el PDF generado automáticamente
    private void abrirPDF(String rutaPDF) {
        try {

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(new java.io.File(rutaPDF));
                } else {
                    System.out.println("Desktop no soporta la acción OPEN");
                }
            } else {
                System.out.println("Desktop no está soportado en este sistema");
            }
        } catch (Exception e) {
            System.err.println("Error al abrir PDF:");
            e.printStackTrace();
        }
    }

    private void configurarTablaResumen(JTable tablaOriginal) {
        String[] columnNames = {"Producto", "Dosis", "Unidad", "Cantidad", "Precio Unitario", "Subtotal"};

        DefaultTableModel modeloResumen = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DefaultTableModel modeloOriginal = (DefaultTableModel) tablaOriginal.getModel();

        for (int i = 0; i < modeloOriginal.getRowCount(); i++) {
            String producto = (String) modeloOriginal.getValueAt(i, 1);
            String dosis = (String) modeloOriginal.getValueAt(i, 6);
            String unidad = (String) modeloOriginal.getValueAt(i, 7);
            Integer cantidad = (Integer) modeloOriginal.getValueAt(i, 3);
            Double precio = (Double) modeloOriginal.getValueAt(i, 2);
            Double subtotal = (Double) modeloOriginal.getValueAt(i, 4);

            modeloResumen.addRow(new Object[]{
                producto, dosis, unidad, cantidad, precio, subtotal
            });
        }

        tblResumen.setModel(modeloResumen);
    }

    private void actualizarLabels() {
        lblFacturaNo.setText(String.valueOf(numeroFactura));
        lblSubtotal.setText(formatoMoneda.format(subtotal));
        lblIva.setText(formatoMoneda.format(iva));
        lblTotal.setText(formatoMoneda.format(total));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public double getTotal() {
        return total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIva() {
        return iva;
    }

    public int getNumeroFactura() {
        return numeroFactura;
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
