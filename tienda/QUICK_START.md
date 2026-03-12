# 🚀 Guía Rápida: Ejecutar y Probar Swagger UI

## Paso 1: Compilar el proyecto

```bash
cd C:\Users\user\Desktop\Trabajo\tienda20\tienda
.\mvnw clean compile
```

**Resultado esperado:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 3.374 s
```

✅ **Estado actual:** La compilación ya fue completada exitosamente.

---

## Paso 2: Ejecutar la aplicación

Desde la misma carpeta, ejecuta:

```bash
.\mvnw spring-boot:run
```

O si prefieres construir un JAR primero:

```bash
.\mvnw clean package
java -jar target/tienda-0.0.1-SNAPSHOT.jar
```

**Verás algo como:**
```
[INFO] Starting TiendaApplication v0.0.1-SNAPSHOT using Java 17.0.x
[INFO] Starting server on port 8080
[INFO] Tomcat started on port(s): 8080 (http)
[INFO] Application started successfully
```

---

## Paso 3: Acceder a Swagger UI

Una vez que la aplicación esté corriendo, abre tu navegador en:

### Opción A: Swagger UI completo
```
http://localhost:8080/swagger-ui.html
```

### Opción B: Swagger UI alternativo
```
http://localhost:8080/swagger-ui/index.html
```

### Opción C: Especificación OpenAPI (JSON)
```
http://localhost:8080/v3/api-docs
```

### Opción D: Especificación OpenAPI (YAML)
```
http://localhost:8080/v3/api-docs.yaml
```

---

## Paso 4: Probar los endpoints

En Swagger UI verás:

### 📋 Foráneos (Documentado)
- **GET** `/api/v1/foraneos` 
  - Descripción: "Obtener todos los foráneos"
  - Haz clic en "Try it out" → "Execute"
  - Ver respuesta

### 📋 Otros endpoints (sin documentación aún)
- ClienteController
- PedidoController
- ProductoController
- InventarioController
- DetallePedidoController
- EmpleadoController
- ProveedoresController

---

## Paso 5: Documentar otros controladores (Opcional)

Para agregar documentación a otros controladores, sigue el patrón del `ForaneoController`:

### Ejemplo: Agregar documentación a ClienteController

```java
@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Clientes", description = "Endpoints para gestionar clientes")
public class ClienteController {

    @GetMapping("/clientes")
    @Operation(summary = "Obtener todos los clientes", 
               description = "Retorna una lista de todos los clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<ClienteDtoResponse> readAll() {
        // Implementación
    }

    @PostMapping("/clientes")
    @Operation(summary = "Crear un nuevo cliente",
               description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ClienteDtoResponse create(@RequestBody ClienteDtoRequest dto) {
        // Implementación
    }
}
```

---

## 🎨 Qué verás en Swagger UI

### Interfaz Visual:
1. **Título y Descripción** - "API Tienda v1.0.0"
2. **Servidor** - `http://localhost:8080`
3. **Secciones de Endpoints** por tag:
   - Foráneos ✓ (Documentado)
   - Clientes (Por documentar)
   - Pedidos (Por documentar)
   - etc.

### Para cada endpoint:
- ✓ Método HTTP (GET, POST, PUT, DELETE)
- ✓ Path del endpoint
- ✓ Descripción clara
- ✓ Parámetros requeridos
- ✓ Códigos de respuesta (200, 400, 404, 500)
- ✓ Esquema de respuesta
- ✓ Botón "Try it out" para probar

---

## 🔧 Solución de problemas

### Puerto 8080 ya está en uso
```bash
# Ejecutar en puerto diferente
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Swagger UI no aparece
1. Verifica que la aplicación esté corriendo (sin errores)
2. Asegúrate de estar en `http://localhost:8080/swagger-ui.html` (no en `/swagger-ui`)
3. Limpia el caché del navegador (Ctrl+Shift+Delete)
4. Intenta en modo incógnito

### Ver logs de la aplicación
Si algo no funciona, busca errores en los logs que aparecen en la terminal donde ejecutaste la app.

---

## 📚 Archivos de referencia en el proyecto

1. **OpenApiConfig.java** - Configuración central de OpenAPI
2. **ForaneoController.java** - Ejemplo de controlador documentado
3. **SWAGGER_EXAMPLE_CONTROLLER.java** - Plantilla completa con CRUD documentado
4. **SWAGGER_UI_GUIDE.md** - Guía detallada
5. **SWAGGER_IMPLEMENTATION_SUMMARY.md** - Resumen de cambios

---

## ✅ Checklist de implementación

- [x] Dependencia agregada al pom.xml
- [x] Configuración OpenAPI creada
- [x] ForaneoController documentado
- [x] Compilación exitosa
- [ ] Aplicación ejecutándose
- [ ] Swagger UI accesible en navegador
- [ ] Probar endpoints en Swagger UI
- [ ] (Opcional) Documentar otros controladores

---

## 🎯 Siguientes pasos

1. **Ejecuta:** `.\mvnw spring-boot:run`
2. **Abre:** `http://localhost:8080/swagger-ui.html`
3. **Explora:** Los endpoints disponibles
4. **Prueba:** El endpoint de Foráneos con "Try it out"
5. **(Opcional) Documenta:** El ClienteController usando el archivo SWAGGER_EXAMPLE_CONTROLLER.java como referencia

¡Tu API ya tiene documentación interactiva lista! 🎉

