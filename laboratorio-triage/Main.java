import java.util.List;

/**
 * Laboratorio Práctico: Sistema Hospitalario de Admisión y Triage de Urgencias.
 */
public class Main {
    public static void main(String[] args) {
        SalaUrgencias sala = new SalaUrgencias();

        System.out.println("=== REGISTRO DE PACIENTES ===");
        sala.registrarPaciente(new Paciente("HC-1001", "Laura Martínez", 3));
        sala.registrarPaciente(new Paciente("HC-1002", "Jorge Ramírez", 5));
        sala.registrarPaciente(new Paciente("HC-1003", "Camila Rojas", 1));
        sala.registrarPaciente(new Paciente("HC-1004", "Andrés Pineda", 3));
        sala.registrarPaciente(new Paciente("HC-1005", "Valentina Ruiz", 2));
        sala.registrarPaciente(new Paciente("HC-1006", "Mateo Castro", 1));
        sala.registrarPaciente(null); // manejo defensivo

        // Validación de datos de entrada: triage fuera de rango
        try {
            sala.registrarPaciente(new Paciente("HC-1007", "Paciente Inválido", 7));
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        System.out.println("\nPacientes en espera: " + sala.cantidadEnEspera());
        for (Paciente p : sala.getListaEspera()) {
            System.out.println("  " + p);
        }

        System.out.println("\n=== FILTRO: PACIENTES CON TRIAGE 3 ===");
        List<Paciente> triage3 = sala.filtrarPorTriage(3);
        for (Paciente p : triage3) {
            System.out.println("  " + p);
        }

        System.out.println("\n=== ORDEN DE ATENCIÓN ===");
        Paciente atendido;
        int turno = 1;
        while ((atendido = sala.atenderSiguiente()) != null) {
            System.out.println("Turno " + turno++ + ": " + atendido);
        }

        System.out.println("\n=== LISTA VACÍA ===");
        System.out.println("atenderSiguiente() -> " + sala.atenderSiguiente());
        System.out.println("filtrarPorTriage(1) -> " + sala.filtrarPorTriage(1));
    }
}
