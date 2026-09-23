# Respuestas escritas del taller

## 8. Taller colaborativo, ejercicio 1: la trampa de la modificación concurrente

### Consigna 1.1: origen técnico de la `ConcurrentModificationException`

El bucle *for-each* no recorre la lista directamente: el compilador lo traduce a un `Iterator` que llama a `hasNext()` y `next()` en cada vuelta.

- `ArrayList` lleva un contador interno de modificaciones estructurales llamado `modCount`, que aumenta cada vez que se agrega o se elimina un elemento.
- Cuando se crea el iterador, este guarda una copia de ese valor en `expectedModCount`.
- Al ejecutar `codigos.remove(c)` directamente sobre la lista, `modCount` aumenta, pero el iterador no se entera y su `expectedModCount` queda desactualizado.
- En la siguiente llamada a `next()`, el iterador compara ambos valores, detecta que no coinciden y lanza `ConcurrentModificationException`. A este comportamiento se le llama *fail-fast*: el iterador prefiere fallar de inmediato antes que seguir recorriendo una estructura que cambió por debajo.

**Observación sobre el código del enunciado:** con exactamente tres elementos, eliminar `"CTA-02"` (el penúltimo) **no** lanza la excepción. Después de borrarlo, la lista queda con tamaño 2 y el cursor del iterador ya está en 2, así que `hasNext()` devuelve `false` y el bucle termina en silencio, **sin visitar `"CTA-03"`**. El error sigue ahí, pero es peor porque no avisa. Con cualquier otra posición (por ejemplo `"CTA-01"`) sí se lanza la excepción. Ambos casos se demuestran en `taller-ej1-modificacion-concurrente/ModificacionConcurrente.java`.

### Consigna 1.2: comparación entre `Iterator` y `removeIf`

```java
// Con Iterator
Iterator<String> it = codigos.iterator();
while (it.hasNext()) {
    if (it.next().equals("CTA-02")) {
        it.remove(); // actualiza modCount y expectedModCount a la vez
    }
}

// Con removeIf (Java 8+)
codigos.removeIf(c -> c.equals("CTA-02"));
```

| Criterio | `Iterator.remove()` | `removeIf(...)` |
|---|---|---|
| Legibilidad | Más verboso (4 a 6 líneas) | Una sola línea, declarativo |
| Riesgo de error | Hay que llamar a `next()` antes de `remove()`, o se lanza `IllegalStateException` | Mínimo: la iteración la maneja la lista |
| Flexibilidad | Permite lógica compleja en el ciclo (registrar lo eliminado, detenerse antes, etc.) | Solo recibe una condición |
| Rendimiento en `ArrayList` | Cada eliminación desplaza los elementos siguientes: O(n²) en el peor caso | Marca los elementos y compacta una sola vez: O(n) |
| Retorno | Ninguno | `true` si eliminó algo |

Conclusión: `removeIf` es la opción recomendada para filtros simples, y el `Iterator` explícito se reserva para cuando se necesita más control dentro del recorrido.

---

## 9. Actividad evaluativa

### 1. Pregunta conceptual de ensayo: capacidad física vs. tamaño lógico

Un `ArrayList` maneja dos cantidades distintas:

- **Capacidad física (*capacity*):** es el largo del arreglo interno `Object[] elementData`, es decir, cuántas casillas hay reservadas en memoria. No se puede consultar con ningún método público y crece automáticamente (aproximadamente × 1,5) cuando se llena.
- **Tamaño lógico (*size*):** es la cantidad de elementos que realmente se han agregado. Es lo que retorna `size()` y define qué índices son válidos: de `0` a `size() - 1`.

Al declarar `ArrayList<String> nombres = new ArrayList<>(50);` se reserva un arreglo interno de 50 casillas, pero **no se agrega ningún elemento**: la capacidad es 50 y el tamaño es 0. Las casillas reservadas son espacio vacío, no elementos de la lista.

El método `get(int index)` valida el índice contra el **tamaño**, no contra la capacidad (internamente usa `Objects.checkIndex(index, size)`). Como el tamaño es 0, no existe ningún índice válido, y `nombres.get(10)` lanza:

```
IndexOutOfBoundsException: Index 10 out of bounds for length 0
```

El constructor con capacidad inicial solo sirve para evitar redimensiones cuando se sabe cuántos elementos llegarán. Para poder hacer `get(10)` primero hay que agregar al menos 11 elementos con `add()`. Esto se demuestra en `actividad-evaluativa-traza/CapacidadVsTamano.java`.

### 2. Análisis de traza de memoria

| Instrucción | Contenido de la lista | `size()` |
|---|---|---|
| `new ArrayList<>()` | `[]` | 0 |
| `add("Bogotá")` | `[Bogotá]` | 1 |
| `add("Medellín")` | `[Bogotá, Medellín]` | 2 |
| `add(1, "Cali")` | `[Bogotá, Cali, Medellín]` (Medellín se desplaza al índice 2) | 3 |
| `remove(0)` | `[Cali, Medellín]` (los índices se compactan a la izquierda) | 2 |

**Salida exacta por consola:**

```
Índice 0 actual: Cali
Tamaño resultante: 2
```

Verificado ejecutando `actividad-evaluativa-traza/TrazaCiudades.java`.

### 3. Selección múltiple con única respuesta

**3.1. Al declarar `Cliente[] cartera = new Cliente[5];`, ¿cuál es el estado exacto de la memoria Heap?**

**Respuesta: b)** Se reserva un bloque continuo para un arreglo que aloja 5 referencias inicializadas en `null`.

- a) es incorrecta: no se crea ningún objeto `Cliente`; cada uno se debe instanciar aparte con `new Cliente(...)`.
- c) es incorrecta: crear el arreglo no llama al constructor de `Cliente`, así que compila sin problema.
- d) es incorrecta: en Java todos los arreglos son objetos y siempre se crean en el Heap.

**3.2. ¿Cuál de las siguientes declaraciones ilustra la mejor práctica de desacoplamiento en Java?**

**Respuesta: b)** `List<Producto> items = new ArrayList<>();`

- La variable se declara con la interfaz `List`, así que se puede cambiar la implementación (por ejemplo a `LinkedList`) sin modificar el resto del código, y usa el operador diamante.
- a) funciona, pero acopla la variable a la clase concreta `ArrayList`.
- c) no compila: `List` es una interfaz y no se puede instanciar.
- d) no compila: los genéricos son invariantes, y un `ArrayList<Producto>` no es un `ArrayList<Object>`.

### 4. Verdadero / Falso justificadas

**4.1. "La remoción de un elemento mediante `ArrayList.remove(0)` deja la posición 0 con el valor `null` para evitar tener que reordenar el resto de la lista."**

**Falso.** `remove(0)` desplaza todos los elementos siguientes una posición a la izquierda con `System.arraycopy`, de modo que el que estaba en el índice 1 pasa al 0, y reduce `size()` en uno. El único `null` que se escribe es en la **última** casilla del arreglo interno, que queda fuera del tamaño lógico, para que el recolector de basura pueda liberar esa referencia. Por eso eliminar al inicio de un `ArrayList` cuesta O(n). Dejar la casilla en `null` es lo que pasa con un arreglo estático cuando se "elimina" manualmente, no con `ArrayList`.

**4.2. "Es posible declarar una colección dinámica de datos numéricos enteros utilizando la sintaxis `List<int> datos = new ArrayList<>();`."**

**Falso.** No compila, porque los genéricos solo aceptan tipos de referencia (objetos), no primitivos. Hay que usar la clase envolvente: `List<Integer> datos = new ArrayList<>();`. Gracias al *autoboxing*, igual se puede escribir `datos.add(5)` y Java convierte el `int` en `Integer` automáticamente.

---

## 10. Preguntas para discusión

### 1. Rendimiento vs. flexibilidad: ¿en qué escenarios sigue siendo obligatorio usar arreglos estáticos?

Los arreglos estáticos siguen siendo la opción obligada cuando el sistema necesita **tiempos predecibles** y **memoria fija conocida de antemano**:

- **Software embebido y microcontroladores:** estos dispositivos tienen muy poca RAM (a veces unos pocos KB) y muchas veces no tienen recolector de basura ni memoria dinámica. Se debe saber exactamente cuánta memoria usa el programa desde que se compila.
- **Control industrial y sistemas de tiempo real** (PLC, frenos ABS, marcapasos, control de vuelo): una operación debe terminar siempre dentro de un plazo fijo. La autoexpansión de un `ArrayList` puede tardar mucho más en una llamada puntual (copia completa del arreglo), y el recolector de basura puede pausar el programa en un momento crítico. Esa variación es inaceptable cuando un retraso puede causar un accidente.
- **Procesamiento de señales en tiempo real** (audio, video, sensores, radares): se trabaja con *buffers* de tamaño fijo (por ejemplo 1024 muestras) y con millones de números por segundo. Un `double[]` guarda los valores seguidos en memoria, lo que aprovecha la caché del procesador. Un `List<Double>` guarda referencias a objetos `Double` repartidos por el Heap, ocupa varias veces más memoria y genera basura constantemente por el *autoboxing*.
- **Estructuras de tamaño fijo por naturaleza:** matrices de imágenes, tableros de juego y tablas de consulta cuyo tamaño nunca cambia.

En resumen: cuando el tamaño es fijo y el rendimiento o la previsibilidad son críticos, se prefiere el arreglo estático. Cuando el volumen de datos varía (como en el carrito del caso de estudio), la flexibilidad del `ArrayList` vale su costo.

### 2. Encapsulamiento defensivo: riesgos de `return this.transacciones;` y cómo lo previene `Collections.unmodifiableList`

Si el getter retorna directamente la lista interna, el código externo recibe **la misma referencia** que usa la clase, y puede modificar su estado interno sin pasar por ninguna validación. Si alguien ejecuta `cuenta.getTransacciones().clear()`:

- **Pérdida de integridad:** se borra todo el historial de la cuenta sin dejar rastro, y el saldo deja de coincidir con las transacciones registradas.
- **Riesgo de seguridad y fraude:** de la misma forma se podrían agregar transacciones falsas con `add()` o eliminar movimientos específicos con `remove()`, saltándose las reglas de negocio (validar montos, fondos suficientes, permisos).
- **Pérdida de trazabilidad:** no queda auditoría de quién hizo el cambio, porque la modificación no pasó por ningún método de la clase `Cuenta`.
- **Errores difíciles de rastrear:** el cambio puede venir de cualquier parte del programa, y el fallo aparece lejos de donde se originó.

**Cómo lo previene `Collections.unmodifiableList`:**

```java
public List<Transaccion> getTransacciones() {
    return Collections.unmodifiableList(this.transacciones);
}
```

Retorna una **vista de solo lectura** de la lista. Se puede recorrer y consultar (`get`, `size`, `for-each`), pero cualquier intento de modificarla (`clear`, `add`, `remove`, `set`) lanza `UnsupportedOperationException`. Así la única forma de cambiar las transacciones es a través de los métodos de la propia clase (por ejemplo `registrarTransaccion(...)`), que sí aplican las validaciones.

Como es una vista, sigue reflejando los cambios que la propia clase haga después. Si se necesita una copia congelada que no cambie, se puede usar `List.copyOf(this.transacciones)` (Java 10+). Cualquiera de las dos protege solo la lista: si los objetos `Transaccion` tienen *setters*, también deberían ser inmutables para que la protección sea completa.
