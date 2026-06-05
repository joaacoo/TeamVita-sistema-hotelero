# Registro de Tareas

## Fase 1: Análisis del problema y diseño inicial
* Joaquín De Luca: Creación del repositorio, UML.
* Lautaro Vita: Diagrama de secuencia 2.
* Claudino Diaz: Diagrama de secuencia 1.

## Fase 2: Diseño orientado a objetos y patrones
* **Joaquín De Luca:** Configuración inicial del entorno Java. Programación de la capa de dominio (`model`), definición de entidades puras, relaciones y justificación teórica de los principios SOLID implementados (SRP con la división de `DetalleReserva`, OCP, LSP).

* **Lautaro Vita:** Responsable de la capa de negocio. Implementación del controlador principal aplicando el patrón Facade (`SistemaHotelFacade`) y estructuración de los patrones creacionales y de comportamiento (Factory Method y Strategy).

* **Claudino Diaz:** A cargo del diseño y programación de la capa de acceso a datos (`repo`), definiendo las interfaces y simulando la persistencia de los DAOs. Responsable de implementar el patrón Singleton para la conexión a la base de datos y documentar los patrones GRASP.