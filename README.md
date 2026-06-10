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
* **Factory Method:** `HabitacionFactory` para instanciar de forma centralizada las variantes de habitaciones.
* **Singleton:** `ConexionBD` para garantizar una única conexión a la base de datos MySQL compartida por todos los DAOs.
* **Facade:** `SistemaHotelFacade` como controlador unificado desde la interfaz gráfica hacia la lógica de dominio.
* **Decorator (Dinámico):** `ServicioDinamicoDecorator` para agregar consumos extra consultando los precios directamente desde la BD en tiempo de ejecución.
* **Strategy:** Implementado en `TarifaStrategy` (cálculo de precios comerciales) y `CategoriaFidelizacion` (beneficios para el huésped).
* **State:** `EstadoReserva` para administrar polimórficamente las transiciones lógicas (Pendiente, Confirmada, Cancelada).

## Principios SOLID aplicados
* **Single Responsibility Principle (SRP):** Alta separación de responsabilidades: los objetos del dominio no acceden a la BD, los DAOs no manejan reglas de negocio y las vistas Swing solo dibujan la interfaz.
* **Open/Closed Principle (OCP):** El uso del `ServicioDinamicoDecorator` permite añadir infinitos servicios adicionales desde la base de datos sin necesidad de crear clases Java nuevas, cerrando el código a modificaciones.
* **Liskov Substitution Principle (LSP):** La jerarquía de habitaciones permite sustituir la clase base en los procesos iterativos asegurando el comportamiento correcto mediante polimorfismo puro.
* **Dependency Inversion Principle (DIP):** El controlador principal no depende de las estrategias concretas de descuento o estado, sino de sus abstracciones correspondientes.

## Patrones GRASP aplicados
* **Controlador (Controller):** La clase `SistemaHotelFacade` recibe y coordina los eventos de la UI hacia el sistema.
* **Experto en Información (Information Expert):** La clase `Estadia` posee la responsabilidad de calcular el costo total porque concentra los días alojados y consumos.
* **Variaciones Protegidas:** Se aisló al sistema central de cambios en lógicas de cálculo mediante interfaces (Strategy y State).
* **Alta Cohesión y Bajo Acoplamiento:** Asegurado mediante la estricta división en paquetes funcionales (`vista`, `negocio`, `model`, `repo`).

## Distribución de tareas
* **Joaquín De Luca**: Lógica de entidades de dominio (`model`), implementación de cálculos polimórficos, refactorización de colecciones para acompañantes.
* **Lautaro Vita**: Desarrollo de controladores (`negocio`), implementación de patrones creacionales, orquestación de la interfaz gráfica Swing y el decorador dinámico.
* **Claudino Diaz**: Arquitectura de persistencia (`repo`), diseño de base de datos MySQL, optimización de consultas SQL y diseño de escenarios de prueba.