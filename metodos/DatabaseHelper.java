package metodos;

import Interfaces.ConfirmacionVentaDialog;
import farmacia.Farmacia;
import java.sql.*;

/**
 *
 * @author Jesus Castillo
 */
public class DatabaseHelper {
    
    public static double calcularTotalFactura(int idFactura) throws SQLException {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null; 
        int iva = ConfirmacionVentaDialog.porcentajeIva;
        
        System.out.println("iva dialog= " + iva);
        
        try {
            conn = Farmacia.ConectarBD();
            
            // Llamada a función MySQL
            String sql = "SELECT CalcularTotalFactura(?,?) AS total";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, idFactura);
            cstmt.setInt(2, iva);
            
            rs = cstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0;
            
        } finally {
            if (rs != null) rs.close();
            if (cstmt != null) cstmt.close();
            if (conn != null) conn.close();
        }
    }
}
