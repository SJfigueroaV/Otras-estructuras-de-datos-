/**
 * Entidad Paciente con encapsulamiento estricto y validación de datos de entrada.
 * nivelTriage: 1 = emergencia vital crítica ... 5 = consulta prioritaria menor.
 */
public class Paciente {
    public static final int TRIAGE_MIN = 1;
    public static final int TRIAGE_MAX = 5;

    private final String historiaClinica;
    private final String nombreCompleto;
    private final int nivelTriage;

    public Paciente(String historiaClinica, String nombreCompleto, int nivelTriage) {
        if (historiaClinica == null || historiaClinica.isBlank()) {
            throw new IllegalArgumentException("La historia clínica es obligatoria.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (nivelTriage < TRIAGE_MIN || nivelTriage > TRIAGE_MAX) {
            throw new IllegalArgumentException(
                "Nivel de triage inválido (" + nivelTriage + "). Debe estar entre "
                + TRIAGE_MIN + " y " + TRIAGE_MAX + ".");
        }
        this.historiaClinica = historiaClinica.trim();
        this.nombreCompleto = nombreCompleto.trim();
        this.nivelTriage = nivelTriage;
    }

    public String getHistoriaClinica() { return historiaClinica; }
    public String getNombreCompleto() { return nombreCompleto; }
    public int getNivelTriage() { return nivelTriage; }

    @Override
    public String toString() {
        return String.format("HC: %-8s | Paciente: %-22s | Triage: %d",
                historiaClinica, nombreCompleto, nivelTriage);
    }
}
