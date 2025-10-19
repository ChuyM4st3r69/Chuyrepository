package Entidades;

import farmacia.Farmacia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    // Insertar empleado usando procedimiento almacenado
    public int insertarEmpleado(Empleado empleado) throws SQLException {
        String sql = "{call sp_InsertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Farmacia.ConectarBD();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, empleado.getNombres());
            cstmt.setString(2, empleado.getApellidos());
            cstmt.setString(3, empleado.getRfc());
            cstmt.setString(4, empleado.getNss());
            cstmt.setString(5, empleado.getCalleNum());
            cstmt.setInt(6, empleado.getIdPuesto());
            cstmt.setInt(7, empleado.getIdColonia() != null ? empleado.getIdColonia() : 0);
            cstmt.registerOutParameter(8, Types.INTEGER);
            
            cstmt.execute();
            return cstmt.getInt(8);
        }
    }
    
    // Actualizar empleado
    public boolean actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "{call sp_ActualizarEmpleado(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = Farmacia.ConectarBD();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, empleado.getId());
            cstmt.setString(2, empleado.getNombres());
            cstmt.setString(3, empleado.getApellidos());
            cstmt.setString(4, empleado.getRfc());
            cstmt.setString(5, empleado.getNss());
            cstmt.setString(6, empleado.getCalleNum());
            cstmt.setInt(7, empleado.getIdPuesto());
            cstmt.setInt(8, empleado.getIdColonia() != null ? empleado.getIdColonia() : 0);
            
            return cstmt.executeUpdate() > 0;
        }
    }
    
    // Obtener todos los empleados
    public List<Empleado> obtenerTodosEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "{call sp_ObtenerTodosEmpleados()}";
        
        try (Connection conn = Farmacia.ConectarBD();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {
            
            while (rs.next()) {
                Empleado emp = new Empleado(
                    rs.getInt("idempleados"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("rfc"),
                    rs.getString("nss"),
                    rs.getString("calle_num"),
                    rs.getInt("tipos_puestos_idtipos_puestos"),
                    rs.getObject("colonias_empleados_idcolonias_empleados", Integer.class)
                );
                empleados.add(emp);
            }
        }
        return empleados;
    }
    
    // Obtener empleado por ID
    public Empleado obtenerEmpleadoPorId(int id) throws SQLException {
        String sql = "{call sp_ObtenerEmpleadoPorId(?)}";
        
        try (Connection conn = Farmacia.ConectarBD();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                        rs.getInt("idempleados"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("rfc"),
                        rs.getString("nss"),
                        rs.getString("calle_num"),
                        rs.getInt("tipos_puestos_idtipos_puestos"),
                        rs.getObject("colonias_empleados_idcolonias_empleados", Integer.class)
                    );
                }
            }
            return null;
        }
    }
}
