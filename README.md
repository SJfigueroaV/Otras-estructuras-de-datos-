# Otras Estructuras de Datos: Arreglos de Objetos y ArrayList

Solución de las actividades con código del taller *Otras Estructuras de Datos* (Java SE 11).

| Carpeta | Actividad |
|---|---|
| `laboratorio-triage/` | **7. Laboratorio Práctico**: sistema hospitalario de admisión y triage (`Paciente`, `SalaUrgencias`, `Main`). |
| `taller-ej1-modificacion-concurrente/` | **8. Taller, ejercicio 1**: `ConcurrentModificationException` (explicación en los comentarios), solución con `Iterator` y con `removeIf`. |
| `taller-ej2-refactor-inventario/` | **8. Taller, ejercicio 2**: inventario `Repuesto[50]` + contador (`legado/`) refactorizado a `List<Repuesto>` (`refactorizado/`). |
| `caso-estudio-carrito/` | **5. Caso de estudio**: reproducción de la falla de `CarritoLegacy` y solución con `List<Producto>` (`ArrayList<>(16)`). |
| `actividad-evaluativa-traza/` | **9. Actividad evaluativa**, puntos 1 y 2: capacidad vs. tamaño y traza de `ciudades`. |

## Cómo ejecutar

Cada carpeta es independiente (sin paquetes). Ejemplo:

```bash
cd laboratorio-triage
javac -encoding UTF-8 *.java
java Main
```

Clases principales: `Main` (laboratorio, taller ej. 2 `refactorizado/`, caso de estudio),
`ModificacionConcurrente` (taller ej. 1), `TrazaCiudades` y `CapacidadVsTamano` (actividad evaluativa).

## Notas del laboratorio

- `Paciente` valida en el constructor que el triage esté entre 1 y 5 (y que historia y nombre no estén vacíos); si no, lanza `IllegalArgumentException`.
- `atenderSiguiente()` recorre la lista buscando el triage más bajo, lo retira con `remove(int)` y lo retorna; si hay empate atiende primero al que llegó antes. Si la lista está vacía retorna `null`.
- `filtrarPorTriage(int)` siempre retorna una lista nueva (vacía si no hay coincidencias o el nivel está fuera de rango, nunca `null`).
