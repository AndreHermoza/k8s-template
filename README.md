# Hackaton Template

API reactiva (Spring Boot 4 + WebFlux + R2DBC) con maestros **Estudiante** y **Producto**.

Puerto por defecto: **9000**

---

## Requisitos previos

- Java 17
- Maven (o usar `./mvnw`)
- Docker Desktop
- kubectl (para Kubernetes)
- Cuenta en [Neon](https://neon.tech) (para despliegue en K8s)

---

## 1. Desarrollo local (H2 en memoria)

```bash
# Compilar
./mvnw clean package

# Ejecutar
./mvnw spring-boot:run
```

En PowerShell:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

La app usa H2 en memoria y carga `schema.sql` automáticamente.

**Probar:**

```bash
curl http://localhost:9000/api/estudiantes
curl http://localhost:9000/api/productos
```

**Crear un producto:**

```bash
curl -X POST http://localhost:9000/api/productos ^
  -H "Content-Type: application/json" ^
  -d "{\"codigo\":\"P001\",\"nombre\":\"Laptop\",\"descripcion\":\"Gamer\",\"precio\":2500.00}"
```

---

## 2. Docker (imagen de la API)

Construir la imagen:

```bash
docker build -t andrehermoza/hackaton-template:1.0 .
```

Ejecutar solo el contenedor (sin Compose, requiere BD externa o variables):

```bash
docker run --rm -p 9000:9000 andrehermoza/hackaton-template:1.0
```

Subir al registry (Docker Hub):

```bash
docker login
docker push andrehermoza/hackaton-template:1.0
```

---

## 3. Docker Compose (PostgreSQL + API)

Levanta Postgres con volumen persistente e importa `schema.sql` al iniciar.

```bash
# Construir y levantar en primer plano
docker compose up --build

# En segundo plano
docker compose up --build -d

# Ver logs
docker compose logs -f

# Detener
docker compose down

# Detener y borrar volumen (reimporta schema.sql en el próximo up)
docker compose down -v
```

**Probar:**

```bash
curl http://localhost:9000/api/productos
curl http://localhost:9000/actuator/health
```

**Variables opcionales** (archivo `.env` en la raíz):

```env
DB_NAME=hackaton
DB_USER=hackaton
DB_PASSWORD=hackaton
DB_PORT=5432
API_PORT=9000
```

---

## 4. Kubernetes + Neon

### 4.1 Preparar Neon

1. Crear proyecto en Neon.
2. Copiar host, usuario, contraseña y nombre de BD.
3. Ejecutar `src/main/resources/schema.sql` en el **SQL Editor** de Neon.

### 4.2 Configurar credenciales

Editar `k8s/secret.yaml` con tus datos de Neon:

```yaml
DB_HOST: "tu-host.neon.tech"
DB_PORT: "5432"
DB_NAME: "neondb"
DB_USER: "tu_usuario"
DB_PASSWORD: "tu_password"
DB_SSL_MODE: "require"
R2DBC_URL: "r2dbc:postgresql://tu-host.neon.tech/neondb?sslMode=require"
```

> No subas credenciales reales a repositorios públicos.

### 4.3 Construir y publicar imagen

```bash
docker build -t andrehermoza/hackaton-template:1.0 .
docker push andrehermoza/hackaton-template:1.0
```

Si usas **minikube**:

```bash
minikube image load andrehermoza/hackaton-template:1.0
```

Si usas **kind**:

```bash
kind load docker-image andrehermoza/hackaton-template:1.0
```

### 4.4 Desplegar en Kubernetes

Aplicar manifiestos (el namespace debe crearse primero; si falla, repetir el comando):

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

O todo junto:

```bash
kubectl apply -f k8s/
```

Si `configmap` o `deployment` fallan por namespace, ejecutar de nuevo:

```bash
kubectl apply -f k8s/
```

### 4.5 Verificar despliegue

```bash
# Estado del pod
kubectl get pods -n hackaton

# Logs de la API
kubectl logs -f deployment/hackaton-api -n hackaton

# Reiniciar tras cambiar imagen o secret
kubectl rollout restart deployment/hackaton-api -n hackaton

# Eliminar todo el namespace
kubectl delete namespace hackaton
```

### 4.6 Acceder a la API

**Port-forward** (recomendado en local):

```bash
kubectl port-forward deployment/hackaton-api 9000:9000 -n hackaton
```

Luego:

```bash
curl http://localhost:9000/api/productos
curl http://localhost:9000/actuator/health
```

---

## 5. Endpoints de la API

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/estudiantes` | Listar estudiantes (`?estado=true`) |
| GET | `/api/estudiantes/{id}` | Obtener por id |
| POST | `/api/estudiantes` | Crear |
| PUT | `/api/estudiantes/{id}` | Actualizar |
| PATCH | `/api/estudiantes/{id}/estado?estado=false` | Cambiar estado |
| DELETE | `/api/estudiantes/{id}` | Eliminar |

Los mismos endpoints existen en `/api/productos`.

---

## 6. Estructura del proyecto

```
src/main/java/.../
├── model/          # Entidades (Estudiante, Producto)
├── repository/     # Repositorios R2DBC
├── service/        # Interfaces
│   └── impl/       # Implementaciones
└── rest/           # Controladores REST

k8s/                # Manifiestos Kubernetes
docker-compose.yml  # Postgres + API
Dockerfile          # Build de la imagen
```

---

## 7. Solución de problemas

| Error | Solución |
|-------|----------|
| `Invalid or corrupt jarfile app.jar` | Reconstruir imagen: `docker build -t andrehermoza/hackaton-template:1.0 .` y volver a desplegar |
| Pod en `CrashLoopBackOff` | `kubectl logs deployment/hackaton-api -n hackaton` |
| Error de conexión a Neon | Verificar `k8s/secret.yaml` y que `schema.sql` esté ejecutado en Neon |
| Namespace not found al apply | Ejecutar `kubectl apply -f k8s/namespace.yaml` y repetir `kubectl apply -f k8s/` |

---

## 8. Orden recomendado desde cero

1. **Local:** `.\mvnw.cmd spring-boot:run`
2. **Compose:** `docker compose up --build`
3. **Docker image:** `docker build` + `docker push`
4. **Neon:** ejecutar `schema.sql`
5. **K8s:** configurar `secret.yaml` → `kubectl apply -f k8s/` → `kubectl port-forward`
