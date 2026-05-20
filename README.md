# Inventory Service

Microservicio de gestión de inventario y stock de productos.

## Información general

| Campo | Valor |
|-------|-------|
| Puerto | `8085` |
| Base de datos | `db_inventory` (PostgreSQL) |
| Contexto | `/api/inventory` |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/inventory/{productId}` | Consultar stock de un producto |
| `POST` | `/api/inventory/add` | Agregar unidades al stock |
| `POST` | `/api/inventory/reduce` | Reducir unidades del stock |

## Ejemplo de uso

**Agregar stock:**
```bash
curl -X POST http://localhost:8085/api/inventory/add \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 100}'
```

**Respuesta:**
```json
{
  "id": 1,
  "productId": 1,
  "quantity": 100
}
```

**Consultar stock:**
```bash
curl http://localhost:8085/api/inventory/1
```

**Reducir stock:**
```bash
curl -X POST http://localhost:8085/api/inventory/reduce \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 5}'
```

## Modelo de datos

```sql
CREATE TABLE inventory (
    id         BIGINT  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT  NOT NULL UNIQUE,
    quantity   INTEGER NOT NULL DEFAULT 0
);
```

## Dependencias externas

| Servicio | Uso | Puerto |
|---------|-----|--------|
| **productos** | Valida que el producto exista antes de registrar inventario | `8081` |

## Configuración (variables de entorno Docker)

| Variable | Descripción |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos |
| `FEIGN_CLIENT_PRODUCT_URL` | URL del servicio de productos |

## Tecnologías

- Java 25 · Spring Boot 4.0.6
- Spring Data JPA · Hibernate 7
- Spring Cloud OpenFeign
- Flyway (migraciones)
- PostgreSQL 16
- Lombok · Bean Validation
