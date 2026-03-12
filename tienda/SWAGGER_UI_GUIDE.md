# Swagger UI - Documentación de la API

## ✅ Swagger UI ha sido implementado exitosamente

Swagger UI está ahora disponible en tu proyecto Spring Boot. Esta es una interfaz visual interactiva para explorar y probar tu API REST.

## 🚀 Cómo acceder a Swagger UI

Una vez que el proyecto esté ejecutándose, puedes acceder a Swagger UI en:

```
http://localhost:8080/swagger-ui.html
```

O también puedes usar:

```
http://localhost:8080/swagger-ui/index.html
```

## 📋 Documentación de OpenAPI

La especificación OpenAPI (JSON) está disponible en:

```
http://localhost:8080/v3/api-docs
```

O en formato YAML:

```
http://localhost:8080/v3/api-docs.yaml
```

## 🔧 Características implementadas

### 1. **Dependencia agregada al pom.xml**
   - `springdoc-openapi-starter-webmvc-ui` v2.4.0
   - Incluye Swagger UI 5.11.8

### 2. **Configuración de OpenAPI**
   - Archivo: `src/main/java/com/inndata20/tienda/config/OpenApiConfig.java`
   - Incluye información general de la API
   - Define servidores (local y producción)
   - Incluye contacto y licencia

### 3. **Anotaciones Swagger en Controladores**
   - `@Tag` - Agrupa endpoints por categorías
   - `@Operation` - Describe cada operación
   - `@ApiResponses` - Documenta respuestas HTTP
   - `@ApiResponse` - Detalla cada respuesta

## 📝 Ejemplo: ForaneoController

El controlador `ForaneoController` ya incluye documentación de Swagger:

```java
@GetMapping("/foraneos")
@Operation(summary = "Obtener todos los foráneos", 
           description = "Retorna una lista de todos los foráneos registrados")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public List<ForaneoEntity> readAll() {
    return foraneoService.readAll();
}
```

## 🎯 Próximos pasos

Para maximizar la utilidad de Swagger UI en tu proyecto, considera:

### 1. **Agregar documentación a otros controladores**
   Aplica las mismas anotaciones de Swagger a los siguientes controladores:
   - `ClienteController`
   - `PedidoController`
   - `ProductoController`
   - `InventarioController`
   - `DetallePedidoController`
   - `EmpleadoController`
   - `ProveedoresController`

### 2. **Documentar DTOs**
   Agrega anotaciones `@Schema` a tus clases de modelos para mejorar la documentación:
   ```java
   @Schema(description = "Entidad Cliente")
   public class ClienteDtoRequest {
       @Schema(description = "Nombre del cliente", example = "Juan Pérez")
       private String nombre;
   }
   ```

### 3. **Ejemplo de anotaciones completas**
   ```java
   @PostMapping("/clientes")
   @Operation(summary = "Crear un nuevo cliente", 
              description = "Crea un nuevo registro de cliente en el sistema")
   @ApiResponses(value = {
       @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente"),
       @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
       @ApiResponse(responseCode = "500", description = "Error interno del servidor")
   })
   public ClienteDtoResponse create(@RequestBody ClienteDtoRequest cliente) {
       return clienteService.create(cliente);
   }
   ```

## 🔒 Seguridad

Si en el futuro implementas autenticación (JWT, OAuth2, etc.), puedes configurarlo en `OpenApiConfig.java`:

```java
.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
.components(new Components()
    .addSecuritySchemes("Bearer Authentication", 
        new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
```

## 📚 Recursos útiles

- [Documentación oficial de Springdoc OpenAPI](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Anotaciones Swagger disponibles](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.6.x)

---

**Nota:** Asegúrate de que tu aplicación Spring Boot esté ejecutándose para acceder a Swagger UI. Si usas un puerto diferente al 8080, reemplázalo en la URL correspondiente.

