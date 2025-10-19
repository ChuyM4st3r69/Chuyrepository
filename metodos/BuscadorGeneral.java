package metodos;

/**
 *
 * @author Jesus Castillo
 */
import Interfaces.Inicio_sesión;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BuscadorGeneral {

    public static void buscar(JTextField campoBuscar, JFrame padre) {
        String criterio = campoBuscar.getText().trim();

        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "Ingresa un término de búsqueda.");
            return;
        }

        String puesto = Inicio_sesión.SesionUsuario.puesto;

        boolean encontrado = false;

        switch (puesto.toLowerCase()) {
            case "administrador" -> {
                if (buscarMedicamento(criterio, padre)) {
                    encontrado = true;
                }
                if (buscarProveedor(criterio, padre)) {
                    encontrado = true;
                }
                if (buscarEmpleado(criterio, padre)) {
                    encontrado = true;
                }
            }

            case "almacenista" -> {
                if (buscarMedicamento(criterio, padre)) {
                    encontrado = true;
                }
                if (buscarProveedor(criterio, padre)) {
                    encontrado = true;
                }
            }

            case "cajero" -> {
                if (buscarMedicamento(criterio, padre)) {
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            JOptionPaneError.showError(padre, "No se encontró ningún resultado.");
        }
    }

    private static boolean buscarMedicamento(String nombre, JFrame padre) {
        String sql = """
        SELECT 
            p.nombre AS medicamento,
            dm.dosis,
            u.nombre AS unidad,
            p.cantidad,
            p.existencia,
            p.precio_unitario
        FROM productos p
        LEFT JOIN unidades u ON p.unidades_idunidades = u.idunidades
        LEFT JOIN detalle_medicamentos dm ON p.idproductos = dm.productos_idproductos
        WHERE p.nombre LIKE ?;
    """;

        try (Connection con = farmacia.Farmacia.ConectarBD(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"Medicamento", "Dosis", "Unidad", "Cantidad", "Stock", "Precio"}, 0
            );

            boolean encontrado = false;

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("medicamento"),
                    rs.getFloat("dosis") + " mg",
                    rs.getString("unidad"),
                    rs.getFloat("cantidad"),
                    rs.getInt("existencia") + " unid.",
                    "$" + rs.getFloat("precio_unitario")
                });
                encontrado = true;
            }

            if (encontrado) {
                mostrarEnDialog(modelo, padre, "Resultados: Medicamentos");
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(padre, "Error al buscar medicamento: " + e.getMessage());
        }
        return false;
    }

    private static boolean buscarProveedor(String nombre, JFrame padre) {
        String sql = """
            SELECT pr.nombre AS proveedor, p.nombre AS medicamento
            FROM proveedores pr
            JOIN productos p ON p.proveedores_idproveedores = pr.idproveedores
            WHERE pr.nombre LIKE ?;
        """;

        try (Connection con = farmacia.Farmacia.ConectarBD(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel modelo = new DefaultTableModel(new String[]{"Proveedor", "Medicamento"}, 0);
            boolean encontrado = false;

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("proveedor"),
                    rs.getString("medicamento")
                });
                encontrado = true;
            }

            if (encontrado) {
                mostrarEnDialog(modelo, padre, "Resultados: Proveedor");
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(padre, "Error al buscar proveedor: " + e.getMessage());
        }
        return false;
    }

    private static boolean buscarEmpleado(String nombre, JFrame padre) {
        String sql = """
        SELECT 
            e.nombres,
            e.apellidos,
            GROUP_CONCAT(DISTINCT ne.numero SEPARATOR ', ') AS numero,
            GROUP_CONCAT(DISTINCT ce.correo SEPARATOR ', ') AS correo
        FROM empleados e
        LEFT JOIN numeros_empleados ne ON e.idempleados = ne.empleados_idempleados
        LEFT JOIN correos_empleados ce ON e.idempleados = ce.empleados_idempleados
        WHERE e.nombres LIKE ?
        GROUP BY e.idempleados;
    """;

        try (Connection con = farmacia.Farmacia.ConectarBD(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel modelo = new DefaultTableModel(
                    new String[]{"Nombres", "Apellidos", "Numeros", "Correos"}, 0
            );

            boolean encontrado = false;

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("numero"),
                    rs.getString("correo")
                });
                encontrado = true;
            }

            if (encontrado) {
                mostrarEnDialog(modelo, padre, "Resultados: Empleado");
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(padre, "Error al buscar empleado: " + e.getMessage());
        }
        return false;
    }

    private static void mostrarEnDialog(DefaultTableModel modelo, JFrame padre, String titulo) {
        JTable tabla = new JTable(modelo);
        Tables.personalizarTabla(tabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new java.awt.Dimension(500, 300));

        JDialog dialogo = new JDialog(padre, titulo, true);
        dialogo.getContentPane().add(scroll);
        dialogo.pack();
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
    }
}
