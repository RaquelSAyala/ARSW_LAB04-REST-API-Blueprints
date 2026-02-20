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
 <img width="651" height="379" alt="image" src="https://github.com/user-attachments/assets/ae9c4f7f-a229-439e-8ff2-b0e1145d7f38" />
 
<img width="1242" height="825" alt="image" src="https://github.com/user-attachments/assets/60211bc9-cef5-45e6-aab4-5042958c3823" />

<img width="709" height="904" alt="image" src="https://github.com/user-attachments/assets/7dcec07c-a704-4b76-ae55-8a116699bbbf" />
<img width="824" height="397" alt="image" src="https://github.com/user-attachments/assets/363a6bed-d0b7-4ec3-9ba0-e461925e1a1b" />

<img width="821" height="932" alt="image" src="https://github.com/user-attachments/assets/b45635f2-1671-4ba4-bd3a-11383039a226" />

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
    
   <img width="1662" height="809" alt="image" src="https://github.com/user-attachments/assets/e38d696a-2af7-4e37-8954-16746e78e82a" />
  
  - `201 Created`: Al crear un nuevo blueprint.
    
    <img width="1694" height="832" alt="image" src="https://github.com/user-attachments/assets/dd4a9d93-7790-426f-8561-9772e85e47b8" />
    
  - `202 Accepted`: Al procesar una actualización de puntos.
    
    <img width="1742" height="742" alt="image" src="https://github.com/user-attachments/assets/1762d07e-c8a1-4670-92ff-8795a1e8b00b" />

  - `400 Bad Request`: Validaciones fallidas (ej. campos nulos).
    
    <img width="1699" height="664" alt="image" src="https://github.com/user-attachments/assets/a7782736-a229-418b-a401-02b8b2d470dd" />
    
  - `404 Not Found`: Blueprints o autores inexistentes.
    
    <img width="1689" height="597" alt="image" src="https://github.com/user-attachments/assets/2693e124-5195-4ac4-bd08-e1d830da86d9" />

  - `409 Conflict`: Intentos de crear duplicados.
    
    <img width="1662" height="441" alt="image" src="https://github.com/user-attachments/assets/b02b3ed4-6dad-4f46-abb6-2b5b95f4bf9c" />


---

### 4. OpenAPI / Swagger
Se integró **SpringDoc OpenAPI** para la documentación automatizada.

- **URL de acceso**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Anotaciones**: Se usaron `@Operation` y `@ApiResponse` para describir cada endpoint, sus parámetros y los posibles códigos de retorno.

#### Evidencia de Swagger UI
<img width="1716" height="857" alt="image" src="https://github.com/user-attachments/assets/77b81ed5-950f-4bab-b95a-28035150faf7" />


---

### 5. Filtros de *Blueprints*
Se implementó un sistema de filtrado dinámico basado en **Perfiles de Spring**:

- **RedundancyFilter**: Elimina puntos consecutivos idénticos. (Activo con `-Dspring.profiles.active=redundancy`)
- **UndersamplingFilter**: Reduce la resolución conservando 1 de cada 2 puntos. (Activo con `-Dspring.profiles.active=undersampling`)
- **IdentityFilter**: Filtro por defecto que no modifica los datos.

#### Evidencia de Filtrado

<img width="1630" height="836" alt="image" src="https://github.com/user-attachments/assets/9b6df29a-4f5e-4b60-acd2-ef2407c0303b" />
<img width="1502" height="705" alt="image" src="https://github.com/user-attachments/assets/573f691f-9b70-4856-bd16-5bcce4810681" />

---

### Pruebas unitarias 

<img width="1887" height="939" alt="image" src="https://github.com/user-attachments/assets/6fa1eb97-a4b4-42cc-97d0-b51b8e466715" />

<img width="1881" height="884" alt="image" src="https://github.com/user-attachments/assets/354a9d69-7d06-4c18-ac04-f82de0643a65" />

<img width="1859" height="936" alt="image" src="https://github.com/user-attachments/assets/3daf1dd2-1b6e-4670-9408-d891a9d635d9" />

<img width="1851" height="914" alt="image" src="https://github.com/user-attachments/assets/bf2aa993-17f4-4583-a66c-299696cb61e7" />



