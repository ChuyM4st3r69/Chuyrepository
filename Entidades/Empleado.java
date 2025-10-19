/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Jesus Castillo
 */
public class Empleado {
    private int id;
    private String nombres;
    private String apellidos;
    private String rfc;
    private String nss;
    private String calleNum;
    private int idPuesto;
    private Integer idColonia; // Usamos Integer para permitir null
    private ColoniaEmpleado colonia; // Objeto completo de colonia

    // Constructor completo
    public Empleado(int id, String nombres, String apellidos, String rfc, 
                   String nss, String calleNum, int idPuesto, Integer idColonia) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rfc = rfc;
        this.nss = nss;
        this.calleNum = calleNum;
        this.idPuesto = idPuesto;
        this.idColonia = idColonia;
    }

    // Constructor vacío
    public Empleado() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getCalleNum() {
        return calleNum;
    }

    public void setCalleNum(String calleNum) {
        this.calleNum = calleNum;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public ColoniaEmpleado getColonia() {
        return colonia;
    }

    public void setColonia(ColoniaEmpleado colonia) {
        this.colonia = colonia;
        this.idColonia = colonia != null ? colonia.getId() : null;
    }

    // Método para obtener dirección completa
    public String getDireccionCompleta() {
        return calleNum + (colonia != null ? ", " + colonia.getNombre() : "");
    }

    @Override
    public String toString() {
        return "Empleado{" +
               "id=" + id +
               ", nombres='" + nombres + '\'' +
               ", apellidos='" + apellidos + '\'' +
               ", rfc='" + rfc + '\'' +
               ", nss='" + nss + '\'' +
               ", calleNum='" + calleNum + '\'' +
               ", idPuesto=" + idPuesto +
               ", idColonia=" + idColonia +
               '}';
    }
}
