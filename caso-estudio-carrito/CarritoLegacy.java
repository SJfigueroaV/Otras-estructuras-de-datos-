/** CÓDIGO CAUSANTE DE LA FALLA (tal como aparece en el caso de estudio). */
public class CarritoLegacy {
    private Producto[] articulos = new Producto[10];
    private int contador = 0;

    public void agregar(Producto p) {
        articulos[contador] = p; // Provoca ArrayIndexOutOfBoundsException al 11.º producto
        contador++;
    }
}
