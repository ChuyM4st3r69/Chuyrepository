/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author Jesus Castillo
 */
public class Puesto {
    private int id;
    private String nombre;
    private double sueldo;
    private int idDepartamento;

    // Constructores
    public Puesto() {}
    
    public Puesto(int id, String nombre, double sueldo, int idDepartamento) {
        this.id = id;
        this.nombre = nombre;
        this.sueldo = sueldo;
        this.idDepartamento = idDepartamento;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
