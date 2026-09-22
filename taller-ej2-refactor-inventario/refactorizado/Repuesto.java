import java.util.Objects;

/**
 * Entidad Repuesto. Se sobrescriben equals() y hashCode() usando el código como
 * identidad para que contains()/remove(Object)/indexOf() funcionen por contenido.
 */
public class Repuesto {
    private final String codigo;
    private final String descripcion;
    private int cantidad;
    private final double precio;

    public Repuesto(String codigo, String descripcion, int cantidad, double precio) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (cantidad < 0 || precio < 0) {
            throw new IllegalArgumentException("Cantidad y precio no pueden ser negativos.");
        }
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        this.cantidad = cantidad;
    }

    public double getValorEnStock() {
        return cantidad * precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repuesto)) return false;
        return codigo.equals(((Repuesto) o).codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-22s | Cant: %4d | $%,12.2f",
                codigo, descripcion, cantidad, precio);
    }
}
