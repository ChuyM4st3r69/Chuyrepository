/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import farmacia.Farmacia;
import static farmacia.Farmacia.ConectarBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Jesus Castillo
 */
public class ProveedorDAO {

    public static Proveedor buscarPorNombre(String nombre) {
    Proveedor prov = null;
    String sql = """
        SELECT p.idproveedores, p.nombre, p.rfc, p.numero_calle, 
               p.colonias_proveedores_idcolonias_proveedores, p.activo, 
               c.nombre AS colonia
        FROM proveedores p
        JOIN colonias_proveedores c ON p.colonias_proveedores_idcolonias_proveedores = c.idcolonias_proveedores
        WHERE p.nombre = ?
    """;

    try (Connection conn = Farmacia.ConectarBD();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            prov = new Proveedor();
            prov.setId(rs.getInt("idproveedores"));
            prov.setNombre(rs.getString("nombre"));
            prov.setRfc(rs.getString("rfc"));
            prov.setNumeroCalle(rs.getString("numero_calle"));
            prov.setColoniaId(rs.getInt("colonias_proveedores_idcolonias_proveedores"));
            prov.setDireccion(rs.getString("numero_calle") + ", " + rs.getString("colonia"));
            prov.setActivo(rs.getInt("activo"));

            // Obtener teléfonos
            prov.setTelefonos(obtenerTelefonos(prov.getId()));

            // Obtener correos
            prov.setCorreos(obtenerCorreos(prov.getId()));
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return prov;
}

    public static boolean modificarProveedor(Proveedor p) {
        Connection conn = Farmacia.ConectarBD();
        String sql = "UPDATE proveedores SET rfc=?, numero_calle=?, activo=? WHERE idproveedores=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getRfc());
            ps.setString(2, p.getDireccion());
            ps.setInt(3, p.getActivo());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> obtenerTelefonos(int idProveedor) {
        ArrayList<String> telefonos = new ArrayList<>();
        Connection conn = Farmacia.ConectarBD();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT numero FROM numeros_proveedores WHERE proveedores_idproveedores = ?")) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                telefonos.add(rs.getString("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return telefonos;
    }

    public static ArrayList<String> obtenerCorreos(int idProveedor) {
        ArrayList<String> correos = new ArrayList<>();
        Connection conn = Farmacia.ConectarBD();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT correo FROM correos_proveedores WHERE proveedores_idproveedores = ?")) {
            ps.setInt(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                correos.add(rs.getString("correo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return correos;
    }

    public static void darDeBaja(int idProveedor) {
        Connection conn = Farmacia.ConectarBD();
        String sql = "CALL BajaProveedores(?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public static boolean actualizarProveedor(Proveedor prov) throws SQLException {
    String sql = """
        UPDATE proveedores
        SET nombre = ?, 
            rfc = ?, 
            numero_calle = ?, 
            colonias_proveedores_idcolonias_proveedores = ?, 
            activo = ?
        WHERE idproveedores = ?
    """;

    try (Connection conn = Farmacia.ConectarBD();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, prov.getNombre());
        ps.setString(2, prov.getRfc());
        ps.setString(3, prov.getNumeroCalle()); // contiene calle + número
        ps.setInt(4, prov.getColoniaId());
        ps.setInt(5, prov.getActivo());
        ps.setInt(6, prov.getId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}

    
    public static boolean darDeBajaProductos(int idProveedor) throws SQLException {
    Connection conn = null;
    try {
        conn = ConectarBD();
        conn.setAutoCommit(false); // Iniciar transacción
        
        // 1. Desactivar productos
        String sqlProductos = "UPDATE productos SET activo = 0 WHERE proveedores_idproveedores = ?";
        try (PreparedStatement psProductos = conn.prepareStatement(sqlProductos)) {
            psProductos.setInt(1, idProveedor);
            psProductos.executeUpdate();
        }
        
        conn.commit(); // Confirmar transacción
        return true;
    } catch (SQLException ex) {
        if (conn != null) {
            conn.rollback(); // Revertir en caso de error
        }
        throw ex; // Relanzar la excepción para manejo superior
    } finally {
        if (conn != null) {
            conn.setAutoCommit(true); // Restaurar autocommit
            conn.close();
        }
    }
}
}
