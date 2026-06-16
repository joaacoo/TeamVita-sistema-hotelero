# Sistema de Gestión Hotelera

## Integrantes del grupo
* Joaquín De Luca
* Lautaro Vita
* Claudino Diaz

## Descripción breve del sistema
Plataforma integral construida en Java bajo una arquitectura de 4 capas (Vista, Negocio, Modelo, Repositorio) para administrar de manera centralizada reservas, habitaciones, huéspedes y servicios adicionales de una cadena hotelera. El sistema automatiza procesos de check-in/check-out, calcula costos dinámicamente y facilita la gestión de la disponibilidad en tiempo real con conexión a una base de datos MySQL.

## Instrucciones para ejecutar el proyecto
1. Clonar el repositorio.
2. Abrir el proyecto en un IDE (IntelliJ IDEA, Eclipse, NetBeans) configurado con Java (JDK 8 o superior).
3. Importar el script `hotel_db.sql` en un servidor local MySQL (ej. XAMPP o MySQL Workbench) para crear la base de datos y poblar los datos iniciales.
4. (Opcional) Verificar que las credenciales en `com.teamvita.hotel.repo.ConexionBD` coincidan con su motor local (por defecto `root` sin contraseña).
5. Ejecutar la clase principal `com.teamvita.hotel.Main` (o directamente `VentanaPrincipal`).
6. Iniciar sesión con:
   - **Recepción**: User: `recepcion` / Pass: `recepcion123`
   - **Administrador**: User: `admin` / Pass: `admin123`

## Patrones aplicados (Fase 3)
### Patrones Estructurales
* **Facade:** `SistemaHotelFacade` proporciona un punto de entrada unificado y simplificado desde el paquete vista hacia la lógica de negocio y model.
* **Decorator:** `ServicioDecorator` permite añadir responsabilidades o servicios adicionales (`SpaDecorator`, `RestauranteDecorator`, `LavanderiaDecorator`) a la estadía de forma dinámica.

### Patrones de Comportamiento
* **Strategy:** `TarifaStrategy` encapsula los algoritmos de cálculo de precios. `CategoriaFidelizacion` encapsula el cálculo de descuentos según el nivel de membresía.
* **State:** `EstadoReserva` administra las transiciones lógicas de la reserva.

## Principios SOLID aplicados
* **Single Responsibility Principle (SRP):** Las clases de `model` representan el dominio, mientras que en `repo` se encargan de la base de datos. Ejemplo clave: división de `Reserva` y `DetalleReserva`.
* **Open/Closed Principle (OCP):** El patrón Decorator permite incorporar nuevos servicios adicionales sin modificar el código en las clases de facturación o estadía.
* **Liskov Substitution Principle (LSP):** La jerarquía de habitaciones permite suministrar una instancia de `Suite`, `HabitacionSimple` o `HabitacionDoble` donde se requiera `Habitacion`.
* **Dependency Inversion Principle (DIP):** El modelo de dominio depende de abstracciones (interfaces como `TarifaStrategy` o `EstadoReserva`) y no de implementaciones concretas.
* **Interface Segregation Principle (ISP):** Se diseñaron interfaces pequeñas y de propósito único, como `TarifaStrategy` que expone únicamente `calcularTarifa()`.

## Patrones GRASP aplicados
* **Controlador (Controller):** `SistemaHotelFacade` actúa como controlador principal.
* **Experto en Información (Information Expert):** `Estadia` posee la responsabilidad de calcular el costo total.
* **Alta Cohesión y Bajo Acoplamiento:** Garantizado mediante la estricta separación en capas (vista, negocio, model, repo).
* **Polimorfismo:** Uso de interfaces (`TarifaStrategy`, `EstadoReserva`, `CategoriaFidelizacion`) para delegar el comportamiento que varía.
* **Fabricación Pura (Pure Fabrication):** Clases como `SistemaHotelFacade`, `ConexionBD` y DAOs introducidas artificialmente para organizar código y aislar responsabilidades.
* **Creador (Creator):** `SistemaHotelFacade` actúa como creador de `Reserva` y `Estadia`.
* **Indirección (Indirection):** `SistemaHotelFacade` actúa como intermediario entre Presentación (Vista) y Dominio/Persistencia.
* **Variaciones Protegidas:** Lógicas como cálculo de tarifas protegidas detrás de la interfaz `TarifaStrategy`.

## Distribución de tareas
* **Joaquín De Luca**: Lógica de entidades de dominio (`model`), implementación de cálculos polimórficos, refactorización de colecciones para acompañantes.
* **Lautaro Vita**: Desarrollo de controladores (`negocio`), implementación de patrones creacionales, orquestación de la interfaz gráfica Swing y el decorador dinámico.
* **Claudino Diaz**: Arquitectura de persistencia (`repo`), diseño de base de datos MySQL, optimización de consultas SQL y diseño de escenarios de prueba.