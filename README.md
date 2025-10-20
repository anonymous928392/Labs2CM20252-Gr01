## Workers y consumo de servicios REST 

### JetNews - Aplicación de Noticias Android

Aplicación móvil moderna para visualizar noticias de diferentes categorías con sincronización automática y persistencia local.

#### 🎯 Características principales

- **📱 Visualización de noticias** por categorías (Tecnología, Deportes, Negocios, Salud, Entretenimiento)
- **⭐ Sistema de favoritos** para guardar artículos
- **🔄 Sincronización automática** con WorkManager (cada 15 minutos)
- **💾 Almacenamiento offline** con Room Database
- **🌓 Selector de tema** (Claro, Oscuro, Seguir sistema)
- **🌍 Multiidioma** (Español, Inglés, Francés)
- **🎨 Material Design 3** con temas dinámicos

#### 🛠️ Stack Tecnológico

**Arquitectura:**
- MVVM (Model-View-ViewModel)
- Repository Pattern
- Clean Architecture

**Librerías principales:**
- **Jetpack Compose** - UI declarativa
- **Room Database** - Persistencia local
- **Retrofit + OkHttp** - API REST
- **WorkManager** - Tareas en segundo plano
- **DataStore** - Preferencias de usuario
- **Kotlin Coroutines & Flow** - Programación asíncrona
- **Navigation Compose** - Navegación
- **Coil** - Carga de imágenes

#### 📋 Funcionalidades implementadas

1. **Pantalla principal (HomeScreen)**
   - Lista de artículos con imágenes
   - Filtrado por categorías
   - Botón de sincronización manual
   - Acceso a configuración

2. **Pantalla de detalle (DetailScreen)**
   - Visualización completa del artículo
   - Agregar/quitar de favoritos
   - Compartir artículo

3. **Pantalla de configuración (SettingsScreen)**
   - Selector de tema (Claro/Oscuro/Sistema)
   - Preferencia guardada con DataStore
   - Información de la app

4. **WorkManager**
   - Sincronización periódica automática
   - Restricciones de red y batería
   - Política de reintentos

#### 🔧 Configuración

1. **API REST:** El proyecto usa MockAPI. Actualiza la URL en:
   ```kotlin
   // RetrofitInstance.kt
   private const val BASE_URL = "TU_URL_AQUI"
   ```

2. **Sincronización:** Configurable en `SyncArticlesWorker.kt`

#### 👥 Grupo 01 - 2025-2
Universidad de Antioquia - Computación Móvil 
