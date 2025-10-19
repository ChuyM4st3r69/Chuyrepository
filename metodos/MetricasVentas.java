package metodos;

import farmacia.Farmacia;
import java.sql.*;
import java.text.NumberFormat;

public class MetricasVentas {

    public static class ResultadoMetricas {
        public double ventasHoy;
        public double ventasAyer;
        public int productosHoy;
        public int productosAyer;
        public int transaccionesHoy;
        public int transaccionesAyer;
        public double ticketPromedioHoy;
        public double ticketPromedioAyer;
    }

    public static ResultadoMetricas obtenerMetricas() {
        ResultadoMetricas metricas = new ResultadoMetricas();

        try (Connection conn = Farmacia.ConectarBD()) {
            // 1. Ventas totales hoy y ayer
            String sqlVentas = "SELECT " +
                "SUM(CASE WHEN DATE(f.fecha) = CURDATE() THEN df.cantidad * df.precio_unitario ELSE 0 END) as ventasHoy, " +
                "SUM(CASE WHEN DATE(f.fecha) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN df.cantidad * df.precio_unitario ELSE 0 END) as ventasAyer " +
                "FROM facturas f JOIN detalle_facturas df ON f.idfacturas = df.facturas_idfacturas " +
                "WHERE DATE(f.fecha) IN (CURDATE(), DATE_SUB(CURDATE(), INTERVAL 1 DAY))";

            try (PreparedStatement stmt = conn.prepareStatement(sqlVentas);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    metricas.ventasHoy = rs.getDouble("ventasHoy");
                    metricas.ventasAyer = rs.getDouble("ventasAyer");
                }
            }

            // 2. Productos vendidos hoy y ayer
            String sqlProductos = "SELECT " +
                "SUM(CASE WHEN DATE(f.fecha) = CURDATE() THEN df.cantidad ELSE 0 END) as productosHoy, " +
                "SUM(CASE WHEN DATE(f.fecha) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN df.cantidad ELSE 0 END) as productosAyer " +
                "FROM facturas f JOIN detalle_facturas df ON f.idfacturas = df.facturas_idfacturas " +
                "WHERE DATE(f.fecha) IN (CURDATE(), DATE_SUB(CURDATE(), INTERVAL 1 DAY))";

            try (PreparedStatement stmt = conn.prepareStatement(sqlProductos);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    metricas.productosHoy = rs.getInt("productosHoy");
                    metricas.productosAyer = rs.getInt("productosAyer");
                }
            }

            // 3. Transacciones hoy y ayer
            String sqlTransacciones = "SELECT " +
                "COUNT(CASE WHEN DATE(fecha) = CURDATE() THEN 1 END) as transaccionesHoy, " +
                "COUNT(CASE WHEN DATE(fecha) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 END) as transaccionesAyer " +
                "FROM facturas " +
                "WHERE DATE(fecha) IN (CURDATE(), DATE_SUB(CURDATE(), INTERVAL 1 DAY))";

            try (PreparedStatement stmt = conn.prepareStatement(sqlTransacciones);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    metricas.transaccionesHoy = rs.getInt("transaccionesHoy");
                    metricas.transaccionesAyer = rs.getInt("transaccionesAyer");
                }
            }

            // 4. Calcular ticket promedio
            if (metricas.transaccionesHoy > 0) {
                metricas.ticketPromedioHoy = metricas.ventasHoy / metricas.transaccionesHoy;
            }
            if (metricas.transaccionesAyer > 0) {
                metricas.ticketPromedioAyer = metricas.ventasAyer / metricas.transaccionesAyer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metricas;
    }

    public static String formatearMoneda(double valor) {
        return NumberFormat.getCurrencyInstance().format(valor);
    }

    public static String formatearVariacionColores(double hoy, double ayer) {
        if (ayer == 0) {
            return "<html><font color='#7f8c8d'>N/A</font></html>";
        }

        double variacion = ((hoy - ayer) / ayer) * 100;
        String color;
        String icono;

        if (variacion > 0) {
            color = "#27ae60"; // Verde para aumento
            icono = "↑";
        } else if (variacion < 0) {
            color = "#e74c3c"; // Rojo para disminución
            icono = "↓";
        } else {
            color = "#3498db"; // Azul para sin cambio
            icono = "→";
        }

        return String.format("<html><font color='%s'>%s %.0f%%</font> vs. ayer</html>", 
                           color, icono, Math.abs(variacion));
    }

    public static String formatearEntero(int valor) {
        return NumberFormat.getIntegerInstance().format(valor);
    }
}