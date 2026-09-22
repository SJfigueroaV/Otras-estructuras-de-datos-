public class Repuesto {
    private String codigo;
    private String descripcion;
    private int cantidad;
    private double precio;

    public Repuesto(String codigo, String descripcion, int cantidad, double precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return String.format("%-8s | %-22s | Cant: %4d | $%,12.2f",
                codigo, descripcion, cantidad, precio);
    }
}
