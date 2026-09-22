import java.util.List;

/**
 * Taller 6 - Ejercicio 2: demostración del inventario refactorizado a List.
 */
public class Main {
    public static void main(String[] args) {
        Inventario inv = new Inventario();
        inv.agregar(new Repuesto("R-001", "Filtro de aceite", 40, 18500));
        inv.agregar(new Repuesto("R-002", "Pastillas de freno", 12, 95000));
        inv.agregar(new Repuesto("R-003", "Bujía iridio", 3, 42000));
        inv.agregar(new Repuesto("R-004", "Correa de distribución", 6, 210000));
        boolean duplicado = inv.agregar(new Repuesto("R-002", "Duplicado", 1, 1));
        System.out.println("¿Se agregó el duplicado R-002? " + duplicado);

        // Supera el límite de 50 del diseño legado sin ningún problema
        for (int i = 5; i <= 60; i++) {
            inv.agregar(new Repuesto(String.format("R-%03d", i), "Tornillo M" + i, 100, 500));
        }
        System.out.println("Total de repuestos (legado se detenía en 50): " + inv.getTotal());

        System.out.println("\nBuscar R-003: "
                + inv.buscar("R-003").map(Repuesto::toString).orElse("no existe"));
        System.out.println("Buscar R-999: "
                + inv.buscar("R-999").map(Repuesto::toString).orElse("no existe"));

        inv.actualizarCantidad("R-003", 25);
        System.out.println("R-003 tras actualizar: " + inv.buscar("R-003").get());

        System.out.println("\n¿Eliminó R-001? " + inv.eliminar("R-001"));
        System.out.println("¿Eliminó R-001 otra vez? " + inv.eliminar("R-001"));
        System.out.println("Primer elemento ahora (índices compactados): " + inv.getStock().get(0));

        System.out.println("\n--- Repuestos con stock bajo (< 10) ---");
        List<Repuesto> bajos = inv.conStockBajo(10);
        bajos.forEach(r -> System.out.println("  " + r));

        System.out.printf("%nValor total del inventario: $%,.2f%n", inv.valorTotal());

        try {
            inv.getStock().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("getStock().clear() bloqueado: la lista interna está protegida.");
        }
    }
}
