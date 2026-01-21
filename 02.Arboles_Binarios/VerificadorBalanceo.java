package arboles;

/**
 * Clase utilitaria para verificar el balanceo fonético de un árbol binario.
 * Utiliza características modernas de Java 21 (Records, Pattern Matching).
 */
public class VerificadorBalanceo {

    // Constante con todas las vocales (incluyendo acentuadas) para la búsqueda
    private static final String VOCALES = "aeiouáéíóúAEIOUÁÉÍÓÚ";

    /**
     * RECORD INTERNO (Java 14+):
     * Usamos un 'record' en lugar de una clase auxiliar clásica.
     * Nos permite devolver dos valores a la vez (si es válido y cuántas vocales tiene)
     * de forma limpia, inmutable y sin escribir getters/setters.
     */
    public record Resultado(boolean esBalanceado, int numVocales) {}

    /**
     * Método principal (Punto de entrada).
     * @param tree El árbol a analizar.
     * @return true si el árbol cumple la condición de balanceo, false en caso contrario.
     */
    public static boolean esBalanceado(BinaryTree<String> tree) {
        // Llamamos a la función recursiva y solo extraemos la parte booleana
        return verificar(tree).esBalanceado();
    }

    /**
     * Método recursivo auxiliar.
     * Realiza un recorrido POST-ORDER (Izquierda -> Derecha -> Raíz).
     * Complejidad: O(n) - Visitamos cada nodo una sola vez.
     */
    private static Resultado verificar(BinaryTree<String> tree) {
        
        // PATTERN MATCHING FOR SWITCH (Java 17/21):
        // Sustituye a los antiguos "if (tree instanceof BLeaf)"
        // Es más limpio y el compilador asegura que cubres todos los casos.
        return switch (tree) {
            
            // CASO 1: Árbol nulo (defensivo) o Vacío
            // Un árbol vacío se considera balanceado y tiene 0 vocales.
            case null -> new Resultado(true, 0);
            case BEmpty<String> t -> new Resultado(true, 0);

            // CASO 2: Hoja (Nodo sin hijos)
            // Una hoja siempre está balanceada (no tiene hijos que comparar).
            // Solo necesitamos contar sus vocales para informar hacia arriba.
            case BLeaf<String> t -> 
                new Resultado(true, contarVocales(t.label()));

            // CASO 3: Nodo Interno (Tiene subárboles izquierdo y derecho)
            case BTree<String> t -> {
                
                // PASO A (Recursión): Preguntamos a los hijos primero (Bottom-Up)
                Resultado izq = verificar(t.left());
                Resultado der = verificar(t.right());

                // PASO B (Lógica de Negocio):
                // 1. ¿Mis hijos son válidos internamente? (Propagación del false)
                boolean hijosSonValidos = izq.esBalanceado() && der.esBalanceado();
                
                // 2. ¿Tengo yo el mismo peso fonético a izquierda y derecha?
                boolean mismoPeso = izq.numVocales() == der.numVocales();

                // PASO C (Cálculo Total):
                // Sumamos vocales de izq + der + las de mi propia etiqueta
                int totalVocales = izq.numVocales() + der.numVocales() + contarVocales(t.label());

                // PASO D (Retorno):
                // Usamos 'yield' para devolver el valor dentro del bloque del switch.
                // El nodo es válido SOLO SI sus hijos lo son Y sus pesos coinciden.
                yield new Resultado(hijosSonValidos && mismoPeso, totalVocales);
            }
        };
    }

    /**
     * Cuenta las vocales en una cadena usando Streams (Programación Funcional).
     */
    private static int contarVocales(String str) {
        if (str == null) return 0;
        return (int) str.chars() // Convierte el String en un flujo de enteros (chars)
                .filter(c -> VOCALES.indexOf(c) != -1) // Filtra solo si es vocal
                .count(); // Cuenta los aciertos
    }
}