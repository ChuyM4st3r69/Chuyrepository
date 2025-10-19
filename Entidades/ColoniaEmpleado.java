package Entidades;

import java.util.Objects;

/**
 *
 * @author Jesus Castillo
 */
public class ColoniaEmpleado {
    private int id;
    private String nombre;

    // Constructor completo
    public ColoniaEmpleado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor vacío
    public ColoniaEmpleado() {}

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

    // Para mostrar correctamente en JComboBox
    @Override
    public String toString() {
        return nombre;
    }

    // Método equals para comparaciones
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ColoniaEmpleado that = (ColoniaEmpleado) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
