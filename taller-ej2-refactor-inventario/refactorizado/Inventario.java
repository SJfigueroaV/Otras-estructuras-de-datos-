import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de inventario REFACTORIZADO a List<Repuesto>.
 * - Sin límite fijo ni contador manual: el tamaño lo gestiona size().
 * - Sin búsquedas por posición manual: se usan stream/removeIf/contains.
 * - La compactación al eliminar la hace ArrayList internamente.
 * - El getter devuelve una vista de solo lectura (encapsulamiento defensivo).
 */
public class Inventario {
    private final List<Repuesto> stock = new ArrayList<>(50);

    /** Agrega un repuesto; si el código ya existe, no lo duplica. */
    public boolean agregar(Repuesto r) {
        if (r == null || stock.contains(r)) { // contains usa equals() por código
            return false;
        }
        return stock.add(r);
    }

    public Optional<Repuesto> buscar(String codigo) {
        return stock.stream()
                .filter(r -> r.getCodigo().equals(codigo))
                .findFirst();
    }

    public boolean eliminar(String codigo) {
        return stock.removeIf(r -> r.getCodigo().equals(codigo));
    }

    public boolean actualizarCantidad(String codigo, int nuevaCantidad) {
        Optional<Repuesto> encontrado = buscar(codigo);
        encontrado.ifPresent(r -> r.setCantidad(nuevaCantidad));
        return encontrado.isPresent();
    }

    public List<Repuesto> conStockBajo(int umbral) {
        List<Repuesto> resultado = new ArrayList<>();
        for (Repuesto r : stock) {
            if (r.getCantidad() < umbral) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public double valorTotal() {
        return stock.stream().mapToDouble(Repuesto::getValorEnStock).sum();
    }

    public int getTotal() {
        return stock.size();
    }

    public List<Repuesto> getStock() {
        return Collections.unmodifiableList(stock);
    }
}
