package metodos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Jesus Castillo
 */
public class Tables {

    public static void personalizarTabla(JTable tabla) {
        // Establecer la fuente y el alto de las filas
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(35);
        
        // Personalizar el encabezado de la tabla
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0, 82, 155));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Ocultar la cuadrícula
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(new Color(220, 240, 255));
        
        // Renderer personalizado para centrar contenido y mejorar la apariencia
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                
                // CENTRADO HORIZONTAL Y VERTICAL - MEJORADO
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                
                // Alternar colores de fila para mejor legibilidad
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(220, 240, 255));
                    setForeground(Color.BLACK);
                }
                
                // Establecer bordes internos para mejor espaciado
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                
                // Asegurar que el texto esté centrado incluso para valores null
                if (value == null) {
                    setText("");
                }
                
                return this;
            }
        };

        // Renderer personalizado para el encabezado - CENTRADO MEJORADO
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // CENTRADO DEL ENCABEZADO
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                
                // Estilo del encabezado
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setForeground(Color.WHITE);
                setBackground(new Color(0, 82, 155));
                setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                setOpaque(true);
                
                return this;
            }
        };

        // Aplicar los renderers a todas las columnas
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            
            // Aplicar renderer de celdas centrado
            column.setCellRenderer(centerRenderer);
            
            // Aplicar renderer de encabezado centrado
            column.setHeaderRenderer(headerRenderer);
        }

        // Ajustar el ancho de las columnas
        ajustarAnchoColumnas(tabla);
    }

    // Método adicional para aplicar solo centrado sin otros estilos
    public static void centrarContenidoTabla(JTable tabla) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        
        // Aplicar a todas las columnas
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Centrar también los encabezados
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setVerticalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    public static void ajustarAnchoColumnas(JTable tabla) {
        for (int columna = 0; columna < tabla.getColumnCount(); columna++) {
            TableColumn tableColumn = tabla.getColumnModel().getColumn(columna);
            int anchoMinimo = 50;
            int anchoMaximo = 0;
            int anchoPreferido = 0;

            // Calcular el ancho del encabezado
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabla, 
                    tableColumn.getHeaderValue(), false, false, 0, columna);
            anchoMaximo = headerComp.getPreferredSize().width;

            // Calcular el ancho de las celdas
            for (int fila = 0; fila < tabla.getRowCount(); fila++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(fila, columna);
                Component c = tabla.prepareRenderer(cellRenderer, fila, columna);
                anchoMaximo = Math.max(c.getPreferredSize().width, anchoMaximo);
            }

            // Establecer los anchos con márgenes
            anchoPreferido = Math.max(anchoMinimo, anchoMaximo + 20);
            tableColumn.setMinWidth(anchoMinimo);
            tableColumn.setPreferredWidth(anchoPreferido);
            tableColumn.setMaxWidth(Integer.MAX_VALUE);
        }
    }
}