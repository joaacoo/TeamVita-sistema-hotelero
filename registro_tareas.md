# Registro de Tareas y Participación Individual (Fases 1, 2 y 3)

El presente documento detalla la participación real de cada integrante del equipo, basada en el historial de commits del repositorio GitHub, demostrando los aportes de cada uno a lo largo del desarrollo del TPO.

## Joaquín De Luca (@joaacoo)
**Rol Principal:** Diseño arquitectónico del Modelo, UI/UX y Estructura Base.
* **Fase 1:** 
  * Estructuración inicial del proyecto y primer commit.
  * Elaboración de los primeros Casos de Uso (CU), Diagrama de Clases (DC) y diagramas UML.
* **Fase 2:**
  * Implementación core de la lógica del negocio (Capa `model`).
  * Reorganización estructural de la carpeta modelo y entidades.
  * Desarrollo inicial de las interfaces gráficas y paneles de vista (login admin, etc.).
  * Refactor de UI y conexión a base de datos.
* **Fase 3:**
  * Configuración del entorno (Librería mysql-connector-j, gitignore).
  * Mejoras finales en UI y corrección de errores visuales.
  * Modificaciones en persistencia de HuespedDAO (identificación con DNI) y ReservaDAO.
  * Ajustes finales en lógica de Factura e implementación de Servicios Adicionales (Decorator dinámico).

## Lautaro Vita (@Lautivita)
**Rol Principal:** Controlador, Fachada y Patrones de Negocio.
* **Fase 1:**
  * Elaboración del Diagrama de Secuencia 2 enfocado en el Check-in.
  * Estructuración inicial de los paquetes `vista` y `negocio`.
* **Fase 2:**
  * Implementación del patrón Facade (`SistemaHotelFacade`) y Factory (`HabitacionFactory`).
  * Implementación de estrategias de Tarifas (`TarifaStrategy`).
  * Correcciones en el diagrama de secuencia de Check-in.
* **Fase 3:**
  * Sincronización entre capa de presentación y repositorio.
  * Cambios avanzados en `repo` y `vista` para mejorar la gestión dinámica de los estados polimórficos de la reserva.

## Claudino Diaz (@ClauDrive / @ClaudinoDiaz)
**Rol Principal:** Persistencia de Datos (DAO), Conexión JDBC y Diseño SQL.
* **Fase 1:**
  * Diseño del Diagrama de Secuencia 1 para la gestión de reservas.
* **Fase 2:**
  * Implementación de la capa de Acceso a Datos (DAOs) y patrón Singleton para `ConexionBD`.
  * Integración de la conexión JDBC a MySQL y desarrollo de las sentencias SQL en todos los DAOs.
  * Actualización técnica de los diagramas de secuencia.
* **Fase 3:**
  * Actualización del script SQL (`hotel_db.sql`) integrando nuevas tablas de facturas, pagos y configuraciones.
  * Programación del Panel Admin dinámico y la gestión del cobro de señas desde la BD.

---

> **Nota para la defensa:** Como se observa en este registro (y en el historial de la rama `main` en GitHub), el desarrollo fue altamente colaborativo. Joaquín sentó las bases del modelo de dominio y la UI, Lautaro interconectó ambas puntas implementando la Fachada y los patrones de comportamiento, y Claudino desarrolló toda la infraestructura de persistencia relacional que hace que el sistema sea completamente funcional.