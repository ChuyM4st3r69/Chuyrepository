package Entidades;

import java.sql.*;

public class UsuarioDAO {
    private Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean altaUsuario(int tipoUsuario, String clave, int idEmpleado, String nombreUsuario) {
        String sql = "{CALL AltaUsuario(?, ?, ?, ?)}";

        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setInt(1, tipoUsuario);
            cs.setString(2, clave);
            cs.setInt(3, idEmpleado);
            cs.setString(4, nombreUsuario);

            cs.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
