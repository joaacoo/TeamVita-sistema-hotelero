# Sistema de Gestión Hotelera

## Integrantes del grupo
* Joaquín De Luca
* Lautaro Vita
* Claudino Diaz

## Descripción breve del sistema
Plataforma integral para administrar de manera centralizada reservas, habitaciones, huéspedes y servicios adicionales de una cadena hotelera. El sistema automatiza procesos de check-in/check-out, calcula costos dinámicos y facilita la gestión de la disponibilidad en tiempo real.

## Instrucciones para ejecutar el proyecto
*Pendiente para la Fase 3.*

## Patrones aplicados (Fase 2)
* **Factory Method:** `HabitacionFactory` para instanciar de forma centralizada las variantes de habitaciones.
* **Singleton:** `ConexionBD` para garantizar una única conexión a la base de datos MySQL.
* **Facade:** `SistemaHotelFacade` como controlador unificado desde la interfaz gráfica hacia el dominio.
* **Decorator:** `ServicioDecorator` para agregar servicios adicionales dinámicamente a la estadía (Spa, Restaurante, Lavandería).
* **Strategy:** Implementado en `TarifaStrategy` (cálculo de precios comerciales) y `CategoriaFidelizacion` (cálculo de beneficios para el huésped).
* **State:** `EstadoReserva` para administrar las transiciones lógicas de las reservas.

## Principios SOLID aplicados
* **Single Responsibility Principle (SRP):** Alta separación de responsabilidades, evidenciada en la refactorización de la clase `Reserva`, extrayendo la lógica específica de ocupación hacia la clase `DetalleReserva` para evitar una "Clase Dios".
* **Open/Closed Principle (OCP):** El uso del patrón Decorator permite añadir nuevos servicios adicionales facturables sin tener que modificar la clase `Estadia`.
* **Liskov Substitution Principle (LSP):** La jerarquía de habitaciones (`Suite`, `HabitacionSimple`, `HabitacionDoble`) permite sustituir la clase base abstracta en los procesos de negocio asegurando el comportamiento correcto mediante polimorfismo.

## Patrones GRASP aplicados
* **Controlador (Controller):** La clase `SistemaHotelFacade` recibe y coordina las operaciones del sistema.
* **Experto en Información (Information Expert):** La clase `Estadia` posee la responsabilidad de calcular el costo total porque contiene los días alojados y los servicios consumidos.
* **Alta Cohesión y Bajo Acoplamiento:** Asegurado mediante la estricta división en 4 paquetes funcionales (`vista`, `negocio`, `model`, `repo`).

## Distribución de tareas
Ver el archivo `registro_tareas.md` para el detalle de la participación y responsabilidades de cada integrante en las distintas fases del proyecto.