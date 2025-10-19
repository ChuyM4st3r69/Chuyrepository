package Entidades;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 *
 * @author Jesus Castillo
 */

public class Devolucion {

    public static void registrarDevolucion(int idProducto, int cantidad, int tipoMotivo, int idFactura) throws SQLException {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = farmacia.Farmacia.ConectarBD();
            cstmt = conn.prepareCall("{call sp_registrar_devolucion_proveedor(?, ?, ?, ?)}");
            cstmt.setInt(1, idProducto);
            cstmt.setInt(2, cantidad);
            cstmt.setInt(3, tipoMotivo);
            cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
            cstmt.execute();

            String resultado = cstmt.getString(4);
            System.out.println("Resultado de la devolución: " + resultado);
        } finally {
            if (cstmt != null) cstmt.close();
            if (conn != null) conn.close();
        }
    }
}
