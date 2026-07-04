Plugin para **Velocity** que permite personalizar por completo los mensajes de desconexión, kick y error que el proxy genera automáticamente, incluyendo los prefijos internos que Velocity antepone a los mensajes de otros plugins (por ejemplo, DiscordSRV, WorldGuard, etc.) instalados en tus servidores backend.

## ¿Por qué existe este plugin?

Por defecto, Velocity envuelve cualquier mensaje de kick proveniente del servidor backend con textos genéricos hardcodeados en su propio código, como:

- `Has sido echado de <servidor>: <razón original>`
- `No se ha podido conectar a <servidor>: <razón original>`

Estos prefijos **no son configurables de forma nativa** en `velocity.toml` ni en ningún archivo estándar de Velocity — están escritos directamente en su código fuente. VelocityCustomMessages soluciona esto interceptando esos eventos y permitiéndote definir tus propios mensajes limpios, con soporte completo de colores y formato mediante MiniMessage.

## Características

- ✅ Elimina los prefijos automáticos de Velocity en mensajes de kick/desconexión.
- ✅ Archivo `messages.yml` generado automáticamente, editable sin tocar código.
- ✅ Soporte completo de **MiniMessage** (colores, gradientes, negritas, etc.).
- ✅ Placeholders dinámicos: `<server>` (nombre del servidor) y `<reason>` (mensaje original enviado por el backend).
- ✅ Recarga en caliente con `/customessages reload`, sin reiniciar el proxy.
- ✅ Compatible con la versión más reciente de Velocity (API 3.4.0+).
- ✅ Compilación automática vía GitHub Actions, sin necesidad de instalar Java o Gradle localmente.

## Instalación

1. Descarga el `.jar` compilado desde la pestaña **Actions** → última ejecución → sección **Artifacts** de este repositorio (o compílalo tú mismo, ver más abajo).
2. Copia el archivo `.jar` a la carpeta `plugins/` de tu instancia de Velocity.
3. Reinicia el proxy por completo (no basta con un reload).
4. Al arrancar, se creará automáticamente `plugins/customessages/messages.yml` con los valores por defecto.

## Configuración

Edita `plugins/customessages/messages.yml`. Cada clave acepta sintaxis [MiniMessage](https://docs.advntr.dev/minimessage/format.html):

```yaml
kicked-while-connected: "<red>Has sido echado de <yellow><server></yellow>:</red> <reason>"
kicked-while-connecting: "<reason>"
already-connected-proxy: "<red>Ya estas conectado a este proxy.</red>"
already-connected-server: "<red>Ya estas conectado a este servidor.</red>"
connection-in-progress: "<red>Ya te estas conectando a un servidor.</red>"
internal-connection-error: "<red>Error interno de conexion. Contacta a un administrador.</red>"
unexpected-disconnect: "<red>Desconexion inesperada del servidor. Puede que se haya caido.</red>"
moved-after-kick: "<red>Fuiste movido de servidor porque te expulsaron de <yellow><server></yellow>.</red>"
no-available-servers: "<red>No hay servidores disponibles a los que conectarte en este momento.</red>"
server-shutdown-generic: "<red>El servidor <yellow><server></yellow> se ha cerrado.</red>"
```

### Placeholders disponibles

| Placeholder | Descripción |
|---|---|
| `<server>` | Nombre del servidor backend involucrado en el evento |
| `<reason>` | Mensaje original de kick/desconexión enviado por el backend (por ejemplo, el texto de DiscordSRV) |

Tras editar el archivo, aplica los cambios sin reiniciar el proxy:

```
/customessages reload
```

Alias disponible: `/cmsg reload`

### Permiso del comando

- `customessages.reload` — requerido para ejecutar el comando desde el chat de un jugador. La consola del servidor siempre tiene acceso.

## Compilar desde el código fuente

Este proyecto incluye un workflow de GitHub Actions que compila el plugin automáticamente en la nube, sin que necesites instalar Java ni Gradle en tu propia computadora.

1. Haz un fork o clona este repositorio.
2. Sube cualquier cambio a la rama `main` (o dispara manualmente el workflow desde la pestaña **Actions** → **Build VelocityCustomMessages** → **Run workflow**).
3. Espera 1-2 minutos a que el workflow termine en verde.
4. Descarga el `.jar` desde la sección **Artifacts** de esa ejecución.

## Requisitos

- Velocity (última versión estable recomendada).
- Java 17 o superior en el proxy.

## Eventos interceptados

Actualmente el plugin escucha `KickedFromServerEvent`, el único evento público y documentado de la API de Velocity que cubre los kicks provenientes de un servidor backend (por ejemplo, el bloqueo de cuentas no vinculadas de DiscordSRV). Otras claves del `messages.yml` (como `already-connected-proxy` o `internal-connection-error`) están preparadas en la configuración para futuras versiones, en caso de que Velocity exponga eventos públicos adicionales para esos escenarios internos.

## Licencia

Uso libre para modificar y adaptar a las necesidades de tu propio servidor.
'''
