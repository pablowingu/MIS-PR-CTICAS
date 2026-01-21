# 🌳 Verificación de Balanceo Fonético

## 📄 Enunciado
Dado un árbol binario genérico donde cada nodo contiene una cadena de texto, diseñar un algoritmo recursivo que determine si el árbol es **"Fonéticamente Balanceado"**.

Un árbol cumple esta propiedad si, para **todos** sus nodos, se cumple que:
1.  El número total de vocales en su subárbol izquierdo es **igual** al número de vocales en su subárbol derecho.
2.  Sus hijos (si existen) también son fonéticamente balanceados.

## 🛠️ Aspectos Técnicos
* **Tecnología:** Java 21 (Preview Features).
* **Conceptos:** Recursividad estructural, `Java Records` para tuplas de retorno, `Pattern Matching` para switch.
* **Complejidad:** $O(n)$ - Se recorre cada nodo una única vez (Post-order traversal).

## 🚀 Ejecución
El algoritmo principal se encuentra en la clase `VerificadorBalanceo.java`.
Requiere la definición de `BinaryTree` incluida en el paquete.