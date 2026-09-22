import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solución de arquitectura del caso "SuperTienda Express":
 * 1. List<Producto> con ArrayList<>(16) en lugar del arreglo de 10.
 * 2. Acceso protegido con Collections.unmodifiableList.
 * 3. Sin contador manual: se delega en size().
 */
public class Carrito {
    private final List<Producto> articulos = new ArrayList<>(16);

    public void agregar(Producto p) {
        if (p != null) {
            articulos.add(p);
        }
    }

    public int cantidad() {
        return articulos.size();
    }

    public double total() {
        double total = 0;
        for (Producto p : articulos) {
            total += p.getPrecio();
        }
        return total;
    }

    public List<Producto> getArticulos() {
        return Collections.unmodifiableList(articulos);
    }
}
