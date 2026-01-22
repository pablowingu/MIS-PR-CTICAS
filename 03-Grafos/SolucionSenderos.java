package grafos;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexCoverAlgorithm;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.alg.vertexcover.GreedyVCImpl;
// Nota: Se asume el uso de una librería tipo JGraphT o similar proporcionada por la cátedra.

// --- 1. Definición del Modelo de Datos (Records) ---
record Refugio(String nombre, int numCamas, int anyosDesdeRenovacion) {}
record Sendero(int id, List<String> puntosInteres, int dificultadTecnica, double distancia) {}

/**
 * Solución a problemas de optimización en grafos (TSP, Coloring, Vertex Cover).
 */
public class SolucionSenderos {

    // --- Apartado A: Filtrado de Subgrafos ---
    /**
     * Devuelve una vista filtrada del grafo original según criterios de calidad.
     * Complejidad: O(V + E) para filtrar elementos.
     */
    public static Graph<Refugio, Sendero> subgrafoCalidad(Graph<Refugio, Sendero> g, 
            int anyos, int camas, int puntos) {
        
        // Usamos programación funcional para definir los predicados de inclusión
        return SubGraphView.of(g,
            refugio -> refugio.numCamas() > camas && refugio.anyosDesdeRenovacion() <= anyos,
            sendero -> sendero.puntosInteres().size() >= puntos
        );
    }

    // --- Apartado B: Ruta Circular Óptima (Traveling Salesman Problem) ---
    /**
     * Resuelve el problema del Viajante de Comercio (TSP) minimizando el esfuerzo.
     * Algoritmo: Held-Karp (Programación Dinámica).
     */
    public static GraphPath<Refugio, Sendero> rutaCircularCalidad(Graph<Refugio, Sendero> g, 
            int minPuntosInteres) {

        // 1. Asignar pesos a las aristas según la función de esfuerzo (Distancia * Dificultad)
        for (Sendero sendero : g.edgeSet()) {
            double esfuerzo = sendero.distancia() * sendero.dificultadTecnica();
            g.setEdgeWeight(sendero, esfuerzo);
        }

        // 2. Calcular el Tour óptimo usando Held-Karp (Exacto para N pequeño/medio)
        HeldKarpTSP<Refugio, Sendero> tsp = new HeldKarpTSP<>();
        GraphPath<Refugio, Sendero> rutaOptima = tsp.getTour(g);

        // 3. Verificaciones de seguridad
        if (rutaOptima == null) return null;

        // 4. Validar restricción de puntos de interés totales usando Streams
        int totalPuntos = rutaOptima.getEdgeList().stream()
                .mapToInt(s -> s.puntosInteres().size())
                .sum();

        return totalPuntos >= minPuntosInteres ? rutaOptima : null;
    }

    // --- Apartado C: Asignación de Recursos (Graph Coloring) ---
    /**
     * Minimiza el número de guías asegurando que refugios adyacentes tengan guías distintos.
     * Problema: Coloreado de Grafos.
     */
    public static Map<Integer, Double> calcularPagosGuias(Graph<Refugio, Sendero> g, 
            double eurosPorRefugio) {
        
        // El coloreado suele requerir un grafo no dirigido
        Graph<Refugio, Sendero> grafoNoDir = Graphs.undirectedGraph(g);

        // Algoritmo Voraz (Greedy) para coloreado
        GreedyColoring<Refugio, Sendero> coloreado = new GreedyColoring<>(grafoNoDir);
        Coloring<Refugio> asignacionGuias = coloreado.getColoring();

        // Calcular pagos: Cada "color" es un guía distinto
        Map<Integer, Double> pagos = new HashMap<>();
        int guiaId = 0; // Identificador del guía (Color)

        // getColorClasses() devuelve conjuntos de vértices pintados del mismo color
        for (Set<Refugio> refugiosDelGuia : asignacionGuias.getColorClasses()) {
            double pagoTotal = refugiosDelGuia.size() * eurosPorRefugio;
            pagos.put(guiaId++, pagoTotal);
        }

        return pagos;
    }

    // --- Apartado D: Cobertura de Seguridad (Vertex Cover) ---
    /**
     * Encuentra el conjunto mínimo de refugios para vigilar todos los senderos.
     * Problema: Vertex Cover (Cobertura de Vértices).
     */
    public static Map<Sendero, String> puestosAyuda(Graph<Refugio, Sendero> g) {
        
        Graph<Refugio, Sendero> grafoNoDir = Graphs.undirectedGraph(g);

        // Algoritmo de aproximación para Vertex Cover (NP-Hard)
        VertexCoverAlgorithm<Refugio> vertexCoverAlg = new GreedyVCImpl<>(grafoNoDir);
        Set<Refugio> refugiosConPuesto = vertexCoverAlg.getVertexCover();

        Map<Sendero, String> puestosPorSendero = new HashMap<>();

        // Para cada sendero, indicamos en qué extremo está la ayuda
        for (Sendero sendero : g.edgeSet()) {
            Refugio inicio = g.getEdgeSource(sendero);
            Refugio fin = g.getEdgeTarget(sendero);

            boolean inicioTiene = refugiosConPuesto.contains(inicio);
            boolean finTiene = refugiosConPuesto.contains(fin);

            String etiqueta;
            if (inicioTiene && finTiene) etiqueta = "Inicio y Fin";
            else if (inicioTiene) etiqueta = "Inicio";
            else etiqueta = "Fin"; // Por definición de Vertex Cover, al menos uno tendrá puesto

            puestosPorSendero.put(sendero, etiqueta);
        }

        return puestosPorSendero;
    }
}