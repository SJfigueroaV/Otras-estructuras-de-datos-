/** Reproduce la falla del carrito legado y verifica la solución con List. */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== CarritoLegacy (arreglo de 10) ===");
        CarritoLegacy legacy = new CarritoLegacy();
        int i = 0;
        try {
            for (i = 1; i <= 12; i++) {
                legacy.agregar(new Producto("Útil escolar " + i, 3500));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Falla al agregar el producto #" + i + ": " + e.getMessage());
        }

        System.out.println("\n=== Carrito (List con capacidad inicial 16) ===");
        Carrito carrito = new Carrito();
        for (i = 1; i <= 25; i++) {
            carrito.agregar(new Producto("Útil escolar " + i, 3500));
        }
        System.out.println("Productos en el carrito: " + carrito.cantidad());
        System.out.printf("Total: $%,.2f%n", carrito.total());
    }
}
