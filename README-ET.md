# RentaCar — Sistema de Microservicios

Proyecto Final Transversal (EFT) — Desarrollo FullStack I (DSY1103)
Integrantes: Mariana / Gissel García

Sistema de gestión de arriendo de vehículos basado en microservicios, compuesto por:

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| `eureka-server` | 8761 | Registro y descubrimiento de servicios |
| `api-gateway` | 8080 | Punto de entrada único, enruta hacia los microservicios |
| `ms-sucursales` | 8085 | Gestión de sucursales y regiones |
| `ms-reportes` | 8087 | Generación de reportes y consolidación de datos (vía Feign a `ms-pagos` y `ms-reservas`) |

> **Nota:** `ms-pagos`, `ms-reservas`, `ms-clientes` y `ms-vehiculos` son consumidos por `ms-reportes` a través de Feign Client, pero no forman parte de este entregable — se asume que ya están desplegados y registrados en Eureka como `ms-pagos` y `ms-reservas`.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.3.5 (microservicios de negocio) / Spring Boot 3.5.15 (api-gateway y eureka-server, requerido por `spring-cloud-starter-gateway-server-webmvc`)
- Spring Cloud 2023.0.3 / 2025.0.x (gateway y eureka)
- Spring Data JPA + Hibernate
- Spring Cloud Netflix Eureka (Service Discovery)
- Spring Cloud Gateway (MVC) — enrutamiento
- Spring Cloud OpenFeign — comunicación entre microservicios
- Spring HATEOAS
- MySQL 8
- Liquibase (`ms-reportes`) / CommandLineRunner (`ms-sucursales`)
- Bean Validation (Jakarta Validation)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, MockMvc, H2 (pruebas)
- Lombok

---

## Requisitos previos

- JDK 17+
- Maven 3.9+ (o usar el wrapper `./mvnw` incluido en cada proyecto)
- MySQL 8 corriendo en `localhost:3306` con usuario `root` sin contraseña (o ajustar credenciales en cada `application.yml`)
- Postman (opcional, para importar la colección incluida)

---

## Perfiles de configuración

Cada microservicio de negocio (`ms-sucursales`, `ms-reportes`) define **3 niveles de configuración**:

1. Bloque común (`spring.application.name`, puerto, Eureka, Swagger).
2. Perfil `dev` (activo por defecto): apunta a una base de datos de desarrollo con datos de ejemplo precargados.
3. Perfil `test`: apunta a una base de datos aislada, sin datos de ejemplo (usada para pruebas de integración manuales, no para los tests automatizados de JUnit — esos usan H2 en memoria vía `src/test/resources/application.yml`).

| Microservicio | BD perfil `dev` | BD perfil `test` |
|---|---|---|
| ms-sucursales | `prueba1_dev` | `prueba1_test` |
| ms-reportes | `prueba4_dev` | `prueba4_test` |

Para cambiar de perfil sin tocar código:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
# o
java -jar target/ms-sucursales-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

Las bases de datos se crean automáticamente (`createDatabaseIfNotExist=true`), no es necesario crearlas a mano.

---

## Variables de entorno / configuración editable

No se requieren variables de entorno obligatorias: todo funciona con los valores por defecto de `application.yml`. Si tu instalación de MySQL usa otro usuario/contraseña, edita:

```yaml
spring:
  datasource:
    username: root
    password:
```

en `src/main/resources/application.yml` de cada microservicio, dentro del bloque del perfil correspondiente (`dev` o `test`).

---

## Orden de ejecución

El orden **es importante** porque cada servicio depende del anterior para registrarse/enrutar correctamente:

1. **`eureka-server`** (puerto 8761) — debe estar arriba primero.
   ```bash
   cd eureka-server
   ./mvnw spring-boot:run
   ```
   Verificar en: http://localhost:8761

2. **`ms-sucursales`** (puerto 8085) y **`ms-reportes`** (puerto 8087) — en cualquier orden entre sí, pero después de Eureka.
   ```bash
   cd ms-sucursales
   ./mvnw spring-boot:run
   ```
   ```bash
   cd ms-reportes
   ./mvnw spring-boot:run
   ```
   Esperar a ver en el log: `... registered with Eureka` / verificar que ambos aparecen como `UP` en http://localhost:8761

3. **`api-gateway`** (puerto 8080) — al final, para que ya encuentre a los microservicios registrados.
   ```bash
   cd api-gateway
   ./mvnw spring-boot:run
   ```

### Verificación rápida de que todo funciona

```bash
# Directo al microservicio
curl http://localhost:8085/api/v1/sucursales

# A través del Gateway (recomendado)
curl http://localhost:8080/api/v1/sucursales
curl http://localhost:8080/api/v1/reportes
```

---

## Documentación Swagger / OpenAPI

| Servicio | URL Swagger UI |
|---|---|
| ms-sucursales | http://localhost:8085/swagger-ui.html |
| ms-reportes | http://localhost:8087/swagger-ui.html |

---

## Colección Postman

Incluida en `postman/RentaCar.postman_collection.json` en la raíz del proyecto. Importarla en Postman y usar la variable de entorno `{{gateway}}` (por defecto `http://localhost:8080`) para probar todos los endpoints a través del API Gateway.

---

## API Gateway — rutas configuradas

| Ruta | Redirige a |
|---|---|
| `/api/v1/sucursales/**` | `ms-sucursales` (vía Eureka, `lb://ms-sucursales`) |
| `/api/v1/regiones/**` | `ms-sucursales` |
| `/api/v1/reportes/**` | `ms-reportes` (vía Eureka, `lb://ms-reportes`) |

Adicionalmente, el `discovery locator` está habilitado: cualquier servicio registrado en Eureka queda accesible también en `/{nombre-servicio}/**`.

---

## Ejecutar las pruebas automatizadas

Cada microservicio incluye pruebas de **Controller** (MockMvc), **Service** (Mockito) y **Repository** (`@DataJpaTest` con H2 en memoria, no requiere MySQL):

```bash
cd ms-sucursales
./mvnw test

cd ../ms-reportes
./mvnw test
```

---

## Arquitectura y estándares aplicados

- Patrón **Controller → Service → Repository**, con la lógica de negocio exclusivamente en la capa Service.
- **Constructor Injection** en todos los componentes (sin `@Autowired` en atributos).
- **DTO Request/Response separados** por cada entidad, con conversión centralizada en `mapper/`.
- **HATEOAS** vía `RepresentationModelAssembler`: los Controllers nunca construyen enlaces manualmente, siempre delegan en el `assembler/` correspondiente.
- **GlobalExceptionHandler + ErrorResponse** separados, con respuestas de error uniformes (`timestamp`, `status`, `error`, `message`, `path`).
- **Feign Client** identificado solo por `name` (nunca por IP/URL fija), resuelto dinámicamente por Eureka + Load Balancer.
- **Logging con SLF4J** en Controllers y Services (solicitudes, operaciones relevantes, advertencias y errores).
- Carga de datos inicial con mínimo 5 registros por entidad, activa solo en el perfil `dev`.

---

## Estructura del repositorio

```
├── eureka-server/
├── api-gateway/
├── ms-sucursales/
│   └── src/main/java/com/prueba/ms_sucursales/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│       ├── dto/{request,response}/
│       ├── mapper/
│       ├── assembler/
│       ├── exception/
│       ├── config/
│       └── runner/
├── ms-reportes/
│   └── src/main/java/com/prueba/ms_reportes/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│       ├── dto/{request,response}/
│       ├── client/          (Feign: ms-pagos, ms-reservas)
│       ├── mapper/
│       ├── assembler/
│       └── exception/
└── postman/
    └── RentaCar.postman_collection.json
```
