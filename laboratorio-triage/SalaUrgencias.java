import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controlador de la sala de urgencias. Gestiona la lista de espera con una
 * colección dinámica tipada (programando hacia la interfaz List).
 */
public class SalaUrgencias {
    private List<Paciente> listaEspera = new ArrayList<>();

    /** Añade un paciente al final de la colección (ignora referencias nulas). */
    public void registrarPaciente(Paciente p) {
        if (p == null) {
            System.out.println("[AVISO] No se puede registrar un paciente nulo.");
            return;
        }
        listaEspera.add(p);
    }

    /**
     * Localiza al paciente con el nivelTriage más bajo (mayor prioridad), lo retira
     * de la lista y lo retorna. Ante empates se respeta el orden de llegada (FIFO).
     * Si la lista está vacía retorna null de forma segura.
     */
    public Paciente atenderSiguiente() {
        if (listaEspera.isEmpty()) {
            return null;
        }
        int indicePrioritario = 0;
        for (int i = 1; i < listaEspera.size(); i++) {
            if (listaEspera.get(i).getNivelTriage()
                    < listaEspera.get(indicePrioritario).getNivelTriage()) {
                indicePrioritario = i;
            }
        }
        // remove(int) retorna el elemento y compacta los índices hacia la izquierda
        return listaEspera.remove(indicePrioritario);
    }

    /** Retorna una NUEVA lista con los pacientes del nivel indicado. */
    public List<Paciente> filtrarPorTriage(int triageObjetivo) {
        List<Paciente> resultado = new ArrayList<>();
        if (triageObjetivo < Paciente.TRIAGE_MIN || triageObjetivo > Paciente.TRIAGE_MAX) {
            return resultado; // nivel fuera de rango: lista vacía, nunca null
        }
        for (Paciente p : listaEspera) {
            if (p.getNivelTriage() == triageObjetivo) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public int cantidadEnEspera() {
        return listaEspera.size();
    }

    /** Vista de solo lectura para no exponer el estado interno. */
    public List<Paciente> getListaEspera() {
        return Collections.unmodifiableList(listaEspera);
    }
}
