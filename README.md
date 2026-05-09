# App de aprendizaje

Aplicación móvil Android desarrollada en Kotlin para gestionar recursos de aprendizaje mediante una API dinámica. Permite visualizar, buscar, guardar favoritos, calificar recursos y administrar contenido según el rol del usuario.

## Tecnologías utilizadas

- Kotlin
- Android Studio
- Retrofit
- Gson Converter
- MockAPI
- RecyclerView
- CardView
- Glide
- SharedPreferences
- Arquitectura MVC

## Arquitectura

El proyecto utiliza arquitectura MVC:

- **Model:** `Usuario.kt`, `Recurso.kt`
- **View:** `LoginActivity.kt`, `RegisterActivity.kt`, `MainActivity.kt`, `RecursoFormActivity.kt` y layouts XML
- **Controller:** `AuthController.kt`, `RecursoController.kt`
- **Network:** `ApiService.kt`, `RetrofitClient.kt`
- **Storage:** `SessionManager.kt`, `FavoritosManager.kt`
- **Adapter:** `RecursoAdapter.kt`
- **Utils:** `PasswordValidator.kt`

## Funcionalidades

- Registro e inicio de sesión.
- Validación segura de contraseña.
- Control de roles: Estudiante y Docente.
- Visualización de recursos desde MockAPI.
- Búsqueda dinámica por ID, título o tipo.
- CRUD completo de recursos para docentes.
- Favoritos persistentes para estudiantes.
- Vista de recursos favoritos.
- Calificación de recursos de 1 a 5 estrellas.
- Persistencia de sesión con SharedPreferences.
- Textos centralizados en `strings.xml`.

## Roles

### Estudiante

Puede visualizar recursos, buscar, agregar o quitar favoritos, ver solo favoritos y calificar recursos.

### Docente

Puede visualizar recursos, buscar, crear, editar y eliminar recursos.

## API utilizada

La aplicación consume una API dinámica creada en MockAPI.

La URL base se configura en el archivo: network/RetrofitClient.kt

## Links para visualizar las api

https://69fe2cd68c70b15fa3ca4b0f.mockapi.io/users
https://69fe2cd68c70b15fa3ca4b0f.mockapi.io/resources
