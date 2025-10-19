package metodos;

import java.awt.Component;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class VencimientoRenderer extends DefaultTableCellRenderer {

    private final int columnaFecha;

    public VencimientoRenderer(int columnaFecha) {
        this.columnaFecha = columnaFecha;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        try {
            // Obtener la fecha de vencimiento desde la columna correspondiente
            Object valorFecha = table.getValueAt(row, columnaFecha);
            if (valorFecha != null && !valorFecha.toString().equals("N/D")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date fechaVencimiento = sdf.parse(valorFecha.toString());

                Calendar hoy = Calendar.getInstance();
                Calendar limite = Calendar.getInstance();
                limite.add(Calendar.MONTH, 3); // Agrega 3 meses

                if (fechaVencimiento.before(limite.getTime()) && fechaVencimiento.after(hoy.getTime())) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    // Restaurar colores por defecto si no cumple la condición
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return c;
    }
}
