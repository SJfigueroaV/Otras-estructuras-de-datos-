/**
 * Repositorio LEGADO de inventario (versión original, antes de refactorizar).
 * Problemas: capacidad fija de 50, contador manual, búsquedas por posición,
 * compactación manual al eliminar y riesgo de ArrayIndexOutOfBoundsException.
 */
public class InventarioLegado {
    private Repuesto[] stock = new Repuesto[50];
    private int total = 0;

    public boolean agregar(Repuesto r) {
        if (total >= stock.length) {
            return false; // inventario "lleno": no puede crecer
        }
        stock[total] = r;
        total++;
        return true;
    }

    public int buscarPosicion(String codigo) {
        for (int i = 0; i < total; i++) {
            if (stock[i].getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    public Repuesto buscar(String codigo) {
        int pos = buscarPosicion(codigo);
        return pos == -1 ? null : stock[pos];
    }

    public boolean eliminar(String codigo) {
        int pos = buscarPosicion(codigo);
        if (pos == -1) {
            return false;
        }
        // Compactación manual: correr todos los elementos a la izquierda
        for (int i = pos; i < total - 1; i++) {
            stock[i] = stock[i + 1];
        }
        stock[total - 1] = null;
        total--;
        return true;
    }

    public double valorTotal() {
        double suma = 0;
        for (int i = 0; i < total; i++) {
            suma += stock[i].getCantidad() * stock[i].getPrecio();
        }
        return suma;
    }

    public int getTotal() { return total; }

    public void listar() {
        for (int i = 0; i < total; i++) {
            System.out.println(stock[i]);
        }
    }
}
