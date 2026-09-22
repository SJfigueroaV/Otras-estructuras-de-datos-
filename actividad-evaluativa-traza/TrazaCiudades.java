import java.util.ArrayList;
import java.util.List;

/**
 * Actividad Evaluativa - Punto 2: Análisis de traza de memoria.
 *
 * Traza:
 *   add("Bogotá")      -> [Bogotá]
 *   add("Medellín")    -> [Bogotá, Medellín]
 *   add(1, "Cali")     -> [Bogotá, Cali, Medellín]   (Medellín se desplaza al índice 2)
 *   remove(0)          -> [Cali, Medellín]           (se compacta a la izquierda)
 *
 * Salida esperada:
 *   Índice 0 actual: Cali
 *   Tamaño resultante: 2
 */
public class TrazaCiudades {
    public static void main(String[] args) {
        List<String> ciudades = new ArrayList<>();
        ciudades.add("Bogotá");
        ciudades.add("Medellín");
        ciudades.add(1, "Cali");
        ciudades.remove(0);
        System.out.println("Índice 0 actual: " + ciudades.get(0));
        System.out.println("Tamaño resultante: " + ciudades.size());
    }
}
