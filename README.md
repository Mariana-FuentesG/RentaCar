# 🚗 Proyecto Microservicios – RentaCar

## 📌 Descripción del Proyecto

El presente proyecto corresponde al desarrollo de una arquitectura basada en microservicios para la gestión de una empresa de arriendo de vehículos denominada **RentaCar**.

La solución fue desarrollada utilizando **Spring Boot**, aplicando principios de arquitectura de microservicios, persistencia de datos, comunicación entre servicios mediante **OpenFeign**, validaciones, manejo de DTOs y controladores REST.

Cada microservicio posee responsabilidades específicas y una base de datos independiente, permitiendo desacoplamiento, escalabilidad y mantenibilidad del sistema.

---

# 🛠️ Tecnologías Utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Web
* Spring Validation
* OpenFeign
* MySQL
* Liquibase
* Flyway
* Maven
* Lombok
* IntelliJ IDEA
* Postman
---
# 🧩 Arquitectura del Proyecto

El sistema está compuesto por los siguientes microservicios:

| Microservicios | 
|----------------|
| ms-clientes    | 
| ms-vehiculos   |
| ms-reservas    |
| ms-pagos       |
| ms-sucursales  | 
| ms-empleados   | 
| ms-reportes    | 

---

# 📦 Microservicios Implementados

---

# 👤 ms-clientes

## Descripción

Microservicio encargado de administrar la información de clientes registrados en el sistema.

## Funcionalidades

* Registro de clientes
* Consulta de clientes
* Actualización de datos
* Eliminación lógica/física
* Validaciones con Jakarta Validation
* Persistencia en MySQL

## Endpoints Principales

| Método | Endpoint              |
| ------ | --------------------- |
| GET    | /api/v1/clientes      |
| GET    | /api/v1/clientes/{id} |
| POST   | /api/v1/clientes      |
| PUT    | /api/v1/clientes/{id} |
| DELETE | /api/v1/clientes/{id} |

---
# 🚘 ms-vehiculos

## Descripción

Microservicio encargado de administrar la información de los vehículos disponibles para arriendo.

## Funcionalidades

* Crear vehículos
* Obtener listado de vehículos
* Buscar vehículo por ID
* Actualizar información de vehículos
* Eliminar vehículos
* Validaciones de datos
* Persistencia con JPA
* Migraciones con Flyway

## Endpoints Principales

| Método | Endpoint               |
| ------ | ---------------------- |
| GET    | /api/v1/vehiculos      |
| GET    | /api/v1/vehiculos/{id} |
| POST   | /api/v1/vehiculos      |
| PUT    | /api/v1/vehiculos/{id} |
| DELETE | /api/v1/vehiculos/{id} |

---

# 📅 ms-reservas

## Descripción

Microservicio encargado de gestionar las reservas realizadas por los clientes.

## Funcionalidades

* Crear reservas
* Consultar reservas
* Modificar reservas
* Eliminar reservas
* Relación con clientes y vehículos
* Uso de Flyway para migraciones
* Persistencia con JPA

## Endpoints Principales

| Método | Endpoint              |
| ------ | --------------------- |
| GET    | /api/v1/reservas      |
| GET    | /api/v1/reservas/{id} |
| POST   | /api/v1/reservas      |
| PUT    | /api/v1/reservas/{id} |
| DELETE | /api/v1/reservas/{id} |

---
# 💳 ms-pagos

## Descripción

Microservicio encargado de gestionar los pagos asociados a las reservas.

## Funcionalidades

* Registrar pagos
* Consultar pagos
* Actualizar pagos
* Eliminar pagos
* Validaciones de montos y estados
* Persistencia con JPA

## Endpoints Principales

| Método | Endpoint           |
| ------ | ------------------ |
| GET    | /api/v1/pagos      |
| GET    | /api/v1/pagos/{id} |
| POST   | /api/v1/pagos      |
| PUT    | /api/v1/pagos/{id} |
| DELETE | /api/v1/pagos/{id} |

---

# 🏢 ms-sucursales

## Descripción

Microservicio encargado de administrar las sucursales de la empresa RentaCar.

Permite gestionar información relacionada con las distintas sedes disponibles para atención de clientes y entrega de vehículos.

## Funcionalidades

* Crear sucursales
* Obtener listado de sucursales
* Buscar sucursal por ID
* Actualizar información de sucursales
* Eliminar sucursales
* Validaciones de datos
* Persistencia con JPA
* Migraciones de base de datos

## Endpoints Principales

| MétodoEndpoint |                         |
| -------------- | ----------------------- |
| GET            | /api/v1/sucursales      |
| GET            | /api/v1/sucursales/{id} |
| POST           | /api/v1/sucursales      |
| PUT            | /api/v1/sucursales/{id} |
| DELETE         | /api/v1/sucursales/{id} |

---

# 👨‍💼 ms-empleados

## Descripción

Microservicio encargado de gestionar la información de los empleados de RentaCar.

Permite registrar y administrar trabajadores asociados a las distintas sucursales de la empresa.

## Funcionalidades

* Registro de empleados
* Consulta de empleados
* Actualización de datos
* Eliminación de empleados
* Validaciones con Jakarta Validation
* Persistencia con Spring Data JPA
* Relación con sucursales

## Endpoints Principales

| Método | Endpoint               |
| ------ | ---------------------- |
| GET    | /api/v1/empleados      |
| GET    | /api/v1/empleados/{id} |
| POST   | /api/v1/empleados      |
| PUT    | /api/v1/empleados/{id} |
| DELETE | /api/v1/empleados/{id} |

---

# 📊 ms-reportes

## Descripción

Microservicio encargado de generar reportes consolidando información desde otros microservicios mediante comunicación Feign Client.

## Funcionalidades

* Generación de reportes
* Consumo de ms-reservas mediante OpenFeign
* Consumo de ms-pagos mediante OpenFeign
* CRUD de reportes
* Persistencia mediante Liquibase
* Comunicación entre microservicios

## Endpoints Principales

| Método | Endpoint                  |
| ------ | ------------------------- |
| GET    | /api/v1/reportes          |
| GET    | /api/v1/reportes/{id}     |
| POST   | /api/v1/reportes          |
| PUT    | /api/v1/reportes/{id}     |
| DELETE | /api/v1/reportes/{id}     |
| GET    | /api/v1/reportes/reservas |
| GET    | /api/v1/reportes/pagos    |

---

# 🧩 Arquitectura del Proyecto

El sistema está compuesto por los siguientes microservicios:

| Microservicio | Puerto | Base de Datos | Entidades (tablas)      |
|---------------|--------|---------------|-------------------------|
| ms-clientes   | 8081   | prueba1       | Cliente - Dirección     |
| ms-vehiculos  | 8082   | prueba1       | Vehiculo - Categoria    |
| ms-reservas   | 8083   | prueba2       | Reserva - EstadoReserva |
| ms-pagos      | 8084   | prueba3       | Pago                    |
| ms-sucursales | 8085   | prueba4       | Sucursal - Region       |
| ms-empleados  | 8086   | prueba1       | Empleado                |
| ms-reportes   | 8087   | prueba1       | Reporte                 |

---

# 🗄️ Bases de Datos Utilizadas

El proyecto utiliza un total de 4 bases de datos en MySQL.

```
CREATE DATABASE prueba1;
CREATE DATABASE prueba2;
CREATE DATABASE prueba3;
CREATE DATABASE prueba4;
```
---
## Distribución de Bases de Datos

| Base de Datos | Microservicios Asociados                               |
| ------------- | ------------------------------------------------------ |
| prueba1       | ms-clientes, ms-vehiculos, ms-sucursales, ms-empleados |
| prueba2       | ms-reservas                                            |
| prueba3       | ms-pagos                                               |
| prueba4       | ms-reportes                                            |


---

# ⚙️ Configuración del Proyecto

## Configuración de application.properties

Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prueba1
spring.datasource.username=root
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# ▶️ Pasos para Ejecutar el Proyecto

## 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/usuario/repositorio.git
```

---

## 2️⃣ Abrir el proyecto en IntelliJ IDEA

* File
* Open
* Seleccionar carpeta raíz del proyecto

---

## 3️⃣ Crear las bases de datos en MySQL

Ejecutar:

```sql
CREATE DATABASE prueba1;
CREATE DATABASE prueba2;
CREATE DATABASE prueba3;
CREATE DATABASE prueba4;
CREATE DATABASE prueba5;
```

---

## 4️⃣ Verificar credenciales de MySQL

Modificar en cada microservicio:

```properties
spring.datasource.username=root
spring.datasource.password=
```

---

## 5️⃣ Ejecutar los microservicios

Orden recomendado:

1.  ms-clientes
2. ms-vehiculos
3. ms-sucursales
4. ms-empleados
5. ms-reservas
6. ms-pagos
7. ms-reportes
---

# 🧪 Pruebas con Postman

## Ejemplo GET

```http
GET http://localhost:8083/api/v1/reservas
```

## Ejemplo POST

```http
POST http://localhost:8085/api/v1/pagos
```

Body:

```json
{
  "monto": 50000,
  "metodoPago": "Transferencia",
  "estado": "Pagado"
}
```

---

# 🔗 Comunicación entre Microservicios

El microservicio `ms-reportes` utiliza OpenFeign para consumir:

* ms-reservas
* ms-pagos

Permitiendo obtener información consolidada desde distintos servicios.

---

# ✅ Características Implementadas

* Arquitectura basada en microservicios
* CRUD completo
* DTOs
* Validaciones
* Relaciones entre servicios
* Persistencia con JPA
* Migraciones con Flyway
* Migraciones con Liquibase
* Comunicación Feign Client
* APIs REST
* Manejo de errores HTTP
* Uso de MySQL
* Organización en capas

---
# 📚 Estructura General

```text
controller
service
repository
entity
dto
client
config
```

---

# 📌 Conclusión

El proyecto implementa una solución distribuida basada en microservicios utilizando tecnologías modernas del ecosistema Spring, aplicando buenas prácticas de desarrollo backend, persistencia de datos y comunicación entre servicios.

La arquitectura permite independencia entre módulos, escalabilidad y mantenibilidad del sistema.
