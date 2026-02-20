## Laboratorio #4 – REST API Blueprints (Java 21 / Spring Boot 3.3.x)
# Escuela Colombiana de Ingeniería – Arquitecturas de Software  

## Autora: Raquel Selma
---


## 📖 Actividades del laboratorio

### 1. Familiarización con el código base


El proyecto está organizado en paquetes que siguen el patrón de capas lógicas:

-	**model:** contiene las entidades principales, Blueprint y Point.
-	**persistence:** define la interfaz BlueprintPersistence y su implementación inicial en memoria InMemoryBlueprintPersistence.
-	**services:** la clase BlueprintsServices coordina la lógica de negocio, aplica filtros y delega en la capa de persistencia.
-	**controllers:** el BlueprintsAPIController expone la API REST con operaciones CRUD

-	**filters:** provee distintos filtros (IdentityFilter, RedundancyFilter, UndersamplingFilter) para transformar datos.

-	**config:** configuración de Swagger y OpenApi

**Observaciones clave**
- El controlador utiliza ResponseEntity para devolver respuestas HTTP con códigos adecuados.
-	Las excepciones personalizadas (BlueprintNotFoundException, BlueprintPersistenceException) permiten un manejo claro de errores.
-	El servicio aplica el filtro antes de retornar un blueprint. Esto demuestra el principio de inyección de dependencias.


### 2. Migración a persistencia en PostgreSQL
Se migró la persistencia de una solución en memoria a una base de datos relacional usando **Spring Data JPA** y **PostgreSQL**.

- **Configuración Docker**: Se incluyó un archivo `docker-compose.yml` para levantar una instancia de PostgreSQL 16.
- **Entidades**: Se crearon las entidades `BlueprintEntity` y `PointEmbeddable` para mapear el modelo de dominio a tablas relacionales.
- **Repositorio JPA**: Se implementó `BlueprintJpaRepository` extendiendo de `JpaRepository`.
- **Implementación de Persistencia**: Se creó `PostgresBlueprintPersistence` bajo el perfil de Spring `postgres`.

#### Evidencia de Persistencia


---

### 3. Buenas prácticas de API REST
Se rediseñó la API para cumplir con estándares modernos:

- **Versionamiento**: El path base se cambió a `/api/v1/blueprints`.
- **Estructura de Respuesta Uniforme**: Se implementó el record `ApiResponse<T>` para que todas las respuestas tengan el formato:
  ```json
  {
    "code": 200,
    "message": "execute ok",
    "data": { ... }
  }
  ```
- **Códigos de Estado HTTP**: 
  - `200 OK`: Consultas exitosas.
  - `201 Created`: Al crear un nuevo blueprint.
  - `202 Accepted`: Al procesar una actualización de puntos.
  - `400 Bad Request`: Validaciones fallidas (ej. campos nulos).
  - `404 Not Found`: Blueprints o autores inexistentes.
  - `409 Conflict`: Intentos de crear duplicados.

#### Evidencia de Consultas (Postman/Curl/Swagger)


---

### 4. OpenAPI / Swagger
Se integró **SpringDoc OpenAPI** para la documentación automatizada.

- **URL de acceso**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Anotaciones**: Se usaron `@Operation` y `@ApiResponse` para describir cada endpoint, sus parámetros y los posibles códigos de retorno.

#### Evidencia de Swagger UI
*(Espacio para foto 3: Interfaz de Swagger con los endpoints documentados)*

---

### 5. Filtros de *Blueprints*
Se implementó un sistema de filtrado dinámico basado en **Perfiles de Spring**:

- **RedundancyFilter**: Elimina puntos consecutivos idénticos. (Activo con `-Dspring.profiles.active=redundancy`)
- **UndersamplingFilter**: Reduce la resolución conservando 1 de cada 2 puntos. (Activo con `-Dspring.profiles.active=undersampling`)
- **IdentityFilter**: Filtro por defecto que no modifica los datos.

#### Evidencia de Filtrado




