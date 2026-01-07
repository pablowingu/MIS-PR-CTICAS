package proyectosGitHub;
import java.util.HashMap;
import java.util.Map;

public class EjercicioT {

    // -------------------------------------------------------------------------
    // 1. Estructura auxiliar para la clave del Mapa (Record)
    // -------------------------------------------------------------------------
    // Al ponerlo aquí dentro, no necesitas crear un archivo InPair.java aparte.
    // Java genera automáticamente equals() y hashCode() para que funcione el Map.
    public record InPair(Integer n, Integer m) {
        public static InPair of(Integer n, Integer m) {
            return new InPair(n, m);
        }
    }

    // -------------------------------------------------------------------------
    // 2. Método Público (Fachada)
    // -------------------------------------------------------------------------
    // Este es el que llamas desde fuera. Se encarga de preparar la memoria.
    public Integer calcularT(Integer n, Integer m) {
        // Creamos el mapa vacío (caché) para esta ejecución concreta
        Map<InPair, Integer> memoria = new HashMap<>();
        return recursivaConMemoria(n, m, memoria);
    }

    // -------------------------------------------------------------------------
    // 3. Método Privado (Lógica Recursiva)
    // -------------------------------------------------------------------------
    private Integer recursivaConMemoria(Integer n, Integer m, Map<InPair, Integer> mapa) {
        
        // A) Verificamos si ya calculamos esto antes (O(1)), para guardar en el res
    	//como resultado el valor del diccionario en esa clave
        InPair key = InPair.of(n, m);
        if (mapa.containsKey(key)) {
            return mapa.get(key);
        }

        Integer res;

        // B) Aplicamos la lógica del enunciado, 
        if (n < 4 && m < 2) {
            res = n + m * m;       // Caso base 1: n + m^2
        } 
        else if (n < 4 || m < 2) {
            res = n * n + m;       // Caso base 2: n^2 + m
        } 
        else if (n % 2 == 0) { 
            // Caso recursivo 1 (n par, n>=4, m>=2)
            res = 3 * recursivaConMemoria(n - 1, m - 1, mapa) + 2;
        } 
        else { 
            // Caso recursivo 2 (EOC)
            
            res = recursivaConMemoria(n - 1, m - 2, mapa) + 
                  recursivaConMemoria(n - 2, m - 2, mapa);
        }

        // C) Guardamos el resultado en memoria antes de devolverlo
        mapa.put(key, res);
        return res;
    }

    // -------------------------------------------------------------------------
    // 4. Main para pruebas
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        EjercicioT ejercicio = new EjercicioT();
        
        System.out.println("--- Probando Ejercicio T ---");

        // Caso simple para verificar cálculo manual
        // T(3, 1) -> n<4 y m<2 -> 3 + 1^2 = 4
        System.out.println("T(3, 1) = " + ejercicio.calcularT(3, 1)); 
        
        // Caso complejo que necesitaría mucha recursión sin memoria
        System.out.println("T(10, 10) = " + ejercicio.calcularT(10, 10)); 
        
        // Prueba de fuego (sin memoria esto tardaría años)
        System.out.println("T(50, 50) = " + ejercicio.calcularT(50, 50)); 
    }
}