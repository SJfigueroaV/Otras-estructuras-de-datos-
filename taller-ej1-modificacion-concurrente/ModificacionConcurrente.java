import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * Taller 6 - Ejercicio 1: La trampa de la modificación concurrente.
 *
 * Consigna 1.1 (origen de la excepción):
 * El for-each se traduce internamente a un Iterator. ArrayList lleva un contador
 * de modificaciones estructurales (modCount) y el iterador guarda una copia
 * (expectedModCount) al crearse. Al llamar codigos.remove(c) directamente sobre la
 * lista, modCount aumenta pero el iterador no se entera; en la siguiente llamada a
 * next() el iterador compara ambos valores, detecta la diferencia y lanza
 * ConcurrentModificationException (comportamiento "fail-fast").
 * (Curiosidad: si el elemento eliminado es el penúltimo, hasNext() devuelve false
 * porque cursor == size y el bucle termina sin excepción, saltándose el último
 * elemento; por eso con 3 elementos se usa aquí "CTA-01" para forzar el fallo, y
 * también se muestra el caso "CTA-02" del enunciado).
 *
 * Consigna 1.2: eliminación segura con Iterator.remove() y con removeIf().
 */
public class ModificacionConcurrente {

    private static List<String> crearCodigos() {
        List<String> codigos = new ArrayList<>();
        codigos.add("CTA-01");
        codigos.add("CTA-02");
        codigos.add("CTA-03");
        return codigos;
    }

    public static void main(String[] args) {
        // --- 1. Bucle defectuoso ---
        System.out.println("=== 1. for-each + list.remove() (defectuoso) ===");
        List<String> codigos = crearCodigos();
        try {
            for (String c : codigos) {
                if (c.equals("CTA-01")) {
                    codigos.remove(c);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Excepción capturada: " + e.getClass().getSimpleName());
        }

        List<String> codigos2 = crearCodigos();
        for (String c : codigos2) {
            System.out.println("  visitado: " + c);
            if (c.equals("CTA-02")) {
                codigos2.remove(c);
            }
        }
        System.out.println("Con CTA-02 no lanza excepción, pero CTA-03 nunca se visitó: "
                + codigos2);

        // --- 2. Solución con Iterator ---
        System.out.println("\n=== 2. Eliminación segura con Iterator ===");
        codigos = crearCodigos();
        Iterator<String> it = codigos.iterator();
        while (it.hasNext()) {
            String c = it.next();
            if (c.equals("CTA-02")) {
                it.remove(); // actualiza modCount y expectedModCount a la vez
            }
        }
        System.out.println("Resultado: " + codigos);

        // --- 3. Solución moderna con removeIf (Java 8+) ---
        System.out.println("\n=== 3. Eliminación con removeIf ===");
        codigos = crearCodigos();
        boolean huboCambios = codigos.removeIf(c -> c.equals("CTA-02"));
        System.out.println("Resultado: " + codigos + " (¿eliminó algo? " + huboCambios + ")");

        /*
         * Comparación:
         * - Iterator: explícito, más verboso, permite lógica compleja dentro del
         *   ciclo (p. ej. registrar lo eliminado o detenerse al primer hallazgo).
         * - removeIf: declarativo, una sola línea, menos propenso a errores y en
         *   ArrayList es más eficiente: marca los elementos a eliminar y compacta el
         *   arreglo una sola vez (O(n)), mientras que varias llamadas a it.remove()
         *   desplazan elementos en cada eliminación (O(n^2) en el peor caso).
         */
    }
}
