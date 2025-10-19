package farmacia;

import Interfaces.Inicio_sesión;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.*;
import javax.swing.JLabel;

public class Farmacia {

    public static void main(String[] args) {
        Inicio_sesión inicio = new Inicio_sesión();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/icons/Logo.png");
        inicio.setIconImage(icon);
        inicio.setVisible(true);
        inicio.setLocationRelativeTo(null);
        inicio.setTitle("Inicio de Sesión");
    }

    public static Connection ConectarBD() {
        Connection conexion = null;
        String host = "jdbc:mysql://localhost:3307/";
        String user = "administrador";
        String pass = "admin1234";
        String bd = "farmacia";

        try {
            conexion = DriverManager.getConnection(host + bd, user, pass);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.getLogger(Farmacia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return conexion;
    }
    
    public static Connection ConectarBDLike(String user, String pass) {
    Connection conexion = null;
    String host = "jdbc:mysql://localhost:3307/";
    String bd = "farmacia";

    try {
        conexion = DriverManager.getConnection(host + bd, user, pass);
    } catch (SQLException ex) {
        System.out.println("Error al conectar: " + ex.getMessage());
        System.getLogger(Farmacia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
    }

    return conexion;
}

    
    public static Connection IniciarBD() {
        Connection conexion = null;
        String host = "jdbc:mysql://localhost:3307/";
        String user = "inicio_sesion";
        String pass = "inicio1234";
        String bd = "farmacia";

        try {
            conexion = DriverManager.getConnection(host + bd, user, pass);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.getLogger(Farmacia.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return conexion;
    }

    public static void mostrarPuesto(JLabel LblPuesto, String claveUsuario) {
        Connection conexion = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            // Conexión a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/farmacia", "root", "123456");

            // Llamar al procedimiento almacenado
            cstmt = conexion.prepareCall("{CALL puestoEmp(?)}");
            cstmt.setString(1, claveUsuario);

            rs = cstmt.executeQuery();

            if (rs.next()) {
                String puesto = rs.getString("Puesto");
                LblPuesto.setText(puesto); // Aquí se coloca el texto en el JLabel
            } else {
                LblPuesto.setText("No encontrado");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            LblPuesto.setText("Error");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cstmt != null) {
                    cstmt.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public double obtenerTotalVentasHoy() {
        double total = 0;
        try {
            Connection conn = Farmacia.ConectarBD(); // Método para obtener conexión
            CallableStatement cstmt = conn.prepareCall("{? = call TotalVentasHoy()}");
            cstmt.registerOutParameter(1, Types.DECIMAL);
            cstmt.execute();
            total = cstmt.getDouble(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total;
    }
    
    public static void registrarSalida(int idEmpleado) throws SQLException {
        String sql = "UPDATE accesos SET hora_salida = NOW() "
                + "WHERE empleados_idempleados = ? AND hora_salida IS NULL "
                + "ORDER BY hora_entrada DESC LIMIT 1";

        try (Connection conn = Farmacia.IniciarBD(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEmpleado);
            pstmt.executeUpdate();
            System.out.println("Realizado");
        }
    }
}
