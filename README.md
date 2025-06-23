# GameHub7 - Plataforma de Organización de Torneos de Videojuegos Online (Equipo #7)

**Backend API REST para gestionar torneos online, emparejamientos automáticos y chat básico entre jugadores.**

---

## 🚀 Tecnologías Utilizadas

| Tecnología        | Versión              |
|------------------|-----------------------|
| Java             | 17                    |
| Spring Boot      | 3.5.0 (última estable)|
| Spring Security  | Integrado             |
| JWT (JJWT)       | Integrado             |
| PostgreSQL       | 15                    |
| Docker           | 24+                   |
| Docker Compose   | 3.x                   |
| Swagger UI       | Integrado             |
| Lombok           | Integrado             |

---

## 🐳 Ejecución del Proyecto con Docker Compose

**Requisitos Previos:**

- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/)

**Pasos:**

```bash
# Clonar el repositorio
git clone https://github.com/rjrequena47/gamehub7.git
cd gamehub-backend

# Levantar los contenedores
docker-compose up --build
```

**Servicios Levantados:**

| Contenedor     | Puerto Local | Descripción                          |
|----------------|--------------|--------------------------------------|
| `gamehub-api`  | `8080`       | API REST (Spring Boot)               |
| `gamehub-db`   | `5432`       | Base de datos PostgreSQL             |

---

## 🌐 Endpoints Útiles

- **Base URL de la API:** `http://localhost:8080/api`
- **Documentación Swagger:** [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

---

## 📢 Endpoints Desarrollados

A continuación se listan los principales endpoints implementados y ejemplos de uso con herramientas como Postman.

### 🔐 Autenticación

#### 1. Registro de Usuario (rol PLAYER por defecto)
**POST** `/api/auth/register`

```json
{
  "username": "jugador1",
  "email": "jugador1@example.com",
  "password": "secreto123"
}
```

**Respuesta exitosa:**

```json
{
  "token": "jwt-generado"
}
```

#### 2. Inicio de Sesión
**POST** `/api/auth/login`

```json
{
  "username": "jugador1",
  "password": "secreto123"
}
```

**Respuesta exitosa:**

```json
{
  "token": "jwt-generado"
}
```

---

### 👤 Gestión de Usuarios

#### 3. Obtener Perfil Propio (Requiere autenticación)
**GET** `/api/users/me`

**Header requerido:**

```
Authorization: Bearer <token>
```

**Ejemplo de respuesta:**

```json
{
  "id": "uuid-del-usuario",
  "username": "jugador1",
  "email": "jugador1@example.com",
  "role": "PLAYER",
  "rank": null,
  "points": 0
}
```

#### 4. Ver Perfil Público de un Usuario
**GET** `/api/users/{id}`

**Ejemplo de uso:**

```
GET /api/users/3f4e0c02-1234-4567-89ab-cdef01234567
```

**Respuesta:**

```json
{
  "id": "uuid-del-usuario",
  "username": "jugador1",
  "rank": null,
  "points": 0
}
```

---

### 🏆 Gestión de Torneos

#### 5. Crear Torneo (Solo ADMIN)
**POST** `/api/tournaments`

**Header:**

```
Authorization: Bearer <token-admin>
```

**Cuerpo:**

```json
{
  "name": "Torneo Oficial GameHub",
  "maxPlayers": 16
}
```

#### 6. Listar Torneos (Público)
**GET** `/api/tournaments`

```json
[
  {
    "id": "uuid-del-torneo",
    "name": "Torneo Oficial GameHub",
    "maxPlayers": 16,
    "status": "CREATED"
  }
]
```

#### 7. Ver Detalle de Torneo (Público)
**GET** `/api/tournaments/{id}`

#### 8. Unirse a un Torneo (Solo PLAYER)
**POST** `/api/tournaments/{id}/join`

**Header:**

```
Authorization: Bearer <token-player>
```

**Respuesta esperada:**

```json
{
  "id": "uuid-del-torneo",
  "name": "Torneo Oficial GameHub",
  "maxPlayers": 16,
  "status": "CREATED"
}
```

---

## ⚠️ Notas de Seguridad

- Todos los endpoints, excepto `/api/auth/**`, `/api/users/{id}` y `/api/tournaments` son protegidos mediante JWT.
- El token debe enviarse en cada petición protegida usando el header:  

```
Authorization: Bearer <token>
```

Para más detalles interactivos, consultar la documentación Swagger:

👉 [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

---

## 🗂️ Estructura del Proyecto

```
gamehub-backend/
├── src/
│   └── main/
│       ├── java/com/bytes7/GameHub/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── security/
│       │   ├── service/
│       │   └── GameHubApplication.java
│       └── resources/
│           ├── application.properties
│           └── static/
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🗃️ Modelo de Datos - GameHub

#### 👤 User (Tabla: users)
| Campo      | Tipo   | Descripción                         |
| ---------- | ------ | ----------------------------------- |
| `id`       | UUID   | Identificador único                 |
| `username` | String | Nombre de usuario                   |
| `email`    | String | Correo electrónico único            |
| `password` | String | Contraseña encriptada               |
| `role`     | Enum   | Rol del usuario (`ADMIN`, `PLAYER`) |
| `rank`     | String | Rango o clasificación               |
| `points`   | int    | Puntuación total                    |

#### 🏆 Tournament (Tabla: tournaments)
| Campo        | Tipo       | Descripción                                   |
| ------------ | ---------- | --------------------------------------------- |
| `id`         | UUID       | Identificador único                           |
| `name`       | String     | Nombre del torneo                             |
| `maxPlayers` | int        | Cantidad máxima de participantes              |
| `status`     | Enum       | Estado (`CREATED`, `IN_PROGRESS`, `FINISHED`) |
| `players`    | List<User> | Participantes inscritos                       |

#### 🎮 Match (Tabla: matches)
| Campo          | Tipo | Descripción                                         |
| -------------- | ---- | --------------------------------------------------- |
| `id`           | UUID | Identificador único del partido                     |
| `tournamentId` | UUID | Referencia al torneo                                |
| `player1`      | User | Primer jugador                                      |
| `player2`      | User | Segundo jugador                                     |
| `result`       | Enum | Resultado (`PENDING`, `PLAYER1_WON`, `PLAYER2_WON`) |
| `round`        | int  | Número de ronda o fase                              |

#### 💬 Message (Tabla: messages)
| Campo          | Tipo            | Descripción                     |
| -------------- | --------------- | ------------------------------- |
| `id`           | UUID            | Identificador único del mensaje |
| `senderId`     | UUID            | Usuario que envía el mensaje    |
| `content`      | String          | Texto del mensaje               |
| `timestamp`    | LocalDateTime   | Fecha y hora del envío          |
| `matchId`      | UUID (opcional) | Partido asociado (si aplica)    |
| `tournamentId` | UUID (opcional) | Torneo asociado (si aplica)     |

#### 🏷️ Enumeraciones Utilizadas
- Role: ADMIN, PLAYER
- Status (Tournament): CREATED, IN_PROGRESS, FINISHED
- Result (Match): PENDING, PLAYER1_WON, PLAYER2_WON

---

## ✅ Funcionalidades Actuales

- [x] Registro e inicio de sesión con JWT
- [x] Gestión de usuarios con roles: `ADMIN`, `PLAYER`
- [x] Creación de torneos (solo ADMIN)
- [x] Listado y detalle de torneos (público)
- [x] Inscripción de jugadores a torneos (solo PLAYER)
- [x] Seguridad basada en JWT y roles
- [x] Documentación de API con Swagger
- [x] Entorno reproducible vía Docker

---

## 🛠️ Funcionalidades en Desarrollo

- [ ] Emparejamiento automático entre jugadores
- [ ] Registro de resultados de partidos
- [ ] Chat básico (HTTP Polling)
- [ ] Sistema de ranking con puntos y estadísticas
- [ ] Pruebas unitarias con JUnit/Mockito
- [ ] Control global de errores con `@ControllerAdvice`

---

## 🤝 Contribución

- Alexandra9804
- rjrequena47
- Ugulbertodev

---

## 📄 Licencia

Apache-2.0 license.  
© 2025 - GameHub7
