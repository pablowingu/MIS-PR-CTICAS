# 🏔️ Optimización en Red de Senderos (Grafos Avanzados)

## 📄 Contexto del Problema
Se ha modelado una red de senderos de montaña como un grafo conexo, dirigido y ponderado. El objetivo es aplicar algoritmos de optimización para la gestión de refugios y rutas.

### Modelo de Datos
* **Vértices (Refugios):** `Refugio(String nombre, int numCamas, int anyosDesdeRenovacion)`
* **Aristas (Senderos):** `Sendero(int id, List<String> puntosInteres, int dificultadTecnica, double distancia)`

## 🧩 Retos Resueltos

### A. Filtrado de Calidad (Subgrafos)
Generar una vista del grafo que solo incluya:
* **Refugios:** Con más de `X` camas y renovados hace menos de `Y` años.
* **Senderos:** Con al menos un número determinado de puntos de interés.

### B. Ruta Circular Óptima (TSP - Traveling Salesman Problem)
Diseñar una ruta que visite **todos** los refugios exactamente una vez y vuelva al inicio, minimizando el esfuerzo total.
* **Función de Coste (Peso):** `distancia * dificultadTecnica`.
* **Restricción:** La ruta debe tener un mínimo de puntos de interés totales.

### C. Asignación de Guías (Graph Coloring)
Minimizar el número total de guías necesarios garantizando que dos refugios conectados directamente **no compartan el mismo guía**.
* **Objetivo:** Calcular el coste total de los guías (`precio * refugios_asignados`).

### D. Puestos de Ayuda (Vertex Cover)
Establecer puestos de socorro en refugios de forma que **todo sendero** tenga al menos un puesto en uno de sus extremos.
* **Objetivo:** Determinar el conjunto mínimo de refugios para cubrir todos los senderos.

---
*Este proyecto utiliza algoritmos avanzados de la librería JGraphT (Held-Karp, Greedy Coloring, Vertex Cover).*