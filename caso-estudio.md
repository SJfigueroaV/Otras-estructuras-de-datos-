# Caso de Estudio: Colapso del Módulo de Facturación de "SuperTienda Express" en Black Friday

## Preguntas para discusión

### 1. ¿De qué manera la decisión de limitar a 10 productos una compra mediante un arreglo estático violó el atributo de adaptabilidad de la norma ISO/IEC 25010?

Al limitar a 10 la cantidad de productos que se pueden ingresar al carrito, se falla en la característica de **Flexibilidad** (llamada *Portabilidad* en la versión 2011 de la norma), específicamente en la subcaracterística de **adaptabilidad**, porque:

- **No se puede cambiar el volumen de datos:** la cantidad de productos que se pueden ingresar no se puede aumentar sin modificar el código fuente.
- **No es posible adaptar la clase a un nuevo escenario:** combos familiares y paquetes escolares requieren manejar más productos, y la clase no se puede ajustar a esa necesidad.
- **Consecuencia:** el sistema queda expuesto a `ArrayIndexOutOfBoundsException` cuando se supera la cantidad de espacios del arreglo, lo que además afecta la fiabilidad (tolerancia a fallos) y dejó transacciones financieras a medio procesar.

### 2. Si se espera un promedio de 12 productos por transacción, ¿qué impacto tiene definir `new ArrayList<>(16)` frente al constructor por defecto `new ArrayList<>()`?

Al definir `new ArrayList<>(16)` se evita el realojamiento de memoria:

- Con `new ArrayList<>()` la lista reserva 10 espacios con el primer `add()`. Al agregar el **undécimo** elemento, el arreglo interno debe redimensionarse a 15 posiciones (10 × 1,5): se crea un arreglo nuevo, se copian las 10 referencias con `System.arraycopy` y el arreglo anterior queda como basura para el *Garbage Collector*, lo que consume tiempo y memoria.
- Con `new ArrayList<>(16)` hay **una sola reserva y ninguna copia** para 12 productos, y queda margen hasta 16 artículos. El costo es mínimo (unas 4 casillas sin usar por carrito).
- Con 25.000 transacciones simultáneas, evitar esa redimensión en cada carrito reduce el trabajo del recolector de basura justo en el pico de carga.
