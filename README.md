# VelocityCustomMessages

Plugin para Velocity que permite personalizar todos los mensajes internos
de desconexion/kick que Velocity genera automaticamente (incluyendo el
prefijo "Has sido echado de X:" o "No se ha podido conectar a X:").

Este proyecto NO incluye el Gradle Wrapper (gradlew). El workflow de
GitHub Actions usa la accion oficial "gradle/actions/setup-gradle", que
instala Gradle automaticamente en el runner. No necesitas instalar nada
en tu propia computadora ni generar ningun wrapper.

## Pasos para compilar usando GitHub Actions

1. Crea un nuevo repositorio en GitHub (puede ser privado o publico).
2. Descomprime este zip y sube TODO su contenido a la raiz de ese
   repositorio (incluyendo la carpeta oculta .github/workflows/build.yml).
   Si usas la interfaz web de GitHub: "Add file" -> "Upload files" ->
   arrastra todas las carpetas y archivos, incluyendo .github.
3. Confirma el commit a la rama "main" (o "master", ajusta el workflow
   si tu rama por defecto se llama distinto).
4. Ve a la pestana "Actions" en la barra superior de tu repositorio.
5. Veras un workflow llamado "Build VelocityCustomMessages" ejecutandose
   automaticamente apenas subiste los archivos. Tarda 1-2 minutos.
6. Cuando el icono se ponga verde (completado), haz clic en esa
   ejecucion y baja hasta la seccion "Artifacts".
7. Descarga el artifact llamado "VelocityCustomMessages" -> es un .zip
   que contiene el .jar final del plugin ya compilado.
8. Copia ese .jar a la carpeta plugins/ de tu servidor Velocity y
   reinicia el proxy por completo.

## Como volver a compilar tras editar el codigo

Cada vez que subas un cambio (push) a la rama main, GitHub Actions
compilara automaticamente una nueva version y la dejara disponible en
"Actions" -> ultima ejecucion -> "Artifacts". Tambien puedes forzar una
compilacion manual desde "Actions" -> "Build VelocityCustomMessages" ->
"Run workflow" (boton a la derecha), gracias a que el workflow tiene
habilitado "workflow_dispatch".

## Uso del plugin una vez instalado

- Al reiniciar Velocity se creara automaticamente:
    plugins/customessages/messages.yml
- Edita ese archivo con formato MiniMessage:
    https://docs.advntr.dev/minimessage/format.html
- Placeholders disponibles en cada mensaje: <server>, <reason>
- Aplica los cambios sin reiniciar el proxy con el comando:
    /customessages reload
  (alias: /cmsg reload)

## Claves editables en messages.yml

- kicked-while-connected: cuando te expulsan de un server al que ya
  estabas conectado.
- kicked-while-connecting: cuando te bloquean/rechazan al intentar
  conectarte (por ejemplo el kick de DiscordSRV por no estar vinculado).
- already-connected-proxy
- already-connected-server
- connection-in-progress
- internal-connection-error
- unexpected-disconnect
- moved-after-kick
- no-available-servers
- server-shutdown-generic
