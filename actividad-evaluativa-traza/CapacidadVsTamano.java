import java.util.ArrayList;

/**
 * Actividad Evaluativa - Punto 1: capacidad física (capacity) vs tamaño lógico (size).
 * new ArrayList<>(50) reserva un arreglo interno de 50 casillas, pero size() sigue
 * siendo 0. get(i) valida el índice contra size (no contra la capacidad), por lo
 * que get(10) lanza IndexOutOfBoundsException.
 */
public class CapacidadVsTamano {
    public static void main(String[] args) {
        ArrayList<String> nombres = new ArrayList<>(50);
        System.out.println("size() = " + nombres.size());
        try {
            nombres.get(10);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException: " + e.getMessage());
        }
    }
}
