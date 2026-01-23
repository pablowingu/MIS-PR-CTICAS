import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# --- 1. CARGA DE DATOS ---
# Cargamos los datos de empleados y evaluaciones
try:
    df_emp = pd.read_csv('empleados.csv')
    df_perf = pd.read_csv('rendimiento.csv')
    print("✅ Datos cargados correctamente.")
except FileNotFoundError:
    print("❌ Error: No se encuentran los archivos CSV.")

# --- 2. MERGE & LIMPIEZA (ETL) ---
# Unimos tablas manteniendo a todos los empleados (Left Join)
df_completo = pd.merge(df_emp, df_perf, on='ID_Empleado', how='left')

# Rellenamos nulos: Si no hay evaluación, asumimos un 0 (Política de empresa)
df_completo['Puntuacion_General'] = df_completo['Puntuacion_General'].fillna(0)

# --- 3. LÓGICA DE NEGOCIO (Cálculo de Bonus) ---
# El bonus es un % del salario basado en la nota (Escala 0-10)
# Fórmula: Salario * (Nota / 100) -> Ejemplo: Nota 10 = 10% del sueldo
df_completo['Bonus_Monetario'] = df_completo['Salario_Base'] * (df_completo['Puntuacion_General'] / 100)

# --- 4. REPORTING (Agrupación) ---
# Generamos el KPI por departamento
tabla_final = df_completo.groupby('Departamento').agg({
    'Salario_Base': 'sum',          # Coste fijo
    'Bonus_Monetario': 'sum',       # Coste variable (Bonus)
    'Puntuacion_General': 'mean'    # Rendimiento medio
}).sort_values('Bonus_Monetario', ascending=False)

# Formateo para que se lea mejor en consola
pd.options.display.float_format = '{:,.2f} €'.format
print("\n--- REPORTE EJECUTIVO: COSTE DE BONUS POR DEPARTAMENTO ---")
print(tabla_final)

# --- 5. VISUALIZACIÓN (El toque final) ---
plt.figure(figsize=(10, 6))
# Creamos un gráfico de barras
sns.barplot(x=tabla_final.index, y=tabla_final['Bonus_Monetario'], palette='viridis')
plt.title('Coste Total de Bonus por Departamento (Presupuesto 2024)')
plt.ylabel('Euros (€)')
plt.xlabel('Departamento')
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.show()